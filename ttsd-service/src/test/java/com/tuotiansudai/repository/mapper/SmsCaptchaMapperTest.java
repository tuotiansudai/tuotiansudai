package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.CaptchaType;
import com.tuotiansudai.repository.model.SmsCaptchaModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class SmsCaptchaMapperTest {
    @Autowired
    private SmsCaptchaMapper smsCaptchaMapper;

    @Test
    @Transactional
    public void shouldCreateSmsCaptcha() {
        SmsCaptchaModel smsCaptchaModel = this.createSmsCaptchaModel();
        smsCaptchaMapper.create(smsCaptchaModel);

        SmsCaptchaModel actualModel = smsCaptchaMapper.findByMobile("13900000000");
        assertNotNull(actualModel);
    }

    @Test
    @Transactional
    public void shouldModifySmsCaptcha() {
        SmsCaptchaModel smsCaptchaModel1 = this.createSmsCaptchaModel();
        smsCaptchaMapper.create(smsCaptchaModel1);

        SmsCaptchaModel smsCaptchaModel3 = smsCaptchaMapper.findByMobile("13900000000");
        int delayedMinute = 10;
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MINUTE, delayedMinute);
        smsCaptchaModel3.setCaptcha("1002");
        smsCaptchaModel3.setExpiredTime(cal.getTime());
        smsCaptchaModel3.setCreatedTime(new Date());
        smsCaptchaMapper.update(smsCaptchaModel3);

        SmsCaptchaModel smsCaptchaModel4 = smsCaptchaMapper.findByMobile("13900000000");
        assertNotNull(smsCaptchaModel4);
        assertEquals("1002", smsCaptchaModel4.getCaptcha());
    }

    @Test
    @Transactional
    public void shouldFindSmsCaptchaByMobileIsNotNull() {
        SmsCaptchaModel smsCaptchaModel1 = this.createSmsCaptchaModel();
        smsCaptchaMapper.create(smsCaptchaModel1);

        SmsCaptchaModel smsCaptchaModel = smsCaptchaMapper.findByMobile("13900000000");

        assertNotNull(smsCaptchaModel);

        assertEquals("13900000000", smsCaptchaModel.getMobile());
        assertEquals("12345", smsCaptchaModel.getCaptcha());
    }

    @Test
    @Transactional
    public void shouldFindSmsCaptchaByMobileIsNull() {
        SmsCaptchaModel smsCaptchaModel = smsCaptchaMapper.findByMobile("12345678900");
        assertNull(smsCaptchaModel);
    }

    private SmsCaptchaModel createSmsCaptchaModel() {
        SmsCaptchaModel smsCaptchaModel = new SmsCaptchaModel();
        smsCaptchaModel.setCaptcha("12345");
        smsCaptchaModel.setMobile("13900000000");
        smsCaptchaModel.setExpiredTime(new Date());
        smsCaptchaModel.setCreatedTime(new Date());
        smsCaptchaModel.setCaptchaType(CaptchaType.REGISTER_CAPTCHA);
        return smsCaptchaModel;
    }

}
