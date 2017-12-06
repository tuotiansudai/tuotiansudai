package com.tuotiansudai.cfca.mapper;

import com.tuotiansudai.cfca.model.AnxinCreateAccountRequestModel;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})@Transactional
public class AnxinCreateAccountRequestMapperTest {

    @Autowired
    private AnxinCreateAccountRequestMapper mapper;

    @Test
    public void shouldCreate() {
        AnxinCreateAccountRequestModel model = new AnxinCreateAccountRequestModel();
        model.setTxTime(DateTime.now().toString("yyyyMMddHHmmss"));
        model.setPersonName("周宝鑫");
        model.setIdentTypeCode("0");
        model.setIdentNo("123456789012345678");
        model.setEmail("abcdefg@ttt.com");
        model.setMobilePhone("13288887777");
        model.setAddress("北京阿里山的咖啡机阿克苏的发生了空间啊");
        model.setAuthenticationMode("公安部");
        model.setNotSendPwd("0");
        model.setCreatedTime(new Date());

        mapper.create(model);
    }
}
