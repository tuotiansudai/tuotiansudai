package com.tuotiansudai.fudian.util;


import com.jcraft.jsch.*;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Properties;

@Component
public class SftpClient {

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(SftpClient.class);

    private final OssClient ossClient;

    private static final int TIME_OUT = 60000;
    private static final String FTP_HOST = "172.16.88.61";
    private static final int FTP_PORT = 22;
    private static final String FTP_USERNAME = "username";
    private static final String FTP_PASSWORD = "password";

    private Session session = null;

    private Channel channel = null;

    @Autowired
    public SftpClient(OssClient ossClient){
        this.ossClient = ossClient;
    }

    public ChannelSftp getChannel() throws JSchException {
        JSch jsch = new JSch();
        session = jsch.getSession(FTP_USERNAME, FTP_HOST, FTP_PORT);
        session.setPassword(FTP_PASSWORD);

        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.setTimeout(TIME_OUT);
        session.connect();

        channel = session.openChannel("sftp");
        channel.connect();
        return (ChannelSftp) channel;
    }

    public void closeChannel(){
        if (channel != null) {
            channel.disconnect();
        }
        if (session != null) {
            session.disconnect();
        }
    }

    public void download(String path, String name) throws JSchException, SftpException, IOException {
        ChannelSftp sftp = getChannel();
        InputStream inputStream = sftp.get(path + "/" + name);
        try {
            ossClient.upload(name, inputStream);
        }catch (Exception e){
            logger.error(MessageFormat.format("oss upload fail fileName:{0}, error:{1}", name, e.getMessage()));
        }
    }
}
