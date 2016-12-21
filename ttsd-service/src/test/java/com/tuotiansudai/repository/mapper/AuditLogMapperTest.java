package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.AuditLogModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import com.tuotiansudai.task.OperationType;
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
public class AuditLogMapperTest {

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private AuditLogMapper auditLogMapper;

    @Autowired
    private UserMapper userMapper;

    @Test
    public void shouldCreateAndFind() {
        String operator = "aaaaaa";
        String auditor = "bbbbbb";

        createUserByUserId(operator);
        createUserByUserId(auditor);

        AuditLogModel auditLogModel = buildMockedAuditLogModel(operator, auditor);
        auditLogMapper.create(auditLogModel);
        AuditLogModel getModel = auditLogMapper.findById(auditLogModel.getId());

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

    private AuditLogModel buildMockedAuditLogModel(String operator, String auditor) {
        AuditLogModel log = new AuditLogModel();
        log.setId(idGenerator.generate());
        log.setOperatorLoginName(operator);
        log.setAuditorLoginName(auditor);
        log.setTargetId("22222");
        log.setOperationType(OperationType.ACTIVITY);
        log.setIp("111.111.111.111");
        log.setDescription("description");
        return log;
    }

}
