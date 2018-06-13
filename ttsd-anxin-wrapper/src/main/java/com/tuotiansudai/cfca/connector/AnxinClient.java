package com.tuotiansudai.cfca.connector;

import cfca.sadk.algorithm.common.Mechanism;
import cfca.sadk.algorithm.common.PKIException;
import cfca.sadk.lib.crypto.JCrypto;
import cfca.sadk.lib.crypto.Session;
import cfca.sadk.util.Signature;
import cfca.sadk.x509.certificate.X509Cert;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tuotiansudai.etcd.ETCDConfigReader;
import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

public class AnxinClient {

    private static final Logger logger = Logger.getLogger(AnxinClient.class);

    private static final AnxinClient anxinClient = new AnxinClient();

    private static String JKS_PWD = ETCDConfigReader.getReader().getValue("anxin.jks.pwd");
    private static String ALIAS = ETCDConfigReader.getReader().getValue("anxin.alias");
    private static String URL = ETCDConfigReader.getReader().getValue("anxin.url");
    private static String PLAT_ID = ETCDConfigReader.getReader().getValue("anxin.plat.id");

    private static KeyStore anxinSignKey;

    private static OkHttpClient httpClient;

    private static Session session;

    private AnxinClient() {
        try {
            String JKS_CLASS_PATH = "anxinsign.jks";
            String DEFAULT_SSL_PROTOCOL = "TLSv1.1";
            String DEFAULT_KEY_ALGORITHM = KeyManagerFactory.getDefaultAlgorithm();
            String DEFAULT_TRUST_ALGORITHM = TrustManagerFactory.getDefaultAlgorithm();
            anxinSignKey = loadAnxinSignKey(JKS_CLASS_PATH, JKS_PWD);

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(DEFAULT_TRUST_ALGORITHM);
            trustManagerFactory.init(anxinSignKey);

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(DEFAULT_KEY_ALGORITHM);
            keyManagerFactory.init(anxinSignKey, JKS_PWD.toCharArray());

            SSLContext sslContext = SSLContext.getInstance(DEFAULT_SSL_PROTOCOL);
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            if (sslSocketFactory != null) {
                logger.info("initSSL, sslSocketFactory is not null");
                httpClient = new OkHttpClient();
                httpClient.setConnectTimeout(300, TimeUnit.SECONDS);
                httpClient.setReadTimeout(300, TimeUnit.SECONDS);
                httpClient.setWriteTimeout(300, TimeUnit.SECONDS);
                httpClient.setSslSocketFactory(sslSocketFactory);
            }

            String deviceName = JCrypto.JSOFT_LIB;
            JCrypto.getInstance().initialize(deviceName, null);
            session = JCrypto.getInstance().openSession(deviceName);
        } catch (GeneralSecurityException | IOException | PKIException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }

    public static AnxinClient getClient() {
        return anxinClient;
    }


    public String send(String txCode, String requestData) throws PKIException {
        String url = MessageFormat.format("{0}/platId/{1}/txCode/{2}/transaction", URL, PLAT_ID, txCode);

        String signature = p7SignMessageDetach(anxinSignKey, JKS_PWD, ALIAS, requestData);

        Request request = new Request.Builder()
                .url(url)
                .post(new FormEncodingBuilder().add("data", requestData).add("signature", signature).build())
                .build();

        try {
            logger.info(MessageFormat.format("[Anxin Client] send request, txCode: {0}, signature: {1}", txCode, signature));

            Response response = httpClient.newCall(request).execute();
            String responseBodyString = response.body().string();

            logger.info(MessageFormat.format("[Anxin Client] send request, txCode: {0}, signature: {1}, response: {2}", txCode, signature, responseBodyString));
            return responseBodyString;
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
            return e.getMessage();
        }
    }

    public byte[] getContractFile(String contractNo) {
        try {
            String url = MessageFormat.format("{0}/platId/{1}/contractNo/{2}/downloading", URL, PLAT_ID, contractNo);
            Request request = new Request.Builder().url(url).get().build();
            Response response = httpClient.newCall(request).execute();
            return response.body().bytes();
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        return null;
    }

    private static KeyStore loadAnxinSignKey(String keyPath, String password) throws GeneralSecurityException, IOException {
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        try (InputStream keyStoreFis = new ClassPathResource(keyPath).getInputStream()) {
            keyStore.load(keyStoreFis, password.toCharArray());
            return keyStore;
        }
    }

    private static String p7SignMessageDetach(KeyStore keyStore, String jksPWD, String alias, String sourceData) throws PKIException {
        if (keyStore == null) {
            throw new PKIException("JKSFile keyStore should not be null");
        }
        if (jksPWD == null) {
            throw new PKIException("JKSFile jksPwd should not be null");
        }
        if (alias == null) {
            throw new PKIException("JKSFile alias should not be null");
        }
        try {
            PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, jksPWD.toCharArray());
            Certificate certificate = keyStore.getCertificate(alias);
            if (certificate == null) {
                throw new PKIException("no such alias cert!");
            }
            X509Cert signCert = new X509Cert(certificate.getEncoded());
            Signature signature = new Signature();
            byte[] signatureByte = signature.p7SignMessageDetach(Mechanism.SHA1_RSA, sourceData.getBytes("UTF-8"), privateKey, signCert, session);
            return new String(signatureByte);
        } catch (Exception e) {
            throw new PKIException("P7 detach signature fail", e);
        }
    }
}
