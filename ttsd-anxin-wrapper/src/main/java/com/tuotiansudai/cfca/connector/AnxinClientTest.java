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
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;


public class AnxinClientTest {

    private static final Logger logger = Logger.getLogger(AnxinClient.class);

    @Autowired
    private OkHttpClient httpClient;
    private String JKS_CLASS_PATH = "anxinsign.jks";
    public String JKS_PWD = "tuotiansudai";
    public String ALIAS = "anxinsign";
    public String URL = "https://210.74.42.33:9443/FEP/";
    public String PLAT_ID = "3FEC02111C593DECE05312016B0A418F";

    private static final String DATA = "data";
    private static final String SIGNATURE = "signature";

    private static final int DEFAULT_CONNECT_TIMEOUT = 3000;
    private static final int DEFAULT_READ_TIMEOUT = 10000;

    private static final String DEFAULT_SSL_PROTOCOL = "TLSv1.1";
    private static final String DEFAULT_KEY_ALGORITHM = KeyManagerFactory.getDefaultAlgorithm();
    private static final String DEFAULT_TRUST_ALGORITHM = TrustManagerFactory.getDefaultAlgorithm();

    private KeyStore anxinSignKey;

    public void initSSL() throws GeneralSecurityException, IOException {
        anxinSignKey = SecurityUtil.loadAnxinSignKey(JKS_CLASS_PATH, JKS_PWD);
        httpClient = new OkHttpClient();
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(DEFAULT_KEY_ALGORITHM);
        keyManagerFactory.init(anxinSignKey, JKS_PWD.toCharArray());

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(DEFAULT_TRUST_ALGORITHM);
        trustManagerFactory.init(anxinSignKey);

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

        String signature = SecurityUtil.p7SignMessageDetach(anxinSignKey, JKS_PWD, ALIAS, requestData);

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
