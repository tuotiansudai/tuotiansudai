package com.tuotiansudai.paywrapper.coupon.service.impl;

import com.google.common.collect.Lists;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.coupon.repository.model.UserGroup;
import com.tuotiansudai.coupon.service.CouponAssignmentService;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.coupon.service.CouponLoanOutService;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.TransferMapper;
import com.tuotiansudai.paywrapper.repository.model.sync.request.TransferRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.TransferResponseModel;
import com.tuotiansudai.paywrapper.service.SystemBillService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.SystemBillBusinessType;
import com.tuotiansudai.repository.model.SystemBillDetailTemplate;
import com.tuotiansudai.util.AmountTransfer;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CouponLoanOutServiceImpl implements CouponLoanOutService {

    static Logger logger = Logger.getLogger(CouponLoanOutServiceImpl.class);

    private static final String COUPON_ORDER_ID_TEMPLATE = "{0}X{1}";

    @Autowired
    private AccountMapper accountMapper;

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

    @Autowired
    private CouponAssignmentService couponAssignmentService;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Override
    public boolean sendRedEnvelope(long loanId) {
        boolean result = true;
        List<UserCouponModel> userCouponModels = userCouponMapper.findByLoanId(loanId, Lists.newArrayList(CouponType.RED_ENVELOPE));

        for (UserCouponModel userCouponModel : userCouponModels) {

            // 实际收益为0，表示这个红包还没有发给用户，现在可以发送（幂等操作）
            if (userCouponModel.getActualInterest() == 0) {
                CouponModel couponModel = this.couponMapper.findById(userCouponModel.getCouponId());
                long transferAmount = couponModel.getAmount();
                boolean isSuccess = transferAmount == 0;
                if (transferAmount > 0) {
                    TransferRequestModel requestModel = TransferRequestModel.newRequest(MessageFormat.format(COUPON_ORDER_ID_TEMPLATE, String.valueOf(userCouponModel.getId()), String.valueOf(new Date().getTime())),
                            accountMapper.findByLoginName(userCouponModel.getLoginName()).getPayUserId(),
                            String.valueOf(transferAmount));
                    try {
                        TransferResponseModel responseModel = paySyncClient.send(TransferMapper.class, requestModel, TransferResponseModel.class);
                        isSuccess = responseModel.isSuccess();
                    } catch (PayException e) {
                        isSuccess = false;
                        result =  isSuccess;
                        logger.error(MessageFormat.format("red envelope coupon transfer in balance failed (userCouponId = {0})", String.valueOf(userCouponModel.getId())), e);
                    }
                }

                if (isSuccess) {
                    try {
                        String detail = MessageFormat.format(SystemBillDetailTemplate.COUPON_RED_ENVELOPE_DETAIL_TEMPLATE.getTemplate(),
                                couponModel.getCouponType().getName(),
                                String.valueOf(userCouponModel.getId()),
                                String.valueOf(loanId),
                                String.valueOf(transferAmount));
                        systemBillService.transferOut(userCouponModel.getId(), transferAmount, SystemBillBusinessType.COUPON_RED_ENVELOPE, detail);

                        amountTransfer.transferInBalance(userCouponModel.getLoginName(),
                                userCouponModel.getId(),
                                transferAmount,
                                couponModel.getCouponType().getUserBillBusinessType(), null, null);

                        userCouponModel.setActualInterest(transferAmount);
                        userCouponMapper.update(userCouponModel);
                    } catch (Exception e) {
                        result =  false;
                        logger.error(MessageFormat.format("red envelope coupon transfer in balance failed (userCouponId = {0})", String.valueOf(userCouponModel.getId())), e);
                    }
                }
            }
        }

        return result;
    }

    @Override
    public boolean assignInvestAchievementUserCoupon(long loanId){
        LoanModel loanModel = loanMapper.findById(loanId);
        boolean result = true;
        if (!createUserCouponModel(loanModel.getFirstInvestAchievementId(), UserGroup.FIRST_INVEST_ACHIEVEMENT, loanId)) {
            logger.error(MessageFormat.format("[标的放款] assign FIRST_INVEST_ACHIEVEMENT is fail, loanId:{0}", String.valueOf(loanId)));
            result = false;
        }

        if (!createUserCouponModel(loanModel.getMaxAmountAchievementId(), UserGroup.MAX_AMOUNT_ACHIEVEMENT, loanId)) {
            logger.error(MessageFormat.format("[标的放款] assign MAX_AMOUNT_ACHIEVEMENT is fail, loanId:{0}", String.valueOf(loanId)));
            result = false;
        }

        if (!createUserCouponModel(loanModel.getLastInvestAchievementId(), UserGroup.LAST_INVEST_ACHIEVEMENT, loanId)) {
            logger.error(MessageFormat.format("[标的放款] assign LAST_INVEST_ACHIEVEMENT is fail, loanId:{0}", String.valueOf(loanId)));
            result = false;
        }

        return result;
    }

    private boolean createUserCouponModel(Long investId, final UserGroup userGroup, long loanId) {
        if (investId == null || investId == 0) {
            logger.error(MessageFormat.format("loan id : {0} nothing {1}", String.valueOf(loanId), userGroup.name()));
            return false;
        }

        boolean result = true;
        List<CouponModel> couponModelList = couponMapper.findAllActiveCoupons();

        List<CouponModel> collect = couponModelList.stream().filter(couponModel -> couponModel.getUserGroup().equals(userGroup)
                && DateTime.now().toDate().before(couponModel.getEndTime())
                && DateTime.now().toDate().after(couponModel.getStartTime())).collect(Collectors.toList());

        for (CouponModel couponModel : collect) {
            if (!couponAssignmentService.assignInvestAchievementUserCoupon(loanId, investMapper.findById(investId).getLoginName(), couponModel.getId())) {
                result = false;
            }
        }
        return result;
    }

}