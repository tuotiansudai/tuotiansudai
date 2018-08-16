package com.tuotiansudai.fudian.util;


import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.jcraft.jsch.*;
import com.tuotiansudai.fudian.config.DownloadConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
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

    private static OSS oss;

    @Autowired
    public DownloadClient(DownloadConfig downloadConfig) {
        this.downloadConfig = downloadConfig;
        oss = new OSSClientBuilder().build(this.downloadConfig.getOssEndpoint(), this.downloadConfig.getAccessKeyId(), this.downloadConfig.getAccessKeySecret());
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
        byte[] bytes = convertToByteOutWithInputClose(inputStream);
        oss.putObject(downloadConfig.getBucketName(), name, new ByteArrayInputStream(bytes));
        List<String> params = new ArrayList<String>();
        try (
                BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(bytes)));
        ) {
            String content = null;
            while ((content = br.readLine()) != null) {
                params.add(content);
            }
        } catch (IOException e) {
            logger.error("[DownloadClient] download() occured error",e);
        }
        return params;
    }

    /**
     * 缺点:把文件加载到内存中来
     * @param inputStream
     * @return
     */
    private byte[] convertToByteOutWithInputClose(InputStream inputStream) {
        byte[] buffer = new byte[1024];
        try (
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ) {
            int len;
            while ((len = inputStream.read(buffer)) > -1) {
                baos.write(buffer, 0, len);
            }
            baos.flush();
            inputStream.close();
            return baos.toByteArray();
        } catch (IOException e) {
            logger.error("[DownloadClient] convertToByteOutWithInputClose occured error", e);
        }
        return buffer;
    }
}
