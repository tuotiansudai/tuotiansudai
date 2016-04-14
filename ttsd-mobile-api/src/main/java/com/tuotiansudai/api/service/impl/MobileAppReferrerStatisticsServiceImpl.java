package com.tuotiansudai.api.service.impl;

import com.tuotiansudai.api.dto.*;
import com.tuotiansudai.api.service.MobileAppBannerService;
import com.tuotiansudai.api.service.MobileAppReferrerStatisticsService;
import com.tuotiansudai.repository.mapper.ReferrerManageMapper;
import com.tuotiansudai.repository.mapper.ReferrerRelationMapper;
import com.tuotiansudai.repository.model.ReferrerManageView;
import com.tuotiansudai.util.AmountConverter;
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
    public BaseResponseDto getReferrerStatistics(BaseParamDto baseParamDto) {
        BaseParam baseParam = baseParamDto.getBaseParam();

        ReferrerStatisticsDto referrerStatisticsDto = new ReferrerStatisticsDto();
        if(!baseParam.getUserId().equals("")){
            referrerStatisticsDto.setRewardAmount(getTotalAmount(baseParam));
            referrerStatisticsDto.setReferrersSum(getReferrersSum(baseParam));
            referrerStatisticsDto.setReferrersInvestAmount(getInvestAmount(baseParam));
        }else{
            referrerStatisticsDto.setRewardAmount("0");
            referrerStatisticsDto.setReferrersSum("0");
            referrerStatisticsDto.setReferrersInvestAmount("0");
        }

        referrerStatisticsDto.setBanner(getBannerResponseDataDto("referrer.json"));

        BaseResponseDto baseResponseDto = new BaseResponseDto();
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseResponseDto.setData(referrerStatisticsDto);

        return baseResponseDto;
    }

    private String getTotalAmount(BaseParam baseParam){
        Long totalAmount =  referrerManageMapper.findReferInvestTotalAmount(baseParam.getUserId(), null, null, null, null);
        return totalAmount != null ? AmountConverter.convertCentToString(totalAmount) : "0";
    }

    private String getInvestAmount(BaseParam baseParam){
        Long investAmount = 0L;
        List<ReferrerManageView> referInvestSumAmountList = referrerManageMapper.findReferInvestSumAmount(baseParam.getUserId());

        if(referInvestSumAmountList.size() > 0){
            investAmount = referInvestSumAmountList.get(0).getInvestAmount();
        }
        return investAmount != null ? AmountConverter.convertCentToString(investAmount) : "0";
    }

    private String getReferrersSum(BaseParam baseParam){
        return String.valueOf(referrerRelationMapper.findByReferrerLoginName(baseParam.getUserId()).size());
    }

    private BannerPictureResponseDataDto getBannerResponseDataDto(String jsonName){
        mobileAppBannerService.setBannerConfigFile(jsonName);
        BaseResponseDto<BannerResponseDataDto> bannerResponseDataDtoList = mobileAppBannerService.generateBannerList();
        if(bannerResponseDataDtoList.getData() != null && bannerResponseDataDtoList.getData().getPictures().size() > 0){
            return bannerResponseDataDtoList.getData().getPictures().get(0);
        }
        return new BannerPictureResponseDataDto();
    }

}
