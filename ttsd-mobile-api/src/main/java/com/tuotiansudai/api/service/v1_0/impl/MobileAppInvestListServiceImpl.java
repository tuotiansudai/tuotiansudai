package com.tuotiansudai.api.service.v1_0.impl;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.UnmodifiableIterator;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppInvestListService;
import com.tuotiansudai.api.util.PageValidUtils;
import com.tuotiansudai.coupon.service.CouponService;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.LoanRepayMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.transfer.service.InvestTransferService;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.RandomUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class MobileAppInvestListServiceImpl implements MobileAppInvestListService {

    @Autowired
    private InvestService investService;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Autowired
    private LoanRepayMapper loanRepayMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private RandomUtils randomUtils;

    @Autowired
    private InvestTransferService investTransferService;

    @Autowired
    private PageValidUtils pageValidUtils;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CouponService couponService;

    private static String RED_ENVELOPE_DESCRIPTION = "%s元投资红包";

    private static String INVEST_COUPON_DESCRIPTION = "%s加息券";

    @Override
    public BaseResponseDto generateInvestList(InvestListRequestDto investListRequestDto) {
        BaseResponseDto dto = new BaseResponseDto();
        Integer index = investListRequestDto.getIndex();
        Integer pageSize = pageValidUtils.validPageSizeLimit(investListRequestDto.getPageSize());
        final String loginName = investListRequestDto.getBaseParam().getUserId();
        long loanId = Long.parseLong(investListRequestDto.getLoanId());

        LoanModel achievementLoanModel = loanMapper.findById(loanId);
        if (achievementLoanModel == null) {
            dto.setCode(ReturnMessage.LOAN_NOT_FOUND.getCode());
            dto.setMessage(ReturnMessage.LOAN_NOT_FOUND.getMsg());
            return dto;
        }

        long count = investMapper.findCountByStatus(loanId, InvestStatus.SUCCESS);

        if (index == null || index <= 0) {
            index = 1;
        }

        List<InvestModel> investModels = investMapper.findByStatus(loanId, (index - 1) * pageSize, pageSize, InvestStatus.SUCCESS);
        List<InvestRecordResponseDataDto> investRecordResponseDataDto = null;
        if (CollectionUtils.isNotEmpty(investModels)) {
            investRecordResponseDataDto = Lists.transform(investModels, input -> {
                input.setLoginName(randomUtils.encryptMobile(loginName, input.getLoginName(), input.getId()));
                return new InvestRecordResponseDataDto(input);
            });
        }

        List<LoanAchievementsResponseDto> loanAchievementsResponseDtoList = Lists.newArrayList(
                getLoanAchievementsResponseDto(UserGroup.FIRST_INVEST_ACHIEVEMENT, achievementLoanModel.getFirstInvestAchievementId(), loginName),
                getLoanAchievementsResponseDto(UserGroup.MAX_AMOUNT_ACHIEVEMENT, achievementLoanModel.getMaxAmountAchievementId(), loginName),
                getLoanAchievementsResponseDto(UserGroup.LAST_INVEST_ACHIEVEMENT, achievementLoanModel.getLastInvestAchievementId(), loginName));

        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        InvestListResponseDataDto investListResponseDataDto = new InvestListResponseDataDto();
        investListResponseDataDto.setInvestRecord(investRecordResponseDataDto);
        investListResponseDataDto.setIndex(index);
        investListResponseDataDto.setPageSize(pageSize);
        investListResponseDataDto.setTotalCount((int) count);
        investListResponseDataDto.setAchievements(loanAchievementsResponseDtoList);

        dto.setData(investListResponseDataDto);
        return dto;
    }

    @Override
    public BaseResponseDto<UserInvestListResponseDataDto> generateUserInvestList(UserInvestListRequestDto requestDto) {
        String loginName = requestDto.getBaseParam().getUserId();
        int pageSize = pageValidUtils.validPageSizeLimit(requestDto.getPageSize());
        int index = (requestDto.getIndex() - 1) * pageSize;
        List<InvestModel> investList;
        int investListCount;
        if (isTransferApplicationTransferable(requestDto)) {
            investList = investMapper.findByLoginNameExceptTransfer(loginName, index, pageSize, false);
            UnmodifiableIterator<InvestModel> filter = Iterators.filter(investList.iterator(), new Predicate<InvestModel>() {
                @Override
                public boolean apply(InvestModel input) {
                    return TransferStatus.TRANSFERABLE == input.getTransferStatus() && investTransferService.isTransferable(input.getId());
                }
            });
            investList = Lists.newArrayList(filter);
            int fromIndex = index;
            int toIndex = fromIndex + pageSize;
            investListCount = investList.size();
            if (fromIndex >= investList.size()) {
                fromIndex = investList.size();
            }
            if (toIndex >= investList.size()) {
                toIndex = investList.size();
            }
            investList = investList.subList(fromIndex, toIndex);
        } else {
            investList = investMapper.findByLoginNameExceptTransfer(loginName, index, pageSize, true);
            investListCount = (int) investMapper.findCountByLoginNameExceptTransfer(loginName);
        }
        // build InvestList
        UserInvestListResponseDataDto dtoData = new UserInvestListResponseDataDto();
        dtoData.setInvestList(convertResponseData(investList, requestDto.getTransferStatus()));
        dtoData.setIndex(requestDto.getIndex());
        dtoData.setPageSize(requestDto.getPageSize());
        dtoData.setTotalCount(investListCount);

        // BaseDto
        BaseResponseDto<UserInvestListResponseDataDto> dto = new BaseResponseDto<>();
        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        dto.setData(dtoData);

        return dto;
    }

    private boolean isTransferApplicationTransferable(UserInvestListRequestDto requestDto) {
        return CollectionUtils.isNotEmpty(requestDto.getTransferStatus())
                && requestDto.getTransferStatus().size() == 1 && requestDto.getTransferStatus().contains(TransferStatus.TRANSFERABLE);
    }

    private List<UserInvestRecordResponseDataDto> convertResponseData(List<InvestModel> investList, List<TransferStatus> transferStatuses) {
        List<UserInvestRecordResponseDataDto> list = Lists.newArrayList();
        Map<Long, LoanModel> loanMapCache = Maps.newHashMap();
        if (investList != null) {
            for (InvestModel invest : investList) {
                long loanId = invest.getLoanId();
                LoanModel loanModel;
                if (loanMapCache.containsKey(loanId)) {
                    loanModel = loanMapCache.get(loanId);
                } else {
                    loanModel = loanMapper.findById(loanId);
                    loanMapCache.put(loanId, loanModel);
                }
                UserInvestRecordResponseDataDto dto = new UserInvestRecordResponseDataDto(invest, loanModel);

                long amount = 0;
                List<InvestRepayModel> investRepayModels = investRepayMapper.findByInvestIdAndPeriodAsc(invest.getId());
                for (InvestRepayModel investRepayModel : investRepayModels) {
                    amount += investRepayModel.getExpectedInterest() - investRepayModel.getExpectedFee();
                }

                if (CollectionUtils.isEmpty(investRepayModels)) {
                    amount = investService.estimateInvestIncome(invest.getLoanId(), invest.getInvestFeeRate(), invest.getLoginName(), invest.getAmount(), new Date());
                }

                dto.setInvestInterest(AmountConverter.convertCentToString(amount));
                String transferStatus;
                if (invest.getTransferStatus() == TransferStatus.TRANSFERABLE) {
                    transferStatus = investTransferService.isTransferable(invest.getId()) ? invest.getTransferStatus().name() : "";
                } else if (invest.getTransferStatus() == TransferStatus.NONTRANSFERABLE) {
                    transferStatus = "";
                } else {
                    transferStatus = invest.getTransferStatus().name();
                }
                dto.setTransferStatus(transferStatus);
                LoanRepayModel loanRepayModel = loanRepayMapper.findCurrentLoanRepayByLoanId(invest.getLoanId());
                dto.setLeftPeriod(loanRepayModel == null ? "0" : String.valueOf(investRepayMapper.findLeftPeriodByTransferInvestIdAndPeriod(invest.getId(), loanRepayModel.getPeriod())));
                list.add(dto);
            }
        }
        return list;

    }

    private LoanAchievementsResponseDto getLoanAchievementsResponseDto(UserGroup userGroup, Long investId, String loginName) {
        LoanAchievementsResponseDto investAchievementResponseDto = new LoanAchievementsResponseDto(userGroup);
        List<CouponModel> fistInvestCoupon = couponService.findCouponByUserGroup(Lists.newArrayList(userGroup));

        fistInvestCoupon.forEach(
                input -> investAchievementResponseDto.getCoupon().add((input.getCouponType().equals(CouponType.RED_ENVELOPE) ?
                        String.format(RED_ENVELOPE_DESCRIPTION, AmountConverter.convertCentToString(input.getAmount()).replaceAll("\\.00", "")) :
                        String.format(INVEST_COUPON_DESCRIPTION, (input.getRate() * 100) + "%").replaceAll("\\.0", ""))));

        if (investId != null) {
            UserModel userModel = userMapper.findByLoginName(investMapper.findById(investId).getLoginName());
            investAchievementResponseDto.setMobile(randomUtils.encryptMobile(loginName, userModel.getLoginName(), investId));
        }

        return investAchievementResponseDto;
    }
}
