package com.tuotiansudai.fudian.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.jcraft.jsch.*;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.dto.QueryDownloadLogFilesType;
import com.tuotiansudai.fudian.dto.QueryRechargeDownloadDto;
import com.tuotiansudai.fudian.dto.request.QueryDownloadLogFiles;
import com.tuotiansudai.fudian.dto.response.QueryDownloadLogFilesContentDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.sign.SignatureHelper;
import com.tuotiansudai.fudian.util.BankClient;
import com.tuotiansudai.fudian.util.SftpClient;
import jdk.internal.org.objectweb.asm.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.Properties;

import static com.tuotiansudai.fudian.service.NotifyCallbackInterface.FIXED_DELAY;

@Service
public class QueryDownloadLogFilesService {

    private static final ApiType API_TYPE = ApiType.QUERY_DOWNLOAD_LOG_FILES;

    private final BankClient bankClient;

    private final SignatureHelper signatureHelper;

    private final SftpClient sftpClient;

    @Autowired
    public QueryDownloadLogFilesService(BankClient bankClient, SignatureHelper signatureHelper, SftpClient sftpClient) {
        this.bankClient = bankClient;
        this.signatureHelper = signatureHelper;
        this.sftpClient = sftpClient;
    }

    @Scheduled(fixedDelay = FIXED_DELAY, initialDelay = 1000 * 10, zone = "Asia/Shanghai")
    @SuppressWarnings(value = "unchecked")
    public void RechargeSchedule() throws SftpException, JSchException, IOException {

        QueryDownloadLogFiles dto = new QueryDownloadLogFiles(QueryDownloadLogFilesType.recharge.name());

        signatureHelper.sign(API_TYPE, dto);
        if (Strings.isNullOrEmpty(dto.getRequestData())) {
            return;
        }

        String responseData = bankClient.send(API_TYPE, dto.getRequestData());

        ResponseDto<QueryDownloadLogFilesContentDto> responseDto = (ResponseDto<QueryDownloadLogFilesContentDto>) API_TYPE.getParser().parse(responseData);

        if (responseDto.isSuccess()) {
            sftpClient.download(responseDto.getContent().getSftpFilePath(), responseDto.getContent().getFilename());
        }

    }

}
