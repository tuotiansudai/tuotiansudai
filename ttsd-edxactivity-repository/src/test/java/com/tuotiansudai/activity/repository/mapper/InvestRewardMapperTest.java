package com.tuotiansudai.activity.repository.mapper;


import com.tuotiansudai.activity.repository.model.AnnualPrizeModel;
import com.tuotiansudai.activity.repository.model.InvestRewardModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})@Transactional
public class InvestRewardMapperTest {

    @Autowired
    private InvestRewardMapper investRewardMapper;


    @Test
    public void shouldCreateAndFindByMobileIsOk(){
        String mobile = "10000000000";
        InvestRewardModel investRewardModel = getInvestRewardModel(mobile);
        investRewardMapper.create(investRewardModel);

        InvestRewardModel findByMobile = investRewardMapper.findByMobile(mobile);
        assertEquals(findByMobile.getMobile(), mobile);
    }

    @Test
    public void shouldUpdateIsOk(){
        String mobile = "10000000000";
        InvestRewardModel investRewardModel = getInvestRewardModel(mobile);
        investRewardMapper.create(investRewardModel);

        investRewardModel.setInvestAmount(10l);
        investRewardMapper.update(investRewardModel);

        InvestRewardModel findByMobile = investRewardMapper.findByMobile(mobile);
        assertEquals(findByMobile.getMobile(), mobile);
        assertEquals(investRewardModel.getInvestAmount(), findByMobile.getInvestAmount());
    }

    private InvestRewardModel getInvestRewardModel(String mobile){
        return new InvestRewardModel("test", "test", mobile, 1, 0L);
    }
}
