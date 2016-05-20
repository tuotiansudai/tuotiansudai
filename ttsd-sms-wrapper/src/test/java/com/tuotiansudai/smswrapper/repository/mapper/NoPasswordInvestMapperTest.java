package com.tuotiansudai.smswrapper.repository.mapper;

import com.tuotiansudai.smswrapper.repository.model.SmsModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:applicationContext.xml"})
public class NoPasswordInvestMapperTest {

    @Autowired
    private TurnOffNoPasswordInvestCaptchaMapper noPasswordInvestMapper;

    @Test
    @Transactional
    public void shouldCreateNoPasswordInvestCaptcha() throws Exception {
        Date now = new Date();
        SmsModel model = new SmsModel();
        model.setMobile("13800000000");
        model.setContent("content");
        model.setSendTime(now);
        model.setResultCode("result");

        noPasswordInvestMapper.create(model);

        List<SmsModel> models = noPasswordInvestMapper.findByMobile(model.getMobile());

        assertThat(models.size(), is(1));
    }
}
