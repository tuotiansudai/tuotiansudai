package com.tuotiansudai.web.utils;


import com.tuotiansudai.repository.model.Environment;
import com.tuotiansudai.util.CaptchaHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-security.xml", "classpath:applicationContext.xml"})
@Transactional
public class CaptchaHelperTest {

    @Autowired
    private CaptchaHelper captchaHelper;
    @Value("${common.environment}")
    private Environment environment;
    @Value("${common.fake.captcha}")
    private String fakeCaptcha;

    @Test
    public void shouldCaptchaVerifyIsOk(){
        boolean flag = captchaHelper.captchaVerify(CaptchaHelper.LOGIN_CAPTCHA,fakeCaptcha);
        if(Environment.PRODUCTION != environment){
            assertTrue(flag);
        }
    }
}
