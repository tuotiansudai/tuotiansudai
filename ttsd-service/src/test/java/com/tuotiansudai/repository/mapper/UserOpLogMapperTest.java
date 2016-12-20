package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class UserOpLogMapperTest {

    @Autowired
    private UserOpLogMapper userOpLogMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private IdGenerator idGenerator;

    @Test
    public void shouldCreateAndFind() {
        String loginName = "aaaa";
        createUserByUserId(loginName);
        UserOpLogModel userOpLogModel = getUserOpLogModel(loginName);
        userOpLogMapper.create(userOpLogModel);
        UserOpLogModel getModel = userOpLogMapper.findById(userOpLogModel.getId());
        assertNotNull(getModel);
    }

    private void createUserByUserId(String userId) {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName(userId);
        userModelTest.setPassword("123abc");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile("1" + RandomStringUtils.randomNumeric(10));
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(userModelTest);
    }

    private UserOpLogModel getUserOpLogModel(String loginName) {
        UserOpLogModel logModel = new UserOpLogModel();
        logModel.setId(idGenerator.generate());
        logModel.setLoginName(loginName);
        logModel.setIp("111.11.11.1");
        logModel.setDeviceId("asdf");
        logModel.setSource(Source.WEB);
        logModel.setOpType(UserOpType.AUTO_INVEST);
        logModel.setCreatedTime(new Date());
        logModel.setDescription("Turn Off");
        return logModel;
    }
}
