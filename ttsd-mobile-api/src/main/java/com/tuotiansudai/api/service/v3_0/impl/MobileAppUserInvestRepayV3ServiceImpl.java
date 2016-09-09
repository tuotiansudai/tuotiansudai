package com.tuotiansudai.api.service.v3_0.impl;

import com.google.common.base.Preconditions;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v3_0.MobileAppUserInvestRepayV3Service;
import com.tuotiansudai.membership.repository.mapper.MembershipMapper;
import com.tuotiansudai.membership.repository.mapper.UserMembershipMapper;
import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipModel;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.InvestRepayModel;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.transfer.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.transfer.repository.model.TransferApplicationModel;
import com.tuotiansudai.util.AmountConverter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MobileAppUserInvestRepayV3ServiceImpl implements MobileAppUserInvestRepayV3Service {

    static Logger logger = Logger.getLogger(MobileAppUserInvestRepayV3ServiceImpl.class);

    @Autowired
    LoanMapper loanMapper;

    @Autowired
    TransferApplicationMapper transferApplicationMapper;

    @Autowired
    InvestRepayMapper investRepayMapper;

    @Autowired
    MembershipMapper membershipMapper;

    @Autowired
    UserMembershipMapper userMembershipMapper;

    public BaseResponseDto userInvestRepay(UserInvestRepayRequestDto userInvestRepayRequestDto) {
        Preconditions.checkNotNull(userInvestRepayRequestDto.getInvestId());
        Preconditions.checkNotNull(userInvestRepayRequestDto.getBaseParam().getUserId());
        //return TransferLoan Details
        TransferApplicationModel transferApplicationModel = transferApplicationMapper.findByInvestId(
                Long.parseLong(userInvestRepayRequestDto.getInvestId().trim()));
        if (null == transferApplicationModel) {
            return new BaseResponseDto(ReturnMessage.ERROR.getCode(), ReturnMessage.ERROR.getMsg());
        }
        LoanModel loanModel = loanMapper.findById(transferApplicationModel.getLoanId());
        if (null == loanModel) {
            return new BaseResponseDto(ReturnMessage.ERROR.getCode(), ReturnMessage.ERROR.getMsg());
        }

        UserInvestRepayResponseDataDto userInvestRepayResponseDataDto = new UserInvestRepayResponseDataDto(loanModel, transferApplicationModel);

        long totalExpectedInterest = 0;
        long totalActualInterest = 0;
        List<InvestRepayModel> investRepayModels = investRepayMapper.findByLoginNameAndInvestId(userInvestRepayRequestDto.getBaseParam().getUserId(),
                Long.parseLong(userInvestRepayRequestDto.getInvestId().trim()));
        for (InvestRepayModel investRepayModel : investRepayModels) {
            totalExpectedInterest += investRepayModel.getExpectedInterest();
            totalActualInterest += investRepayModel.getActualInterest();
            userInvestRepayResponseDataDto.getInvestRepays().add(new InvestRepayDataDto(investRepayModel));
            if (investRepayModel.getPeriod() == loanModel.getPeriods()) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
                userInvestRepayResponseDataDto.setLastRepayDate(simpleDateFormat.format(investRepayModel.getRepayDate()));
            }
        }
        userInvestRepayResponseDataDto.setExpectedInterest(AmountConverter.convertCentToString(totalExpectedInterest));
        userInvestRepayResponseDataDto.setActualInterest(AmountConverter.convertCentToString(totalActualInterest));
        userInvestRepayResponseDataDto.setUnPaidRepay(AmountConverter.convertCentToString(totalExpectedInterest - totalActualInterest));

        List<UserMembershipModel> userMembershipModels = userMembershipMapper.findByLoginName(userInvestRepayRequestDto.getBaseParam().getUserId());
        UserMembershipModel curMembership = null;
        for (UserMembershipModel userMembershipModel : userMembershipModels) {
            if (null == curMembership) {
                curMembership = userMembershipModel;
            } else if (userMembershipModel.getExpiredTime().after(new Date()) && curMembership.getMembershipId() < userMembershipModel.getMembershipId()) {
                //等级越高的Membership id越大
                curMembership = userMembershipModel;
            }
        }
        if (null == curMembership) {
            userInvestRepayResponseDataDto.setMembershipLevel("V0会员服务费10折");
            logger.error(MessageFormat.format("[MobileAppUserInvestRepayV3ServiceImpl][userInvestRepay] {0}没有会员.", userInvestRepayRequestDto.getBaseParam().getUserId()));
        } else {
            MembershipModel membershipModel = membershipMapper.findById(curMembership.getMembershipId());
            if (null == membershipModel) {
                userInvestRepayResponseDataDto.setMembershipLevel("V0会员服务费10折");
                logger.warn(MessageFormat.format("[MobileAppUserInvestRepayV3ServiceImpl][userInvestRepay]会员不存在, loginName:{0}}", userInvestRepayRequestDto.getBaseParam().getUserId()));
            } else {
                final double baseFeeRate = 0.1D;
                userInvestRepayResponseDataDto.setMembershipLevel(MessageFormat.format("V{0}会员服务费{1}折", membershipModel.getLevel(), (int) ((membershipModel.getFee() / baseFeeRate) * 10)));
            }
        }
        BaseResponseDto baseResponseDto = new BaseResponseDto(ReturnMessage.SUCCESS.getCode(), ReturnMessage.SUCCESS.getMsg());
        baseResponseDto.setData(userInvestRepayResponseDataDto);

        return baseResponseDto;
    }
}
