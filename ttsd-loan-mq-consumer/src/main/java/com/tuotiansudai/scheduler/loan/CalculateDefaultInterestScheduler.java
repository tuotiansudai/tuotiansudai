package com.tuotiansudai.scheduler.loan;

import com.google.common.collect.Lists;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.dto.Environment;
import com.tuotiansudai.message.EMailMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.DateUtil;
import com.tuotiansudai.util.InterestCalculator;
import com.tuotiansudai.util.MapBuilder;
import com.tuotiansudai.util.SendCloudTemplate;
import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Component
public class CalculateDefaultInterestScheduler {
    private static Logger logger = LoggerFactory.getLogger(CalculateDefaultInterestScheduler.class);

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
    private InvestExtraRateMapper investExtraRateMapper;

    @Autowired
    private CouponRepayMapper couponRepayMapper;

    @Autowired
    private TransferApplicationMapper transferApplicationMapper;

    @Value("${common.environment}")
    private Environment environment;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Value("#{'${check.user.balance.notify.email}'.split('\\|')}")
    private List<String> notifyEmailAddressList;

    //@Scheduled(cron = "0 0 1 * * ?", zone = "Asia/Shanghai")
    @Scheduled(cron = "0 0/15 * * * ?", zone = "Asia/Shanghai")
    public void calculateDefaultInterest() {
        String startTime = new DateTime().toString("yyyy-MM-dd HH:mm:ss");
        int defaultUpdateCount = 0;
        int overdueUpdateCount = 0;
        List<LoanRepayModel> loanRepayModels = loanRepayMapper.findNotCompleteLoanRepay();
        for (LoanRepayModel loanRepayModel : loanRepayModels) {
            try {
                defaultUpdateCount += calculateDefaultInterestEveryLoan(loanRepayModel);
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage(), e);
            }
        }
        //最后一期逾期额外计算利息
        List<LoanRepayModel> overdueLoanRepays = loanRepayMapper.findOverdueLoanRepay();
        for (LoanRepayModel loanRepayModel : overdueLoanRepays) {
            try {
                overdueUpdateCount += calculateOverdueInterestEveryLoan(loanRepayModel);
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage(), e);
            }
        }
        String endTime = new DateTime().toString("yyyy-MM-dd HH:mm:ss");
        //发送邮件通知数据更新
        mqWrapperClient.sendMessage(MessageQueue.EMailMessage, new EMailMessage(Lists.newArrayList(notifyEmailAddressList),
                SendCloudTemplate.EVERY_DAY_CHECK_DEFAULT_INTEREST.getTitle(),
                SendCloudTemplate.EVERY_DAY_CHECK_DEFAULT_INTEREST.generateContent(new MapBuilder<String, String>()
                        .put("startTime", startTime)
                        .put("endTime", endTime)
                        .put("env", environment.name())
                        .put("defaultQueryCount", String.valueOf(loanRepayModels.size()))
                        .put("defaultUpdateCount", String.valueOf(defaultUpdateCount))
                        .put("overdueQueryCount", String.valueOf(overdueLoanRepays.size()))
                        .put("overdueUpdateCount", String.valueOf(overdueUpdateCount)).build())));


    }

    //计算最后一期逾期利息
    public int calculateOverdueInterestEveryLoan(LoanRepayModel loanRepayModel) {
        LoanModel loanModel = loanMapper.findById(loanRepayModel.getLoanId());
        if (loanModel.getPeriods() != loanRepayModel.getPeriod()) {
            return 0;
        }
        List<InvestRepayModel> investRepayModels = investRepayMapper.findInvestRepayByLoanIdAndPeriod(loanModel.getId(), loanRepayModel.getPeriod());
        for (InvestRepayModel investRepayModel : investRepayModels) {
            long overdueInterest = InterestCalculator.calculateLoanInterest(loanModel.getBaseRate(), investMapper.findById(investRepayModel.getInvestId()).getAmount(), new DateTime(investRepayModel.getRepayDate()), new DateTime());
            investRepayModel.setOverdueInterest(overdueInterest);
            //计算逾期利息 和逾期利息手续费
            InvestModel investModel = investMapper.findById(investRepayModel.getInvestId());
            //如果是转让项目，需要从转让日开始计算
            if (investModel.getTransferInvestId() != null && investMapper.findById(investModel.getTransferInvestId()).isOverdueTransfer()) {
                TransferApplicationModel transferApplicationModel = transferApplicationMapper.findByInvestId(investModel.getId());
                overdueInterest = InterestCalculator.calculateLoanInterest(loanModel.getBaseRate(), investModel.getAmount(), new DateTime(transferApplicationModel.getApplicationTime()), new DateTime());
            }
            long overdueFeeValue = new BigDecimal(overdueInterest).multiply(new BigDecimal(investModel.getInvestFeeRate())).setScale(0, BigDecimal.ROUND_DOWN).longValue();
            investRepayModel.setOverdueFee(overdueFeeValue);
            logger.info(MessageFormat.format("[calculate overdue interest]investRepayModelId:{0},overdueInterest:{1},overdueFeeValue:{2},amount:{3},rate:{4}", investRepayModel.getId(), overdueInterest, overdueFeeValue, investModel.getAmount(), loanModel.getBaseRate()));
            investRepayMapper.updateOverdueAndFee(investRepayModel.getId(), investRepayModel.getOverdueInterest(), investRepayModel.getOverdueFee());
        }
        long repayOverdueInterest = InterestCalculator.calculateLoanInterest(loanModel.getBaseRate(), loanModel.getLoanAmount(), new DateTime(loanRepayModel.getRepayDate()), new DateTime());
        loanRepayModel.setOverdueInterest(repayOverdueInterest);
        logger.info(MessageFormat.format("[calculate overdue interest]loanRepayModelId:{0},overdueInterest:{1},overdueFeeValue:{2},amount:{3},rate:{4}", loanRepayModel.getId(), repayOverdueInterest, 0, loanModel.getLoanAmount(), loanModel.getBaseRate()));
        loanRepayMapper.updateOverdueInterest(loanRepayModel.getId(), loanRepayModel.getOverdueInterest());
        return 1;
    }

    /**
     * 计算罚息
     *
     * @param loanRepayModel
     * @throws Exception
     */
    private int calculateDefaultInterestEveryLoan(LoanRepayModel loanRepayModel) throws Exception {
        int updateResult = 0;
        LoanModel loanModel = loanMapper.findById(loanRepayModel.getLoanId());
        List<InvestRepayModel> investRepayModels = investRepayMapper.findInvestRepayByLoanIdAndPeriod(loanModel.getId(), loanRepayModel.getPeriod());
        for (InvestRepayModel investRepayModel : investRepayModels) {
            if (investRepayModel.getRepayDate().before(new Date())) {
                investRepayModel.setStatus(RepayStatus.OVERDUE);
                if (isNeedCalculateDefaultInterestInvestRepay(investRepayModel)) {
                    InvestModel investModel = investMapper.findById(investRepayModel.getInvestId());
                    long investRepayDefaultInterest = new BigDecimal(investModel.getAmount()).multiply(new BigDecimal(overdueFee))
                            .multiply(new BigDecimal(DateUtil.differenceDay(investRepayModel.getRepayDate(), new Date()) + 1L))
                            .setScale(0, BigDecimal.ROUND_DOWN).longValue();
                    investRepayModel.setDefaultInterest(investRepayDefaultInterest);
                    //如果是逾期债券转让,罚息需要从转让日计算
                    if (investModel.getTransferInvestId() != null && investMapper.findById(investModel.getTransferInvestId()).isOverdueTransfer()) {
                        TransferApplicationModel transferApplicationModel = transferApplicationMapper.findByInvestId(investModel.getId());
                        investRepayDefaultInterest = InterestCalculator.calculateLoanInterestDateRate(overdueFee, investModel.getAmount(), new DateTime(transferApplicationModel.getApplicationTime()), new DateTime());
                    }
                    long investRepayDefaultFee = new BigDecimal(investRepayDefaultInterest).multiply(new BigDecimal(investModel.getInvestFeeRate())).setScale(0, BigDecimal.ROUND_DOWN).longValue();
                    investRepayModel.setDefaultFee(investRepayDefaultFee);
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
            if (isLastPeriod) {
                List<InvestExtraRateModel> investExtraRateModels = investExtraRateMapper.findByLoanId(loanRepayModel.getLoanId());
                investExtraRateModels.forEach(investExtraRateModel -> {
                    investExtraRateModel.setStatus(RepayStatus.OVERDUE);
                    investExtraRateMapper.update(investExtraRateModel);
                });
                logger.info("investExtraRate status to overdue");
            }
            updateResult = 1;
        }
        logger.info(MessageFormat.format("loanRepayId:{0} couponRepay status to overdue", loanRepayModel.getId()));
        List<CouponRepayModel> couponRepayModels = couponRepayMapper.findCouponRepayByLoanIdAndPeriod(loanModel.getId(), loanRepayModel.getPeriod());
        for (CouponRepayModel couponRepayModel : couponRepayModels) {
            if (couponRepayModel.getRepayDate().before(new Date())) {
                couponRepayModel.setStatus(RepayStatus.OVERDUE);
                couponRepayMapper.update(couponRepayModel);
            }
        }
        return updateResult;
    }

    private boolean isNeedCalculateDefaultInterestLoanRepay(LoanRepayModel loanRepayModel) {
        long loanId = loanRepayModel.getLoanId();
        int period = loanRepayModel.getPeriod();
        List<LoanRepayModel> loanRepayModels = loanRepayMapper.findByLoanIdAndLTPeriod(loanId, period);
        return CollectionUtils.isEmpty(loanRepayModels) || loanRepayModels.stream().noneMatch(input -> input.getStatus() == RepayStatus.OVERDUE);
    }

    private boolean isNeedCalculateDefaultInterestInvestRepay(InvestRepayModel investRepayModel) {
        long investId = investRepayModel.getInvestId();
        int period = investRepayModel.getPeriod();
        List<InvestRepayModel> investRepayModels = investRepayMapper.findByInvestIdAndLTPeriod(investId, period);
        return CollectionUtils.isEmpty(investRepayModels) || investRepayModels.stream().noneMatch(input -> input.getStatus() == RepayStatus.OVERDUE);
    }

    @PostConstruct
    public void initScheduler() {
        logger.info("=================计算罚息初始化=======================");
        calculateDefaultInterest();
        logger.info("=================计算罚息初jieshu=======================");
    }

}
