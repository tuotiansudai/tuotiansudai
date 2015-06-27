package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.CaptchaStatus;
import com.tuotiansudai.repository.model.CaptchaType;
import com.tuotiansudai.repository.model.SmsCaptchaModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;

import static junit.framework.Assert.*;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@TransactionConfiguration
public class SmsCaptchaMapperTest {
    @Autowired
    private SmsCaptchaMapper smsCaptchaMapper;

    @Test
    @Transactional
    public void shouldCreateSmsCaptcha() {
        SmsCaptchaModel smsCaptchaModel = this.getSmsCaptchaModeal();
        smsCaptchaMapper.insertSmsCaptcha(smsCaptchaModel);

        SmsCaptchaModel smsCaptchaModel1 = smsCaptchaMapper.findRegisterCaptchaByMobile("13900000000");
        assertNotNull(smsCaptchaModel1);
    }


    @Test
    @Transactional
    public void shouldModifySmsCaptcha() {
        SmsCaptchaModel smsCaptchaModel1 = this.getSmsCaptchaModeal();
        smsCaptchaMapper.insertSmsCaptcha(smsCaptchaModel1);

        SmsCaptchaModel smsCaptchaModel3 = smsCaptchaMapper.findRegisterCaptchaByMobile("13900000000");
        int delayedMinute = 10;
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MINUTE, delayedMinute);
        smsCaptchaModel3.setCode("1002");
        smsCaptchaModel3.setDeadLine(cal.getTime());
        smsCaptchaModel3.setGenerationTime(new Date());
        smsCaptchaMapper.updateSmsCaptchaByMobile(smsCaptchaModel3);

        SmsCaptchaModel smsCaptchaModel4 = smsCaptchaMapper.findRegisterCaptchaByMobile("13900000000");
        assertNotNull(smsCaptchaModel4);
        assertEquals("1002",smsCaptchaModel4.getCode());

    }

    @Test
    @Transactional
    public void shouldFindSmsCaptchaByMobileIsNotNull() {
        SmsCaptchaModel smsCaptchaModel1 = this.getSmsCaptchaModeal();
        smsCaptchaMapper.insertSmsCaptcha(smsCaptchaModel1);

        SmsCaptchaModel smsCaptchaModel = smsCaptchaMapper.findRegisterCaptchaByMobile("13900000000");

        assertNotNull(smsCaptchaModel);

        assertEquals("13900000000", smsCaptchaModel.getMobile());
        assertEquals("12345",smsCaptchaModel.getCode());



    }

    @Test
    @Transactional
    public void shouldFindSmsCaptchaByMobileIsNull() {
        SmsCaptchaModel smsCaptchaModel = smsCaptchaMapper.findRegisterCaptchaByMobile("12345678900");
        assertNull(smsCaptchaModel);

    }

    @Test
    @Transactional
    public void shouldFindSmsCaptchaByMobileAndCaptchaIsNotNull() {
        SmsCaptchaModel smsCaptchaModel1 = this.getSmsCaptchaModeal();
        smsCaptchaModel1.setStatus(CaptchaStatus.ACTIVE);
        smsCaptchaMapper.insertSmsCaptcha(smsCaptchaModel1);
        SmsCaptchaModel smsCaptchaModel = new SmsCaptchaModel();
        smsCaptchaModel.setMobile("13900000000");
        smsCaptchaModel.setCode("12345");
        smsCaptchaModel.setCaptchaType(CaptchaType.REGISTERCAPTCHA);
        smsCaptchaModel.setStatus(CaptchaStatus.ACTIVE);

        SmsCaptchaModel smsCaptchaModel2 = smsCaptchaMapper.findSmsCaptchaByMobileAndCaptcha(smsCaptchaModel);

        assertNotNull(smsCaptchaModel2);
    }

    @Test
    @Transactional
    public void shouldFindSmsCaptchaByMobileAndCaptchaIsNull() {
        SmsCaptchaModel smsCaptchaModel = new SmsCaptchaModel();
        smsCaptchaModel.setMobile("13900000000");
        smsCaptchaModel.setCode("123456");
        smsCaptchaModel.setCaptchaType(CaptchaType.REGISTERCAPTCHA);
        smsCaptchaModel.setStatus(CaptchaStatus.ACTIVE);

        SmsCaptchaModel smsCaptchaModel2 = smsCaptchaMapper.findSmsCaptchaByMobileAndCaptcha(smsCaptchaModel);

        assertNull(smsCaptchaModel2);
    }

    public SmsCaptchaModel getSmsCaptchaModeal() {
        SmsCaptchaModel smsCaptchaModel = new SmsCaptchaModel();
        smsCaptchaModel.setCode("12345");
        smsCaptchaModel.setMobile("13900000000");
        smsCaptchaModel.setDeadLine(new Date());
        smsCaptchaModel.setGenerationTime(new Date());
        smsCaptchaModel.setStatus(CaptchaStatus.ACTIVE);
        smsCaptchaModel.setCaptchaType(CaptchaType.REGISTERCAPTCHA);
        smsCaptchaModel.setUserId(100001);
        return smsCaptchaModel;
    }

}
