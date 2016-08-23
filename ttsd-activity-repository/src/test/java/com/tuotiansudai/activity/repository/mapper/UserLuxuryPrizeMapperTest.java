package com.tuotiansudai.activity.repository.mapper;

import com.tuotiansudai.activity.repository.model.LuxuryPrizeModel;
import com.tuotiansudai.activity.repository.model.UserLuxuryPrizeModel;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

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

    @Test
    public void shouldGetUserLuxuryPrizeListIsSuccess(){
        LuxuryPrizeModel luxuryPrizeModel = fakeLuxuryPrizeModel();
        luxuryPrizeMapper.create(luxuryPrizeModel);
        UserLuxuryPrizeModel userLuxuryPrizeModel1 = fakeUserLuxuryPrizeModel(luxuryPrizeModel);
        UserLuxuryPrizeModel userLuxuryPrizeModel2 = fakeUserLuxuryPrizeModel(luxuryPrizeModel);
        userLuxuryPrizeMapper.create(userLuxuryPrizeModel1);
        userLuxuryPrizeMapper.create(userLuxuryPrizeModel2);
        List<UserLuxuryPrizeModel> userLuxuryPrizeModels = userLuxuryPrizeMapper.getUserLuxuryPrizeList(userLuxuryPrizeModel1.getMobile(), null, null, 0, 10);
        assertEquals(2,userLuxuryPrizeModels.size());
        assertEquals(userLuxuryPrizeModels.get(1).getId(), userLuxuryPrizeModel1.getId());
        assertEquals(userLuxuryPrizeModels.get(1).getMobile(), userLuxuryPrizeModel1.getMobile());
        assertEquals(userLuxuryPrizeModels.get(1).getInvestAmount(), userLuxuryPrizeModel1.getInvestAmount());
        assertEquals(userLuxuryPrizeModels.get(1).getPrize(), userLuxuryPrizeModel1.getPrize());
        assertEquals(userLuxuryPrizeModels.get(1).getLoginName(), userLuxuryPrizeModel1.getLoginName());
        assertEquals(userLuxuryPrizeModels.get(1).getPrizeId(), luxuryPrizeModel.getId());
    }

    @Test
    public void shouldGetUserLuxuryPrizeListByCreatedTimeIsSuccess(){
        LuxuryPrizeModel luxuryPrizeModel = fakeLuxuryPrizeModel();
        luxuryPrizeMapper.create(luxuryPrizeModel);
        UserLuxuryPrizeModel userLuxuryPrizeModel1 = fakeUserLuxuryPrizeModel(luxuryPrizeModel);
        userLuxuryPrizeMapper.create(userLuxuryPrizeModel1);
        List<UserLuxuryPrizeModel> userLuxuryPrizeModels = userLuxuryPrizeMapper.getUserLuxuryPrizeList(userLuxuryPrizeModel1.getMobile(),new DateTime(2016,7,30,0,0,0).toDate(),new DateTime(2016,7,30,0,0,0).toDate(),0,10);
        assertEquals(1,userLuxuryPrizeModels.size());
        assertEquals(userLuxuryPrizeModels.get(0).getId(), userLuxuryPrizeModel1.getId());
        assertEquals(userLuxuryPrizeModels.get(0).getMobile(), userLuxuryPrizeModel1.getMobile());
        assertEquals(userLuxuryPrizeModels.get(0).getInvestAmount(), userLuxuryPrizeModel1.getInvestAmount());
        assertEquals(userLuxuryPrizeModels.get(0).getPrize(), userLuxuryPrizeModel1.getPrize());
        assertEquals(userLuxuryPrizeModels.get(0).getLoginName(), userLuxuryPrizeModel1.getLoginName());
        assertEquals(userLuxuryPrizeModels.get(0).getPrizeId(), luxuryPrizeModel.getId());
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
        userLuxuryPrizeModel.setCreatedTime(new DateTime(2016,7,30,0,0,0).toDate());

        return userLuxuryPrizeModel;
    }
}
