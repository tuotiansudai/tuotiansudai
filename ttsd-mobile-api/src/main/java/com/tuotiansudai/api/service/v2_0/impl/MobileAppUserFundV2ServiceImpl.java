package com.tuotiansudai.api.service.v2_0.impl;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.dto.v2_0.UserFundResponseDataDto;
import com.tuotiansudai.api.service.v2_0.MobileAppUserFundV2Service;
import com.tuotiansudai.coupon.service.UserCouponService;
import com.tuotiansudai.membership.repository.mapper.MembershipPrivilegeMapper;
import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.repository.model.MembershipPrivilegeModel;
import com.tuotiansudai.membership.repository.model.UserMembershipModel;
import com.tuotiansudai.membership.service.UserMembershipEvaluator;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.UserFundMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.UserFundView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class MobileAppUserFundV2ServiceImpl implements MobileAppUserFundV2Service {

    @Autowired
    private UserFundMapper userFundMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private UserMembershipEvaluator userMembershipEvaluator;

    @Autowired
    private UserCouponService userCouponService;

    @Autowired
    private MembershipPrivilegeMapper membershipPrivilegeMapper;

    @Override
    public BaseResponseDto<UserFundResponseDataDto> getUserFund(String loginName) {
        UserFundView userFundView = userFundMapper.findByLoginName(loginName);

        AccountModel accountModel = accountMapper.findByLoginName(loginName);

        MembershipModel evaluate = userMembershipEvaluator.evaluate(loginName);

        UserMembershipModel userMembershipModel = userMembershipEvaluator.evaluateUserMembership(loginName, new Date());

        int membershipLevel = evaluate != null ? evaluate.getLevel() : 0;
        long balance = accountModel != null ? accountModel.getBalance() : 0;
        long point = accountModel != null ? accountModel.getPoint() : 0;
        long membershipPoint = accountModel != null ? accountModel.getMembershipPoint() : 0;
        int usableUserCouponCount = userCouponService.getUnusedUserCoupons(loginName).size();
        Date membershipExpiredDate = userMembershipModel != null && (userMembershipModel.getType().name().equals("GIVEN") || userMembershipModel.getType().name().equals("PURCHASED")) ? userMembershipModel.getExpiredTime() : null;
        MembershipPrivilegeModel membershipPrivilegeModel = membershipPrivilegeMapper.findValidPrivilegeModelByLoginName(loginName,new Date());
        Date membershipPrivilegeExpiredDate = membershipPrivilegeModel !=null?membershipPrivilegeModel.getEndTime():null;
        BaseResponseDto<UserFundResponseDataDto> dto = new BaseResponseDto<>(ReturnMessage.SUCCESS);
        dto.setData(new UserFundResponseDataDto(userFundView, balance, point, membershipLevel, membershipPoint, usableUserCouponCount, membershipExpiredDate,membershipPrivilegeExpiredDate));

        return dto;
    }
}
