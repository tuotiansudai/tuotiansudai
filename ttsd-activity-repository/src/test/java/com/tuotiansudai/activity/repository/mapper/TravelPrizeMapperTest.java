package com.tuotiansudai.activity.repository.mapper;

import com.google.common.collect.Lists;
import com.tuotiansudai.activity.repository.model.TravelPrizeModel;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static org.junit.Assert.assertEquals;


public class TravelPrizeMapperTest extends BaseMapperTest{
    @Autowired
    private TravelPrizeMapper travelPrizeMapper;

    @Test
    public void shouldCreateTravelPrizeIsSuccess(){
        TravelPrizeModel travelPrizeModel = fakeTravelPrizeModel();
        travelPrizeMapper.create(travelPrizeModel);
        TravelPrizeModel travelPrizeModelReturn = travelPrizeMapper.findById(travelPrizeModel.getId());

        assertEquals(travelPrizeModel.getId(),travelPrizeModelReturn.getId());
        assertEquals(travelPrizeModel.getName(),travelPrizeModelReturn.getName());
        assertEquals(travelPrizeModel.getPrice(),travelPrizeModelReturn.getPrice());
        assertEquals(travelPrizeModel.getImage(),travelPrizeModelReturn.getImage());
        assertEquals(travelPrizeModel.getInvestAmount(),travelPrizeModelReturn.getInvestAmount());
        assertEquals(travelPrizeModel.getIntroduce(),travelPrizeModelReturn.getIntroduce());
        assertEquals(travelPrizeModel.getCreatedBy(),travelPrizeModelReturn.getCreatedBy());
        assertEquals(travelPrizeModel.getUpdatedBy(),travelPrizeModelReturn.getUpdatedBy());


    }

    private TravelPrizeModel fakeTravelPrizeModel(){
        TravelPrizeModel travelPrizeModel = new TravelPrizeModel();
        travelPrizeModel.setName("name");
        travelPrizeModel.setPrice("1000");
        travelPrizeModel.setImage("image");
        travelPrizeModel.setInvestAmount(2000l);
        travelPrizeModel.setIntroduce("introduce");
        travelPrizeModel.setCreatedBy("createdBy");
        travelPrizeModel.setCreatedTime(new Date());
        travelPrizeModel.setUpdatedBy("updatedBy");
        travelPrizeModel.setUpdatedTime(new Date());
        return travelPrizeModel;
    }
}
