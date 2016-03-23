package com.tuotiansudai.paywrapper.service.impl;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.TransferCashDto;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.repository.mapper.TransferMapper;
import com.tuotiansudai.paywrapper.repository.model.sync.request.TransferRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.TransferResponseModel;
import com.tuotiansudai.paywrapper.service.SystemBillService;
import com.tuotiansudai.paywrapper.service.TransferCashService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.SystemBillBusinessType;
import com.tuotiansudai.repository.model.SystemBillDetailTemplate;
import com.tuotiansudai.repository.model.UserBillBusinessType;
import com.tuotiansudai.util.AmountTransfer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
public class TransferCashServiceImpl implements TransferCashService{

    static Logger logger = Logger.getLogger(TransferCashServiceImpl.class);

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private PaySyncClient paySyncClient;

    @Autowired
    private AmountTransfer amountTransfer;

    @Autowired
    private SystemBillService systemBillService;

    @Override
    public BaseDto<PayDataDto> transferCash(TransferCashDto transferCashDto) {
        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        PayDataDto payDataDto = new PayDataDto();
        baseDto.setData(payDataDto);
        AccountModel accountModel = accountMapper.findByLoginName(transferCashDto.getLoginName());
        TransferResponseModel responseModel = null;
        try {
            TransferRequestModel requestModel = TransferRequestModel.newRequest(transferCashDto.getOrderId(), accountModel.getPayUserId(), transferCashDto.getAmount());
            responseModel = paySyncClient.send(TransferMapper.class, requestModel, TransferResponseModel.class);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        try {
            if (responseModel.isSuccess()) {
                amountTransfer.transferInBalance(transferCashDto.getLoginName(), Long.parseLong(transferCashDto.getOrderId()), Long.parseLong(transferCashDto.getAmount()),
                        UserBillBusinessType.LOTTERY_CASH, null, null);
                String detail = MessageFormat.format(SystemBillDetailTemplate.LOTTERY_CASH_DETAIL_TEMPLATE.getTemplate(), transferCashDto.getLoginName(), transferCashDto.getAmount());
                systemBillService.transferOut(Long.parseLong(transferCashDto.getOrderId()), Long.parseLong(transferCashDto.getAmount()), SystemBillBusinessType.LOTTERY_CASH, detail);
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return baseDto;
    }

}
