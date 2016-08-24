package com.tuotiansudai.activity.repository.mapper;

import com.tuotiansudai.activity.repository.model.TravelPrizeModel;
import com.tuotiansudai.activity.repository.model.UserTravelPrizeModel;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static junit.framework.Assert.assertEquals;

public class UserTravelPrizeMapperTest extends BaseMapperTest{
    @Autowired
    private UserTravelPrizeMapper userTravelPrizeMapper;
    @Autowired
    private TravelPrizeMapper travelPrizeMapper;

    @Test
    public void shouldCreateUserTravelPrizeIsSuccess(){
        TravelPrizeModel travelPrizeModel = fakeTravelPrizeModel();
        travelPrizeMapper.create(travelPrizeModel);
        UserTravelPrizeModel userTravelPrizeModel = fakeUserTravelPrizeModel(travelPrizeModel);
        userTravelPrizeMapper.create(userTravelPrizeModel);
        UserTravelPrizeModel userLuxuryPrizeModelReturn = userTravelPrizeMapper.findById(userTravelPrizeModel.getId());


        assertEquals(userTravelPrizeModel.getId(), userLuxuryPrizeModelReturn.getId());
        assertEquals(userTravelPrizeModel.getMobile(), userLuxuryPrizeModelReturn.getMobile());
        assertEquals(userTravelPrizeModel.getInvestAmount(), userLuxuryPrizeModelReturn.getInvestAmount());
        assertEquals(userTravelPrizeModel.getPrize(), userLuxuryPrizeModelReturn.getPrize());
        assertEquals(userTravelPrizeModel.getLoginName(), userLuxuryPrizeModelReturn.getLoginName());
        assertEquals(userTravelPrizeModel.getPrizeId(), travelPrizeModel.getId());
    }

    private TravelPrizeModel fakeTravelPrizeModel(){
        TravelPrizeModel travelPrizeModel = new TravelPrizeModel();
        travelPrizeModel.setDescription("brand");
        travelPrizeModel.setName("name");
        travelPrizeModel.setPrice(1000l);
        travelPrizeModel.setImage("image");
        travelPrizeModel.setInvestAmount(2000l);
        travelPrizeModel.setIntroduce("introduce");
        travelPrizeModel.setCreatedBy("createdBy");
        travelPrizeModel.setCreatedTime(new Date());
        travelPrizeModel.setUpdatedBy("updatedBy");
        travelPrizeModel.setUpdatedTime(new Date());
        return travelPrizeModel;
    }

    private UserTravelPrizeModel fakeUserTravelPrizeModel(TravelPrizeModel travelPrizeModel){
        UserTravelPrizeModel userTravelPrizeModel = new UserTravelPrizeModel();
        userTravelPrizeModel.setPrizeId(travelPrizeModel.getId());
        userTravelPrizeModel.setInvestAmount(travelPrizeModel.getInvestAmount());
        userTravelPrizeModel.setLoginName("loginName");
        userTravelPrizeModel.setMobile("mobile");
        userTravelPrizeModel.setPrize("prize");
        userTravelPrizeModel.setCreatedTime(new Date());

        return userTravelPrizeModel;
    }
}
