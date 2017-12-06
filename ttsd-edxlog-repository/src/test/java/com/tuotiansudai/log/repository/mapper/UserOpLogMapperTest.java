package com.tuotiansudai.log.repository.mapper;

import com.tuotiansudai.enums.UserOpType;
import com.tuotiansudai.log.repository.model.UserOpLogModel;
import com.tuotiansudai.repository.model.Source;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})@Transactional
public class UserOpLogMapperTest {

    @Autowired
    private UserOpLogMapper userOpLogMapper;

    @Test
    public void shouldCreateAndFind() {
        String loginName = "aaaa";
        UserOpLogModel userOpLogModel = getUserOpLogModel(loginName);
        userOpLogMapper.create(userOpLogModel);
        UserOpLogModel getModel = userOpLogMapper.findById(userOpLogModel.getId());
        assertNotNull(getModel);
    }

    private UserOpLogModel getUserOpLogModel(String loginName) {
        UserOpLogModel logModel = new UserOpLogModel();
        logModel.setId(1111111);
        logModel.setLoginName(loginName);
        logModel.setMobile("13311112222");
        logModel.setIp("111.11.11.1");
        logModel.setDeviceId("asdf");
        logModel.setSource(Source.WEB);
        logModel.setOpType(UserOpType.AUTO_INVEST);
        logModel.setCreatedTime(new Date());
        logModel.setDescription("Turn Off");
        return logModel;
    }
}
