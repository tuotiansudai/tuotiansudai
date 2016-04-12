package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class SystemBillMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SystemBillMapper systemBillMapper;

    @Test
    public void shouldCreateSystemBill() throws Exception {
        SystemBillModel systemBillModel = new SystemBillModel();
        systemBillModel.setCreatedTime(new Date());
        systemBillModel.setDetail("detail");
        systemBillModel.setAmount(1);
        systemBillModel.setBusinessType(SystemBillBusinessType.BIND_BANK_CARD);
        systemBillModel.setOrderId(11111111111111L);
        systemBillModel.setOperationType(SystemBillOperationType.OUT);
        systemBillMapper.create(systemBillModel);
        assertTrue(systemBillModel.getId() > 0);

    }



}
