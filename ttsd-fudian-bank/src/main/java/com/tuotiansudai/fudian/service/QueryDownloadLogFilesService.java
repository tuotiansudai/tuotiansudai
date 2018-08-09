package com.tuotiansudai.fudian.service;


import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.download.*;
import com.tuotiansudai.fudian.dto.request.QueryDownloadLogFilesRequestDto;
import com.tuotiansudai.fudian.dto.response.QueryDownloadLogFilesContentDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.message.BankQueryDownloadFilesMessage;
import com.tuotiansudai.fudian.sign.SignatureHelper;
import com.tuotiansudai.fudian.strategy.DownloadFileMatchDtoParser;
import com.tuotiansudai.fudian.util.BankClient;
import com.tuotiansudai.fudian.util.MessageQueueClient;
import com.tuotiansudai.fudian.util.DownloadClient;
import com.tuotiansudai.mq.client.model.MessageQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

@Service
public class QueryDownloadLogFilesService {

    private static Logger logger = LoggerFactory.getLogger(QueryDownloadLogFilesService.class);

    private static final ApiType API_TYPE = ApiType.QUERY_DOWNLOAD_LOG_FILES;

    private static final Map<QueryDownloadLogFilesType, Class> classMap = Maps.newHashMap(ImmutableMap.<QueryDownloadLogFilesType, Class>builder()
            .put(QueryDownloadLogFilesType.RECHARGE, RechargeDownloadDto.class)
            .put(QueryDownloadLogFilesType.WITHDRAW, WithdrawDownloadDto.class)
            .put(QueryDownloadLogFilesType.LOAN_INVEST, LoanInvestDownloadDto.class)
            .put(QueryDownloadLogFilesType.LOAN_REPAY, LoanRepayDownloadDto.class)
            .put(QueryDownloadLogFilesType.LOAN_CALLBACK, LoanCallBackDownloadDto.class)
            .put(QueryDownloadLogFilesType.LOAN_CREDIT_INVEST, LoanCreditInvestDownloadDto.class)
            .put(QueryDownloadLogFilesType.LOAN_FULL, LoanFullDownloadDto.class)
            .build());

    private final BankClient bankClient;

    private final SignatureHelper signatureHelper;

    private final DownloadClient downloadClient;

    private final MessageQueueClient messageQueueClient;

    @Autowired
    public QueryDownloadLogFilesService(BankClient bankClient, SignatureHelper signatureHelper, DownloadClient downloadClient, MessageQueueClient messageQueueClient) {
        this.bankClient = bankClient;
        this.signatureHelper = signatureHelper;
        this.downloadClient = downloadClient;
        this.messageQueueClient = messageQueueClient;
    }

    @Scheduled(cron = "0 0 0/1 * * ?", zone = "Asia/Shanghai")
    @SuppressWarnings(value = "unchecked")
    public void schedule() throws JSchException {
        ChannelSftp sftp = downloadClient.getChannel();

        List<QueryDownloadLogFilesType> types = Lists.newArrayList(QueryDownloadLogFilesType.values());
        types.forEach(type -> {
            downloadFiles(sftp, type, classMap.get(type));
        });
        downloadClient.closeChannel();
    }

    private <T extends DownloadFilesMatch> void downloadFiles(ChannelSftp sftp, QueryDownloadLogFilesType type, Class<T> dtoClass) {
        try {
            QueryDownloadLogFilesRequestDto dto = new QueryDownloadLogFilesRequestDto(type.name());
            signatureHelper.sign(API_TYPE, dto);
            if (Strings.isNullOrEmpty(dto.getRequestData())) {
                return;
            }
            String responseData = bankClient.send(API_TYPE, dto.getRequestData());

            ResponseDto<QueryDownloadLogFilesContentDto> responseDto = (ResponseDto<QueryDownloadLogFilesContentDto>) API_TYPE.getParser().parse(responseData);

            if (!responseDto.isSuccess()){
                logger.error("[QueryDownloadFiles] callback is failure, type: {}, message {}", type.name(), responseDto.getRetMsg());
                return;
            }

            List<String> params = downloadClient.download(sftp, responseDto.getContent().getSftpFilePath(), responseDto.getContent().getFilename());
            List<T> list = DownloadFileMatchDtoParser.parse(dtoClass, params);

            if (list.size() == 0){
                messageQueueClient.sendMessage(MessageQueue.QueryDownloadFiles,
                        new BankQueryDownloadFilesMessage<>(dto.getQueryDate(), type, 0, list));
            }

            int batchSize = list.size() / 200 + (list.size() % 200 > 0 ? 1 : 0);
            for (int batch = 0; batch < batchSize; batch ++){
                messageQueueClient.sendMessage(MessageQueue.QueryDownloadFiles,
                        new BankQueryDownloadFilesMessage<>(dto.getQueryDate(), type, list.size(), list.subList(batch * 200, (batch + 1) * 200 > list.size() ? list.size() : (batch + 1) * 200)));
            }

        } catch (Exception e) {
            logger.error(MessageFormat.format("[QueryDownloadFiles] fail type:{0}, message:{1}", type.name(), e.getMessage()));
        }
    }
}
