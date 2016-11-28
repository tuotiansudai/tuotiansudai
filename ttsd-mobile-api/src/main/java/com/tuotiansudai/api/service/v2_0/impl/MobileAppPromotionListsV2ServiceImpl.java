package com.tuotiansudai.api.service.v2_0.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.activity.repository.mapper.PromotionMapper;
import com.tuotiansudai.activity.repository.model.PromotionModel;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.dto.v2_0.PromotionListResponseDataDto;
import com.tuotiansudai.api.dto.v2_0.PromotionRecordResponseDataDto;
import com.tuotiansudai.api.dto.v2_0.PromotionRequestDto;
import com.tuotiansudai.api.service.v2_0.MobileAppPromotionListsV2Service;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MobileAppPromotionListsV2ServiceImpl implements MobileAppPromotionListsV2Service{

    @Autowired
    private PromotionMapper promotionMapper;

    @Value("${mobile.static.server}")
    private String staticServer;

    @Override
    public BaseResponseDto<PromotionListResponseDataDto> generatePromotionList(PromotionRequestDto promotionRequestDto) {
        List<PromotionModel> promotionModelList = promotionMapper.findPromotionList();
        PromotionListResponseDataDto dtoData = new PromotionListResponseDataDto();
        dtoData.setTotalCount(CollectionUtils.isNotEmpty(promotionModelList) ? promotionModelList.size() : 0);
        dtoData.setPopList(convertResponseData(promotionModelList));
        BaseResponseDto<PromotionListResponseDataDto> dto = new BaseResponseDto<>();
        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        dto.setData(dtoData);
        return dto;
    }

    private List<PromotionRecordResponseDataDto> convertResponseData(List<PromotionModel> promotionModels) {
        List<PromotionRecordResponseDataDto> list = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(promotionModels)) {
            for (PromotionModel promotionModel : promotionModels) {
                PromotionRecordResponseDataDto dto = new PromotionRecordResponseDataDto();
                dto.setImgUrl(staticServer + promotionModel.getImageUrl());
                dto.setLinkUrl(Strings.isNullOrEmpty(promotionModel.getLinkUrl())? promotionModel.getJumpToLink():promotionModel.getLinkUrl());
                list.add(dto);
            }
        }
        return list;
    }

}
