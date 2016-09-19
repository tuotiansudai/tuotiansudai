package com.tuotiansudai.api.service.v1_0.impl;


import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppMembershipPerceptionService;
import com.tuotiansudai.api.service.v1_0.MobileAppMembershipService;
import com.tuotiansudai.coupon.service.CouponService;
import com.tuotiansudai.membership.repository.mapper.MembershipExperienceBillMapper;
import com.tuotiansudai.membership.repository.mapper.MembershipMapper;
import com.tuotiansudai.membership.repository.mapper.UserMembershipMapper;
import com.tuotiansudai.membership.repository.model.MembershipExperienceBillModel;
import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipModel;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.util.AmountConverter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class MobileAppMembershipPerceptionServiceImpl implements MobileAppMembershipPerceptionService {


    @Autowired
    private UserMembershipMapper userMemberhshipMapper;

    @Autowired
    private CouponService couponService;

    @Autowired
    private InvestService investService;

    @Autowired
    private MembershipMapper membershipMapper;

    @Override
    public BaseResponseDto getMembershipPerception(MembershipPerceptionRequestDto requestDto) {
        String loginName = requestDto.getBaseParam().getUserId();
        String loanId = requestDto.getLoanId();
        if (StringUtils.isEmpty(loanId)) {
            return new BaseResponseDto(ReturnMessage.REQUEST_PARAM_IS_WRONG.getCode(), ReturnMessage.REQUEST_PARAM_IS_WRONG.getMsg());
        }
        String amount = requestDto.getInvestAmount();
        if (StringUtils.isEmpty(loanId)) {
            return new BaseResponseDto(ReturnMessage.REQUEST_PARAM_IS_WRONG.getCode(), ReturnMessage.REQUEST_PARAM_IS_WRONG.getMsg());
        }
        List<Long> couponIds = requestDto.getUserCouponIds();
        if(CollectionUtils.isEmpty(couponIds)){
            return new BaseResponseDto(ReturnMessage.REQUEST_PARAM_IS_WRONG.getCode(), ReturnMessage.REQUEST_PARAM_IS_WRONG.getMsg());
        }

        UserMembershipModel userMembershipModel = userMemberhshipMapper.findCurrentMaxByLoginName(loginName);
        MembershipModel membershipModel = membershipMapper.findById(userMembershipModel.getMembershipId());
        long couponExpectedInterest = couponService.estimateCouponExpectedInterest(loginName, Long.parseLong(loanId), requestDto.getUserCouponIds(), AmountConverter.convertStringToCent(amount));
        long investExpectedInterest = investService.estimateInvestIncome( Long.parseLong(loanId), loginName,  AmountConverter.convertStringToCent(amount));
        String getMoney = AmountConverter.convertCentToString(investService.calculateMembershipPreference(loginName,  Long.parseLong(loanId),  AmountConverter.convertStringToCent(amount)));

        MembershipPerceptionResponseDataDto membershipPerceptionResponseDataDto = new MembershipPerceptionResponseDataDto();
        membershipPerceptionResponseDataDto.setTip(MessageFormat.format("{0}是实际收益.V{1}会员,专享服务费{2}折优惠,已经多赚{3}元",
                                                                        AmountConverter.convertCentToString(investExpectedInterest + couponExpectedInterest),
                                                                        membershipModel.getLevel(),
                                                                        membershipModel.getFee() * 100,
                                                                        getMoney
                                                                        ));

        BaseResponseDto<MembershipPerceptionResponseDataDto> responseDto = new BaseResponseDto<>();
        responseDto.setCode(ReturnMessage.SUCCESS.getCode());
        responseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        responseDto.setData(membershipPerceptionResponseDataDto);
        return responseDto;
    }
}
