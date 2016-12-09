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
import com.tuotiansudai.client.RedisWrapperClient;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.httpclient.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.util.Date;
import java.util.List;

@Service
public class MobileAppPromotionListsV2ServiceImpl implements MobileAppPromotionListsV2Service{

    @Autowired
    private PromotionMapper promotionMapper;

    @Value("${mobile.static.server}")
    private String staticServer;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    private static final String PROMOTION_ALERT_KEY = "app:promotion:pop";

    @Override
    public BaseResponseDto<PromotionListResponseDataDto> generatePromotionList(PromotionRequestDto promotionRequestDto) {
        String deviceId = promotionRequestDto.getBaseParam().getDeviceId();

        PromotionListResponseDataDto dtoData = new PromotionListResponseDataDto();
        if(!redisWrapperClient.hexists(PROMOTION_ALERT_KEY, deviceId)){
            List<PromotionModel> promotionModelList = promotionMapper.findPromotionList();
            dtoData.setTotalCount(CollectionUtils.isNotEmpty(promotionModelList) ? promotionModelList.size() : 0);
            dtoData.setPopList(convertResponseData(promotionModelList, deviceId));
        }
        else{
            dtoData.setTotalCount(0);
            dtoData.setPopList(Lists.newArrayList());
        }
        BaseResponseDto<PromotionListResponseDataDto> dto = new BaseResponseDto<>();
        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        dto.setData(dtoData);
        return dto;
    }

    private List<PromotionRecordResponseDataDto> convertResponseData(List<PromotionModel> promotionModels, String deviceId) {
        List<PromotionRecordResponseDataDto> list = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(promotionModels)) {
            for (PromotionModel promotionModel : promotionModels) {
                PromotionRecordResponseDataDto dto = new PromotionRecordResponseDataDto();
                dto.setImgUrl(staticServer + promotionModel.getImageUrl());
                dto.setLinkUrl(Strings.isNullOrEmpty(promotionModel.getLinkUrl())? promotionModel.getJumpToLink():promotionModel.getLinkUrl());
                list.add(dto);

                int timout = this.getLeftSeconds(promotionModel.getStartTime(), promotionModel.getEndTime());
                redisWrapperClient.hset(PROMOTION_ALERT_KEY, deviceId, String.valueOf(promotionModel.getId()), timout);
            }
        }
        return list;
    }

    private int getLeftSeconds(Date startTime, Date endTime){
        long seconds = 0;
        long time1 = startTime.getTime();
        long time2 = endTime.getTime();
        if (time1 < time2) {
            seconds = (time2 - time1) / 1000;
        }
        return (int)seconds;
    }

}
