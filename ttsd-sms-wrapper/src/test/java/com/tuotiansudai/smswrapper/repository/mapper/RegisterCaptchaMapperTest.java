package com.tuotiansudai.smswrapper.repository.mapper;

import com.tuotiansudai.smswrapper.repository.model.SmsModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.util.Date;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml"})
@TransactionConfiguration
public class RegisterCaptchaMapperTest {

    @Autowired
    private RegisterCaptchaMapper registerCaptchaMapper;

    @Test
    public void shouldCreateRegisterCaptcha() throws Exception {
        Date now = new Date();
        SmsModel model = new SmsModel();
        model.setMobile("13800000000");
        model.setContent("content");
        model.setExt("ext");
        model.setRrid("rrid");
        model.setStime(now);
        model.setSendTime(now);
        model.setResultCode("result");

        registerCaptchaMapper.create(model);

        List<SmsModel> models = registerCaptchaMapper.findByMobile(model.getMobile());

        assertThat(models.size(), is(1));
    }
}
