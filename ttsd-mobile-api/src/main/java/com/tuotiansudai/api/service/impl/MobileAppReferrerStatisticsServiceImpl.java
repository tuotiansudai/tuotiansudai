package com.tuotiansudai.api.service.impl;

import com.tuotiansudai.api.dto.*;
import com.tuotiansudai.api.service.MobileAppBannerService;
import com.tuotiansudai.api.service.MobileAppReferrerBannerService;
import com.tuotiansudai.api.service.MobileAppReferrerStatisticsService;
import com.tuotiansudai.repository.mapper.ReferrerManageMapper;
import com.tuotiansudai.repository.mapper.ReferrerRelationMapper;
import com.tuotiansudai.repository.model.ReferrerManageView;
import com.tuotiansudai.util.AmountConverter;
import org.apache.commons.collections.CollectionUtils;
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
    private MobileAppReferrerBannerService mobileAppReferrerBannerService;

    @Override
    public BaseResponseDto getReferrerStatistics(BaseParamDto paramDto) {
        String referrerLoginName = paramDto.getBaseParam().getUserId();
        List<ReferrerManageView> referInvestSumAmountList = referrerManageMapper.findReferInvestSumAmount(referrerLoginName);

        ReferrerStatisticsResponseDataDto referrerStatisticsResponseDataDto = new ReferrerStatisticsResponseDataDto();

        referrerStatisticsResponseDataDto.setRewardAmount(AmountConverter.convertCentToString(referrerManageMapper.findReferInvestTotalAmount(referrerLoginName, null, null, null, null)));
        referrerStatisticsResponseDataDto.setReferrersSum(String.valueOf(referrerRelationMapper.findReferrerCountByReferrerLoginName(referrerLoginName)));
        referrerStatisticsResponseDataDto.setReferrersInvestAmount(CollectionUtils.isNotEmpty(referInvestSumAmountList) ? AmountConverter.convertCentToString(referInvestSumAmountList.get(0).getInvestAmount()) : "0.00");

        referrerStatisticsResponseDataDto.setBanner(getBannerResponseDataDto());

        BaseResponseDto baseResponseDto = new BaseResponseDto();
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        baseResponseDto.setData(referrerStatisticsResponseDataDto);

        return baseResponseDto;
    }

    private BannerPictureResponseDataDto getBannerResponseDataDto(){
        BaseResponseDto<BannerResponseDataDto> bannerResponseDataDtoList = mobileAppReferrerBannerService.generateReferrerBannerList();
        if(bannerResponseDataDtoList.getData() != null && CollectionUtils.isNotEmpty(bannerResponseDataDtoList.getData().getPictures())){
            return bannerResponseDataDtoList.getData().getPictures().get(0);
        }
        return new BannerPictureResponseDataDto();
    }

}
