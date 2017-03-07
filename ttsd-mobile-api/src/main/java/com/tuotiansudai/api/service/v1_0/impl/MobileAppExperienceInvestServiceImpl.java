package com.tuotiansudai.api.service.v1_0.impl;


import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.InvestExperienceResponseDto;
import com.tuotiansudai.api.dto.v1_0.InvestRequestDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.service.v1_0.MobileAppChannelService;
import com.tuotiansudai.api.service.v1_0.MobileAppExperienceInvestService;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.InvestDto;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.service.ExperienceInvestService;
import com.tuotiansudai.util.AmountConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class MobileAppExperienceInvestServiceImpl implements MobileAppExperienceInvestService {

    @Autowired
    private ExperienceInvestService experienceInvestService;

    @Autowired
    private MobileAppChannelService mobileAppChannelService;

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
            InvestExperienceResponseDto investExperienceResponseDto = new InvestExperienceResponseDto();
            responseDto.setData(investExperienceResponseDto);
        }
        return responseDto;
    }

    private InvestDto convertInvestDto(InvestRequestDto investRequestDto) {
        Source source = Source.valueOf(investRequestDto.getBaseParam().getPlatform().toUpperCase(Locale.ENGLISH));
        InvestDto investDto = new InvestDto();
        investDto.setSource(source);
        investDto.setAmount(String.valueOf(AmountConverter.convertStringToCent(investRequestDto.getInvestMoney())));
        investDto.setLoanId(investRequestDto.getLoanId());
        investDto.setLoginName(investRequestDto.getBaseParam().getUserId());
        investDto.setChannel(mobileAppChannelService.obtainChannelBySource(investRequestDto.getBaseParam()));
        investDto.setUserCouponIds(investRequestDto.getUserCouponIds());
        return investDto;
    }

}
