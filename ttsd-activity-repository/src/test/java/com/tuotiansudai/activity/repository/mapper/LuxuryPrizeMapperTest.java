package com.tuotiansudai.activity.repository.mapper;

import com.tuotiansudai.activity.repository.model.LuxuryPrizeModel;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static org.junit.Assert.assertEquals;


public class LuxuryPrizeMapperTest extends BaseMapperTest{
    @Autowired
    private LuxuryPrizeMapper luxuryPrizeMapper;

    @Test
    public void shouldCreateLuxuryPrizeIsSuccess(){
        LuxuryPrizeModel luxuryPrizeModel = fakeLuxuryPrizeModel();
        luxuryPrizeMapper.create(luxuryPrizeModel);
        LuxuryPrizeModel luxuryPrizeModelReturn = luxuryPrizeMapper.findById(luxuryPrizeModel.getId());

        assertEquals(luxuryPrizeModel.getId(),luxuryPrizeModelReturn.getId());
        assertEquals(luxuryPrizeModel.getBrand(),luxuryPrizeModelReturn.getBrand());
        assertEquals(luxuryPrizeModel.getName(),luxuryPrizeModelReturn.getName());
        assertEquals(luxuryPrizeModel.getPrice(),luxuryPrizeModelReturn.getPrice());
        assertEquals(luxuryPrizeModel.getImage(),luxuryPrizeModelReturn.getImage());
        assertEquals(luxuryPrizeModel.getInvestAmount(),luxuryPrizeModelReturn.getInvestAmount());
        assertEquals(luxuryPrizeModel.getTenPercentOffInvestAmount(),luxuryPrizeModelReturn.getTenPercentOffInvestAmount());
        assertEquals(luxuryPrizeModel.getTwentyPercentOffInvestAmount(),luxuryPrizeModelReturn.getTwentyPercentOffInvestAmount());
        assertEquals(luxuryPrizeModel.getThirtyPercentOffInvestAmount(),luxuryPrizeModelReturn.getThirtyPercentOffInvestAmount());
        assertEquals(luxuryPrizeModel.getIntroduce(),luxuryPrizeModelReturn.getIntroduce());
        assertEquals(luxuryPrizeModel.getCreatedBy(),luxuryPrizeModelReturn.getCreatedBy());
        assertEquals(luxuryPrizeModel.getUpdatedBy(),luxuryPrizeModelReturn.getUpdatedBy());


    }

    @Test
    public void shouldUpdateLuxuryPrizeIsSuccess(){
        LuxuryPrizeModel luxuryPrizeModel = fakeLuxuryPrizeModel();
        luxuryPrizeMapper.create(luxuryPrizeModel);

        LuxuryPrizeModel luxuryPrizeModel1 = luxuryPrizeMapper.findById(luxuryPrizeModel.getId());
        luxuryPrizeModel1.setBrand("brand1");
        luxuryPrizeModel1.setName("name1");
        luxuryPrizeModel1.setPrice("price1");
        luxuryPrizeModel1.setImage("image1");
        luxuryPrizeModel1.setInvestAmount(20001l);
        luxuryPrizeModel1.setTenPercentOffInvestAmount(30001l);
        luxuryPrizeModel1.setTwentyPercentOffInvestAmount(40001l);
        luxuryPrizeModel1.setThirtyPercentOffInvestAmount(50001l);
        luxuryPrizeModel1.setIntroduce("introduce1");
        luxuryPrizeModel1.setCreatedBy("createdBy1");
        luxuryPrizeModel1.setCreatedTime(new Date());
        luxuryPrizeModel1.setUpdatedBy("updatedBy1");
        luxuryPrizeModel1.setUpdatedTime(new Date());

        luxuryPrizeMapper.update(luxuryPrizeModel1);
        LuxuryPrizeModel luxuryPrizeModelReturn = luxuryPrizeMapper.findById(luxuryPrizeModel1.getId());

        assertEquals(luxuryPrizeModel1.getBrand(),luxuryPrizeModelReturn.getBrand());

        assertEquals(luxuryPrizeModel1.getBrand(),luxuryPrizeModelReturn.getBrand());
        assertEquals(luxuryPrizeModel1.getName(),luxuryPrizeModelReturn.getName());
        assertEquals(luxuryPrizeModel1.getPrice(),luxuryPrizeModelReturn.getPrice());
        assertEquals(luxuryPrizeModel1.getImage(),luxuryPrizeModelReturn.getImage());

    }

    private LuxuryPrizeModel fakeLuxuryPrizeModel(){
        LuxuryPrizeModel luxuryPrizeModel = new LuxuryPrizeModel();
        luxuryPrizeModel.setBrand("brand");
        luxuryPrizeModel.setName("name");
        luxuryPrizeModel.setPrice("price");
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
}
