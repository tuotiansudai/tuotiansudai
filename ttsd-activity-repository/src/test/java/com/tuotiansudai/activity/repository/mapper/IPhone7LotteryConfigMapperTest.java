package com.tuotiansudai.activity.repository.mapper;

import com.tuotiansudai.activity.repository.model.IPhone7LotteryConfigModel;
import com.tuotiansudai.activity.repository.model.IPhone7LotteryConfigStatus;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static org.junit.Assert.*;

public class IPhone7LotteryConfigMapperTest extends BaseMapperTest {
    @Autowired
    private IPhone7LotteryConfigMapper mapper;

    @Test
    public void shouldCreateAndRemoveSuccess() {
        String testLoginName = "loginName";
        IPhone7LotteryConfigModel model = generateTestModel(testLoginName);

        int effectiveCount = mapper.create(model);
        assertEquals(1, effectiveCount);

        IPhone7LotteryConfigModel dbModel = mapper.findById(model.getId());
        assertNotNull(dbModel);
        assertNotEquals(0, dbModel.getId());
        assertEquals(model.getId(), dbModel.getId());
        assertEquals(model.getLotteryNumber(), dbModel.getLotteryNumber());
        assertEquals(model.getInvestAmount(), dbModel.getInvestAmount());
        assertNull(dbModel.getEffectiveTime());
        assertNotNull(dbModel.getCreatedTime());
        assertEquals(model.getCreatedBy(), dbModel.getCreatedBy());
        assertEquals(IPhone7LotteryConfigStatus.TO_APPROVE, dbModel.getStatus());

        mapper.removeUnApprovedConfig(model.getId());
        dbModel = mapper.findById(model.getId());
        assertNull(dbModel);
    }

    @Test
    public void shouldApproveSuccess() {
        String testLoginName = "loginName";
        String testAuditedLoginName = "auditedUser";
        IPhone7LotteryConfigModel model = generateTestModel(testLoginName);
        mapper.create(model);
        mapper.approve(model.getId(), testAuditedLoginName, new Date());
        IPhone7LotteryConfigModel dbModel = mapper.findById(model.getId());
        assertEquals(testAuditedLoginName, dbModel.getAuditedBy());
        assertEquals(IPhone7LotteryConfigStatus.APPROVED, dbModel.getStatus());

        mapper.removeApprovedConfig(model.getInvestAmount());
        dbModel = mapper.findById(model.getId());
        assertNull(dbModel);
    }

    private IPhone7LotteryConfigModel generateTestModel(String testLoginName) {
        IPhone7LotteryConfigModel model = new IPhone7LotteryConfigModel();
        model.setLotteryNumber("111122");
        model.setCreatedBy(testLoginName);
        model.setAuditedBy(testLoginName);
        model.setAuditedTime(new Date());
        model.setCreatedTime(new Date());
        model.setEffectiveTime(new Date());
        model.setInvestAmount(50);
        return model;
    }
}
