package com.tuotiansudai.api.service.v1_0.impl;


import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.MembershipPerceptionRequestDto;
import com.tuotiansudai.api.dto.v1_0.MembershipPerceptionResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.service.v1_0.MobileAppMembershipPerceptionService;
import com.tuotiansudai.repository.mapper.UserCouponMapper;
import com.tuotiansudai.repository.model.UserCouponModel;
import com.tuotiansudai.membership.repository.mapper.MembershipMapper;
import com.tuotiansudai.membership.repository.mapper.MembershipPrivilegeMapper;
import com.tuotiansudai.membership.repository.mapper.UserMembershipMapper;
import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.repository.model.MembershipPrivilege;
import com.tuotiansudai.membership.repository.model.MembershipPrivilegeModel;
import com.tuotiansudai.membership.repository.model.UserMembershipModel;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.util.AmountConverter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Service
public class MobileAppMembershipPerceptionServiceImpl implements MobileAppMembershipPerceptionService {


    @Autowired
    private UserMembershipMapper userMemberhshipMapper;

    @Autowired
    private InvestService investService;

    @Autowired
    private MembershipMapper membershipMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private MembershipPrivilegeMapper membershipPrivilegeMapper;

    @Override
    public BaseResponseDto<MembershipPerceptionResponseDataDto> getMembershipPerception(MembershipPerceptionRequestDto requestDto) {
        String loginName = requestDto.getBaseParam().getUserId();
        String loanId = requestDto.getLoanId();
        if (StringUtils.isEmpty(loanId)) {
            return new BaseResponseDto(ReturnMessage.REQUEST_PARAM_IS_WRONG.getCode(), ReturnMessage.REQUEST_PARAM_IS_WRONG.getMsg());
        }
        String amount = requestDto.getInvestAmount();
        if (StringUtils.isEmpty(loanId)) {
            return new BaseResponseDto(ReturnMessage.REQUEST_PARAM_IS_WRONG.getCode(), ReturnMessage.REQUEST_PARAM_IS_WRONG.getMsg());
        }
        List<Long> userCouponIds = requestDto.getUserCouponIds();
        if (userCouponIds == null) {
            return new BaseResponseDto(ReturnMessage.REQUEST_PARAM_IS_WRONG.getCode(), ReturnMessage.REQUEST_PARAM_IS_WRONG.getMsg());
        }

        UserMembershipModel userMembershipModel = userMemberhshipMapper.findCurrentMaxByLoginName(loginName);
        MembershipModel membershipModel = membershipMapper.findById(userMembershipModel.getMembershipId());
        long couponId = userCouponIds.size() != 0 ? userCouponMapper.findById(userCouponIds.get(0)).getCouponId() : 0;
        String getMoney = AmountConverter.convertCentToString(investService.calculateMembershipPreference(loginName, Long.parseLong(loanId), Lists.newArrayList(couponId), AmountConverter.convertStringToCent(amount), Source.MOBILE));

        MembershipPrivilegeModel membershipPrivilegeModel = membershipPrivilegeMapper.findValidPrivilegeModelByLoginName(loginName, new Date());
        MembershipPerceptionResponseDataDto membershipPerceptionResponseDataDto = new MembershipPerceptionResponseDataDto();
        DecimalFormat decimalFormat = new DecimalFormat("######0.##");
        membershipPerceptionResponseDataDto.setTip(membershipPrivilegeModel != null ?
                String.format("增值特权，专享服务费7折优惠，已多赚%s元", getMoney)
                : String.format("V%s会员,专享服务费%s折优惠,已经多赚%s元",
                decimalFormat.format(membershipModel.getLevel()),
                decimalFormat.format(membershipModel.getFee() * 100)
                , getMoney));

        BaseResponseDto<MembershipPerceptionResponseDataDto> responseDto = new BaseResponseDto<>();
        responseDto.setCode(ReturnMessage.SUCCESS.getCode());
        responseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        responseDto.setData(membershipPerceptionResponseDataDto);
        return responseDto;
    }
}
