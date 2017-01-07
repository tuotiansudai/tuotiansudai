package com.tuotiansudai.service.impl;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.coupon.repository.mapper.CouponRepayMapper;
import com.tuotiansudai.coupon.repository.model.CouponRepayModel;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.LoanRepayDataItemDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.sms.RepayNotifyDto;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.LoanRepayService;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.DateUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.*;

@Service
public class LoanRepayServiceImpl implements LoanRepayService {

    static Logger logger = Logger.getLogger(LoanRepayServiceImpl.class);

    @Value("${pay.overdue.fee}")
    private double overdueFee;

    @Autowired
    private LoanRepayMapper loanRepayMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private SmsWrapperClient smsWrapperClient;

    @Value("#{'${repay.remind.mobileList}'.split('\\|')}")
    private List<String> repayRemindMobileList;

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Autowired
    private CouponRepayMapper couponRepayMapper;

    @Autowired
    private InvestExtraRateMapper investExtraRateMapper;

    @Override
    public List<LoanRepayModel> findLoanRepayInAccount(String loginName, Date startTime, Date endTime, int startLimit, int endLimit) {
        return this.loanRepayMapper.findByLoginNameAndTimeRepayList(loginName, startTime, endTime, startLimit, endLimit);
    }

    @Override
    public long findByLoginNameAndTimeSuccessRepay(String loginName, Date startTime, Date endTime) {
        return loanRepayMapper.findByLoginNameAndTimeSuccessRepay(loginName, startTime, endTime);
    }

    @Override
    public void calculateDefaultInterest() {
        List<LoanRepayModel> loanRepayModels = loanRepayMapper.findNotCompleteLoanRepay();
        for (LoanRepayModel loanRepayModel : loanRepayModels) {
            try {
                calculateDefaultInterestEveryLoan(loanRepayModel);

            } catch (Exception e) {
                logger.error(e.getLocalizedMessage(), e);
            }
        }

    }

    private boolean isNeedCalculateDefaultInterestLoanRepay(LoanRepayModel loanRepayModel) {
        long loanId = loanRepayModel.getLoanId();
        int period = loanRepayModel.getPeriod();
        List<LoanRepayModel> loanRepayModels = loanRepayMapper.findByLoanIdAndLTPeriod(loanId, period);
        return CollectionUtils.isEmpty(loanRepayModels) || !Iterators.any(loanRepayModels.iterator(), new Predicate<LoanRepayModel>() {
            @Override
            public boolean apply(LoanRepayModel input) {
                return input.getStatus() == RepayStatus.OVERDUE;
            }
        });
    }

    private boolean isNeedCalculateDefaultInterestInvestRepay(InvestRepayModel investRepayModel) {
        long investId = investRepayModel.getInvestId();
        int period = investRepayModel.getPeriod();
        List<InvestRepayModel> investRepayModels = investRepayMapper.findByInvestIdAndLTPeriod(investId, period);
        return CollectionUtils.isEmpty(investRepayModels) || !Iterators.any(investRepayModels.iterator(), new Predicate<InvestRepayModel>() {
            @Override
            public boolean apply(InvestRepayModel input) {
                return input.getStatus() == RepayStatus.OVERDUE;
            }
        });
    }

    private void calculateDefaultInterestEveryLoan(LoanRepayModel loanRepayModel) throws Exception {
        LoanModel loanModel = loanMapper.findById(loanRepayModel.getLoanId());
        List<InvestRepayModel> investRepayModels = investRepayMapper.findInvestRepayByLoanIdAndPeriod(loanModel.getId(), loanRepayModel.getPeriod());
        for (InvestRepayModel investRepayModel : investRepayModels) {
            if (investRepayModel.getRepayDate().before(new Date())) {
                investRepayModel.setStatus(RepayStatus.OVERDUE);
                if (isNeedCalculateDefaultInterestInvestRepay(investRepayModel)) {
                    long investRepayDefaultInterest = new BigDecimal(investMapper.findById(investRepayModel.getInvestId()).getAmount()).multiply(new BigDecimal(overdueFee))
                            .multiply(new BigDecimal(DateUtil.differenceDay(investRepayModel.getRepayDate(), new Date()) + 1L))
                            .setScale(0, BigDecimal.ROUND_DOWN).longValue();
                    investRepayModel.setDefaultInterest(investRepayDefaultInterest);
                }
                investRepayMapper.update(investRepayModel);
            }
        }
        if (loanRepayModel.getRepayDate().before(new Date())) {
            loanRepayModel.setStatus(RepayStatus.OVERDUE);
            if (isNeedCalculateDefaultInterestLoanRepay(loanRepayModel)) {
                long loanRepayDefaultInterest = new BigDecimal(loanModel.getLoanAmount()).multiply(new BigDecimal(overdueFee))
                        .multiply(new BigDecimal(DateUtil.differenceDay(loanRepayModel.getRepayDate(), new Date()) + 1L)).setScale(0, BigDecimal.ROUND_DOWN).longValue();
                loanRepayModel.setDefaultInterest(loanRepayDefaultInterest);
            }
            loanRepayMapper.update(loanRepayModel);
            loanModel.setStatus(LoanStatus.OVERDUE);
            loanMapper.update(loanModel);
            boolean isLastPeriod = loanRepayMapper.findLastLoanRepay(loanRepayModel.getLoanId()).getPeriod() == loanRepayModel.getPeriod();
            if(isLastPeriod){
                List<InvestExtraRateModel> investExtraRateModels = investExtraRateMapper.findByLoanId(loanRepayModel.getLoanId());
                investExtraRateModels.stream().forEach(investExtraRateModel -> {
                    investExtraRateModel.setStatus(RepayStatus.OVERDUE);
                    investExtraRateMapper.update(investExtraRateModel);
                });
                logger.info(String.format("investExtraRate status to overdue"));
            }
        }
        logger.info(MessageFormat.format("loanRepayId:{0} couponRepay status to overdue", loanRepayModel.getId()));
        List<CouponRepayModel> couponRepayModels = couponRepayMapper.findCouponRepayByLoanIdAndPeriod(loanModel.getId(), loanRepayModel.getPeriod());
        for (CouponRepayModel couponRepayModel : couponRepayModels) {
            if (couponRepayModel.getRepayDate().before(new Date())) {
                couponRepayModel.setStatus(RepayStatus.OVERDUE);
                couponRepayMapper.update(couponRepayModel);
            }
        }

    }

    @Override
    public void loanRepayNotify() {
        String today = DateFormatUtils.format(new Date(), "yyyy-MM-dd");
        List<LoanRepayNotifyModel> loanRepayNotifyModelList = loanRepayMapper.findLoanRepayNotifyToday(today);

        Map<String, Long> notifyMap = new HashMap<>();
        for (String mobile : repayRemindMobileList) {
            notifyMap.put(mobile, 0L);
        }

        for (LoanRepayNotifyModel model : loanRepayNotifyModelList) {
            try {
                BaseDto<PayDataDto> response = payWrapperClient.autoRepay(model.getId());
                if (response.isSuccess() && response.getData().getStatus()) {
                    continue;
                }
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage(), e);
                continue;
            }
            for (String mobile : repayRemindMobileList) {
                notifyMap.put(mobile, notifyMap.get(mobile) + model.getRepayAmount());
            }
            if (notifyMap.get(model.getMobile()) == null) {
                notifyMap.put(model.getMobile(), model.getRepayAmount());
            } else {
                notifyMap.put(model.getMobile(), notifyMap.get(model.getMobile()) + model.getRepayAmount());
            }
        }

        if (loanRepayNotifyModelList.size() > 0) {
            for (Map.Entry entry : notifyMap.entrySet()) {
                long amount = (Long) entry.getValue();
                if (amount > 0) {
                    logger.info("sent loan repay notify sms message to " + entry.getKey() + ", money:" + entry.getValue());
                    RepayNotifyDto dto = new RepayNotifyDto();
                    dto.setMobile(((String) entry.getKey()).trim());
                    dto.setRepayAmount(AmountConverter.convertCentToString(amount));
                    smsWrapperClient.sendLoanRepayNotify(dto);
                }
            }
        }
    }

}
