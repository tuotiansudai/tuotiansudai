package com.tuotiansudai.service.impl;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.LoanRepayMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.LoanRepayService;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.DateUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public BaseDto<BasePaginationDataDto> findLoanRepayPagination(int index, int pageSize, Long loanId,
                                                                  String loginName, Date startTime, Date endTime, RepayStatus repayStatus) {
        if (index < 1) {
            index = 1;
        }
        if (pageSize < 1) {
            pageSize = 10;
        }

        BaseDto<BasePaginationDataDto> baseDto = new BaseDto<>();
        int count = loanRepayMapper.findLoanRepayCount(loanId, loginName, repayStatus, startTime, endTime);
        List<LoanRepayModel> loanRepayModels = loanRepayMapper.findLoanRepayPagination((index - 1) * pageSize, pageSize,
                loanId, loginName, repayStatus, startTime, endTime);
        List<LoanRepayDataItemDto> loanRepayDataItemDtos = Lists.newArrayList();
        for (LoanRepayModel loanRepayModel : loanRepayModels) {
            LoanRepayDataItemDto loanRepayDataItemDto = new LoanRepayDataItemDto(loanRepayModel);
            loanRepayDataItemDtos.add(loanRepayDataItemDto);
        }
        BasePaginationDataDto<LoanRepayDataItemDto> basePaginationDataDto = new BasePaginationDataDto<>(index, pageSize, count, loanRepayDataItemDtos);
        basePaginationDataDto.setStatus(true);
        baseDto.setData(basePaginationDataDto);
        return baseDto;

    }

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

    private boolean isNeedCalculateDefaultInterestLoanRepay (LoanRepayModel loanRepayModel) {
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

    private boolean isNeedCalculateDefaultInterestInvestRepay (InvestRepayModel investRepayModel) {
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

    private void calculateDefaultInterestEveryLoan(LoanRepayModel loanRepayModel) throws Exception{
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
                    LoanRepayNotifyDto dto = new LoanRepayNotifyDto();
                    dto.setMobile(((String) entry.getKey()).trim());
                    dto.setRepayAmount(AmountConverter.convertCentToString(amount));
                    smsWrapperClient.sendLoanRepayNotify(dto);
                }
            }
        }
    }

}
