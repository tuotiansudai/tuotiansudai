package com.tuotiansudai.paywrapper.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.exception.AmountTransferException;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.dto.LoanRepayJobResultDto;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.TransferMapper;
import com.tuotiansudai.paywrapper.repository.model.sync.request.TransferRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.TransferResponseModel;
import com.tuotiansudai.paywrapper.service.CouponRepayService;
import com.tuotiansudai.paywrapper.service.SystemBillService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.LoanRepayMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.AmountTransfer;
import com.tuotiansudai.util.InterestCalculator;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

@Service
public class CouponRepayServiceImpl implements CouponRepayService {

    static Logger logger = Logger.getLogger(CouponRepayServiceImpl.class);

    protected final static String COUPON_REPAY_JOB_DATA_KEY_TEMPLATE = "pay:coupon_repay:{0}";

    @Autowired
    private AccountMapper accountMapper;

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
    protected RedisWrapperClient redisWrapperClient;

    @Autowired
    private SystemBillService systemBillService;

    protected ObjectMapper objectMapper = new ObjectMapper();

    public void repay(long loanRepayId) {
        String value = redisWrapperClient.get(MessageFormat.format(COUPON_REPAY_JOB_DATA_KEY_TEMPLATE, String.valueOf(loanRepayId)));
        try {
            jobData = objectMapper.readValue(value, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }


        LoanRepayModel currentLoanRepayModel = loanRepayMapper.findById(loanRepayId);
        LoanModel loanModel = loanMapper.findById(currentLoanRepayModel.getLoanId());
        List<LoanRepayModel> loanRepayModels = this.loanRepayMapper.findByLoanIdOrderByPeriodAsc(currentLoanRepayModel.getLoanId());
        List<UserCouponModel> userCouponModels = userCouponMapper.findByLoanId(loanModel.getId());

        for (UserCouponModel userCouponModel : userCouponModels) {
            CouponModel couponModel = this.couponMapper.findById(userCouponModel.getCouponId());
            long actualInterest = this.calculateActualInterest(couponModel, userCouponModel, loanModel, currentLoanRepayModel, loanRepayModels);
            long actualFee = (long) (actualInterest * loanModel.getInvestFeeRate());

            try {
                amountTransfer.transferInBalance(userCouponModel.getLoginName(),
                        userCouponModel.getId(),
                        actualInterest,
                        UserBillBusinessType.NEWBIE_COUPON, null, null);
                amountTransfer.transferOutBalance(userCouponModel.getLoginName(),
                        userCouponModel.getId(),
                        actualFee,
                        UserBillBusinessType.INVEST_FEE, null, null);
            } catch (AmountTransferException e) {
                logger.error(MessageFormat.format("coupon transfer in balance failed (userCouponId = {0})", String.valueOf(userCouponModel.getId())), e);
            }

            if (actualInterest > 0) {
                TransferRequestModel requestModel = TransferRequestModel.newCouponRequest(String.valueOf(userCouponModel.getId()),
                        accountMapper.findByLoginName(userCouponModel.getLoginName()).getPayUserId(),
                        String.valueOf(actualInterest - actualFee));
                try {
                    TransferResponseModel responseModel = paySyncClient.send(TransferMapper.class, requestModel, TransferResponseModel.class);
                } catch (PayException e) {


                }
            }


        }
    }

    private long calculateActualInterest(CouponModel couponModel,
                                         UserCouponModel userCouponModel,
                                         LoanModel loanModel,
                                         LoanRepayModel currentLoanRepayModel,
                                         List<LoanRepayModel> loanRepayModels) {

        LoanRepayModel lastLoanRepayModel = null;
        for (LoanRepayModel loanRepayModel : loanRepayModels) {
            if (loanRepayModel.getPeriod() < currentLoanRepayModel.getPeriod() && loanRepayModel.getStatus() == RepayStatus.COMPLETE) {
                lastLoanRepayModel = loanRepayModel;
            }
        }

        DateTime lastRepayDate = new DateTime(loanModel.getType().getInterestInitiateType() == InterestInitiateType.INTEREST_START_AT_INVEST ? userCouponModel.getUsedTime() : loanModel.getRecheckTime()).minusDays(1);
        if (lastLoanRepayModel != null) {
            lastRepayDate = new DateTime(lastLoanRepayModel.getActualRepayDate());
        }

        int periodDuration = Days.daysBetween(lastRepayDate.withTimeAtStartOfDay(), new DateTime().withTimeAtStartOfDay()).getDays();
        long corpusMultiplyPeriodDays = couponModel.getAmount() * periodDuration;

        return InterestCalculator.calculateInterest(loanModel, corpusMultiplyPeriodDays);
    }
}
