package com.tuotiansudai.mq.consumer.loan;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.tuotiansudai.enums.WeChatMessageType;
import com.tuotiansudai.message.WeChatMessageNotify;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.JsonConverter;
import com.tuotiansudai.util.MobileEncoder;
import com.tuotiansudai.util.WeChatClient;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class WeChatMessageNotifyConsumer implements MessageConsumer {

    private static Logger logger = LoggerFactory.getLogger(WeChatMessageNotifyConsumer.class);

    private final WeChatClient weChatClient = WeChatClient.getClient();

    private final static String commonRemark = "如有疑问，可随时致电客服400-169-1188（客服时间：工作日9:00-20:00）。";
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private InvestMapper investMapper;
    @Autowired
    private LoanMapper loanMapper;
    @Autowired
    private WeChatUserMapper weChatUserMapper;
    @Autowired
    private InvestRepayMapper investRepayMapper;
    @Autowired
    private WithdrawMapper withdrawMapper;
    @Autowired
    private TransferApplicationMapper transferApplicationMapper;

    @Override
    public MessageQueue queue() {
        return MessageQueue.WeChatMessageNotify;
    }

    @Transactional
    @Override
    public void consume(String message) {
        logger.info("[MQ WeChatMessageNotify] receive message: {}.", message);

        if (Strings.isNullOrEmpty(message)) {
            return;
        }

        try {
            WeChatMessageNotify weChatMessageNotify = JsonConverter.readValue(message, WeChatMessageNotify.class);
            if (weChatMessageNotify.getWeChatMessageType() == null) {
                logger.info("[MQ WeChatMessageNotify] weChatMessageNotify is invalid", message);
                return;
            }
            if (weChatMessageNotify.getWeChatMessageType() == WeChatMessageType.LOAN_OUT_SUCCESS) {
                this.loanOutMessageNotify(weChatMessageNotify);
            } else {
                String openId = fetchOpenId(weChatMessageNotify.getLoginName());
                if (openId == null) {
                    logger.info("[MQ WeChatMessageNotify] loginName:{} unbound", weChatMessageNotify.getLoginName());
                    return;
                }
                notifyMap.get(weChatMessageNotify.getWeChatMessageType()).messageNotify(openId, weChatMessageNotify);
            }

        } catch (IOException e) {
            logger.error("[MQ WeChatMessageNotify] message is invalid receive message: {}.", message);
        }

        logger.info("[MQ WeChatMessageNotify] consume message success: {}.", message);
    }

    private String fetchOpenId(String loginName) {
        Optional<WeChatUserModel> optional = weChatUserMapper.findByLoginName(loginName).stream().filter(s -> s.isBound()).findFirst();
        return optional.map(o -> o.getOpenid()).orElse(null);
    }

    private void loanOutMessageNotify(WeChatMessageNotify weChatMessageNotify) {
        if (weChatMessageNotify.getBusinessId() == null) {
            logger.info("[MQ WeChatMessageNotify] type:{} BusinessId is null",
                    weChatMessageNotify.getWeChatMessageType());
            return;
        }

        LoanModel loanModel = loanMapper.findById(weChatMessageNotify.getBusinessId());
        List<InvestModel> investModels = investMapper.findSuccessInvestsByLoanId(weChatMessageNotify.getBusinessId());

        investModels.stream()
                .filter(s -> fetchOpenId(s.getLoginName()) != null)
                .forEach(investModel -> weChatClient.sendTemplateMessage(WeChatMessageType.LOAN_OUT_SUCCESS, Maps.newHashMap(ImmutableMap.<String, String>builder()
                        .put("openid", fetchOpenId(investModel.getLoginName()))
                        .put("first", String.format("您投资的”%s”已经满标放款了，具体详情如下：", loanModel.getName()))
                        .put("keyword1", loanModel.getName())
                        .put("keyword2", AmountConverter.convertCentToString(loanModel.getLoanAmount()))
                        .put("keyword3", AmountConverter.convertCentToString(investModel.getAmount()))
                        .put("remark", commonRemark)
                        .build())));
    }

    WeChatMessageNotifyAction<String, WeChatMessageNotify> bind = (openId, weChatMessageNotify) -> {

        UserModel userModel = userMapper.findByLoginName(weChatMessageNotify.getLoginName());

        weChatClient.sendTemplateMessage(WeChatMessageType.BOUND_TO_OTHER_USER, Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("openid", openId)
                .put("first", "您的拓天速贷账号已被其他微信号绑定，请知悉")
                .put("keyword1", MobileEncoder.encode(userModel.getMobile()))
                .put("keyword2", new DateTime().toString("yyyy-MM-dd HH:mm:ss"))
                .put("remark", "如非您本人操作，请及时联系客服：400-169-1188（客服时间：工作日9:00-20:00）。")
                .build()));
        logger.info("[MQ WeChatMessageNotify] type:{} message notify successfully. user: {}, openid: {}",
                weChatMessageNotify.getWeChatMessageType(),
                weChatMessageNotify.getLoginName(),
                openId);

    };

    WeChatMessageNotifyAction<String, WeChatMessageNotify> invest = (openId, weChatMessageNotify) -> {
        if (weChatMessageNotify.getBusinessId() == null) {
            logger.info("[MQ WeChatMessageNotify] type:{} businessId is null. user:{},openid: {}",
                    weChatMessageNotify.getWeChatMessageType(),
                    weChatMessageNotify.getLoginName(),
                    openId);
            return;
        }
        InvestModel investModel = investMapper.findById(weChatMessageNotify.getBusinessId());
        LoanModel loanModel = loanMapper.findById(investModel.getLoanId());

        weChatClient.sendTemplateMessage(WeChatMessageType.INVEST_SUCCESS, Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("openid", openId)
                .put("first", String.format("您已成功投资: %s", loanModel.getName()))
                .put("keyword1", loanModel.getName())
                .put("keyword2", AmountConverter.convertCentToString(investModel.getAmount()))
                .put("remark", commonRemark)
                .build()));

        logger.info("[MQ WeChatMessageNotify] type:{} invest success message notify successfully. user: {}, openid: {}",
                weChatMessageNotify.getWeChatMessageType(),
                weChatMessageNotify.getLoginName(),
                openId);


    };

    WeChatMessageNotifyAction<String, WeChatMessageNotify> normalRepay = (openId, weChatMessageNotify) -> {
        InvestRepayModel investRepayModel = investRepayMapper.findById(weChatMessageNotify.getBusinessId());
        InvestModel investModel = investMapper.findById(investRepayModel.getInvestId());
        LoanModel loanModel = loanMapper.findById(investModel.getLoanId());


        weChatClient.sendTemplateMessage(WeChatMessageType.NORMAL_REPAY_SUCCESS, Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("openid", openId)
                .put("first", String.format("您投资的”%s”回款已到账", loanModel.getName()))
                .put("keyword1", loanModel.getName())
                .put("keyword2", investRepayModel.getActualRepayDate() != null ? new DateTime(investRepayModel.getActualRepayDate()).toString("yyyy-MM-dd HH:mm:ss") : "")
                .put("keyword3", AmountConverter.convertCentToString(investRepayModel.getCorpus() + investRepayModel.getActualInterest()))
                .put("remark", commonRemark)
                .build()));
    };

    WeChatMessageNotifyAction<String, WeChatMessageNotify> advanceRepay = (openId, weChatMessageNotify) -> {
        InvestRepayModel investRepayModel = investRepayMapper.findById(weChatMessageNotify.getBusinessId());
        InvestModel investModel = investMapper.findById(investRepayModel.getInvestId());
        LoanModel loanModel = loanMapper.findById(investModel.getLoanId());

        weChatClient.sendTemplateMessage(WeChatMessageType.ADVANCE_REPAY_SUCCESS, Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("openid", openId)
                .put("first", String.format("由于您投资的%s，因借款方有充分的还款能力和还款意愿，特提前还款", loanModel.getName()))
                .put("keyword1", investRepayModel.getRepayDate() != null ? new DateTime(investRepayModel.getRepayDate()).toString("yyyy-MM-dd") : "")
                .put("keyword2", investRepayModel.getActualRepayDate() != null ? new DateTime(investRepayModel.getActualRepayDate()).toString("yyyy-MM-dd") : "")
                .put("remark", "请注意查收并提前做好资金安排，如有疑问，可随时致电客服400-169-1188（客服时间：工作日9:00-20:00）。")
                .build()));

    };

    WeChatMessageNotifyAction<String, WeChatMessageNotify> withdrawApply = (openId, weChatMessageNotify) -> {

        WithdrawModel withdrawModel = withdrawMapper.findById(weChatMessageNotify.getBusinessId());

        weChatClient.sendTemplateMessage(WeChatMessageType.WITHDRAW_APPLY_SUCCESS, Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("openid", openId)
                .put("first", "您的提现申请成功，请等待银行处理")
                .put("keyword1", AmountConverter.convertCentToString(withdrawModel.getAmount()))
                .put("keyword2", withdrawModel.getBankCard().getBankCode())
                .put("keyword3", withdrawModel.getApplyNotifyTime() != null ? new DateTime(withdrawModel.getApplyNotifyTime()).toString("yyyy-MM-dd") : "")
                .put("remark", "请您耐心等待，留意银行卡资金变化。如有疑问，可随时致电客服400-169-1188（客服时间：工作日9:00-20:00）。")
                .build()));

    };

    WeChatMessageNotifyAction<String, WeChatMessageNotify> withdrawNotify = (openId, weChatMessageNotify) -> {
        WithdrawModel withdrawModel = withdrawMapper.findById(weChatMessageNotify.getBusinessId());

        weChatClient.sendTemplateMessage(WeChatMessageType.WITHDRAW_NOTIFY_SUCCESS, Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("openid", openId)
                .put("first", "您的账户发起的提现申请现已到账")
                .put("keyword1", withdrawModel.getApplyNotifyTime() != null ? new DateTime(withdrawModel.getApplyNotifyTime()).toString("yyyy-MM-dd") : "")
                .put("keyword2", AmountConverter.convertCentToString(withdrawModel.getAmount()))
                .put("keyword3", withdrawModel.getNotifyTime() != null ? new DateTime(withdrawModel.getNotifyTime()).toString("yyyy-MM-dd") : "")
                .put("remark", commonRemark)
                .build()));

    };


    WeChatMessageNotifyAction<String, WeChatMessageNotify> transfer = (openId, weChatMessageNotify) -> {
        TransferApplicationModel transferApplicationModel = transferApplicationMapper.findById(weChatMessageNotify.getBusinessId());
        weChatClient.sendTemplateMessage(WeChatMessageType.WITHDRAW_NOTIFY_SUCCESS, Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("openid", openId)
                .put("first", "您发起的债权转让已经转让完成。")
                .put("keyword1", AmountConverter.convertCentToString(transferApplicationModel.getInvestAmount()))
                .put("keyword2", AmountConverter.convertCentToString(transferApplicationModel.getTransferAmount()))
                .put("keyword3", AmountConverter.convertCentToString(transferApplicationModel.getTransferFee()))
                .put("keyword4", AmountConverter.convertCentToString(transferApplicationModel.getTransferAmount()))
                .put("remark", "回款金额已返还到您个人账户余额中。如有疑问，可随时致电客服400-169-1188（客服时间：工作日9:00-20:00）。")
                .build()));

    };

    Map<WeChatMessageType, WeChatMessageNotifyAction> notifyMap = Maps.newHashMap(ImmutableMap.<WeChatMessageType, WeChatMessageNotifyAction>builder()
            .put(WeChatMessageType.BOUND_TO_OTHER_USER, bind)
            .put(WeChatMessageType.INVEST_SUCCESS, invest)
            .put(WeChatMessageType.NORMAL_REPAY_SUCCESS, normalRepay)
            .put(WeChatMessageType.ADVANCE_REPAY_SUCCESS, advanceRepay)
            .put(WeChatMessageType.WITHDRAW_APPLY_SUCCESS, withdrawApply)
            .put(WeChatMessageType.WITHDRAW_NOTIFY_SUCCESS, withdrawNotify)
            .put(WeChatMessageType.TRANSFER_SUCCESS, transfer)
            .build());

}

@FunctionalInterface
interface WeChatMessageNotifyAction<T, P> {
    void messageNotify(T t, P p);
}