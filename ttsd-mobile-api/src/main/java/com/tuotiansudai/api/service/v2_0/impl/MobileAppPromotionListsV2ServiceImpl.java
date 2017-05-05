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
import com.tuotiansudai.util.RedisWrapperClient;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Service
public class MobileAppPromotionListsV2ServiceImpl implements MobileAppPromotionListsV2Service {

    private static final String PROMOTION_ALERT_KEY = "app:promotion:pop";

    private static final String DEVICEID_PROMOTION_ID_KEY = "deviceId:{0}:promotionId:{1}";

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    @Autowired
    private PromotionMapper promotionMapper;

    @Value("${common.static.server}")
    private String commonStaticServer;

    @Override
    public BaseResponseDto<PromotionListResponseDataDto> generatePromotionList(PromotionRequestDto promotionRequestDto) {
        String deviceId = promotionRequestDto.getBaseParam().getDeviceId();
        PromotionListResponseDataDto dtoData = new PromotionListResponseDataDto();
        List<PromotionModel> promotionModelList = promotionMapper.findPromotionList();
        List<PromotionRecordResponseDataDto> convertResponseDataList = convertResponseData(promotionModelList, deviceId);
        dtoData.setTotalCount(convertResponseDataList.size());
        dtoData.setPopList(convertResponseDataList);
        BaseResponseDto<PromotionListResponseDataDto> dto = new BaseResponseDto<>();
        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        dto.setData(dtoData);
        return dto;
    }

    private String getRedisKeyFromTemplateByDate(String template, String deviceId, String promotionId) {
        return MessageFormat.format(template, deviceId, promotionId);
    }

    private List<PromotionRecordResponseDataDto> convertResponseData(List<PromotionModel> promotionModels, String deviceId) {
        List<PromotionRecordResponseDataDto> list = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(promotionModels)) {
            for (PromotionModel promotionModel : promotionModels) {
                if (redisWrapperClient.hexists(PROMOTION_ALERT_KEY, getRedisKeyFromTemplateByDate(DEVICEID_PROMOTION_ID_KEY, deviceId, String.valueOf(promotionModel.getId())))) {
                    continue;
                }
                PromotionRecordResponseDataDto dto = new PromotionRecordResponseDataDto();
                dto.setImgUrl(commonStaticServer + promotionModel.getImageUrl());
                dto.setLinkUrl(Strings.isNullOrEmpty(promotionModel.getLinkUrl()) ? promotionModel.getJumpToLink() : promotionModel.getLinkUrl());
                list.add(dto);

                int timeout = this.getLeftSeconds(promotionModel.getStartTime(), promotionModel.getEndTime());
                redisWrapperClient.hset(PROMOTION_ALERT_KEY, getRedisKeyFromTemplateByDate(DEVICEID_PROMOTION_ID_KEY, deviceId, String.valueOf(promotionModel.getId())), String.valueOf(promotionModel.getId()), timeout);
            }
        }
        return list;
    }

    private int getLeftSeconds(Date startTime, Date endTime) {
        long seconds = 0;
        long time1 = startTime.getTime();
        long time2 = endTime.getTime();
        if (time1 < time2) {
            seconds = (time2 - time1) / 1000;
        }
        return (int) seconds;
    }

}
