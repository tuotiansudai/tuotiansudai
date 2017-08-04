package com.tuotiansudai.api.service.v3_0.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.dto.v3_0.UserInvestListRequestDto;
import com.tuotiansudai.api.dto.v3_0.UserInvestListResponseDataDto;
import com.tuotiansudai.api.dto.v3_0.UserInvestRecordResponseDataDto;
import com.tuotiansudai.api.service.v3_0.MobileAppInvestListsV3Service;
import com.tuotiansudai.api.util.PageValidUtils;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.transfer.service.InvestTransferService;
import com.tuotiansudai.util.AmountConverter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
public class MobileAppInvestListsV3ServiceImpl implements MobileAppInvestListsV3Service {

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private InvestExtraRateMapper investExtraRateMapper;

    @Autowired
    private CouponRepayMapper couponRepayMapper;

    @Autowired
    private TransferApplicationMapper transferApplicationMapper;

    @Autowired
    private PageValidUtils pageValidUtils;

    @Autowired
    private InvestTransferService investTransferService;

    @Override
    public BaseResponseDto<UserInvestListResponseDataDto> generateUserInvestList(UserInvestListRequestDto userInvestListRequestDto) {
        String loginName = userInvestListRequestDto.getBaseParam().getUserId();
        int pageSize = pageValidUtils.validPageSizeLimit(userInvestListRequestDto.getPageSize());
        int index = (userInvestListRequestDto.getIndex() - 1) * pageSize;

        List<InvestModel> investModels = investMapper.findInvestorInvestAndTransferPagination(loginName, userInvestListRequestDto.getStatus(), index, pageSize);

        UserInvestListResponseDataDto dtoData = new UserInvestListResponseDataDto();
        dtoData.setInvestList(convertResponseData(investModels, userInvestListRequestDto.getStatus()));
        dtoData.setIndex(userInvestListRequestDto.getIndex());
        dtoData.setPageSize(userInvestListRequestDto.getPageSize());
        long investModelCount = investMapper.countInvestorInvestAndTransferPagination(loginName, userInvestListRequestDto.getStatus());
        dtoData.setTotalCount((int) investModelCount);

        BaseResponseDto<UserInvestListResponseDataDto> dto = new BaseResponseDto<>();
        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        dto.setData(dtoData);

        return dto;
    }

    private List<UserInvestRecordResponseDataDto> convertResponseData(List<InvestModel> investModels, LoanStatus loanStatus) {
        List<UserInvestRecordResponseDataDto> list = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(investModels)) {
            for (InvestModel investModel : investModels) {
                LoanModel loanModel = loanMapper.findById(investModel.getLoanId());
                UserInvestRecordResponseDataDto dto = new UserInvestRecordResponseDataDto(investModel, loanModel);

                TransferApplicationModel transferApplicationModel;
                if (investModel.getTransferInvestId() != null) {
                    transferApplicationModel = transferApplicationMapper.findByInvestId(investModel.getId());
                } else {
                    List<TransferApplicationModel> transferApplicationModels = transferApplicationMapper.findByTransferInvestId(investModel.getId(), Lists.newArrayList(TransferStatus.TRANSFERRING));
                    transferApplicationModel = CollectionUtils.isNotEmpty(transferApplicationModels) ? transferApplicationModels.get(0) : null;
                }
                dto.setLoanName(transferApplicationModel != null ? transferApplicationModel.getName() : loanModel.getName());
                dto.setTransferApplicationId(transferApplicationModel != null ? String.valueOf(transferApplicationModel.getId()) : "");
                dto.setInvestAmount(transferApplicationModel != null ? AmountConverter.convertCentToString(transferApplicationModel.getInvestAmount()) : AmountConverter.convertCentToString(investModel.getAmount()));
                dto.setTransferInvest(transferApplicationModel != null ? true : false);

                /*if (loanStatus.equals(LoanStatus.REPAYING) && loanModel.getProductType().equals(ProductType.EXPERIENCE)) {
                    List<UserCouponModel> userCouponModelList = userCouponMapper.findByInvestId(investModel.getId());
                    if (CollectionUtils.isNotEmpty(userCouponModelList)) {
                        dto.setInvestAmount(AmountConverter.convertCentToString(couponMapper.findById(userCouponModelList.get(0).getCouponId()).getAmount()));
                    }
                }*/

                long actualInterest = 0;
                long expectedInterest = 0;
                String lastRepayDate = null;
                int investRepayCount = 1;

                List<InvestRepayModel> investRepayModels = investRepayMapper.findByInvestIdAndPeriodAsc(investModel.getId());
                for (InvestRepayModel investRepayModel : investRepayModels) {
                    CouponRepayModel couponRepayModel = couponRepayMapper.findCouponRepayByInvestIdAndPeriod(investRepayModel.getInvestId(), investRepayModel.getPeriod());
                    actualInterest += investRepayModel.getActualInterest() - investRepayModel.getActualFee() + investRepayModel.getDefaultInterest();
                    expectedInterest += investRepayModel.getExpectedInterest() - investRepayModel.getExpectedFee() + investRepayModel.getDefaultInterest();
                    if (couponRepayModel != null) {
                        actualInterest += couponRepayModel.getRepayAmount();
                        expectedInterest += couponRepayModel.getExpectedInterest() - couponRepayModel.getExpectedFee();
                    }
                    //取最后一个的到期日
                    if (investRepayCount == investRepayModels.size()) {
                        lastRepayDate = new DateTime(loanModel.getStatus() == LoanStatus.COMPLETE ? investRepayModel.getActualRepayDate() : investRepayModel.getRepayDate()).toString("yyyy-MM-dd");
                    }
                    investRepayCount++;
                }
                dto.setActualInterest(AmountConverter.convertCentToString(actualInterest));
                dto.setLastRepayDate(StringUtils.trimToEmpty(lastRepayDate));

                //阶梯加息的利息计算
                long expectedExtraInterest = 0;
                InvestExtraRateModel investExtraRateModel = investExtraRateMapper.findByInvestId(investModel.getId());
                if (investExtraRateModel != null) {
                    expectedExtraInterest = investExtraRateModel.getExpectedInterest() - investExtraRateModel.getExpectedFee();
                }

                //债权转让折扣计算
                long discount = 0;
                List<TransferApplicationModel> transferApplicationModelSuccess = transferApplicationMapper.findByTransferInvestId(investModel.getId(), Lists.newArrayList(TransferStatus.SUCCESS));
                if (transferApplicationModelSuccess.size() > 0) {
                    discount = transferApplicationModelSuccess.get(0).getInvestAmount() - transferApplicationModelSuccess.get(0).getTransferAmount();
                }

                expectedInterest = dto.isTransferInvest() ? expectedInterest + expectedExtraInterest + discount : expectedInterest + expectedExtraInterest;

                dto.setExpectedInterest(AmountConverter.convertCentToString(expectedInterest));

                dto.setLastRepayDate(StringUtils.trimToEmpty(lastRepayDate));
                dto.setTransferStatus(investTransferService.isTransferable(investModel.getId()) ? TransferStatus.TRANSFERABLE.name() : (investModel.getTransferStatus().equals(TransferStatus.TRANSFERABLE) ? TransferStatus.NONTRANSFERABLE.name() : investModel.getTransferStatus().name()));
                List<UserCouponModel> userCouponModels = userCouponMapper.findUserCouponSuccessByInvestId(investModel.getId());
                List<CouponType> couponTypes = Lists.newArrayList();
                for (UserCouponModel userCouponModel : userCouponModels) {
                    CouponModel couponModel = couponMapper.findById(userCouponModel.getCouponId());
                    couponTypes.add(couponModel.getCouponType());
                }

                dto.setUsedCoupon(CollectionUtils.isNotEmpty(couponTypes) && !couponTypes.contains(CouponType.RED_ENVELOPE));
                dto.setUsedRedEnvelope(couponTypes.contains(CouponType.RED_ENVELOPE));
                dto.setProductNewType(loanModel.getProductType().name());

                createContractLink(dto, investModel);
                list.add(dto);
            }
        }
        return list;
    }

    private static final String NEW_CONTRACT_LINK_TEMPLATE = "https://tuotiansudai.com/contract/invest/contractNo/{0}";

    private static final String OLD_CONTRACT_LINK_TEMPLATE_INVEST = "https://tuotiansudai.com/contract/investor/loanId/{0}/investId/{1}";

    private static final String OLD_CONTRACT_LINK_TEMPLATE_TRANSFER = "https://tuotiansudai.com/contract/transfer/transferApplicationId/{0}";

    private void createContractLink(UserInvestRecordResponseDataDto dto, InvestModel investModel) {
        if (investModel != null && !Strings.isNullOrEmpty(investModel.getContractNo()) && !investModel.getContractNo().equals("OLD")) {
            dto.setContractLink(MessageFormat.format(NEW_CONTRACT_LINK_TEMPLATE, investModel.getContractNo()));
        } else {
            if (dto.isTransferInvest() && !TransferStatus.CANCEL.name().equals(dto.getTransferStatus())) {
                dto.setContractLink(MessageFormat.format(OLD_CONTRACT_LINK_TEMPLATE_TRANSFER, dto.getTransferApplicationId()));
            } else {
                dto.setContractLink(MessageFormat.format(OLD_CONTRACT_LINK_TEMPLATE_INVEST, dto.getLoanId(), dto.getInvestId()));
            }
        }
    }
}
