package com.tuotiansudai.activity.repository.mapper;


import com.tuotiansudai.activity.repository.model.AnnualPrizeModel;
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
public class AnnualPrizeMapperTest {

    @Autowired
    public AnnualPrizeMapper annualPrizeMapper;

    @Test
    public void shouldCreateAndFindByMobileIsOk(){
        String mobile = "10000000000";
        AnnualPrizeModel annualPrizeModel = getAnnualPrizeModel(mobile);
        annualPrizeMapper.create(annualPrizeModel);

        AnnualPrizeModel findByMobile = annualPrizeMapper.findByMobile(mobile);
        assertEquals(findByMobile.getMobile(), mobile);
    }

    @Test
    public void shouldUpdateIsOk(){
        String mobile = "10000000000";
        AnnualPrizeModel annualPrizeModel = getAnnualPrizeModel(mobile);
        annualPrizeMapper.create(annualPrizeModel);

        annualPrizeModel.setInvestAmount(10l);
        annualPrizeMapper.update(annualPrizeModel);

        AnnualPrizeModel findByMobile = annualPrizeMapper.findByMobile(mobile);
        assertEquals(findByMobile.getMobile(), mobile);
        assertEquals(annualPrizeModel.getInvestAmount(), findByMobile.getInvestAmount());
    }

    private AnnualPrizeModel getAnnualPrizeModel(String mobile){
        return new AnnualPrizeModel("test", "test", mobile, 1, false, false);
    }
}
