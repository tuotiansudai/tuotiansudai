package com.tuotiansudai.paywrapper.loanout;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.message.TransferRedEnvelopCallbackMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.ProjectTransferNotifyMapper;
import com.tuotiansudai.paywrapper.repository.mapper.TransferMapper;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.ProjectTransferNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.request.TransferRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.TransferResponseModel;
import com.tuotiansudai.paywrapper.service.SystemBillService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.SystemBillBusinessType;
import com.tuotiansudai.repository.model.SystemBillDetailTemplate;
import com.tuotiansudai.util.AmountTransfer;
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
    private PayAsyncClient payAsyncClient;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Override
    public boolean sendRedEnvelope(long loanId) {
        boolean result = true;
        List<UserCouponModel> userCouponModels = userCouponMapper.findByLoanId(loanId, Lists.newArrayList(CouponType.RED_ENVELOPE));

        for (UserCouponModel userCouponModel : userCouponModels) {
            // 实际收益为0，表示这个红包还没有发给用户，现在可以发送（幂等操作）
            if (userCouponModel.getActualInterest() == 0) {
                CouponModel couponModel = this.couponMapper.findById(userCouponModel.getCouponId());
                long transferAmount = couponModel.getAmount();
                if (transferAmount > 0) {
                    TransferRequestModel requestModel = TransferRequestModel.newTransferCouponRequest(MessageFormat.format(COUPON_ORDER_ID_TEMPLATE, String.valueOf(userCouponModel.getId()), String.valueOf(new Date().getTime())),
                            accountMapper.findByLoginName(userCouponModel.getLoginName()).getPayUserId(),
                            String.valueOf(transferAmount));
                    try {
                        TransferResponseModel responseModel = paySyncClient.send(TransferMapper.class, requestModel, TransferResponseModel.class);
                        result = !result ? false : responseModel.isSuccess();
                    } catch (PayException e) {
                        result = false;
                        logger.error(MessageFormat.format("red envelope coupon transfer in balance failed (userCouponId = {0})", String.valueOf(userCouponModel.getId())), e);
                    }
                }
            }
        }
        return result;
    }

    @Override
    public String transferRedEnvelopNotify(Map<String, String> paramsMap, String queryString) {
        logger.info("[标的放款] transfer red envelop call back begin.");
        BaseCallbackRequestModel callbackRequest = this.payAsyncClient.parseCallbackRequest(
                paramsMap,
                queryString,
                ProjectTransferNotifyMapper.class,
                ProjectTransferNotifyRequestModel.class);

        if (callbackRequest == null || Strings.isNullOrEmpty(callbackRequest.getOrderId())) {
            logger.error(MessageFormat.format("[标的放款] transfer red envelop payback callback parse is failed (queryString = {0})", queryString));
            return null;
        }

        long userCouponId = Long.parseLong(callbackRequest.getOrderId());
        UserCouponModel userCouponModel = userCouponMapper.findById(userCouponId);
        TransferRedEnvelopCallbackMessage transferRedEnvelopCallbackMessage = new TransferRedEnvelopCallbackMessage(userCouponModel.getLoanId(),
                userCouponModel.getInvestId(),
                userCouponModel.getLoginName(),
                userCouponId);

        logger.info(MessageFormat.format("[标的放款] send message TransferRedEnvelopCallback,loanId:{0}, investId:{1}, loginName:{2} queryString:{3}, orderId:{4}",
                userCouponModel.getLoanId(), userCouponModel.getInvestId(), userCouponModel.getLoanName(), queryString, callbackRequest.getOrderId()));
        mqWrapperClient.sendMessage(MessageQueue.TransferRedEnvelopCallback, transferRedEnvelopCallbackMessage);

        String respData = callbackRequest.getResponseData();
        return respData;
    }

    @Override
    public boolean sendRedEnvelopTransferInBalanceCallBack(long userCouponId) {
        logger.info(MessageFormat.format("[标的放款] send redEnvelop transfer in balance callBack, userCouponId:{0}", String.valueOf(userCouponId)));
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
            systemBillService.transferOut(userCouponModel.getId(), transferAmount, SystemBillBusinessType.COUPON_RED_ENVELOPE, detail);

            amountTransfer.transferInBalance(userCouponModel.getLoginName(),
                    userCouponModel.getId(),
                    transferAmount,
                    couponModel.getCouponType().getUserBillBusinessType(), null, null);

            userCouponModel.setActualInterest(transferAmount);
            userCouponMapper.update(userCouponModel);
        } catch (Exception e) {
            logger.error(MessageFormat.format("red envelope coupon transfer in balance failed (userCouponId = {0})", String.valueOf(userCouponModel.getId())), e);
            return false;
        }
        return true;
    }
}