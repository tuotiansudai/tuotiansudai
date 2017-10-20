package com.tuotiansudai.smswrapper.repository.mapper;

import com.tuotiansudai.smswrapper.repository.model.SmsModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class TransferLoanNotifyMapperTest {
    @Autowired
    TransferLoanNotifyMapper transferLoanNotifyMapper;

    @Test
    public void testAllMethods() throws Exception {
        SmsModel smsModel = new SmsModel("18311112222", "testContent1", "0001");
        transferLoanNotifyMapper.create(smsModel);
        smsModel.setContent("testContent2");
        transferLoanNotifyMapper.create(smsModel);
        smsModel.setMobile("18300001111");
        transferLoanNotifyMapper.create(smsModel);
        List<SmsModel> smsModelList = transferLoanNotifyMapper.findByMobile("18311112222");
        assertEquals(2, smsModelList.size());
        smsModelList = transferLoanNotifyMapper.findByMobile("18300001111");
        assertEquals(1, smsModelList.size());
    }
}
