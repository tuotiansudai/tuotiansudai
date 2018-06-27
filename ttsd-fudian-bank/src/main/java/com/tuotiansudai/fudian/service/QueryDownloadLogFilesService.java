package com.tuotiansudai.fudian.service;


import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
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
import com.tuotiansudai.fudian.util.SftpClient;
import com.tuotiansudai.mq.client.model.MessageQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import static com.tuotiansudai.fudian.service.NotifyCallbackInterface.FIXED_DELAY;

@Service
public class QueryDownloadLogFilesService {

    private static Logger logger = LoggerFactory.getLogger(QueryDownloadLogFilesService.class);

    private static final ApiType API_TYPE = ApiType.QUERY_DOWNLOAD_LOG_FILES;

    private final BankClient bankClient;

    private final SignatureHelper signatureHelper;

    private final SftpClient sftpClient;

    private final MessageQueueClient messageQueueClient;

    @Autowired
    public QueryDownloadLogFilesService(BankClient bankClient, SignatureHelper signatureHelper, SftpClient sftpClient, MessageQueueClient messageQueueClient) {
        this.bankClient = bankClient;
        this.signatureHelper = signatureHelper;
        this.sftpClient = sftpClient;
        this.messageQueueClient = messageQueueClient;
    }

    @Scheduled(cron = "0 2 * * * ?", zone = "Asia/Shanghai")
    @SuppressWarnings(value = "unchecked")
    public void RechargeSchedule() throws SftpException, JSchException, IOException {
        ChannelSftp sftp = sftpClient.getChannel();

        Map<QueryDownloadLogFilesType, Class> classMap = Maps.newHashMap(ImmutableMap.<QueryDownloadLogFilesType, Class>builder()
                .put(QueryDownloadLogFilesType.recharge, RechargeDownloadDto.class)
                .put(QueryDownloadLogFilesType.withdraw, WithdrawDownloadDto.class)
                .put(QueryDownloadLogFilesType.invest, InvestDownloadDto.class)
                .put(QueryDownloadLogFilesType.repayment, RechargeDownloadDto.class)
                .put(QueryDownloadLogFilesType.loanBack, LoanRepayDownloadDto.class)
                .put(QueryDownloadLogFilesType.creditInvest, LoanCreditInvestDownloadDto.class)
                .put(QueryDownloadLogFilesType.loanFull, LoanRepayInvestDownloadDto.class)
                .build());

        List<QueryDownloadLogFilesType> types = Lists.newArrayList(QueryDownloadLogFilesType.values());
        types.forEach(type -> {
            downloadFiles(sftp, type, classMap.get(type));
        });
        sftpClient.closeChannel();
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

            if (responseDto.isSuccess()) {
                List<String> params = sftpClient.download(sftp, responseDto.getContent().getSftpFilePath(), responseDto.getContent().getFilename());
                List<T> list = DownloadFileMatchDtoParser.parse(dtoClass, params);
                messageQueueClient.sendMessage(MessageQueue.QueryDownloadFiles, new BankQueryDownloadFilesMessage<>(dto.getQueryDate(), type, list));
            }

        } catch (Exception e) {
            logger.error(MessageFormat.format("query download files fail type:{0}, message:{1}", type.name(), e.getMessage()));
        }
    }
}
