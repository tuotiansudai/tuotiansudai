package com.tuotiansudai.cfca.connector;

import cfca.sadk.algorithm.common.PKIException;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tuotiansudai.cfca.util.SecurityUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;


public class AnxinClientTest {

    private static final Logger logger = Logger.getLogger(AnxinClient.class);

    @Autowired
    private OkHttpClient httpClient;
    public String JKS_PATH = "/workspace/dev-config/anxinsign.jks";
    public String JKS_PWD = "123abc";
    public String ALIAS = "anxinsign";
    public String URL = "https://210.74.42.33:9443/FEP/";
    public String PLAT_ID = "3FEC02111C593DECE05312016B0A418F";

    private static final String DATA = "data";
    private static final String SIGNATURE = "signature";

    private static final int DEFAULT_CONNECT_TIMEOUT = 3000;
    private static final int DEFAULT_READ_TIMEOUT = 10000;

    private static final String DEFAULT_SSL_PROTOCOL = "TLSv1.1";
    private static final String DEFAULT_KEY_ALGORITHM = KeyManagerFactory.getDefaultAlgorithm();
    private static final String DEFAULT_KEY_STORE_TYPE = KeyStore.getDefaultType();
    private static final String DEFAULT_TRUST_ALGORITHM = TrustManagerFactory.getDefaultAlgorithm();
    private static final String DEFAULT_TRUST_STORE_TYPE = KeyStore.getDefaultType();


    public void initSSL() throws GeneralSecurityException, IOException {
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(DEFAULT_KEY_ALGORITHM);
        KeyStore keyStore = KeyStore.getInstance(DEFAULT_KEY_STORE_TYPE);
        FileInputStream keyStoreFis = null;
        try {
            keyStoreFis = new FileInputStream(JKS_PATH);
            keyStore.load(keyStoreFis, JKS_PWD.toCharArray());
            keyManagerFactory.init(keyStore, JKS_PWD.toCharArray());
        } finally {
            if (keyStoreFis != null) {
                keyStoreFis.close();
            }
        }

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(DEFAULT_TRUST_ALGORITHM);
        KeyStore trustStore = KeyStore.getInstance(DEFAULT_TRUST_STORE_TYPE);
        FileInputStream trustStoreFis = null;
        try {
            trustStoreFis = new FileInputStream(JKS_PATH);
            trustStore.load(trustStoreFis, JKS_PWD.toCharArray());
            trustManagerFactory.init(trustStore);
        } finally {
            if (trustStoreFis != null) {
                trustStoreFis.close();
            }
        }

        SSLContext sslContext = SSLContext.getInstance(DEFAULT_SSL_PROTOCOL);
        sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());
        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

        if (sslSocketFactory != null) {
            httpClient.setSslSocketFactory(sslSocketFactory);
        }

        httpClient.setConnectTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS);
        httpClient.setReadTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    public String send(String txCode, String requestData) throws PKIException {

        String url = URL + "platId/" + PLAT_ID + "/txCode/" + txCode + "/transaction";

        String signature = SecurityUtil.p7SignMessageDetach(JKS_PATH, JKS_PWD, ALIAS, requestData);

        FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder();
        formEncodingBuilder.add(DATA, requestData);
        formEncodingBuilder.add(SIGNATURE, signature);

        Request request = new Request.Builder().url(url).post(formEncodingBuilder.build()).build();

        try {
            Response response = httpClient.newCall(request).execute();
            String responseBodyString = response.body().string();
            logger.info(" txCode: " + txCode + ", response body: " + responseBodyString);
            return responseBodyString;
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
            return e.getMessage();
        }
    }

    public byte[] getContractFile(String contractNo) {
        try {
            String url = URL + "platId/" + PLAT_ID + "/contractNo/" + contractNo + "/downloading";

            Request request = new Request.Builder().url(url).get().build();
            Response response = httpClient.newCall(request).execute();

            logger.info("get file, response code: " + response.code());
            logger.info("get file, response message: " + response.message());

            return response.body().bytes();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
