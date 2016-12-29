package com.tuotiansudai.mq.consumer.loan;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.mapper.CouponRepayMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.CouponRepayModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.coupon.service.CouponAssignmentService;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.CouponRepayDto;
import com.tuotiansudai.dto.Environment;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.sms.SmsFatalNotifyDto;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.LoanRepayMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.InterestCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RepayPaybackInvestMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(RepayPaybackInvestMessageConsumer.class);

    public static final List<CouponType> COUPON_TYPE_LIST = Lists.newArrayList(CouponType.NEWBIE_COUPON,
            CouponType.INVEST_COUPON,
            CouponType.INTEREST_COUPON,
            CouponType.BIRTHDAY_COUPON);

    @Autowired
    private MQWrapperClient mqClient;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private LoanRepayMapper loanRepayMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private CouponRepayMapper couponRepayMapper;

    @Autowired
    private SmsWrapperClient smsWrapperClient;

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public MessageQueue queue() {
        return MessageQueue.CouponAssigning;
    }

    @Value("${common.environment}")
    private Environment environment;

    @Value(value = "${pay.coupon.repay.notify.process.batch.size}")
    private int couponRepayProcessListSize;

    private final static String REPAY_REDIS_KEY_TEMPLATE = "COUPON_REPAY:{0}";

    private static final String COUPON_ORDER_ID_TEMPLATE = "{0}X{1}";


    private void couponRepay (long loanRepayId, boolean isAdvanced){
        logger.info(MessageFormat.format("[MQ] [Coupon Repay {0}] coupon repay is starting...", String.valueOf(loanRepayId)));
        LoanRepayModel currentLoanRepayModel = this.loanRepayMapper.findById(loanRepayId);
        LoanModel loanModel = loanMapper.findById(currentLoanRepayModel.getLoanId());
        List<LoanRepayModel> loanRepayModels = this.loanRepayMapper.findByLoanIdOrderByPeriodAsc(currentLoanRepayModel.getLoanId());
        List<UserCouponModel> userCouponModels = userCouponMapper.findByLoanId(loanModel.getId(), COUPON_TYPE_LIST);

        for (UserCouponModel userCouponModel : userCouponModels) {
            logger.info(MessageFormat.format("[MQ] [Coupon Repay {0}] user coupon({1}) repay is starting...", String.valueOf(loanRepayId), String.valueOf(userCouponModel.getId())));

            CouponModel couponModel = this.couponMapper.findById(userCouponModel.getCouponId());
            if (couponModel.getCouponType() == CouponType.BIRTHDAY_COUPON && currentLoanRepayModel.getPeriod() > 1) {
                continue;
            }

            InvestModel investModel = investMapper.findById(userCouponModel.getInvestId());
            if (investModel == null || investModel.getStatus() != InvestStatus.SUCCESS || investModel.getTransferStatus() == TransferStatus.SUCCESS) {
                logger.info(MessageFormat.format("[MQ] [Coupon Repay {0}] invest({1}) is nonexistent or not success or has transferred",
                        String.valueOf(loanRepayId),
                        investModel == null ? "null" : String.valueOf(investModel.getId())));
                continue;
            }
            CouponRepayModel couponRepayModel = couponRepayMapper.findByUserCouponIdAndPeriod(userCouponModel.getId(), currentLoanRepayModel.getPeriod());

            if (couponRepayModel == null) {
                logger.error(MessageFormat.format("[MQ] Coupon Repay loanRepayId:{0},userCouponId:{1},period:{2} is nonexistent",
                        currentLoanRepayModel.getLoanId(),
                        userCouponModel.getId(),
                        currentLoanRepayModel.getPeriod()));
                continue;
            }

            long investAmount = investModel.getAmount();
            long actualInterest = InterestCalculator.calculateCouponActualInterest(investAmount, couponModel, userCouponModel, loanModel, currentLoanRepayModel, loanRepayModels);
            if (actualInterest < 0) {
                continue;
            }
            long actualFee = (long) (actualInterest * investModel.getInvestFeeRate());
            long transferAmount = actualInterest - actualFee;
            logger.info(MessageFormat.format("[MQ] [Coupon Repay {0}] user coupon({1}) is {2}, repay amount is  {3}({4} - {5})",
                    String.valueOf(currentLoanRepayModel.getId()),
                    String.valueOf(userCouponModel.getId()),
                    couponModel.getCouponType().name(),
                    String.valueOf(transferAmount),
                    String.valueOf(actualInterest),
                    String.valueOf(actualFee)));

            userCouponModel.setActualInterest(userCouponModel.getActualInterest() + actualInterest);
            userCouponModel.setActualFee(userCouponModel.getActualFee() + actualFee);
            userCouponMapper.update(userCouponModel);

            this.updateCouponRepayBeforeCallback(actualInterest, actualFee, investModel.getId(), couponRepayModel, isAdvanced);

            if (transferAmount > 0) {
                    CouponRepayDto couponRepayDto = new CouponRepayDto(loanRepayId,
                            accountMapper.findByLoginName(userCouponModel.getLoginName()).getPayUserId(),
                            couponRepayModel.getId(),
                            userCouponModel.getId(),
                            transferAmount);
                BaseDto<PayDataDto> payDataDto =  this.payWrapperClient.couponRepayAfterRepaySuccess(couponRepayDto);
                if(payDataDto.isSuccess()){
                    logger.info(MessageFormat.format("[MQ] [payWrapperClient Coupon Repay {0}] coupon repay is success", String.valueOf(loanRepayId)));
                }

            } else {
                this.updateCouponRepayAfterCallback(investModel.getId(), couponRepayModel, isAdvanced);
            }
        }
        logger.info(MessageFormat.format("[MQ] [Coupon Repay {0}] coupon repay is success", String.valueOf(loanRepayId)));
    }


    private void updateCouponRepayBeforeCallback(long actualInterest, long actualFee, long investId, final CouponRepayModel couponRepayModel, boolean isAdvanced) {
        try {
            couponRepayModel.setActualInterest(actualInterest);
            couponRepayModel.setActualFee(actualFee);
            couponRepayModel.setRepayAmount(actualInterest - actualFee);
            couponRepayModel.setActualRepayDate(new Date());
            couponRepayMapper.update(couponRepayModel);
            if (isAdvanced) {
                List<CouponRepayModel> advancedCouponRepayModels = Lists.newArrayList(couponRepayMapper.findByUserCouponByInvestId(investId).stream().filter(input -> input.getPeriod() > couponRepayModel.getPeriod()).collect(Collectors.toList()));
                for (CouponRepayModel advancedCouponRepayModel : advancedCouponRepayModels) {
                    if (advancedCouponRepayModel.getStatus() == RepayStatus.REPAYING) {
                        advancedCouponRepayModel.setActualRepayDate(new Date());
                        couponRepayMapper.update(advancedCouponRepayModel);
                        logger.info(MessageFormat.format("[MQ] [Advance Repay] update other ActualRepayDate coupon repay({0})",
                                String.valueOf(advancedCouponRepayModel.getId())));
                    }
                }
            }
        } catch (Exception e) {
            fatalLog("[MQ] updateCouponRepayBeforeCallback Exception. currentCouponRepayModelId:" + couponRepayModel.getId(), e);
        }

    }

    private void updateCouponRepayAfterCallback(long investId, final CouponRepayModel couponRepayModel, boolean isAdvanced) {
        try {
            couponRepayModel.setStatus(RepayStatus.COMPLETE);
            couponRepayMapper.update(couponRepayModel);
            if (isAdvanced) {
                List<CouponRepayModel> advancedCouponRepayModels = Lists.newArrayList(couponRepayMapper.findByUserCouponByInvestId(investId).stream().filter(input -> input.getPeriod() > couponRepayModel.getPeriod()).collect(Collectors.toList()));
                for (CouponRepayModel advancedCouponRepayModel : advancedCouponRepayModels) {
                    if (advancedCouponRepayModel.getStatus() == RepayStatus.REPAYING) {
                        advancedCouponRepayModel.setStatus(RepayStatus.COMPLETE);
                        couponRepayMapper.update(advancedCouponRepayModel);
                        logger.info(MessageFormat.format("[MQ] [Advance Repay] update other REPAYING coupon repay({0}) status to COMPLETE",
                                String.valueOf(advancedCouponRepayModel.getId())));
                    }
                }
            }
        } catch (Exception e) {
            fatalLog("[MQ] updateCouponRepayAfterCallback Exception. currentCouponRepayModelId:" + couponRepayModel.getId(), e);
        }
    }

    @Override
    @Transactional
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);
        if (!StringUtils.isEmpty(message)) {
            String[] msgParts = message.split(":");
            if (msgParts.length == 2) {
                logger.info("[MQ] ready to consume message: repay payback invest.");
                if(String.valueOf(msgParts[1]).equals("0")){
                    //正常还款之优惠券还款
                    this.couponRepay(Long.parseLong(msgParts[0]), false);
                    //正常还款值阶梯加息

                }else{
                    //提前还款之优惠券还款
                    this.couponRepay(Long.parseLong(msgParts[0]), true);
                    //提前还款之阶梯加息

                }
                logger.info("[MQ] consume message success.");

            }
        }
    }

    private void fatalLog(String errMsg, Throwable e) {
        sendSmsErrNotify(MessageFormat.format("{0},{1}", environment, errMsg));
    }

    private void sendSmsErrNotify(String errMsg) {
        logger.info("[MQ] sent coupon repay fatal sms message");
        SmsFatalNotifyDto dto = new SmsFatalNotifyDto(MessageFormat.format("[MQ] 还款时优惠券发放业务错误。详细信息：{0}", errMsg));
        smsWrapperClient.sendFatalNotify(dto);
    }


}
