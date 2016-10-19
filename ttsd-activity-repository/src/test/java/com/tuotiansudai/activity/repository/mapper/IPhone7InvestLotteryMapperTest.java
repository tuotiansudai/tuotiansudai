package com.tuotiansudai.activity.repository.mapper;

import com.tuotiansudai.activity.repository.model.IPhone7InvestLotteryModel;
import com.tuotiansudai.activity.repository.model.IPhone7InvestLotteryStatView;
import com.tuotiansudai.activity.repository.model.IPhone7InvestLotteryWinnerView;
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
        String lotteryNumber = "1000000";
        IPhone7InvestLotteryModel model = new IPhone7InvestLotteryModel();
        model.setInvestAmount(20000);
        model.setInvestId(100000);
        model.setLoginName(testLoginName);
        model.setLotteryNumber(lotteryNumber);
        model.setLotteryTime(new Date());

        int statUserCountBefore = mapper.statUserCount();

        int statInvestCountBefore = mapper.statInvestCount();

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


        IPhone7InvestLotteryModel dbModel2 = mapper.findByLotteryNumber(lotteryNumber);
        assertEquals(model.getId(), dbModel2.getId());
        assertEquals(model.getInvestAmount(), dbModel2.getInvestAmount());
        assertEquals(model.getInvestId(), dbModel2.getInvestId());
        assertEquals(model.getLotteryNumber(), dbModel2.getLotteryNumber());


        List<IPhone7InvestLotteryStatView> statViewList = mapper.statInvest(testLoginName, 0, 10);
        assertNotNull(statViewList);
        assertEquals(1, statViewList.size());
        assertEquals(model.getInvestAmount(), statViewList.get(0).getInvestAmountTotal());

        int statUserCount = mapper.statUserCount();
        assertEquals(1, statUserCount - statUserCountBefore);

        int statInvestCount = mapper.statInvestCount();
        assertEquals(1, statInvestCount - statInvestCountBefore);

        List<IPhone7InvestLotteryWinnerView> winners = mapper.listWinner();
        assertNotNull(winners);

        int updateEffectiveCount = mapper.updateByLotteryNumber(lotteryNumber);
        assertEquals(1, updateEffectiveCount);
    }
}
