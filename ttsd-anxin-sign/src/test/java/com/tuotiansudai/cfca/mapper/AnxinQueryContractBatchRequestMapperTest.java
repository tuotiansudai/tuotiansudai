package com.tuotiansudai.cfca.mapper;

import com.tuotiansudai.cfca.model.AnxinQueryContractBatchRequestModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class AnxinQueryContractBatchRequestMapperTest {

    @Autowired
    private AnxinQueryContractBatchRequestMapper anxinQueryContractBatchRequestMapper;

    @Test
    public void shouldCreateIsOk(){
        AnxinQueryContractBatchRequestModel anxinQueryContractBatchRequestModel = new AnxinQueryContractBatchRequestModel();
        anxinQueryContractBatchRequestModel.setBatchNo("t");
        anxinQueryContractBatchRequestModel.setCreatedTime(new Date());
        anxinQueryContractBatchRequestModel.setTxTime("123");
        anxinQueryContractBatchRequestMapper.create(anxinQueryContractBatchRequestModel);
    }
}
