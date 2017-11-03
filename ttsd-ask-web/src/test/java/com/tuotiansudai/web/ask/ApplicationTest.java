package com.tuotiansudai.web.ask;


import com.tuotiansudai.web.ask.controller.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml", "classpath:dispatcher-servlet.xml", "classpath:spring-session.xml"})@WebAppConfiguration
@Transactional
public class ApplicationTest {
    @Autowired
    private AnswerController answerController;
    @Autowired
    private CaptchaController captchaController;
    @Autowired
    private ErrorController errorController;
    @Autowired
    private HomeController homeController;
    @Autowired
    private QuestionController questionController;

    @Test
    public void shouldLoadContext() {
        assertNotNull(answerController);
        assertNotNull(captchaController);
        assertNotNull(errorController);
        assertNotNull(homeController);
        assertNotNull(questionController);
    }
}
