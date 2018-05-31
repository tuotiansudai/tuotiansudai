package com.tuotiansudai.cfca.connector;

import cfca.sadk.algorithm.common.PKIException;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tuotiansudai.cfca.util.SecurityUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

@Component
public class AnxinClient {

    private static final Logger logger = Logger.getLogger(AnxinClient.class);

    private final OkHttpClient httpClient;

    @Value("${anxin.jks.pwd}")
    private String JKS_PWD;

    @Value("${anxin.alias}")
    private String ALIAS;

    @Value("${anxin.url}")
    private String URL;

    @Value("${anxin.plat.id}")
    private String PLAT_ID;

    private static final String JKS_CLASS_PATH = "anxinsign.jks";
    private static final String DATA = "data";
    private static final String SIGNATURE = "signature";

    private static final String DEFAULT_SSL_PROTOCOL = "TLSv1.1";
    private static final String DEFAULT_KEY_ALGORITHM = KeyManagerFactory.getDefaultAlgorithm();
    private static final String DEFAULT_TRUST_ALGORITHM = TrustManagerFactory.getDefaultAlgorithm();

    private KeyStore anxinSignKey;

    public AnxinClient() {
        this.httpClient = new OkHttpClient();
        this.httpClient.setConnectTimeout(300, TimeUnit.SECONDS);
        this.httpClient.setReadTimeout(300, TimeUnit.SECONDS);
        this.httpClient.setWriteTimeout(300, TimeUnit.SECONDS);
    }

    @PostConstruct
    public void initSSL() throws GeneralSecurityException, IOException {
        anxinSignKey = SecurityUtil.loadAnxinSignKey(JKS_CLASS_PATH, JKS_PWD);
        logger.info("into AnxinClient initSSl method. Thread id: " + Thread.currentThread().getId());
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(DEFAULT_KEY_ALGORITHM);
        keyManagerFactory.init(anxinSignKey, JKS_PWD.toCharArray());

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(DEFAULT_TRUST_ALGORITHM);
        trustManagerFactory.init(anxinSignKey);

        SSLContext sslContext = SSLContext.getInstance(DEFAULT_SSL_PROTOCOL);
        sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());
        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

        if (sslSocketFactory != null) {
            logger.info("initSSL, sslSocketFactory is not null");
            httpClient.setSslSocketFactory(sslSocketFactory);
        }

    }

    public String send(String txCode, String requestData) throws PKIException {

        String url = URL + "platId/" + PLAT_ID + "/txCode/" + txCode + "/transaction";

        String signature = SecurityUtil.p7SignMessageDetach(anxinSignKey, JKS_PWD, ALIAS, requestData);

        FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder();
        formEncodingBuilder.add(DATA, requestData);
        formEncodingBuilder.add(SIGNATURE, signature);

        Request request = new Request.Builder().url(url).post(formEncodingBuilder.build()).build();

        try {
            logger.info("send anxin request, txCode: " + txCode + ", signature: " + signature);
            Response response = httpClient.newCall(request).execute();
            String responseBodyString = response.body().string();
            logger.info("send anxin response, txCode: " + txCode + ", body: " + responseBodyString);
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

    public byte[] batchDownContracts(String contractNos) {
        try {
            String url = URL + "platId/" + PLAT_ID + "/contractNos/" + contractNos + "/batchDownloading";

            Request request = new Request.Builder().url(url).get().build();
            Response response = httpClient.newCall(request).execute();

            logger.info("get files, response code: " + response.code());
            logger.info("get files, response message: " + response.message());

            return response.body().bytes();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
