package com.tuotiansudai.activity.service;


import com.google.common.base.Strings;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class RedEnvelopSplitActivityServiceTest {

    @InjectMocks
    private RedEnvelopSplitActivityService redEnvelopSplitActivityService;

    @Mock
    private UserMapper userMapper;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldGetShareReferrerUrlIs() throws UnsupportedEncodingException {
        UserModel userModel = getUserModelTest();
        when(userMapper.findByLoginName(userModel.getLoginName())).thenReturn(userModel);

        String url = redEnvelopSplitActivityService.getShareReferrerUrl(userModel.getLoginName());
        String base64 = url.substring(url.indexOf("param=") + 6, url.length());

        String json = new String(Base64.getDecoder().decode(base64), "utf-8");
        assertTrue(!Strings.isNullOrEmpty(url));
        assertEquals(json, "{\"title\":\"您的好友王琦送你三重好礼\",\"description\":\"完成注册即可领取8.88元现金红包+5888元体验金+588元优惠券\",\"shareUrl\":\"\"}");
    }

    private UserModel getUserModelTest() {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName("helloworld");
        userModelTest.setUserName("王琦");
        userModelTest.setPassword("123abc");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile("13900000000");
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        return userModelTest;
    }
}
