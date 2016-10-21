package com.tuotiansudai.activity.repository.mapper;

import com.tuotiansudai.activity.repository.model.IPhone7InvestLotteryModel;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class IPhone7InvestLotteryMapperTest extends BaseMapperTest {
    @Autowired
    private IPhone7InvestLotteryMapper mapper;

    @Test
    public void shouldCreateIsSuccess() {
        String testLoginName = "loginName";
        IPhone7InvestLotteryModel model = new IPhone7InvestLotteryModel();
        model.setInvestAmount(20000);
        model.setInvestId(100000);
        model.setLoginName(testLoginName);
        model.setLotteryNumber("111122");
        model.setLotteryTime(new Date());

        int effectiveCount = mapper.create(model);
        assertEquals(1, effectiveCount);

        List<IPhone7InvestLotteryModel> modelList = mapper.findByLoginName(testLoginName);
        assertNotNull(modelList);
        assertEquals(1, modelList.size());

        IPhone7InvestLotteryModel dbModel = modelList.get(0);
        assertEquals(model.getId(), dbModel.getId());
        assertEquals(model.getInvestAmount(), dbModel.getInvestAmount());
        assertEquals(model.getInvestId(), dbModel.getInvestId());
        assertEquals(model.getLotteryNumber(), dbModel.getLotteryNumber());
    }
}
