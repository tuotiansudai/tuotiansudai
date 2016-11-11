package com.tuotiansudai.cfca.mapper;


import com.tuotiansudai.cfca.model.AnxinQueryContractRequestModel;
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
public class AnxinQueryContractRequestMapperTest {

    @Autowired
    private AnxinQueryContractRequestMapper anxinQueryContractRequestMapper;

    @Test
    public void shouldCreateIsOk(){
        AnxinQueryContractRequestModel anxinQueryContractRequestModel = new AnxinQueryContractRequestModel();
        anxinQueryContractRequestModel.setBatchNo("t");
        anxinQueryContractRequestModel.setCreatedTime(DateTime.now().toDate());
        anxinQueryContractRequestModel.setTxTime("123");
        anxinQueryContractRequestMapper.create(anxinQueryContractRequestModel);
    }
}
