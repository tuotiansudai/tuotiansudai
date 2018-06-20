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

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final int TIME_OUT = 60000; // 超时时间60s
    private Session session = null;
    private Channel channel = null;

    @Autowired
    public QueryDownloadLogFilesService(BankClient bankClient, SignatureHelper signatureHelper) {
        this.bankClient = bankClient;
        this.signatureHelper = signatureHelper;
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
            download(responseDto.getContent().getFilename(), responseDto.getContent().getSftpFilePath());
        }

    }

    public ChannelSftp getChannel() throws JSchException {

        String ftpHost = "172.16.88.61";
        int port = 22;
        String ftpUserName = "sftpuser";
        String ftpPassword = "sftpuser";


        JSch jsch = new JSch(); // 创建JSch对象
        session = jsch.getSession(ftpUserName, ftpHost, port); // 根据用户名，主机ip，端口获取一个Session对象
        session.setPassword(ftpPassword); // 设置密码

        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config); // 为Session对象设置properties
        session.setTimeout(TIME_OUT); // 设置timeout时间
        session.connect(); // 通过Session建立链接

        channel = session.openChannel("sftp"); // 打开SFTP通道
        channel.connect(); // 建立SFTP通道的连接
        return (ChannelSftp) channel;
    }

    public void closeChannel() throws Exception {
        if (channel != null) {
            channel.disconnect();
        }
        if (session != null) {
            session.disconnect();
        }
    }

    public void download(String name, String path) throws JSchException, SftpException, IOException {
        ChannelSftp sftp = getChannel();
        sftp.cd(path);
        InputStream inputStream = sftp.get(path + "/" + name);
        ObjectInputStream objectInputStream = null;
        try {
            objectMapper.readValue(inputStream, QueryRechargeDownloadDto.class);

            objectInputStream = new ObjectInputStream(inputStream);
            QueryRechargeDownloadDto[] dtos = (QueryRechargeDownloadDto[]) objectInputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            inputStream.close();
            objectInputStream.close();
        }
    }
}
