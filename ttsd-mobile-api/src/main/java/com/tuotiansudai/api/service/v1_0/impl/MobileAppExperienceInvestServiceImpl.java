package com.tuotiansudai.api.service.v1_0.impl;


import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppChannelService;
import com.tuotiansudai.api.service.v1_0.MobileAppExperienceInvestService;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.coupon.repository.model.UserGroup;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.InvestDto;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.service.ExperienceInvestService;
import com.tuotiansudai.util.AmountConverter;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

@Service
public class MobileAppExperienceInvestServiceImpl implements MobileAppExperienceInvestService {

    @Autowired
    private ExperienceInvestService experienceInvestService;

    @Autowired
    private MobileAppChannelService mobileAppChannelService;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Override
    public BaseResponseDto<InvestExperienceResponseDto> experienceInvest(InvestRequestDto investRequestDto) {
        BaseResponseDto<InvestExperienceResponseDto> responseDto = new BaseResponseDto<>();
        InvestDto investDto = convertInvestDto(investRequestDto);
        BaseDto<BaseDataDto> baseDto = experienceInvestService.invest(investDto);
        if (!baseDto.getData().getStatus()) {
            responseDto.setCode(ReturnMessage.INVEST_FAILED.getCode());
            responseDto.setMessage(ReturnMessage.INVEST_FAILED.getMsg());
        }else{
            responseDto.setCode(ReturnMessage.SUCCESS.getCode());
            responseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
            InvestExperienceResponseDto investExperienceResponseDto = new InvestExperienceResponseDto(convertInvestExperienceResponseDataDto(investRequestDto.getBaseParam().getUserId()));
            responseDto.setData(investExperienceResponseDto);
        }
        return responseDto;
    }

    private List<InvestExperienceResponseDataDto> convertInvestExperienceResponseDataDto (String loginName) {
        DecimalFormat decimalFormat = new DecimalFormat("######0.##");
        List<InvestExperienceResponseDataDto> investExperienceResponseDataDtos = Lists.newArrayList();
        List<UserCouponModel> userCouponModels = userCouponMapper.findByLoginName(loginName, null);
        for (UserCouponModel userCouponModel : userCouponModels) {
            CouponModel couponModel = couponMapper.findById(userCouponModel.getCouponId());
            if (couponModel.getUserGroup() == UserGroup.EXPERIENCE_INVEST_SUCCESS) {
                InvestExperienceResponseDataDto investExperienceResponseDataDto = new InvestExperienceResponseDataDto();
                investExperienceResponseDataDto.setAmount(AmountConverter.convertCentToString(couponModel.getAmount()));
                investExperienceResponseDataDto.setEndDate(new DateTime(userCouponModel.getEndTime()).toString("yyyy-MM-dd"));
                investExperienceResponseDataDto.setInvestLowerLimit(AmountConverter.convertCentToString(couponModel.getInvestLowerLimit()));
                investExperienceResponseDataDto.setName(couponModel.getCouponType().getName());
                investExperienceResponseDataDto.setProductNewTypes(couponModel.getProductTypes());
                investExperienceResponseDataDto.setRate(decimalFormat.format(couponModel.getRate() * 100));
                investExperienceResponseDataDto.setStartDate(new DateTime(userCouponModel.getStartTime()).toString("yyyy-MM-dd"));
                investExperienceResponseDataDto.setUserCouponId(String.valueOf(userCouponModel.getId()));
                investExperienceResponseDataDto.setType(couponModel.getCouponType().name());
                investExperienceResponseDataDtos.add(investExperienceResponseDataDto);
            }
        }
        return investExperienceResponseDataDtos;
    }

    private InvestDto convertInvestDto(InvestRequestDto investRequestDto) {
        Source source = Source.valueOf(investRequestDto.getBaseParam().getPlatform().toUpperCase(Locale.ENGLISH));
        InvestDto investDto = new InvestDto();
        investDto.setSource(source);
        investDto.setAmount(investRequestDto.getInvestMoney());
        investDto.setLoanId(investRequestDto.getLoanId());
        investDto.setLoginName(investRequestDto.getBaseParam().getUserId());
        investDto.setChannel(mobileAppChannelService.obtainChannelBySource(investRequestDto.getBaseParam()));
        investDto.setUserCouponIds(investRequestDto.getUserCouponIds());
        return investDto;
    }

}
