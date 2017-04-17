package com.tuotiansudai.cfca.mapper;

import com.tuotiansudai.cfca.model.AnxinQueryContractBatchResponseModel;
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
public class AnxinQueryContractBatchResponseMapperTest {

    @Autowired
    private AnxinQueryContractBatchResponseMapper anxinQueryContractBatchResponseMapper;

    @Test
    public void shouldCreateIsOk() {
        AnxinQueryContractBatchResponseModel anxinQueryContractBatchResponseModel = new AnxinQueryContractBatchResponseModel();
        anxinQueryContractBatchResponseModel.setTxTime("1");
        anxinQueryContractBatchResponseModel.setCreatedTime(new Date());
        anxinQueryContractBatchResponseModel.setBatchNo("1");
        anxinQueryContractBatchResponseModel.setRetCode("1");
        anxinQueryContractBatchResponseModel.setRetMessage("1");
        anxinQueryContractBatchResponseModel.setContractNo("1");
        anxinQueryContractBatchResponseModel.setBusinessId(1l);
        anxinQueryContractBatchResponseMapper.create(anxinQueryContractBatchResponseModel);
    }
}
