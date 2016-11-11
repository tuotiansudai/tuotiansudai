package com.tuotiansudai.cfca.mapper;


import com.tuotiansudai.cfca.model.AnxinQueryContractResponseModel;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class AnxinQueryContractResponseMapperTest {

    @Autowired
    private AnxinQueryContractResponseMapper anxinQueryContractResponseMapper;

    @Test
    public void shouldCreateIsOk() {
        AnxinQueryContractResponseModel anxinQueryContractResponseModel = new AnxinQueryContractResponseModel();
        anxinQueryContractResponseModel.setTxTime("1");
        anxinQueryContractResponseModel.setCreatedTime(DateTime.now().toDate());
        anxinQueryContractResponseModel.setBatchNo("1");
        anxinQueryContractResponseModel.setRetCode("1");
        anxinQueryContractResponseModel.setRetMessage("1");
        anxinQueryContractResponseModel.setContractNo("1");
        anxinQueryContractResponseModel.setBusinessId(1l);
        anxinQueryContractResponseMapper.create(anxinQueryContractResponseModel);
    }
}
