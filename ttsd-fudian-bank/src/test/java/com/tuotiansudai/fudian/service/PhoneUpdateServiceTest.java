package com.tuotiansudai.fudian.service;

import com.tuotiansudai.fudian.dto.BankChangeMobileDto;
import com.tuotiansudai.fudian.dto.request.PhoneUpdateRequestDto;
import com.tuotiansudai.fudian.dto.request.Source;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.mapper.fudian.InsertMapper;
import com.tuotiansudai.fudian.mapper.fudian.SelectMapper;
import com.tuotiansudai.fudian.mapper.fudian.UpdateMapper;
import com.tuotiansudai.fudian.message.BankChangeMobileMessage;
import com.tuotiansudai.fudian.sign.SignatureHelper;
import com.tuotiansudai.fudian.util.MessageQueueClient;
import com.tuotiansudai.mq.client.model.MessageQueue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.*;

/**
 * Created by qduljs2011 on 2018/9/6.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
public class PhoneUpdateServiceTest {
    @InjectMocks
    private PhoneUpdateService phoneUpdateService;
    @Mock
    private SignatureHelper signatureHelper;
    @Mock
    private SelectMapper selectMapper;
    @Mock
    private InsertMapper insertMapper;
    @Mock
    private UpdateMapper updateMapper;
    @Mock
    private RedisTemplate<String, String> redisTemplate;
    @Mock
    private MessageQueueClient messageQueueClient;

    @Before
    public void init() {
        try {
            Field field = phoneUpdateService.getClass().getDeclaredField("redisTemplate");
            field.setAccessible(true);
            field.set(phoneUpdateService, redisTemplate);
            Field field2 = phoneUpdateService.getClass().getDeclaredField("messageQueueClient");
            field2.setAccessible(true);
            field2.set(phoneUpdateService, messageQueueClient);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void updateSuccess() {
        BankChangeMobileDto bankChangeMobileDto = new BankChangeMobileDto("loginName", "mobile", "bankUserName", "bankAccountNo", "1", "18354275208");
        doNothing().when(signatureHelper).sign(any(), argThat(o -> {
            ((PhoneUpdateRequestDto) o).setOrderNo("111111");
            ((PhoneUpdateRequestDto) o).setRequestData("requestData");
            return false;
        }));
        when(redisTemplate.opsForHash()).thenReturn(mock(HashOperations.class));
        PhoneUpdateRequestDto phoneUpdateRequestDto = phoneUpdateService.update(Source.WEB, bankChangeMobileDto);

        verify(insertMapper, times(1)).insertPhoneUpdate(any(PhoneUpdateRequestDto.class));
        verify(this.redisTemplate, times(1)).expire(anyString(), anyLong(), any(TimeUnit.class));
        assertNotNull(phoneUpdateRequestDto);
    }

    @Test
    public void updateFalse() {
        when(redisTemplate.opsForHash()).thenReturn(mock(HashOperations.class));
        PhoneUpdateRequestDto phoneUpdateRequestDto = phoneUpdateService.update(Source.WEB, new BankChangeMobileDto());
        verify(insertMapper, times(0)).insertPhoneUpdate(any(PhoneUpdateRequestDto.class));
        assertNull(phoneUpdateRequestDto);

    }

    @Test
    public void notifyCallbackSuccess() {
        String responseStr = "{\"certInfo\":\"certInfo\",\"content\":{\"accountNo\":\"UA02727473217971001\",\"extMark\":\"{\\\"loginName\\\":\\\"jsbfhqxo\\\",\\\"mobile\\\":\\\"13260123054\\\"}\",\"merchantNo\":\"M02689149095591001\",\"newPhone\":\"13260123053\",\"notifyUrl\":\"http://39.107.217.19:10003/callback/notify-url/phone_update\",\"orderDate\":\"20180827\",\"orderNo\":\"20180827000000000021\",\"returnUrl\":\"http://qa.tuotiansudai.com:10001/callback/return-url/phone_update\",\"status\":1,\"type\":2,\"userName\":\"UU02727473214331001\"},\"retCode\":\"0000\",\"retMsg\":\"操作成功\",\"sign\":\"sign\"}";
        when(updateMapper.updateNotifyResponseData(anyString(), any(ResponseDto.class))).thenReturn(1);
        HashOperations hashOperations = mock(HashOperations.class);
        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        when(hashOperations.get(anyString(), anyString())).thenReturn("{\"status\":true,\"message\":\"message\",\"loginName\":\"loginName\",\"bankAccountNo\":\"bankAccountNo\",\"bankUserName\":\"bankUserName\",\"newPhone\":\"newPhone\"}");

        ResponseDto responseDto = phoneUpdateService.notifyCallback(responseStr);
        verify(updateMapper, times(1)).updateNotifyResponseData(anyString(), any(ResponseDto.class));
        verify(messageQueueClient, times(1)).sendMessage(eq(MessageQueue.BankChangeMobile), any(BankChangeMobileMessage.class));
        assertNotNull(responseDto);
        assertEquals(true, responseDto.isSuccess());
    }

    @Test
    public void notifyCallbackFalse() {
        ResponseDto responseDto = phoneUpdateService.notifyCallback("");
        assertNull(responseDto);
    }

    @Test
    public void notifyCallbackFalseStatus() {
        String responseStr = "{\"certInfo\":\"certInfo\",\"content\":{\"accountNo\":\"UA02727473217971001\",\"extMark\":\"{\\\"loginName\\\":\\\"jsbfhqxo\\\",\\\"mobile\\\":\\\"13260123054\\\"}\",\"merchantNo\":\"M02689149095591001\",\"newPhone\":\"13260123053\",\"notifyUrl\":\"http://39.107.217.19:10003/callback/notify-url/phone_update\",\"orderDate\":\"20180827\",\"orderNo\":\"20180827000000000021\",\"returnUrl\":\"http://qa.tuotiansudai.com:10001/callback/return-url/phone_update\",\"status\":1,\"type\":2,\"userName\":\"UU02727473214331001\"},\"retCode\":\"0000\",\"retMsg\":\"操作成功\",\"sign\":\"sign\"}";
        when(updateMapper.updateNotifyResponseData(anyString(), any(ResponseDto.class))).thenReturn(0);
        HashOperations hashOperations = mock(HashOperations.class);
        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        when(hashOperations.get(anyString(), anyString())).thenReturn("{\"status\":true,\"message\":\"message\",\"loginName\":\"loginName\",\"bankAccountNo\":\"bankAccountNo\",\"bankUserName\":\"bankUserName\",\"newPhone\":\"newPhone\"}");

        ResponseDto responseDto = phoneUpdateService.notifyCallback(responseStr);
        verify(updateMapper, times(1)).updateNotifyResponseData(anyString(), any(ResponseDto.class));
        verify(messageQueueClient, times(0)).sendMessage(eq(MessageQueue.BankChangeMobile), any(BankChangeMobileMessage.class));
        assertNotNull(responseDto);
    }


}
