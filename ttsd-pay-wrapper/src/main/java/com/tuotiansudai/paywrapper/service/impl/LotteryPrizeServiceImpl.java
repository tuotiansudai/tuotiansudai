package com.tuotiansudai.paywrapper.service.impl;


import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.repository.mapper.TransferMapper;
import com.tuotiansudai.paywrapper.repository.model.sync.request.TransferRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.response.TransferResponseModel;
import com.tuotiansudai.paywrapper.service.LotteryPrizeService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.MessageFormat;
import java.util.Date;

public class LotteryPrizeServiceImpl implements LotteryPrizeService {

    private static Logger logger = Logger.getLogger(LotteryPrizeServiceImpl.class);

    private static String LOTTERY_PRIZE_ORDER_ID_TEMPLATE = "lotteryPrize:{0}:{1}";

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private PaySyncClient paySyncClient;


    @Override
    public void sendTianDouLotteryPrize20(String loginName) {

        long amount = 2000;

        TransferRequestModel requestModel = TransferRequestModel.newRequest(MessageFormat.format(LOTTERY_PRIZE_ORDER_ID_TEMPLATE, loginName, String.valueOf(new Date().getTime())),
                accountMapper.findByLoginName(loginName).getPayUserId(),
                String.valueOf(amount));

        boolean isSuccess = false;

        try {
            TransferResponseModel responseModel = paySyncClient.send(TransferMapper.class, requestModel, TransferResponseModel.class);
            isSuccess = responseModel.isSuccess();
        } catch (PayException e) {
            logger.error(MessageFormat.format("send tiandou lottery prize 20 failed (loginName = {0}, amount = {1})", loginName, String.valueOf(amount)), e);
        }

        if (!isSuccess) {
            logger.error(MessageFormat.format("send tiandou lottery prize 20 failed (loginName = {0}, amount = {1})", loginName, String.valueOf(amount)));
        }
    }

}
