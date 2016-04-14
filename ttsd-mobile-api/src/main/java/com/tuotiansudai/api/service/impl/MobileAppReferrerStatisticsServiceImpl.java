package com.tuotiansudai.api.service.impl;

import com.tuotiansudai.api.dto.*;
import com.tuotiansudai.api.service.MobileAppBannerService;
import com.tuotiansudai.api.service.MobileAppReferrerStatisticsService;
import com.tuotiansudai.repository.mapper.ReferrerManageMapper;
import com.tuotiansudai.repository.mapper.ReferrerRelationMapper;
import com.tuotiansudai.repository.model.ReferrerManageView;
import com.tuotiansudai.util.AmountConverter;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.validator.internal.metadata.aggregated.rule.ReturnValueMayOnlyBeMarkedOnceAsCascadedPerHierarchyLine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MobileAppReferrerStatisticsServiceImpl implements MobileAppReferrerStatisticsService {

    @Autowired
    private ReferrerManageMapper referrerManageMapper;
    @Autowired
    private ReferrerRelationMapper referrerRelationMapper;
    @Autowired
    private MobileAppBannerService mobileAppBannerService;



    @Override
    public BaseResponseDto getReferrerStatistics(BaseParamDto paramDto) {
        String referrerLoginName = paramDto.getBaseParam().getUserId();
        List<ReferrerManageView> referInvestSumAmountList = referrerManageMapper.findReferInvestSumAmount(referrerLoginName);

        ReferrerStatisticsResponseDataDto referrerStatisticsResponseDataDto = new ReferrerStatisticsResponseDataDto();

        referrerStatisticsResponseDataDto.setRewardAmount(AmountConverter.convertCentToString(referrerManageMapper.findReferInvestTotalAmount(referrerLoginName, null, null, null, null)));
        referrerStatisticsResponseDataDto.setReferrersSum(String.valueOf(referrerRelationMapper.findByReferrerLoginName(referrerLoginName).size()));
        referrerStatisticsResponseDataDto.setReferrersInvestAmount(CollectionUtils.isNotEmpty(referInvestSumAmountList) ? AmountConverter.convertCentToString(referInvestSumAmountList.get(0).getInvestAmount()) : "0");

        referrerStatisticsResponseDataDto.setBanner(getBannerResponseDataDto("referrer.json"));

        BaseResponseDto baseResponseDto = new BaseResponseDto();
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        baseResponseDto.setData(referrerStatisticsResponseDataDto);

        return baseResponseDto;
    }

    private BannerPictureResponseDataDto getBannerResponseDataDto(String jsonName){
        mobileAppBannerService.setBannerConfigFile(jsonName);
        BaseResponseDto<BannerResponseDataDto> bannerResponseDataDtoList = mobileAppBannerService.generateBannerList();
        if(bannerResponseDataDtoList.getData() != null && CollectionUtils.isNotEmpty(bannerResponseDataDtoList.getData().getPictures())){
            return bannerResponseDataDtoList.getData().getPictures().get(0);
        }
        return new BannerPictureResponseDataDto();
    }

}
