package com.tuotiansudai.paywrapper.validation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class DailyValidationTest {

    @Resource(name = "extraRateDailyValidation")
    private DailyValidation extraRateDailyValidation;

    @Test
    public void name() throws Exception {
//        ValidationReport validate = extraRateDailyValidation.validate();
//        assertNotNull(validate);
    }
}
