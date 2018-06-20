package com.tuotiansudai.fudian.util;


import com.jcraft.jsch.*;
import com.tuotiansudai.fudian.dto.QueryRechargeDownloadDto;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Properties;

public class SftpClient {

    private static final int TIME_OUT = 60000; // 超时时间60s

    private Session session = null;

    private Channel channel = null;

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



    public void download(String path, String name) throws JSchException, SftpException, IOException {
        ChannelSftp sftp = getChannel();
        sftp.cd(path);
        InputStream inputStream = sftp.get(path + "/" + name);
        ObjectInputStream objectInputStream = null;
        try {
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
