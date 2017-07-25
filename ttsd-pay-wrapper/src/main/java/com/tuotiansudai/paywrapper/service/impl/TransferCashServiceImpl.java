package com.tuotiansudai.paywrapper.service.impl;

import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.TransferCashDto;
import com.tuotiansudai.enums.SystemBillBusinessType;
import com.tuotiansudai.enums.SystemBillMessageType;
import com.tuotiansudai.enums.TransferType;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.message.AmountTransferMessage;
import com.tuotiansudai.message.SystemBillMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.repository.mapper.TransferMapper;
import com.tuotiansudai.paywrapper.repository.model.async.request.TransferRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.TransferResponseModel;
import com.tuotiansudai.paywrapper.service.TransferCashService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.SystemBillDetailTemplate;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    @Transactional
    public BaseDto<PayDataDto> transferCash(TransferCashDto transferCashDto) {
        PayDataDto payDataDto = new PayDataDto();
        BaseDto<PayDataDto> baseDto = new BaseDto<>(payDataDto);
        try {
            AccountModel accountModel = accountMapper.findByLoginName(transferCashDto.getLoginName());
            TransferRequestModel requestModel = TransferRequestModel.newLotteryReward(transferCashDto.getOrderId(), accountModel.getPayUserId(), accountModel.getPayAccountId(), transferCashDto.getAmount());
            TransferResponseModel responseModel = paySyncClient.send(TransferMapper.class, requestModel, TransferResponseModel.class);
            if (responseModel.isSuccess()) {

                AmountTransferMessage atm = new AmountTransferMessage(TransferType.TRANSFER_IN_BALANCE, transferCashDto.getLoginName(), Long.parseLong(transferCashDto.getOrderId()), Long.parseLong(transferCashDto.getAmount()),
                        UserBillBusinessType.INVEST_CASH_BACK, null, null);
                mqWrapperClient.sendMessage(MessageQueue.AmountTransfer, atm);
                String detail = MessageFormat.format(SystemBillDetailTemplate.LOTTERY_CASH_DETAIL_TEMPLATE.getTemplate(), transferCashDto.getLoginName(), transferCashDto.getAmount());

                SystemBillMessage sbm = new SystemBillMessage(SystemBillMessageType.TRANSFER_OUT, Long.parseLong(transferCashDto.getOrderId()), Long.parseLong(transferCashDto.getAmount()), SystemBillBusinessType.LOTTERY_CASH, detail);
                mqWrapperClient.sendMessage(MessageQueue.SystemBill, sbm);
            }
            payDataDto.setStatus(responseModel.isSuccess());
            payDataDto.setCode(responseModel.getRetCode());
            payDataDto.setMessage(responseModel.getRetMsg());
        } catch (Exception e) {
            payDataDto.setMessage(e.getMessage());
            logger.error(e.getLocalizedMessage(), e);
        }
        baseDto.setData(payDataDto);
        return baseDto;
    }

}
