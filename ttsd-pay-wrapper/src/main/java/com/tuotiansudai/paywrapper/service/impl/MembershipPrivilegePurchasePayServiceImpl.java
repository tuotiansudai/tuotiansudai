package com.tuotiansudai.paywrapper.service.impl;

import com.google.common.collect.Lists;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.SmsNotifyDto;
import com.tuotiansudai.enums.*;
import com.tuotiansudai.membership.dto.MembershipPrivilegePurchaseDto;
import com.tuotiansudai.membership.repository.mapper.MembershipPrivilegeMapper;
import com.tuotiansudai.membership.repository.mapper.MembershipPrivilegePurchaseMapper;
import com.tuotiansudai.membership.repository.model.MembershipPrivilegeModel;
import com.tuotiansudai.membership.repository.model.MembershipPrivilegePurchaseModel;
import com.tuotiansudai.message.AmountTransferMessage;
import com.tuotiansudai.message.EventMessage;
import com.tuotiansudai.message.PushMessage;
import com.tuotiansudai.message.SystemBillMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.TransferAsynMapper;
import com.tuotiansudai.paywrapper.repository.mapper.TransferNotifyMapper;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.TransferNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.TransferAsynRequestModel;
import com.tuotiansudai.paywrapper.service.MembershipPrivilegePurchasePayService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.util.IdGenerator;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Date;
import java.util.Map;

@Service
public class MembershipPrivilegePurchasePayServiceImpl implements MembershipPrivilegePurchasePayService {

    static Logger logger = Logger.getLogger(MembershipPrivilegePurchasePayServiceImpl.class);

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private PayAsyncClient payAsyncClient;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Autowired
    private MembershipPrivilegePurchaseMapper membershipPrivilegePurchaseMapper;

    @Autowired
    private MembershipPrivilegeMapper membershipPrivilegeMapper;

    @Override
    public BaseDto<PayFormDataDto> purchase(MembershipPrivilegePurchaseDto dto) {
        MembershipPrivilegePurchaseModel purchaseModel = new MembershipPrivilegePurchaseModel(IdGenerator.generate(), dto);

        AccountModel accountModel = accountMapper.findByLoginName(dto.getLoginName());

        TransferAsynRequestModel requestModel = TransferAsynRequestModel.createMembershipPrivilegePurchaseRequestModel(String.valueOf(purchaseModel.getId()),
                accountModel.getPayUserId(), accountModel.getPayAccountId(), String.valueOf(purchaseModel.getAmount()), dto.getSource());
        try {
            BaseDto<PayFormDataDto> baseDto = payAsyncClient.generateFormData(TransferAsynMapper.class, requestModel);
            if (baseDto.isSuccess()) {
                membershipPrivilegePurchaseMapper.create(purchaseModel);
            }
            return baseDto;
        } catch (PayException e) {
            PayFormDataDto payFormDataDto = new PayFormDataDto();
            payFormDataDto.setMessage(e.getMessage());
            return new BaseDto<>(payFormDataDto);
        }
    }

    @Override
    @Transactional
    public String purchaseCallback(Map<String, String> paramsMap, String originalQueryString) {
        BaseCallbackRequestModel callbackRequest = payAsyncClient.parseCallbackRequest(paramsMap, originalQueryString, TransferNotifyMapper.class, TransferNotifyRequestModel.class);

        if (callbackRequest == null) {
            return null;
        }

        this.postPurchaseCallback(callbackRequest);

        return callbackRequest.getResponseData();
    }

    private void postPurchaseCallback(BaseCallbackRequestModel callbackRequestModel) {
        long orderId;

        logger.info("Into membership privilege purchase call back.");

        try {
            orderId = Long.parseLong(callbackRequestModel.getOrderId());
        } catch (NumberFormatException e) {
            logger.error(MessageFormat.format("[membership privilege purchase] callback order is not a number (orderId = {0})", callbackRequestModel.getOrderId()), e);
            return;
        }

        MembershipPrivilegePurchaseModel membershipPrivilegePurchaseModel = membershipPrivilegePurchaseMapper.findById(orderId);
        if (membershipPrivilegePurchaseModel == null) {
            logger.error(MessageFormat.format("[membership privilege purchase] callback order is not exist (orderId = {0})", callbackRequestModel.getOrderId()));
            return;
        }

        if (membershipPrivilegePurchaseModel.getStatus() != MembershipPrivilegePurchaseStatus.WAIT_PAY) {
            logger.warn(MessageFormat.format("[membership privilege purchase] status is not WAIT_PAY (orderId = {0})", callbackRequestModel.getOrderId()));
            return;
        }

        membershipPrivilegePurchaseModel.setStatus(MembershipPrivilegePurchaseStatus.FAIL);
        if (callbackRequestModel.isSuccess()) {
            String loginName = membershipPrivilegePurchaseModel.getLoginName();
            long amount = membershipPrivilegePurchaseModel.getAmount();
            membershipPrivilegePurchaseModel.setStatus(MembershipPrivilegePurchaseStatus.SUCCESS);
            AmountTransferMessage atm = new AmountTransferMessage(TransferType.TRANSFER_OUT_BALANCE, loginName, orderId, amount, UserBillBusinessType.MEMBERSHIP_PRIVILEGE_PURCHASE);
            mqWrapperClient.sendMessage(MessageQueue.AmountTransfer, atm);

            SystemBillMessage sbm = new SystemBillMessage(SystemBillMessageType.TRANSFER_IN,
                    orderId, amount, SystemBillBusinessType.MEMBERSHIP_PRIVILEGE_PURCHASE,
                    MessageFormat.format("{0}购买增值特权{1}天", membershipPrivilegePurchaseModel.getMobile(), membershipPrivilegePurchaseModel.getPrivilegePriceType().getDuration()));
            mqWrapperClient.sendMessage(MessageQueue.SystemBill, sbm);
        }
        membershipPrivilegePurchaseMapper.update(membershipPrivilegePurchaseModel);
        Date endTime = new DateTime().plusDays(membershipPrivilegePurchaseModel.getPrivilegePriceType().getDuration() + 1).withTimeAtStartOfDay().minusSeconds(1).toDate();
        membershipPrivilegeMapper.create(new MembershipPrivilegeModel(
                membershipPrivilegePurchaseModel.getLoginName(),
                membershipPrivilegePurchaseModel.getPrivilege(),
                new Date(),
                endTime));

        //Title:恭喜您已成功购买{0}天增值特权！
        //Content:尊敬的用户，恭喜您已成功购买增值特权，有效期至{0}日，【马上投资】享受增值特权吧！
        String title = MessageFormat.format(MessageEventType.MEMBERSHIP_PRIVILEGE_BUY_SUCCESS.getTitleTemplate(), membershipPrivilegePurchaseModel.getPrivilegePriceType().getDuration());
        String content = MessageFormat.format(MessageEventType.MEMBERSHIP_PRIVILEGE_BUY_SUCCESS.getContentTemplate(), new DateTime(endTime).toString("yyyy-MM-dd"));
        mqWrapperClient.sendMessage(MessageQueue.EventMessage, new EventMessage(MessageEventType.MEMBERSHIP_PRIVILEGE_BUY_SUCCESS,
                Lists.newArrayList(membershipPrivilegePurchaseModel.getLoginName()), title, content, membershipPrivilegePurchaseModel.getId()));
        mqWrapperClient.sendMessage(MessageQueue.PushMessage, new PushMessage(Lists.newArrayList(membershipPrivilegePurchaseModel.getLoginName()),
                PushSource.ALL, PushType.MEMBERSHIP_PRIVILEGE_BUY_SUCCESS, title, AppUrl.MESSAGE_CENTER_LIST));
        mqWrapperClient.sendMessage(MessageQueue.SmsNotify, new SmsNotifyDto(JianZhouSmsTemplate.SMS_MEMBERSHIP_PRIVILEGE_BUY_SUCCESS_TEMPLATE, Lists.newArrayList(membershipPrivilegePurchaseModel.getMobile())));
    }
}
