package com.tuotiansudai.service.impl;

import com.google.common.collect.Lists;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.LoanRepayService;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.DateUtil;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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

    @Autowired
    private UserMapper userMapper;

    @Value("#{'${repay.remind.mobileList}'.split('\\|')}")
    private List<String> repayRemindMobileList;

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
            calculateDefaultInterestEveryLoan(loanRepayModel);
        }
    }

    @Transactional
    private void calculateDefaultInterestEveryLoan(LoanRepayModel loanRepayModel) {
        LoanModel loanModel = loanMapper.findById(loanRepayModel.getLoanId());
        List<InvestRepayModel> investRepayModels = investRepayMapper.findInvestRepayByLoanIdAndPeriod(loanModel.getId(), loanRepayModel.getPeriod());
        for (InvestRepayModel investRepayModel : investRepayModels) {
            if (investRepayModel.getRepayDate().before(new Date())) {
                investRepayModel.setStatus(RepayStatus.OVERDUE);
                long investRepayDefaultInterest = new BigDecimal(investMapper.findById(investRepayModel.getInvestId()).getAmount()).multiply(new BigDecimal(overdueFee))
                        .multiply(new BigDecimal(DateUtil.differenceDay(investRepayModel.getRepayDate(), new Date()) + 1L))
                        .setScale(0, BigDecimal.ROUND_DOWN).longValue();
                investRepayModel.setDefaultInterest(investRepayDefaultInterest);
                investRepayMapper.update(investRepayModel);
            }
        }
        if (loanRepayModel.getRepayDate().before(new Date())) {
            loanRepayModel.setStatus(RepayStatus.OVERDUE);
            long loanRepayDefaultInterest = new BigDecimal(loanModel.getLoanAmount()).multiply(new BigDecimal(overdueFee))
                    .multiply(new BigDecimal(DateUtil.differenceDay(loanRepayModel.getRepayDate(), new Date()) + 1L)).setScale(0, BigDecimal.ROUND_DOWN).longValue();
            loanRepayModel.setDefaultInterest(loanRepayDefaultInterest);
            loanRepayMapper.update(loanRepayModel);
            loanModel.setStatus(LoanStatus.OVERDUE);
            loanMapper.update(loanModel);
        }
    }

    @Override
    public void loanRepayNotify() {

        List<LoanRepayNotifyModel> loanRepayNotifyModelList = loanRepayMapper.findLoanRepayNotifyToday();

        for (LoanRepayNotifyModel model : loanRepayNotifyModelList) {

            logger.info("sent loan repay notify sms message to " + model.getMobile() + ", loan name:" + model.getLoanName().trim());

            LoanRepayNotifyDto dto = new LoanRepayNotifyDto();
            dto.setMobile(model.getMobile().trim());
            dto.setLoanName(model.getLoanName().trim());
            dto.setRepayAmount(AmountConverter.convertCentToString(model.getRepayAmount()));
            smsWrapperClient.sendLoanRepayNotify(dto);

            for (String mobile : repayRemindMobileList) {
                logger.info("sent loan repay notify sms message to " + mobile + ", loan name:" + model.getLoanName().trim());
                LoanRepayNotifyDto notifyDto = new LoanRepayNotifyDto();
                notifyDto.setMobile(mobile.trim());
                notifyDto.setLoanName(model.getLoanName().trim());
                notifyDto.setRepayAmount(AmountConverter.convertCentToString(model.getRepayAmount()));
                smsWrapperClient.sendLoanRepayNotify(notifyDto);
            }
        }
    }

    @Override
    public void loanBirthdayNotify() {

        List<String> userMobileList = userMapper.findUsersBirthdayMobile();
        for (String mobile : userMobileList) {

            logger.info("sent user birthday notify sms message to " + mobile );

            SmsCouponNotifyDto notifyDto = new SmsCouponNotifyDto();
            smsWrapperClient.sendCouponNotify(notifyDto);
        }
    }
}
