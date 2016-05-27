package com.tuotiansudai.api.service.v2_0.impl;

import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v2_0.ReturnMessage;
import com.tuotiansudai.api.dto.v2_0.UserInvestListRequestDto;
import com.tuotiansudai.api.dto.v2_0.UserInvestListResponseDataDto;
import com.tuotiansudai.api.dto.v2_0.UserInvestRecordResponseDataDto;
import com.tuotiansudai.api.service.v2_0.MobileAppInvestListsService;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.transfer.service.InvestTransferService;
import com.tuotiansudai.util.AmountConverter;
import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MobileAppInvestListsServiceImpl implements MobileAppInvestListsService{

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Autowired
    private InvestTransferService investTransferService;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Override
    public BaseResponseDto<UserInvestListResponseDataDto> generateUserInvestList(UserInvestListRequestDto userInvestListRequestDto) {
        String loginName = userInvestListRequestDto.getBaseParam().getUserId();
        int pageSize = userInvestListRequestDto.getPageSize();
        int index = (userInvestListRequestDto.getIndex() - 1) * pageSize;
        List<InvestModel>  investModels = investMapper.findInvestorInvestWithoutTransferPagination(loginName, userInvestListRequestDto.getStatus(), index, pageSize);

        UserInvestListResponseDataDto dtoData = new UserInvestListResponseDataDto();
        dtoData.setInvestList(convertResponseData(investModels));
        dtoData.setIndex(userInvestListRequestDto.getIndex());
        dtoData.setPageSize(userInvestListRequestDto.getPageSize());
        long investModelCount = investMapper.countInvestorInvestWithoutTransferPagination(loginName, userInvestListRequestDto.getStatus());
        dtoData.setTotalCount((int)investModelCount);

        BaseResponseDto<UserInvestListResponseDataDto> dto = new BaseResponseDto<>();
        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        dto.setData(dtoData);

        return dto;
    }

    private List<UserInvestRecordResponseDataDto> convertResponseData(List<InvestModel>  investModels) {
        List<UserInvestRecordResponseDataDto> list = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(investModels)) {
            for (InvestModel investModel : investModels) {
                LoanModel loanModel = loanMapper.findById(investModel.getLoanId());
                UserInvestRecordResponseDataDto dto = new UserInvestRecordResponseDataDto(investModel, loanModel);
                long actualInterest = 0;
                long expectedInterest = 0;
                List<InvestRepayModel> investRepayModels = investRepayMapper.findByInvestIdAndPeriodAsc(investModel.getId());
                for (InvestRepayModel investRepayModel : investRepayModels) {
                    actualInterest += investRepayModel.getActualInterest() - investRepayModel.getActualFee() + investRepayModel.getDefaultInterest();
                    expectedInterest += investRepayModel.getExpectedInterest() - investRepayModel.getExpectedFee() + investRepayModel.getDefaultInterest();
                }
                dto.setActualInterest(AmountConverter.convertCentToString(actualInterest));
                dto.setExpectedInterest(AmountConverter.convertCentToString(expectedInterest));
                InvestRepayModel lastInvestRepayModel = investRepayMapper.findByInvestIdAndPeriod(investModel.getId(), loanModel.getPeriods());
                if (lastInvestRepayModel != null) {
                    dto.setLastRepayDate(new DateTime(loanModel.getStatus() == LoanStatus.COMPLETE ? lastInvestRepayModel.getActualRepayDate() : lastInvestRepayModel.getRepayDate()).toString("yyyy-MM-dd"));
                }

                String transferStatus;
                if (investModel.getTransferStatus() == TransferStatus.TRANSFERABLE) {
                    transferStatus = investTransferService.isTransferable(investModel.getId()) ? investModel.getTransferStatus().name() : "";
                } else if (investModel.getTransferStatus() == TransferStatus.NONTRANSFERABLE) {
                    transferStatus = "";
                } else {
                    transferStatus = investModel.getTransferStatus().name();
                }
                dto.setTransferStatus(transferStatus);

                List<UserCouponModel> userCouponModels = userCouponMapper.findUserCouponSuccessByInvestId(investModel.getId());
                List<CouponType> couponTypes = Lists.newArrayList();
                for (UserCouponModel userCouponModel : userCouponModels) {
                    CouponModel couponModel = couponMapper.findById(userCouponModel.getCouponId());
                    couponTypes.add(couponModel.getCouponType());
                }

                dto.setUserCoupons(couponTypes);
                dto.setUsedCoupon(CollectionUtils.isNotEmpty(couponTypes) && !couponTypes.contains(CouponType.RED_ENVELOPE));
                dto.setUsedRedEnvelope(couponTypes.contains(CouponType.RED_ENVELOPE));
                list.add(dto);
            }
        }
        return list;
    }

}
