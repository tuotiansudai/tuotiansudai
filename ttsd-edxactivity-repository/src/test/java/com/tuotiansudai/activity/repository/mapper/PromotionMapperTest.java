package com.tuotiansudai.activity.repository.mapper;

import com.tuotiansudai.activity.repository.dto.PromotionStatus;
import com.tuotiansudai.activity.repository.model.PromotionModel;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static org.junit.Assert.assertEquals;


public class PromotionMapperTest extends BaseMapperTest {
    @Autowired
    private PromotionMapper promotionMapper;

    @Test
    public void shouldPromotionIsSuccess() {
        PromotionModel promotionModel = fakePromotionModel();
        promotionMapper.create(promotionModel);

        PromotionModel promotionModelReturn = promotionMapper.findById(promotionModel.getId());

        assertEquals(promotionModel.getId(), promotionModelReturn.getId());
        assertEquals(promotionModel.getName(), promotionModelReturn.getName());
        assertEquals(promotionModel.getImageUrl(), promotionModelReturn.getImageUrl());
        assertEquals(promotionModel.getLinkUrl(), promotionModelReturn.getLinkUrl());
        assertEquals(promotionModel.getSeq(), promotionModelReturn.getSeq());
    }

    private PromotionModel fakePromotionModel() {
        PromotionModel promotionModel = new PromotionModel();
        promotionModel.setName("name");
        promotionModel.setImageUrl("imageUrl");
        promotionModel.setLinkUrl("linkUrl");
        promotionModel.setSeq(1);
        promotionModel.setStartTime(new DateTime().plusDays(1).toDate());
        promotionModel.setEndTime(new DateTime().plusDays(6).toDate());
        promotionModel.setStatus(PromotionStatus.TO_APPROVED);
        promotionModel.setCreatedTime(new Date());
        promotionModel.setCreatedBy("createdBy");
        return promotionModel;
    }
}
