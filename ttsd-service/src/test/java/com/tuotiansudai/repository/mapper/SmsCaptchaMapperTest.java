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

import java.util.Date;
import java.util.List;

import static junit.framework.Assert.*;

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

        List<SmsCaptchaModel> smsCaptchaMappers = smsCaptchaMapper.findCaptchabyMobile(smsCaptchaModel);
        assertEquals(1, smsCaptchaMappers.size());

    }

    @Test
    @Transactional
    public void shouldModifySmsCaptcha() {
        SmsCaptchaModel smsCaptchaModel1 = this.getSmsCaptchaModeal();
        SmsCaptchaModel smsCaptchaModel2 = this.getSmsCaptchaModeal();
        smsCaptchaModel2.setCode("1001");
        smsCaptchaMapper.insertSmsCaptcha(smsCaptchaModel1);
        smsCaptchaMapper.insertSmsCaptcha(smsCaptchaModel2);

        SmsCaptchaModel smsCaptchaModel3 = new SmsCaptchaModel();
        smsCaptchaModel3.setMobile("13900000000");
        smsCaptchaModel3.setCaptchaType(CaptchaType.MOBILECAPTCHA);
        smsCaptchaModel3.setStatus(CaptchaStatus.INACTIVE);
        smsCaptchaMapper.updateStatusByMobile(smsCaptchaModel3);

        List<SmsCaptchaModel> smsCaptchaMappers = smsCaptchaMapper.findCaptchabyMobile(smsCaptchaModel1);

        for (SmsCaptchaModel sms : smsCaptchaMappers) {
            assertEquals(CaptchaStatus.INACTIVE, sms.getStatus());
        }
        assertEquals(2, smsCaptchaMappers.size());

    }

    @Test
    @Transactional
    public void shouldFindSmsCaptchabyMobileIsNotNull() {
        SmsCaptchaModel smsCaptchaModel1 = this.getSmsCaptchaModeal();
        SmsCaptchaModel smsCaptchaModel2 = this.getSmsCaptchaModeal();
        smsCaptchaModel2.setCode("1234");
        smsCaptchaMapper.insertSmsCaptcha(smsCaptchaModel1);
        smsCaptchaMapper.insertSmsCaptcha(smsCaptchaModel2);

        List<SmsCaptchaModel> smsCaptchaMappers = smsCaptchaMapper.findCaptchabyMobile(smsCaptchaModel1);

        for (SmsCaptchaModel sms : smsCaptchaMappers) {
            assertNotNull(sms);
        }

    }

    @Test
    @Transactional
    public void shouldFindSmsCaptchabyMobileIsNull() {
        SmsCaptchaModel smsCaptchaModel1 = this.getSmsCaptchaModeal();
        smsCaptchaModel1.setMobile("12345678900");
        smsCaptchaModel1.setCaptchaType(CaptchaType.MOBILECAPTCHA);
        List<SmsCaptchaModel> smsCaptchaMappers = smsCaptchaMapper.findCaptchabyMobile(smsCaptchaModel1);
        assertEquals(0, smsCaptchaMappers.size());
    }

    @Test
    @Transactional
    public void shouldFindSmsCaptchaByMobileAndCaptchaIsNotNull() {
        SmsCaptchaModel smsCaptchaModel1 = this.getSmsCaptchaModeal();
        smsCaptchaModel1.setStatus(CaptchaStatus.ACTIVATED);
        smsCaptchaMapper.insertSmsCaptcha(smsCaptchaModel1);
        SmsCaptchaModel smsCaptchaModel = new SmsCaptchaModel();
        smsCaptchaModel.setMobile("13900000000");
        smsCaptchaModel.setCode("12345");
        smsCaptchaModel.setCaptchaType(CaptchaType.MOBILECAPTCHA);
        smsCaptchaModel.setStatus(CaptchaStatus.ACTIVATED);

        SmsCaptchaModel smsCaptchaModel2 = smsCaptchaMapper.findSmsCaptchaByMobileAndCaptcha(smsCaptchaModel);

        assertNotNull(smsCaptchaModel2);
    }

    @Test
    @Transactional
    public void shouldFindSmsCaptchaByMobileAndCaptchaIsNull() {
        SmsCaptchaModel smsCaptchaModel = new SmsCaptchaModel();
        smsCaptchaModel.setMobile("13900000000");
        smsCaptchaModel.setCode("123456");
        smsCaptchaModel.setCaptchaType(CaptchaType.MOBILECAPTCHA);
        smsCaptchaModel.setStatus(CaptchaStatus.ACTIVATED);

        SmsCaptchaModel smsCaptchaModel2 = smsCaptchaMapper.findSmsCaptchaByMobileAndCaptcha(smsCaptchaModel);

        assertNull(smsCaptchaModel2);
    }

    public SmsCaptchaModel getSmsCaptchaModeal() {
        SmsCaptchaModel smsCaptchaModel = new SmsCaptchaModel();
        smsCaptchaModel.setCode("12345");
        smsCaptchaModel.setMobile("13900000000");
        smsCaptchaModel.setDeadLine(new Date());
        smsCaptchaModel.setGenerationTime(new Date());
        smsCaptchaModel.setStatus(CaptchaStatus.ACTIVATED);
        smsCaptchaModel.setCaptchaType(CaptchaType.MOBILECAPTCHA);
        smsCaptchaModel.setUserId(100001);
        return smsCaptchaModel;
    }

}
