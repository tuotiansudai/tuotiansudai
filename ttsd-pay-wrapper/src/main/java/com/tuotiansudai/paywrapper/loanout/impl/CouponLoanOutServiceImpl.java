package com.tuotiansudai.paywrapper.loanout.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.enums.*;
import com.tuotiansudai.exception.AmountTransferException;
import com.tuotiansudai.message.AmountTransferMessage;
import com.tuotiansudai.message.SystemBillMessage;
import com.tuotiansudai.message.TransferRedEnvelopCallbackMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.loanout.CouponLoanOutService;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectTransferNotifyMapper;
import com.tuotiansudai.paywrapper.repository.mapper.RedEnvelopTransferMapper;
import com.tuotiansudai.paywrapper.repository.mapper.RedEnvelopTransferNotifyMapper;
import com.tuotiansudai.paywrapper.repository.mapper.TransferMapper;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.ProjectTransferNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.TransferNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.TransferRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.request.SyncRequestStatus;
import com.tuotiansudai.paywrapper.repository.model.sync.response.TransferResponseModel;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.CouponMapper;
import com.tuotiansudai.repository.mapper.UserCouponMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.CouponModel;
import com.tuotiansudai.repository.model.UserCouponModel;
import com.tuotiansudai.util.RedisWrapperClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class CouponLoanOutServiceImpl implements CouponLoanOutService {

    static Logger logger = Logger.getLogger(CouponLoanOutServiceImpl.class);

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    private static final String COUPON_ORDER_ID_TEMPLATE = "{0}X{1}";

    private final static String SEND_RED_ENVELOP = "SEND_RED_ENVELOP";

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private PaySyncClient paySyncClient;

    @Autowired
    private PayAsyncClient payAsyncClient;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Override
    public boolean sendRedEnvelope(long loanId) {
        boolean result = true;
        String redisKey = MessageFormat.format(LoanServiceImpl.LOAN_OUT_IDEMPOTENT_CHECK_TEMPLATE, String.valueOf(loanId));
        String beforeSendStatus = redisWrapperClient.hget(redisKey, SEND_RED_ENVELOP);

        if (!Strings.isNullOrEmpty(beforeSendStatus) && beforeSendStatus.equals(SyncRequestStatus.SUCCESS.name())) {
            return true;
        }

        List<UserCouponModel> userCouponModels = userCouponMapper.findByLoanId(loanId, Lists.newArrayList(CouponType.RED_ENVELOPE));
        for (UserCouponModel userCouponModel : userCouponModels) {
            // 实际收益为0，表示这个红包还没有发给用户，现在可以发送（幂等操作）
            if (userCouponModel.getActualInterest() == 0) {
                CouponModel couponModel = this.couponMapper.findById(userCouponModel.getCouponId());
                long transferAmount = couponModel.getAmount();
                if (transferAmount > 0) {
                    AccountModel accountModel = accountMapper.findByLoginName(userCouponModel.getLoginName());
                    TransferRequestModel requestModel = TransferRequestModel.newRedEnvelopeCouponRequest(MessageFormat.format(COUPON_ORDER_ID_TEMPLATE,
                            String.valueOf(userCouponModel.getId()), String.valueOf(new Date().getTime())),
                            accountModel.getPayUserId(),
                            accountModel.getPayAccountId(),
                            String.valueOf(transferAmount));
                    try {
                        TransferResponseModel responseModel = paySyncClient.send(RedEnvelopTransferMapper.class, requestModel, TransferResponseModel.class);
                        result = result && responseModel.isSuccess();
                    } catch (PayException e) {
                        result = false;
                        logger.error(MessageFormat.format("red envelope coupon transfer in balance failed (userCouponId = {0})", String.valueOf(userCouponModel.getId())), e);
                    }
                }
            }
        }

        redisWrapperClient.hset(redisKey, SEND_RED_ENVELOP, result ? SyncRequestStatus.SUCCESS.name() : SyncRequestStatus.FAILURE.name());
        return result;
    }

    @Override
    public String transferRedEnvelopNotify(Map<String, String> paramsMap, String queryString) {
        logger.info("[标的放款] transfer red envelop call back begin.");
        BaseCallbackRequestModel callbackRequest = this.payAsyncClient.parseCallbackRequest(
                paramsMap,
                queryString,
                RedEnvelopTransferNotifyMapper.class,
                TransferNotifyRequestModel.class);

        if (callbackRequest == null || Strings.isNullOrEmpty(callbackRequest.getOrderId()) || !callbackRequest.getOrderId().contains("X")) {
            logger.error(MessageFormat.format("[标的放款:投资红包] transfer red envelop payback callback parse is failed (queryString = {0})", queryString));
            return null;
        }

        if (!callbackRequest.isSuccess()) {
            return callbackRequest.getResponseData();
        }

        long userCouponId = Long.parseLong(callbackRequest.getOrderId().substring(0, callbackRequest.getOrderId().indexOf("X")));
        UserCouponModel userCouponModel = userCouponMapper.findById(userCouponId);
        if (userCouponModel == null) {
            logger.error(MessageFormat.format("[标的放款:投资红包] TransferRedEnvelopCallback payback callback failed, order id({0}) is not exist", callbackRequest.getOrderId()));
            return callbackRequest.getResponseData();
        }

        TransferRedEnvelopCallbackMessage transferRedEnvelopCallbackMessage = new TransferRedEnvelopCallbackMessage(userCouponModel.getLoanId(),
                userCouponModel.getInvestId(),
                userCouponModel.getLoginName(),
                userCouponId);

        logger.info(MessageFormat.format("[标的放款:投资红包] send message TransferRedEnvelopCallback,loanId:{0}, investId:{1}, loginName:{2} queryString:{3}, orderId:{4}",
                String.valueOf(userCouponModel.getLoanId()), String.valueOf(userCouponModel.getInvestId()), userCouponModel.getLoanName(), queryString, callbackRequest.getOrderId()));
        mqWrapperClient.sendMessage(MessageQueue.TransferRedEnvelopCallback, transferRedEnvelopCallbackMessage);

        return callbackRequest.getResponseData();
    }

    @Override
    public boolean sendRedEnvelopTransferInBalanceCallBack(long userCouponId) {
        logger.info(MessageFormat.format("[标的放款:投资红包] send redEnvelop transfer in balance callBack, userCouponId:{0}", String.valueOf(userCouponId)));
        UserCouponModel userCouponModel = userCouponMapper.findById(userCouponId);
        if (userCouponModel.getActualInterest() != 0) {
            return true;
        }
        CouponModel couponModel = this.couponMapper.findById(userCouponModel.getCouponId());
        long transferAmount = couponModel.getAmount();
        try {
            String detail = MessageFormat.format(SystemBillDetailTemplate.COUPON_RED_ENVELOPE_DETAIL_TEMPLATE.getTemplate(),
                    couponModel.getCouponType().getName(),
                    String.valueOf(userCouponModel.getId()),
                    String.valueOf(userCouponModel.getLoanId()),
                    String.valueOf(transferAmount));

            userCouponModel.setActualInterest(transferAmount);
            userCouponMapper.update(userCouponModel);

            SystemBillMessage sbm = new SystemBillMessage(SystemBillMessageType.TRANSFER_OUT, userCouponModel.getId(), transferAmount, SystemBillBusinessType.COUPON_RED_ENVELOPE, detail);
            AmountTransferMessage atm = new AmountTransferMessage(TransferType.TRANSFER_IN_BALANCE, userCouponModel.getLoginName(),
                    userCouponModel.getId(), transferAmount, couponModel.getCouponType().getUserBillBusinessType(), null, null);

            mqWrapperClient.sendMessage(MessageQueue.SystemBill, sbm);
            mqWrapperClient.sendMessage(MessageQueue.AmountTransfer, atm);

            return true;
        } catch (Exception e) {
            logger.error(MessageFormat.format("[标的放款:投资红包] red envelope coupon transfer in balance failed (userCouponId = {0})", String.valueOf(userCouponModel.getId())), e);
        }

        return false;
    }
}