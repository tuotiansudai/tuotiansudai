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
public class LoanRepayNotifyMapperTest {

    @Autowired
    private LoanRepayNotifyMapper loanRepayNotifyMapper;

    @Test
    @Transactional
    public void shouldCreateLoanRepayNotify() throws Exception {
        Date now = new Date();
        SmsModel model = new SmsModel();
        model.setMobile("13800000000");
        model.setContent("content");
        model.setSendTime(now);
        model.setResultCode("result");

        loanRepayNotifyMapper.create(model);

        List<SmsModel> models = loanRepayNotifyMapper.findByMobile(model.getMobile());

        assertThat(models.size(), is(1));
    }
}
