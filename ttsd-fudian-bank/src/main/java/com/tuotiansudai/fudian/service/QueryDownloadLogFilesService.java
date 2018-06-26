package com.tuotiansudai.fudian.service;


import com.google.common.base.Strings;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.download.RechargeDownloadDto;
import com.tuotiansudai.fudian.download.QueryDownloadLogFilesType;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.tuotiansudai.fudian.service.NotifyCallbackInterface.FIXED_DELAY;

@Service
public class QueryDownloadLogFilesService {

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

    @Scheduled(fixedDelay = FIXED_DELAY, initialDelay = 1000 * 10, zone = "Asia/Shanghai")
    @SuppressWarnings(value = "unchecked")
    public void RechargeSchedule() throws SftpException, JSchException, IOException {
//        ChannelSftp sftp = sftpClient.getChannel();
        rechargeFile(null);
        sftpClient.closeChannel();

    }

    private void rechargeFile(ChannelSftp sftp) throws SftpException {

//        List<String> params = sftpClient.download(sftp, responseDto.getContent().getSftpFilePath(), responseDto.getContent().getFilename());
//        List<String> params = new ArrayList<>();
//        params.add("1231|1231|12312|1231");
//        params.add("1231|1231|12312|1231");
//        params.add("1231|1231|12312|1231");
//        List<RechargeDownloadDto> list = DownloadFileMatchDtoParser.parse(RechargeDownloadDto.class, params);

        QueryDownloadLogFilesRequestDto dto = new QueryDownloadLogFilesRequestDto(QueryDownloadLogFilesType.recharge.name());

        signatureHelper.sign(API_TYPE, dto);
        if (Strings.isNullOrEmpty(dto.getRequestData())) {
            return;
        }

        String responseData = bankClient.send(API_TYPE, dto.getRequestData());

        ResponseDto<QueryDownloadLogFilesContentDto> responseDto = (ResponseDto<QueryDownloadLogFilesContentDto>) API_TYPE.getParser().parse(responseData);

        if (responseDto.isSuccess()) {
            List<String> params = sftpClient.download(sftp, responseDto.getContent().getSftpFilePath(), responseDto.getContent().getFilename());
            List<RechargeDownloadDto> list = DownloadFileMatchDtoParser.parse(RechargeDownloadDto.class, params);
            messageQueueClient.sendMessage(MessageQueue.QueryDownloadFiles, new BankQueryDownloadFilesMessage<>(dto.getQueryDate(), QueryDownloadLogFilesType.recharge, list));
        }
    }
}
