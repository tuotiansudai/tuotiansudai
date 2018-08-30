package com.tuotiansudai.fudian.service;

import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.dto.BankLoanCreateDto;
import com.tuotiansudai.fudian.dto.request.LoanCreateRequestDto;
import com.tuotiansudai.fudian.dto.request.RegisterRequestDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.mapper.fudian.InsertMapper;
import com.tuotiansudai.fudian.mapper.fudian.UpdateMapper;
import com.tuotiansudai.fudian.message.BankLoanCreateMessage;
import com.tuotiansudai.fudian.sign.SignatureHelper;
import com.tuotiansudai.fudian.util.BankClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.*;

/**
 * Created by qduljs2011 on 2018/8/30.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
public class LoanCreateServiceTest {
    @InjectMocks
    private LoanCreateService loanCreateService;
    @Mock
    private SignatureHelper signatureHelper;
    @Mock
    private BankClient bankClient;
    @Mock
    private InsertMapper insertMapper;
    @Mock
    private UpdateMapper updateMapper;

    @Test
    public void createSuccess() {
        ArgumentCaptor<LoanCreateRequestDto> dtoCaptor = ArgumentCaptor.forClass(LoanCreateRequestDto.class);
        String responseStr = "{\"certInfo\":\"certInfo\",\"content\":{\"accountNo\":\"UA02688350925091001\",\"amount\":100.00,\"extMark\":\"\",\"loanAccNo\":\"LA02688394699331001\",\"loanName\":\"房产抵押借款18001\",\"loanTxNo\":\"LU02688394699271001\",\"loanType\":\"1\",\"merchantNo\":\"M02608959047521001\",\"orderDate\":\"20180709\",\"orderNo\":\"20180709000000000008\",\"userName\":\"UU02688350925031001\"},\"retCode\":\"0000\",\"retMsg\":\"操作成功\",\"sign\":\"sign\"}";

        doNothing().when(signatureHelper).sign(any(), argThat(new ArgumentMatcher<RegisterRequestDto>() {
            @Override
            public boolean matches(Object o) {
                ((LoanCreateRequestDto) o).setOrderNo("111111");
                ((LoanCreateRequestDto) o).setRequestData("requestData");
                return false;
            }
        }));
        when(bankClient.send(any(ApiType.class), anyString())).thenReturn(responseStr);
        when(signatureHelper.verifySign(eq(responseStr))).thenReturn(true);
        BankLoanCreateMessage bankLoanCreateMessage = loanCreateService.create(new BankLoanCreateDto());

        verify(insertMapper, times(1)).insertLoanCreate(dtoCaptor.capture());
        verify(updateMapper, times(1)).updateNotifyResponseData(anyString(), any(ResponseDto.class));
        assertNotNull(bankLoanCreateMessage);
        assertEquals(true, bankLoanCreateMessage.isStatus());
    }

    @Test
    public void createFalseSign() {
        BankLoanCreateMessage bankLoanCreateMessage = loanCreateService.create(new BankLoanCreateDto());
        verify(insertMapper, times(0)).insertLoanCreate(any(LoanCreateRequestDto.class));
        assertNotNull(bankLoanCreateMessage);
        assertEquals(false, bankLoanCreateMessage.isStatus());
    }

    @Test
    public void createFalseVerifySign() {
        doNothing().when(signatureHelper).sign(any(), argThat(new ArgumentMatcher<RegisterRequestDto>() {
            @Override
            public boolean matches(Object o) {
                ((LoanCreateRequestDto) o).setOrderNo("111111");
                ((LoanCreateRequestDto) o).setRequestData("requestData");
                return false;
            }
        }));
        when(bankClient.send(any(ApiType.class), anyString())).thenReturn(null);
        BankLoanCreateMessage bankLoanCreateMessage = loanCreateService.create(new BankLoanCreateDto());
        verify(insertMapper, times(1)).insertLoanCreate(any(LoanCreateRequestDto.class));
        verify(updateMapper, times(0)).updateNotifyResponseData(anyString(), any(ResponseDto.class));
        assertNotNull(bankLoanCreateMessage);
        assertEquals(false, bankLoanCreateMessage.isStatus());
    }
}
