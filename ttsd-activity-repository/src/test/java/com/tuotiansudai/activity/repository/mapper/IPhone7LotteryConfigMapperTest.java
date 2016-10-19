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
        String lotteryNumber = "1000000";
        IPhone7LotteryConfigModel model = generateTestModel(testLoginName, lotteryNumber);

        List<IPhone7LotteryConfigModel> configModelListBefore = mapper.list();
        assertNotNull(configModelListBefore);

        int effectiveCount = mapper.create(model);
        assertEquals(1, effectiveCount);

        List<IPhone7LotteryConfigModel> configModelListAfter = mapper.list();
        assertEquals(1, configModelListAfter.size() - configModelListBefore.size());

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

        List<IPhone7LotteryConfigModel> configModelList = mapper.findByInvestAmount(50);
        assertNotNull(configModelList);

        mapper.removeUnApprovedConfig(model.getId());
        dbModel = mapper.findById(model.getId());
        assertNull(dbModel);
    }

    @Test
    public void shouldApproveSuccess() {
        String testLoginName = "loginName";
        String testAuditedLoginName = "auditedUser";
        String testLotteryNumber = "2000000";
        List<IPhone7LotteryConfigModel> allApprovedConfigBefore = mapper.findAllApproved();

        IPhone7LotteryConfigModel model = generateTestModel(testLoginName, testLotteryNumber);
        mapper.create(model);
        mapper.approve(model.getId(), testAuditedLoginName, new Date());
        IPhone7LotteryConfigModel dbModel = mapper.findById(model.getId());
        assertNotNull(dbModel);
        assertEquals(testAuditedLoginName, dbModel.getAuditedBy());
        assertEquals(IPhone7LotteryConfigStatus.APPROVED, dbModel.getStatus());

        List<IPhone7LotteryConfigModel> allApprovedConfig = mapper.findAllApproved();
        assertNotNull(allApprovedConfig);
        assertEquals(1, allApprovedConfig.size() - allApprovedConfigBefore.size());

        mapper.removeApprovedConfig(model.getInvestAmount());
        dbModel = mapper.findById(model.getId());
        assertNull(dbModel);

        mapper.create(model);
        int effectiveRowCount = mapper.effective(model.getId());
        assertEquals(1, effectiveRowCount);

        IPhone7LotteryConfigModel model1 = mapper.findEffictiveConfigByLotteryNumber(testLotteryNumber);
        assertNotNull(model1);
        assertEquals(model.getId(), model1.getId());


        List<IPhone7LotteryConfigModel> effectiveList = mapper.effectiveList();
        assertNotNull(effectiveList);

        int currentLotteryInvestAmount = mapper.getCurrentLotteryInvestAmount();
        assertEquals(50, currentLotteryInvestAmount);

        mapper.create(model);
        int removeRowCount = mapper.removeUnApprovedConfig(model.getId());
        assertEquals(1, removeRowCount);

        mapper.create(model);
        int refusedRowCount = mapper.refuse(model.getId(), testAuditedLoginName, new Date());
        assertEquals(1, refusedRowCount);
    }

    private IPhone7LotteryConfigModel generateTestModel(String testLoginName, String lotteryNumber) {
        IPhone7LotteryConfigModel model = new IPhone7LotteryConfigModel();
        model.setLotteryNumber(lotteryNumber);
        model.setMobile("13800138000");
        model.setCreatedBy(testLoginName);
        model.setAuditedBy(testLoginName);
        model.setAuditedTime(new Date());
        model.setCreatedTime(new Date());
        model.setEffectiveTime(new Date());
        model.setInvestAmount(50);
        return model;
    }
}
