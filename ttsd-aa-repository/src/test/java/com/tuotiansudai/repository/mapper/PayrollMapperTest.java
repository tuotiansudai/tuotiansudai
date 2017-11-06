package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.PayrollModel;
import com.tuotiansudai.repository.model.PayrollStatusType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class PayrollMapperTest {

    @Autowired
    private PayrollMapper payrollMapper;

    @Test
    public void shouldCreatePayrollSuccess() throws Exception {
        PayrollModel payrollModel = new PayrollModel();
        payrollModel.setId(1000001111L);
        payrollModel.setTitle("test代发工资");
        payrollModel.setTotalAmount(10000);
        payrollModel.setHeadCount(10);
        payrollModel.setCreatedBy("testuserl");
        payrollModel.setCreatedTime(new Date());
        payrollModel.setStatus(PayrollStatusType.PENDING);
        payrollMapper.create(payrollModel);

        PayrollModel PayrollModel1 = payrollMapper.findById(payrollModel.getId());
        assertThat(PayrollModel1.getTitle(), is("test代发工资"));
        assertThat(PayrollModel1.getTotalAmount(), is(10000L));
        assertThat(PayrollModel1.getHeadCount(), is(10L));
        assertThat(PayrollModel1.getCreatedBy(), is("testuserl"));
        assertThat(PayrollModel1.getStatus(), is(PayrollStatusType.PENDING));
    }

    @Test
    public void shouldUpdatePayrollSuccess() throws Exception {
        PayrollModel payrollModel = new PayrollModel();
        payrollModel.setId(1000001111L);
        payrollModel.setTitle("test代发工资");
        payrollModel.setTotalAmount(10000);
        payrollModel.setHeadCount(10);
        payrollModel.setCreatedBy("testuserl");
        payrollModel.setCreatedTime(new Date());
        payrollModel.setStatus(PayrollStatusType.PENDING);
        payrollMapper.create(payrollModel);

        PayrollModel payrollModel1 = new PayrollModel();
        payrollModel1.setId(payrollModel.getId());
        payrollModel1.setTitle("test代发工资222");
        payrollModel1.setTotalAmount(20000L);
        payrollModel1.setHeadCount(30L);
        payrollModel1.setUpdatedBy("testuser2");
        payrollModel1.setUpdatedTime(new Date());
        payrollModel1.setStatus(PayrollStatusType.PENDING);
        payrollMapper.update(payrollModel1);

        PayrollModel PayrollModel2 = payrollMapper.findById(payrollModel.getId());
        assertThat(PayrollModel2.getTitle(), is("test代发工资222"));
        assertThat(PayrollModel2.getTotalAmount(), is(20000L));
        assertThat(PayrollModel2.getHeadCount(), is(30L));
        assertThat(PayrollModel2.getStatus(), is(PayrollStatusType.PENDING));
    }
}
