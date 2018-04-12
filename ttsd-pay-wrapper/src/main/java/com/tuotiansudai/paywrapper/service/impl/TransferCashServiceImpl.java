package com.tuotiansudai.paywrapper.service.impl;

import com.tuotiansudai.activity.repository.dto.InviteHelpActivityPayCashDto;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.TransferCashDto;
import com.tuotiansudai.enums.SystemBillMessageType;
import com.tuotiansudai.enums.TransferType;
import com.tuotiansudai.message.AmountTransferMessage;
import com.tuotiansudai.message.SystemBillMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.repository.mapper.TransferMapper;
import com.tuotiansudai.paywrapper.repository.model.async.request.TransferRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.TransferResponseModel;
import com.tuotiansudai.paywrapper.service.TransferCashService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.WeChatUserMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.WeChatUserModel;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;

@Service
public class TransferCashServiceImpl implements TransferCashService {

    static Logger logger = Logger.getLogger(TransferCashServiceImpl.class);

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private PaySyncClient paySyncClient;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Autowired
    private WeChatUserMapper weChatUserMapper;

    @Override
    @Transactional
    public BaseDto<PayDataDto> transferCash(TransferCashDto transferCashDto) {
        PayDataDto payDataDto = new PayDataDto();
        BaseDto<PayDataDto> baseDto = new BaseDto<>(payDataDto);
        try {
            AccountModel accountModel = accountMapper.findByLoginName(transferCashDto.getLoginName());
            if (accountModel == null) {
                payDataDto.setCode(String.valueOf(HttpStatus.BAD_REQUEST));
                payDataDto.setMessage("用户未实名认证");
            } else {
                TransferRequestModel requestModel = TransferRequestModel.newLotteryReward(transferCashDto.getOrderId(), accountModel.getPayUserId(), accountModel.getPayAccountId(), transferCashDto.getAmount());
                TransferResponseModel responseModel = paySyncClient.send(TransferMapper.class, requestModel, TransferResponseModel.class);
                if (responseModel.isSuccess()) {
                    AmountTransferMessage atm = new AmountTransferMessage(TransferType.TRANSFER_IN_BALANCE, transferCashDto.getLoginName(), Long.parseLong(transferCashDto.getOrderId()), Long.parseLong(transferCashDto.getAmount()),
                            transferCashDto.getUserBillBusinessType(), null, null);
                    mqWrapperClient.sendMessage(MessageQueue.AmountTransfer, atm);
                    String detail = MessageFormat.format(transferCashDto.getSystemBillDetailTemplate().getTemplate(), transferCashDto.getLoginName(), transferCashDto.getAmount());

                    SystemBillMessage sbm = new SystemBillMessage(SystemBillMessageType.TRANSFER_OUT, Long.parseLong(transferCashDto.getOrderId()), Long.parseLong(transferCashDto.getAmount()), transferCashDto.getSystemBillBusinessType(), detail);
                    mqWrapperClient.sendMessage(MessageQueue.SystemBill, sbm);
                }
                payDataDto.setStatus(responseModel.isSuccess());
                payDataDto.setCode(responseModel.getRetCode());
                payDataDto.setMessage(responseModel.getRetMsg());
            }
        } catch (Exception e) {
            payDataDto.setMessage(e.getMessage());
            logger.error(e.getLocalizedMessage(), e);
        }
        baseDto.setData(payDataDto);
        return baseDto;
    }

    @Override
    public BaseDto<PayDataDto> transferCashInviteHelpActivity(InviteHelpActivityPayCashDto dto) {
        PayDataDto payDataDto = new PayDataDto();
        BaseDto<PayDataDto> baseDto = new BaseDto<>(payDataDto);
        WeChatUserModel weChatUserModel = weChatUserMapper.findByOpenid(dto.getOpenid());
        if (weChatUserModel == null || !weChatUserModel.isBound()) {
            payDataDto.setCode(String.valueOf(HttpStatus.BAD_REQUEST));
            payDataDto.setMessage("用户未注册");
            baseDto.setData(payDataDto);
            return baseDto;
        }
        TransferCashDto transferCashDto = new TransferCashDto(weChatUserModel.getLoginName(), dto.getOrderId(), dto.getAmount(), dto.getUserBillBusinessType(), dto.getSystemBillBusinessType(), dto.getSystemBillDetailTemplate());
        return transferCash(transferCashDto);
    }
}
