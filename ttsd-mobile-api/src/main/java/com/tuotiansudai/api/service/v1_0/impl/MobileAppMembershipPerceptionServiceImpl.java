package com.tuotiansudai.api.service.v1_0.impl;


import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.MembershipPerceptionRequestDto;
import com.tuotiansudai.api.dto.v1_0.MembershipPerceptionResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.service.v1_0.MobileAppMembershipPerceptionService;
import com.tuotiansudai.membership.repository.mapper.MembershipMapper;
import com.tuotiansudai.membership.repository.mapper.UserMembershipMapper;
import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipModel;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.util.AmountConverter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
public class MobileAppMembershipPerceptionServiceImpl implements MobileAppMembershipPerceptionService {


    @Autowired
    private UserMembershipMapper userMemberhshipMapper;

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
        if(couponIds == null){
            return new BaseResponseDto(ReturnMessage.REQUEST_PARAM_IS_WRONG.getCode(), ReturnMessage.REQUEST_PARAM_IS_WRONG.getMsg());
        }

        UserMembershipModel userMembershipModel = userMemberhshipMapper.findCurrentMaxByLoginName(loginName);
        MembershipModel membershipModel = membershipMapper.findById(userMembershipModel.getMembershipId());
        String getMoney = AmountConverter.convertCentToString(investService.calculateMembershipPreference(loginName,  Long.parseLong(loanId),  AmountConverter.convertStringToCent(amount)));

        MembershipPerceptionResponseDataDto membershipPerceptionResponseDataDto = new MembershipPerceptionResponseDataDto();
        membershipPerceptionResponseDataDto.setTip(MessageFormat.format("V{0}会员,专享服务费{1}折优惠,已经多赚{2}元",
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
