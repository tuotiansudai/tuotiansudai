package com.tuotiansudai.fudian.util;


import com.aliyun.oss.OSSClient;
import com.jcraft.jsch.*;
import com.tuotiansudai.fudian.config.DownloadConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

@Component
public class DownloadClient {

    private static Logger logger = LoggerFactory.getLogger(DownloadClient.class);

    private static final int TIME_OUT = 60000;

    private Session session = null;

    private Channel channel = null;

    private final DownloadConfig downloadConfig;

    private static OSSClient ossClient;

    @Autowired
    public DownloadClient(DownloadConfig downloadConfig) {
        this.downloadConfig = downloadConfig;
        ossClient = new OSSClient(this.downloadConfig.getOssEndpoint(), this.downloadConfig.getAccessKeyId(), this.downloadConfig.getAccessKeySecret());
    }

    public ChannelSftp getChannel() throws JSchException {
        JSch jsch = new JSch();
        session = jsch.getSession(downloadConfig.getSftpUsername(), downloadConfig.getSftpHost(), downloadConfig.getSftpPort());
        session.setPassword(downloadConfig.getSftpPassword());

        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.setTimeout(TIME_OUT);
        session.connect();

        channel = session.openChannel("sftp");
        channel.connect();
        return (ChannelSftp) channel;
    }

    public void closeChannel() {
        if (channel != null) {
            channel.disconnect();
        }
        if (session != null) {
            session.disconnect();
        }
    }

    public List<String> download(ChannelSftp sftp, String path, String name) throws SftpException {
        InputStream inputStream = sftp.get(path + "/" + name);
        ossClient.putObject(downloadConfig.getBucketName(), name, inputStream);
        List<String> params = new ArrayList<String>();
        try (Scanner scanner = new Scanner(inputStream)) {
            while (scanner.hasNext()) {
                params.add(scanner.next());
            }
        } catch (Exception e) {
            logger.error(MessageFormat.format("download fail fileName:{0}, error:{1}", name, e.getMessage()));
        }
        return params;
    }
}
