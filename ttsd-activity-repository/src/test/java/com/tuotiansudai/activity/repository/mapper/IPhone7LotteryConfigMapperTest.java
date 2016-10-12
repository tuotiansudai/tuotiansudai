package com.tuotiansudai.activity.repository.mapper;

import com.tuotiansudai.activity.repository.model.IPhone7LotteryConfigModel;
import com.tuotiansudai.activity.repository.model.IPhone7LotteryConfigStatus;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

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

        List<IPhone7LotteryConfigModel> modelList = mapper.findByStatus(IPhone7LotteryConfigStatus.TO_APPROVE);
        assertNotNull(modelList);
        assertEquals(1, modelList.size());

        IPhone7LotteryConfigModel dbModel = modelList.get(0);
        assertNotEquals(0, dbModel.getId());
        assertEquals(model.getId(), dbModel.getId());
        assertEquals(model.getLotteryNumber(), dbModel.getLotteryNumber());
        assertEquals(model.getPeriod(), dbModel.getPeriod());
        assertNull(dbModel.getEffectiveTime());
        assertNotNull(dbModel.getCreatedTime());
        assertEquals(model.getCreatedBy(), dbModel.getCreatedBy());

        List<IPhone7LotteryConfigModel> approvedModelList = mapper.findByStatus(IPhone7LotteryConfigStatus.APPROVED);
        assertEquals(0, approvedModelList.size());

        int removeCount = mapper.removeUnApprovedConfig(model.getId());
        assertEquals(1, removeCount);
        List<IPhone7LotteryConfigModel> modelList2 = mapper.findByStatus(IPhone7LotteryConfigStatus.TO_APPROVE);
        assertEquals(0, modelList2.size());
    }

    @Test
    public void shouldApproveSuccess() {
        String testLoginName = "loginName";
        String testAuditedLoginName = "auditedUser";
        IPhone7LotteryConfigModel model = generateTestModel(testLoginName);
        mapper.create(model);

        List<IPhone7LotteryConfigModel> unApprovedModelList = mapper.findByStatus(IPhone7LotteryConfigStatus.TO_APPROVE);
        assertEquals(1, unApprovedModelList.size());

        List<IPhone7LotteryConfigModel> approvedModelList = mapper.findByStatus(IPhone7LotteryConfigStatus.APPROVED);
        assertEquals(0, approvedModelList.size());

        mapper.approve(model.getId(), testAuditedLoginName, new Date());

        unApprovedModelList = mapper.findByStatus(IPhone7LotteryConfigStatus.TO_APPROVE);
        assertEquals(0, unApprovedModelList.size());

        approvedModelList = mapper.findByStatus(IPhone7LotteryConfigStatus.APPROVED);
        assertEquals(1, approvedModelList.size());

        IPhone7LotteryConfigModel approvedModel = approvedModelList.get(0);
        assertEquals(testAuditedLoginName, approvedModel.getAuditedBy());
        assertEquals(IPhone7LotteryConfigStatus.APPROVED, approvedModel.getStatus());

        mapper.removeApprovedConfig(model.getPeriod());
        unApprovedModelList = mapper.findByStatus(IPhone7LotteryConfigStatus.TO_APPROVE);
        assertEquals(0, unApprovedModelList.size());

        approvedModelList = mapper.findByStatus(IPhone7LotteryConfigStatus.APPROVED);
        assertEquals(0, approvedModelList.size());
    }

    private IPhone7LotteryConfigModel generateTestModel(String testLoginName) {
        IPhone7LotteryConfigModel model = new IPhone7LotteryConfigModel();
        model.setLotteryNumber("111122");
        model.setCreatedBy(testLoginName);
        model.setAuditedBy(testLoginName);
        model.setAuditedTime(new Date());
        model.setCreatedTime(new Date());
        model.setEffectiveTime(new Date());
        model.setPeriod(3);
        return model;
    }
}
