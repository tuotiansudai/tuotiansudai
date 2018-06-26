package com.tuotiansudai.mq.consumer.loan;


import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.tuotiansudai.fudian.download.RechargeDownloadDto;
import com.tuotiansudai.fudian.message.BankQueryDownloadFilesMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.repository.mapper.BankRechargeMapper;
import com.tuotiansudai.repository.model.BankRechargeModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public class QueryDownloadFilesMessageConsumer implements MessageConsumer{

    private static Logger logger = LoggerFactory.getLogger(QueryDownloadFilesMessageConsumer.class);

    private final Gson gson  = new GsonBuilder().create();

    private final BankRechargeMapper bankRechargeMapper;

    @Autowired
    public QueryDownloadFilesMessageConsumer(BankRechargeMapper bankRechargeMapper){
        this.bankRechargeMapper = bankRechargeMapper;
    }

    @Override
    public MessageQueue queue() {
        return MessageQueue.QueryDownloadFiles;
    }

    @Override
    public void consume(String message) {
        if (Strings.isNullOrEmpty(message)) {
            logger.warn("[MQ] message is empty");
            return;
        }

        try {
            BankQueryDownloadFilesMessage bankQueryDownloadFilesMessage = gson.fromJson(message, BankQueryDownloadFilesMessage.class);
            if (!bankQueryDownloadFilesMessage.isStatus()) {
                return;
            }

            recharge(bankQueryDownloadFilesMessage);

        } catch (JsonSyntaxException e) {
            logger.error("[MQ] message is invalid: {}", message);
        }

        logger.info("[MQ] consume message success.");
    }

    private void recharge(BankQueryDownloadFilesMessage bankQueryDownloadFilesMessage){
        List<RechargeDownloadDto> dtos = bankQueryDownloadFilesMessage.getData();

        List<RechargeDownloadDto> queryList = dtos.stream().filter(dto->dto.getStatus().equals("1")).collect(Collectors.toList());
        List<BankRechargeModel> modelList = bankRechargeMapper.findSuccessByDate(bankQueryDownloadFilesMessage.getQueryDate());
    }
}
