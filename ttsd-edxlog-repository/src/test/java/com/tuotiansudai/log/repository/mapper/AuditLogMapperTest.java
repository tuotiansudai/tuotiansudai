package com.tuotiansudai.log.repository.mapper;

import com.tuotiansudai.enums.OperationType;
import com.tuotiansudai.log.repository.model.AuditLogModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})@Transactional
public class AuditLogMapperTest {


    @Autowired
    private AuditLogMapper auditLogMapper;


    @Test
    public void shouldCreateAndFind() {
        String operator = "aaaaaa";
        String auditor = "bbbbbb";

        AuditLogModel auditLogModel = buildMockedAuditLogModel(operator, auditor);
        auditLogMapper.create(auditLogModel);
        AuditLogModel getModel = auditLogMapper.findById(auditLogModel.getId());

        assertNotNull(getModel);
    }

    private AuditLogModel buildMockedAuditLogModel(String operator, String auditor) {
        AuditLogModel log = new AuditLogModel();
        log.setId(11111111);
        log.setOperatorLoginName(operator);
        log.setOperatorMobile("13333331111");
        log.setAuditorLoginName(auditor);
        log.setAuditorMobile("13311112222");
        log.setTargetId("22222");
        log.setOperationType(OperationType.ACTIVITY);
        log.setIp("111.111.111.111");
        log.setDescription("description");
        return log;
    }

}
