package com.tuotiansudai.paywrapper.service.impl;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.exception.AmountTransferException;
import com.tuotiansudai.membership.dto.MembershipPurchaseDto;
import com.tuotiansudai.membership.repository.mapper.MembershipMapper;
import com.tuotiansudai.membership.repository.mapper.MembershipPurchaseMapper;
import com.tuotiansudai.membership.repository.mapper.UserMembershipMapper;
import com.tuotiansudai.membership.repository.model.MembershipPurchaseModel;
import com.tuotiansudai.membership.repository.model.MembershipPurchaseStatus;
import com.tuotiansudai.membership.repository.model.UserMembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipType;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.TransferAsynMapper;
import com.tuotiansudai.paywrapper.repository.mapper.TransferNotifyMapper;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.TransferNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.TransferAsynRequestModel;
import com.tuotiansudai.paywrapper.service.MembershipPurchasePayService;
import com.tuotiansudai.paywrapper.service.SystemBillService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.AmountTransfer;
import com.tuotiansudai.util.IdGenerator;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Map;

@Service
public class MembershipPurchasePayServiceImpl implements MembershipPurchasePayService {

    static Logger logger = Logger.getLogger(MembershipPurchasePayServiceImpl.class);

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private MembershipPurchaseMapper membershipPurchaseMapper;

    @Autowired
    private MembershipMapper membershipMapper;

    @Autowired
    private UserMembershipMapper userMembershipMapper;

    @Autowired
    private PayAsyncClient payAsyncClient;

    @Autowired
    private AmountTransfer amountTransfer;

    @Autowired
    private SystemBillService systemBillService;

    @Override
    public BaseDto<PayFormDataDto> purchase(MembershipPurchaseDto dto) {
        MembershipPurchaseModel purchaseModel = new MembershipPurchaseModel(idGenerator.generate(), dto);

        AccountModel accountModel = accountMapper.findByLoginName(dto.getLoginName());

        TransferAsynRequestModel requestModel = TransferAsynRequestModel.createMembershipPurchaseRequestModel(String.valueOf(purchaseModel.getId()),
                accountModel.getPayUserId(), String.valueOf(purchaseModel.getAmount()), dto.getSource());
        try {
            BaseDto<PayFormDataDto> baseDto = payAsyncClient.generateFormData(TransferAsynMapper.class, requestModel);
            if (baseDto.isSuccess()) {
                membershipPurchaseMapper.create(purchaseModel);
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
        BaseCallbackRequestModel callbackRequest = this.payAsyncClient.parseCallbackRequest(paramsMap, originalQueryString, TransferNotifyMapper.class, TransferNotifyRequestModel.class);

        if (callbackRequest == null) {
            return null;
        }

        this.postPurchaseCallback(callbackRequest);

        return callbackRequest.getResponseData();
    }

    private void postPurchaseCallback(BaseCallbackRequestModel callbackRequestModel) {
        long orderId;

        try {
            orderId = Long.parseLong(callbackRequestModel.getOrderId());
        } catch (NumberFormatException e) {
            logger.error(MessageFormat.format("[membership purchase] callback order is not a number (orderId = {0})", callbackRequestModel.getOrderId()), e);
            return;
        }

        MembershipPurchaseModel membershipPurchaseModel = membershipPurchaseMapper.findById(orderId);
        if (membershipPurchaseModel == null) {
            logger.error(MessageFormat.format("[membership purchase] callback order is not exist (orderId = {0})", callbackRequestModel.getOrderId()));
            return;
        }

        if (membershipPurchaseModel.getStatus() != MembershipPurchaseStatus.WAIT_PAY) {
            logger.error(MessageFormat.format("[membership purchase] status is not WAIT_PAY (orderId = {0})", callbackRequestModel.getOrderId()));
            return;
        }

        membershipPurchaseModel.setStatus(MembershipPurchaseStatus.FAIL);
        if (callbackRequestModel.isSuccess()) {
            String loginName = membershipPurchaseModel.getLoginName();
            long amount = membershipPurchaseModel.getAmount();
            membershipPurchaseModel.setStatus(MembershipPurchaseStatus.SUCCESS);
            try {
                amountTransfer.transferOutBalance(loginName, orderId, amount, UserBillBusinessType.MEMBERSHIP_PURCHASE, null, null);
                systemBillService.transferIn(orderId, amount, SystemBillBusinessType.MEMBERSHIP_PURCHASE,
                        MessageFormat.format("{0}购买会员等级{1}，{2}天", membershipPurchaseModel.getMobile(), membershipPurchaseModel.getLevel(), membershipPurchaseModel.getDuration()));
            } catch (AmountTransferException e) {
                logger.error(MessageFormat.format("[membership purchase] transfer out balance failed (orderId = {0})", String.valueOf(callbackRequestModel.getOrderId())));
            }

        }
        membershipPurchaseMapper.update(membershipPurchaseModel);

        userMembershipMapper.create(new UserMembershipModel(membershipPurchaseModel.getLoginName(),
                membershipMapper.findByLevel(membershipPurchaseModel.getLevel()).getId(),
                new DateTime().plusDays(membershipPurchaseModel.getDuration() + 1).withTimeAtStartOfDay().minusSeconds(1).toDate(),
                UserMembershipType.PURCHASED));
    }
}
