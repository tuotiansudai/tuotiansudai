package com.tuotiansudai.activity.repository.mapper;

import com.tuotiansudai.activity.repository.model.LuxuryPrizeModel;
import com.tuotiansudai.activity.repository.model.UserLuxuryPrizeModel;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static junit.framework.Assert.assertEquals;

public class UserLuxuryPrizeMapperTest extends BaseMapperTest{
    @Autowired
    private UserLuxuryPrizeMapper userLuxuryPrizeMapper;
    @Autowired
    private LuxuryPrizeMapper luxuryPrizeMapper;

    @Test
    public void shouldCreateUserLuxuryPrizeIsSuccess(){
        LuxuryPrizeModel luxuryPrizeModel = fakeLuxuryPrizeModel();
        luxuryPrizeMapper.create(luxuryPrizeModel);
        UserLuxuryPrizeModel userLuxuryPrizeModel = fakeUserLuxuryPrizeModel(luxuryPrizeModel);
        userLuxuryPrizeMapper.create(userLuxuryPrizeModel);
        UserLuxuryPrizeModel userLuxuryPrizeModelReturn = userLuxuryPrizeMapper.findById(userLuxuryPrizeModel.getId());


        assertEquals(userLuxuryPrizeModel.getId(), userLuxuryPrizeModelReturn.getId());
        assertEquals(userLuxuryPrizeModel.getMobile(), userLuxuryPrizeModelReturn.getMobile());
        assertEquals(userLuxuryPrizeModel.getInvestAmount(), userLuxuryPrizeModelReturn.getInvestAmount());
        assertEquals(userLuxuryPrizeModel.getPrize(), userLuxuryPrizeModelReturn.getPrize());
        assertEquals(userLuxuryPrizeModel.getLoginName(), userLuxuryPrizeModelReturn.getLoginName());
        assertEquals(userLuxuryPrizeModel.getPrizeId(), luxuryPrizeModel.getId());
    }

    private LuxuryPrizeModel fakeLuxuryPrizeModel(){
        LuxuryPrizeModel luxuryPrizeModel = new LuxuryPrizeModel();
        luxuryPrizeModel.setBrand("brand");
        luxuryPrizeModel.setName("name");
        luxuryPrizeModel.setPrice(1000l);
        luxuryPrizeModel.setImage("image");
        luxuryPrizeModel.setInvestAmount(2000l);
        luxuryPrizeModel.setTenPercentOffInvestAmount(3000l);
        luxuryPrizeModel.setTwentyPercentOffInvestAmount(4000l);
        luxuryPrizeModel.setThirtyPercentOffInvestAmount(5000l);
        luxuryPrizeModel.setIntroduce("introduce");
        luxuryPrizeModel.setCreatedBy("createdBy");
        luxuryPrizeModel.setCreatedTime(new Date());
        luxuryPrizeModel.setUpdatedBy("updatedBy");
        luxuryPrizeModel.setUpdatedTime(new Date());
        return luxuryPrizeModel;
    }

    private UserLuxuryPrizeModel fakeUserLuxuryPrizeModel(LuxuryPrizeModel luxuryPrizeModel){
        UserLuxuryPrizeModel userLuxuryPrizeModel = new UserLuxuryPrizeModel();
        userLuxuryPrizeModel.setPrizeId(luxuryPrizeModel.getId());
        userLuxuryPrizeModel.setInvestAmount(luxuryPrizeModel.getTwentyPercentOffInvestAmount());
        userLuxuryPrizeModel.setLoginName("loginName");
        userLuxuryPrizeModel.setMobile("mobile");
        userLuxuryPrizeModel.setPrize(20000l);
        userLuxuryPrizeModel.setCreatedTime(new Date());

        return userLuxuryPrizeModel;
    }
}
