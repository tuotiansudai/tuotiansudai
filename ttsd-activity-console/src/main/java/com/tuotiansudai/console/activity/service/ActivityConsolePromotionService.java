package com.tuotiansudai.console.activity.service;

import com.tuotiansudai.activity.repository.dto.PromotionDto;
import com.tuotiansudai.activity.repository.dto.PromotionStatus;
import com.tuotiansudai.activity.repository.mapper.PromotionMapper;
import com.tuotiansudai.activity.repository.model.PromotionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ActivityConsolePromotionService {

    @Autowired
    private PromotionMapper promotionMapper;

    public List<PromotionModel> promotionList() {
        return promotionMapper.findAll();
    }

    public PromotionModel findById(long id) {
        return promotionMapper.findById(id);
    }

    public void create(String loginName, PromotionDto promotionDto) {
        PromotionModel promotionModel = new PromotionModel(promotionDto);
        promotionModel.setCreatedBy(loginName);
        promotionModel.setCreatedTime(new Date());
        promotionModel.setStatus(PromotionStatus.TO_APPROVED);
        promotionMapper.create(promotionModel);
    }

    public boolean delPromotion(String loginName, long id) {
        PromotionModel promotionModel = promotionMapper.findById(id);
        promotionModel.setDeactivatedBy(loginName);
        promotionModel.setDeactivatedTime(new Date());
        promotionModel.setDeleted(true);
        promotionMapper.update(promotionModel);
        return true;
    }

    public void updatePromotion(String loginName, PromotionDto promotionDto) {
        PromotionModel promotionModel = promotionMapper.findById(promotionDto.getId());
        promotionModel.setName(promotionDto.getName());
        promotionModel.setImageUrl(promotionDto.getImageUrl());
        promotionModel.setLinkUrl(promotionDto.getLinkUrl());
        promotionModel.setStartTime(promotionDto.getStartTime());
        promotionModel.setEndTime(promotionDto.getEndTime());
        promotionModel.setSeq(promotionDto.getSeq());
        promotionModel.setId(promotionDto.getId());
        promotionModel.setUpdatedBy(loginName);
        promotionModel.setUpdatedTime(new Date());
        promotionMapper.update(promotionModel);
    }

    public void AuditPromotion(String loginName, PromotionStatus promotionStatus, long id) {
        PromotionModel promotionModel = promotionMapper.findById(id);
        promotionModel.setStatus(promotionStatus);
        promotionModel.setUpdatedBy(loginName);
        promotionModel.setUpdatedTime(new Date());
        promotionMapper.update(promotionModel);
    }

}
