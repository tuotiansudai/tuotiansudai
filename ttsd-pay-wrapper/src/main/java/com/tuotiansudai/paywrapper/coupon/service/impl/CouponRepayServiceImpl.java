package com.tuotiansudai.paywrapper.coupon.service.impl;

import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.TransferMapper;
import com.tuotiansudai.paywrapper.repository.model.sync.request.TransferRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.TransferResponseModel;
import com.tuotiansudai.paywrapper.coupon.service.CouponRepayService;
import com.tuotiansudai.paywrapper.service.SystemBillService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.LoanRepayMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.AmountTransfer;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Service
public class CouponRepayServiceImpl implements CouponRepayService {

    static Logger logger = Logger.getLogger(CouponRepayServiceImpl.class);

    private static final String COUPON_ORDER_ID_TEMPLATE = "{0}X{1}";

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private LoanRepayMapper loanRepayMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private PaySyncClient paySyncClient;

    @Autowired
    private AmountTransfer amountTransfer;

    @Autowired
    private SystemBillService systemBillService;

    @Override
    @Transactional
    public void repay(long loanRepayId) {
        LoanRepayModel currentLoanRepayModel = loanRepayMapper.findById(loanRepayId);
        LoanModel loanModel = loanMapper.findById(currentLoanRepayModel.getLoanId());
        List<LoanRepayModel> loanRepayModels = this.loanRepayMapper.findByLoanIdOrderByPeriodAsc(currentLoanRepayModel.getLoanId());
        List<UserCouponModel> userCouponModels = userCouponMapper.findByLoanId(loanModel.getId());

        for (UserCouponModel userCouponModel : userCouponModels) {
            CouponModel couponModel = this.couponMapper.findById(userCouponModel.getCouponId());
            long actualInterest = this.calculateActualInterest(couponModel, userCouponModel, loanModel, currentLoanRepayModel, loanRepayModels);
            long actualFee = (long) (actualInterest * loanModel.getInvestFeeRate());
            long transferAmount = actualInterest - actualFee;
            boolean isSuccess = transferAmount == 0;
            if (transferAmount > 0) {
                TransferRequestModel requestModel = TransferRequestModel.newCouponRequest(MessageFormat.format(COUPON_ORDER_ID_TEMPLATE, String.valueOf(userCouponModel.getId()), String.valueOf(new Date().getTime())) ,
                        accountMapper.findByLoginName(userCouponModel.getLoginName()).getPayUserId(),
                        String.valueOf(transferAmount));
                try {
                    TransferResponseModel responseModel = paySyncClient.send(TransferMapper.class, requestModel, TransferResponseModel.class);
                    isSuccess = responseModel.isSuccess();
                } catch (PayException e) {
                    logger.error(MessageFormat.format("coupon transfer in balance failed (userCouponId = {0})", String.valueOf(userCouponModel.getId())), e);
                }
            }

            if (isSuccess) {
                try {
                    userCouponModel.setActualInterest(userCouponModel.getActualInterest() + actualInterest);
                    userCouponModel.setActualFee(userCouponModel.getActualFee() + actualFee);
                    userCouponMapper.update(userCouponModel);

                    String detail = MessageFormat.format(SystemBillDetailTemplate.COUPON_INTEREST_DETAIL_TEMPLATE.getTemplate(),
                            
                            String.valueOf(userCouponModel.getId()),
                            String.valueOf(currentLoanRepayModel.getId()),
                            String.valueOf(transferAmount));
                    systemBillService.transferOut(userCouponModel.getId(), transferAmount, SystemBillBusinessType.COUPON, detail);

                    amountTransfer.transferInBalance(userCouponModel.getLoginName(),
                            userCouponModel.getId(),
                            actualInterest,
                            UserBillBusinessType.NEWBIE_COUPON, null, null);

                    amountTransfer.transferOutBalance(userCouponModel.getLoginName(),
                            userCouponModel.getId(),
                            actualFee,
                            UserBillBusinessType.INVEST_FEE, null, null);
                } catch (Exception e) {
                    logger.error(MessageFormat.format("coupon transfer in balance failed (userCouponId = {0})", String.valueOf(userCouponModel.getId())), e);
                }
            }
        }
    }

    private long calculateActualInterest(CouponModel couponModel, UserCouponModel userCouponModel, LoanModel loanModel, LoanRepayModel currentLoanRepayModel, List<LoanRepayModel> loanRepayModels) {

        DateTime currentRepayDate = new DateTime(currentLoanRepayModel.getActualRepayDate());

        LoanRepayModel lastLoanRepayModel = null;
        for (LoanRepayModel loanRepayModel : loanRepayModels) {
            if (loanRepayModel.getPeriod() < currentLoanRepayModel.getPeriod() && loanRepayModel.getStatus() == RepayStatus.COMPLETE && loanRepayModel.getActualRepayDate().before(currentLoanRepayModel.getActualRepayDate())) {
                lastLoanRepayModel = loanRepayModel;
            }
        }

        DateTime lastRepayDate = new DateTime(loanModel.getType().getInterestInitiateType() == InterestInitiateType.INTEREST_START_AT_INVEST ? userCouponModel.getUsedTime() : loanModel.getRecheckTime()).minusDays(1);
        if (lastLoanRepayModel != null) {
            lastRepayDate = new DateTime(lastLoanRepayModel.getActualRepayDate());
        }

        int daysOfYear = new DateTime(loanModel.getRecheckTime()).withTimeAtStartOfDay().dayOfYear().getMaximumValue();
        int periodDuration = Days.daysBetween(lastRepayDate.withTimeAtStartOfDay(), currentRepayDate.withTimeAtStartOfDay()).getDays();

        long expectedInterest = 0;
        switch (couponModel.getCouponType()) {
            case NEWBIE_COUPON:
            case INVEST_COUPON:
                expectedInterest = new BigDecimal(periodDuration * couponModel.getAmount())
                        .multiply(new BigDecimal(loanModel.getBaseRate()).add(new BigDecimal(loanModel.getActivityRate())))
                        .divide(new BigDecimal(daysOfYear), 0, BigDecimal.ROUND_DOWN).longValue();
                break;
            case INTEREST_COUPON:
                expectedInterest = new BigDecimal(periodDuration * investMapper.findById(userCouponModel.getInvestId()).getAmount())
                        .multiply(new BigDecimal(couponModel.getRate()))
                        .divide(new BigDecimal(daysOfYear), 0, BigDecimal.ROUND_DOWN).longValue();
                break;
        }

        return expectedInterest;
    }
}