package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.PayrollDetailModel;
import com.tuotiansudai.repository.model.PayrollModel;
import com.tuotiansudai.repository.model.PayrollPayStatus;
import com.tuotiansudai.repository.model.PayrollStatusType;
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
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class PayrollDetailMapperTest {

    @Autowired
    private PayrollMapper payrollMapper;

    @Autowired
    private PayrollDetailMapper payrollDetailMapper;

    @Test
    public void shouldCreatePayrollDetailSuccess() throws Exception {
        PayrollModel payrollModel = new PayrollModel();
        payrollModel.setId(1000001113L);
        payrollModel.setTitle("test代发工资");
        payrollModel.setTotalAmount(10000);
        payrollModel.setHeadCount(2);
        payrollModel.setCreatedBy("testuserl");
        payrollModel.setCreatedTime(new Date());
        payrollModel.setStatus(PayrollStatusType.PENDING);
        payrollMapper.create(payrollModel);

        PayrollDetailModel payrollDetailModel = new PayrollDetailModel();
        payrollDetailModel.setId(1L);
        payrollDetailModel.setPayrollId(payrollModel.getId());
        payrollDetailModel.setAmount(5000);
        payrollDetailModel.setMobile("13900000000");
        payrollDetailModel.setStatus(PayrollPayStatus.WAITING);
        payrollDetailModel.setCreatedTime(new Date());
        payrollDetailModel.setUserName("张山");
        payrollDetailModel.setLoginName("test11");
        payrollDetailMapper.create(payrollDetailModel);

        PayrollDetailModel payrollDetailModel2 = new PayrollDetailModel();
        payrollDetailModel2.setId(2L);
        payrollDetailModel2.setPayrollId(payrollModel.getId());
        payrollDetailModel2.setAmount(5000);
        payrollDetailModel2.setMobile("13900000001");
        payrollDetailModel2.setStatus(PayrollPayStatus.WAITING);
        payrollDetailModel2.setCreatedTime(new Date());
        payrollDetailModel2.setUserName("李四");
        payrollDetailModel2.setLoginName("test12");
        payrollDetailMapper.create(payrollDetailModel);

        List<PayrollDetailModel> payrollDetailModelList = payrollDetailMapper.findByPayrollId(payrollModel.getId());

        assertThat(payrollDetailModelList.size(), is(2));
    }

}
