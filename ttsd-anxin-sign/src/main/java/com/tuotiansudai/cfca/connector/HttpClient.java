package com.tuotiansudai.cfca.connector;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.SecureRandom;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import com.tuotiansudai.cfca.util.CommonUtil;

public class HttpClient {
    public static final String BOUNDARY = java.util.UUID.randomUUID().toString();
    public static final String PREFIX = "--", LINEND = "\r\n";
    public static final String DEFAULT_CHARSET = "UTF-8";

    public static final int DEFAULT_BUFFER_SIZE = 2048;
    public static final int DEFAULT_CONNECT_TIMEOUT = 3000;
    public static final int DEFAULT_READ_TIMEOUT = 30000;

    public static final String DEFAULT_SSL_PROTOCOL = "TLSv1.1";
    public static final String DEFAULT_KEY_ALGORITHM = KeyManagerFactory.getDefaultAlgorithm();
    public static final String DEFAULT_KEY_STORE_TYPE = KeyStore.getDefaultType();
    public static final String DEFAULT_TRUST_ALGORITHM = TrustManagerFactory.getDefaultAlgorithm();
    public static final String DEFAULT_TRUST_STORE_TYPE = KeyStore.getDefaultType();

    public static final String DEFAULT_HTTP_USER_AGENT = "client";
    public static final String DEFAULT_HTTP_CONNECTION = "close";
    public static final String DEFAULT_HTTP_CONTENT_TYPE = "text/plain";
    public static final String DEFAULT_HTTP_ACCEPT = "text/plain";

    private static HostnameVerifier ignoreHostnameVerifier = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    public Config config = new Config();
    public SSLConfig sslConfig = new SSLConfig();
    public HttpConfig httpConfig = new HttpConfig();

    private SSLSocketFactory sslSocketFactory;

    public void initSSL(String keyStorePath, char[] keyStorePassword, String trustStorePath, char[] trustStorePassword) throws GeneralSecurityException,
            IOException {
        KeyManagerFactory keyManagerFactory = null;
        KeyStore keyStore = null;
        if (CommonUtil.isEmpty(sslConfig.keyProvider)) {
            keyManagerFactory = KeyManagerFactory.getInstance(sslConfig.keyAlgorithm);
            if (CommonUtil.isNotEmpty(sslConfig.keyStoreType)) {
                keyStore = KeyStore.getInstance(sslConfig.keyStoreType);
            }
        } else {
            keyManagerFactory = KeyManagerFactory.getInstance(sslConfig.keyAlgorithm, sslConfig.keyProvider);
            if (CommonUtil.isNotEmpty(sslConfig.keyStoreType)) {
                keyStore = KeyStore.getInstance(sslConfig.keyStoreType, sslConfig.keyProvider);
            }
        }
        if (CommonUtil.isEmpty(keyStorePath)) {
            keyManagerFactory.init(keyStore, keyStorePassword);
        } else {
            FileInputStream fileInputStream = null;
            try {
                fileInputStream = new FileInputStream(keyStorePath);
                keyStore.load(fileInputStream, keyStorePassword);
                keyManagerFactory.init(keyStore, keyStorePassword);
            } finally {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            }
        }

        TrustManagerFactory trustManagerFactory = null;
        KeyStore trustStore = null;
        if (CommonUtil.isEmpty(sslConfig.trustProvider)) {
            trustManagerFactory = TrustManagerFactory.getInstance(sslConfig.trustAlgorithm);
            if (CommonUtil.isNotEmpty(sslConfig.trustStoreType)) {
                trustStore = KeyStore.getInstance(sslConfig.trustStoreType);
            }
        } else {
            trustManagerFactory = TrustManagerFactory.getInstance(sslConfig.trustAlgorithm, sslConfig.trustProvider);
            if (CommonUtil.isNotEmpty(sslConfig.trustStoreType)) {
                trustStore = KeyStore.getInstance(sslConfig.trustStoreType, sslConfig.trustProvider);
            }
        }
        if (CommonUtil.isEmpty(trustStorePath)) {
            trustManagerFactory.init(trustStore);
        } else {
            FileInputStream fileInputStream = null;
            try {
                fileInputStream = new FileInputStream(trustStorePath);
                trustStore.load(fileInputStream, trustStorePassword);
                trustManagerFactory.init(trustStore);
                fileInputStream.close();
            } finally {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            }
        }

        SSLContext sslContext = null;
        if (CommonUtil.isEmpty(sslConfig.sslProvider)) {
            sslContext = SSLContext.getInstance(sslConfig.sslProtocol);
        } else {
            sslContext = SSLContext.getInstance(sslConfig.sslProtocol, sslConfig.sslProvider);
        }
        sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());
        sslSocketFactory = sslContext.getSocketFactory();
    }

    public HttpURLConnection connect(String url, String method) throws MalformedURLException, IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        if (sslSocketFactory != null) {
            HttpsURLConnection httpsConn = (HttpsURLConnection) connection;
            httpsConn.setSSLSocketFactory(sslSocketFactory);
            if (sslConfig.ignoreHostname) {
                httpsConn.setHostnameVerifier(ignoreHostnameVerifier);
            }
        }
        connection.setConnectTimeout(config.connectTimeout);
        connection.setReadTimeout(config.readTimeout);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setUseCaches(false);
        connection.setRequestMethod(method);
        connection.setRequestProperty("User-Agent", httpConfig.userAgent);
        connection.setRequestProperty("Connection", httpConfig.connection);
        connection.setRequestProperty("Content-Type", httpConfig.contentType + ";charset=" + config.charset);
        connection.setRequestProperty("Accept", httpConfig.accept);
        connection.setRequestProperty("Accept-Charset", config.charset);
        // NOT connect, delay until send() for set length
        return connection;
    }

    public int send(HttpURLConnection connection, byte[] requestData) throws IOException {
        if (requestData != null) {
            connection.setFixedLengthStreamingMode(requestData.length);
            connection.connect();
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(requestData);
            outputStream.flush();
        } else {
            connection.connect();
        }
        return connection.getResponseCode();
    }

    public int send(HttpURLConnection connection, byte[] requestData, File file, String signature) throws IOException {
        if (requestData != null) {
            String BOUNDARY = java.util.UUID.randomUUID().toString();
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("Charsert", "UTF-8");
            connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + BOUNDARY);
            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            StringBuilder sb = new StringBuilder();
            sb.append(PREFIX);
            sb.append(BOUNDARY);
            sb.append(LINEND);
            sb.append("Content-Disposition: form-data; name=\"data\"" + LINEND);
            sb.append(LINEND);

            sb.append(new String(requestData, "UTF-8"));
            sb.append(LINEND);

            sb.append(PREFIX);
            sb.append(BOUNDARY);
            sb.append(LINEND);
            sb.append("Content-Disposition: form-data; name=\"signature\"" + LINEND);
            sb.append(LINEND);
            sb.append(signature);
            sb.append(LINEND);

            String requestDataString = sb.toString();
            outputStream.write(requestDataString.getBytes());

            if (file != null) {
                StringBuilder sb1 = new StringBuilder();
                sb1.append("--");
                sb1.append(BOUNDARY);
                sb1.append("\r\n");
                // name是post中传参的键 filename是文件的名称
                sb1.append("Content-Disposition: form-data; name=\"contractFile\"; filename=\"" + file.getName() + "\"" + "\r\n");
                sb1.append("Content-Type: application/octet-stream; charset=" + "--" + "\r\n");
                sb1.append("\r\n");
                outputStream.write(sb1.toString().getBytes());

                // byte[] gzipBytes = GZipUtil.compress(new FileInputStream(file));
                // ByteArrayInputStream is = new ByteArrayInputStream(gzipBytes);
                FileInputStream is = new FileInputStream(file);

                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = is.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, len);
                }

                is.close();
                outputStream.write("\r\n".getBytes());
            }

            byte[] end_data = ("--" + BOUNDARY + "--" + "\r\n").getBytes();
            outputStream.write(end_data);
            outputStream.flush();

        } else {
            connection.connect();
        }
        return connection.getResponseCode();
    }

    public byte[] receive(HttpURLConnection connection) throws IOException {
        InputStream inputStream = connection.getErrorStream();
        if (inputStream == null) {
            inputStream = connection.getInputStream();
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(config.bufferSize);
        byte[] buffer = new byte[config.bufferSize];
        int read = -1;
        int length = 0;
        while ((read = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, read);
            length += read;
        }
        System.out.println("length:" + length);
        return byteArrayOutputStream.toByteArray();
    }

    public void disconnect(HttpURLConnection connection) {
        connection.disconnect();
    }

    public static class Config {
        public String charset = DEFAULT_CHARSET;
        public int bufferSize = DEFAULT_BUFFER_SIZE;
        public int connectTimeout = DEFAULT_CONNECT_TIMEOUT;
        public int readTimeout = DEFAULT_READ_TIMEOUT;
    }

    public static class SSLConfig {
        public String sslProvider = null;
        public String sslProtocol = DEFAULT_SSL_PROTOCOL;
        public String keyProvider = null;
        public String keyAlgorithm = DEFAULT_KEY_ALGORITHM;
        public String keyStoreType = DEFAULT_KEY_STORE_TYPE;
        public String trustProvider = null;
        public String trustAlgorithm = DEFAULT_TRUST_ALGORITHM;
        public String trustStoreType = DEFAULT_TRUST_STORE_TYPE;
        public boolean ignoreHostname = true;
    }

    public static class HttpConfig {
        public String userAgent = DEFAULT_HTTP_USER_AGENT;
        public String connection = DEFAULT_HTTP_CONNECTION;
        public String contentType = DEFAULT_HTTP_CONTENT_TYPE;
        public String accept = DEFAULT_HTTP_ACCEPT;
    }
}
