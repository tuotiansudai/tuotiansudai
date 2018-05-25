package com.tuotiansudai.etcd;

import com.coreos.jetcd.Client;
import com.coreos.jetcd.KV;
import com.coreos.jetcd.data.ByteSequence;
import com.coreos.jetcd.kv.GetResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ETCDConfigReader {

    private static Logger logger = LoggerFactory.getLogger(ETCDConfigReader.class);

    private static final String ENDPOINTS_ENV_VAR = "TTSD_ETCD_ENV";

    private static final String ENV = Strings.isNullOrEmpty(System.getenv(ENDPOINTS_ENV_VAR)) ? "dev" : System.getenv(ENDPOINTS_ENV_VAR).toLowerCase();

    private static KV kvClient;

    private static ETCDConfigReader etcdConfigReader = new ETCDConfigReader();

    static {
        kvClient = Client.builder().endpoints(fetchEndpoints()).build().getKVClient();
    }

    private ETCDConfigReader() {
    }

    public static ETCDConfigReader getReader() {
        return etcdConfigReader;
    }

    public String getValue(String key) {
        if (Strings.isNullOrEmpty(key)) {
            return null;
        }

        key = MessageFormat.format("/{0}/{1}", ENV.toLowerCase(), key);

        CompletableFuture<GetResponse> completableFuture = kvClient.get(ByteSequence.fromString(key));

        try {
            GetResponse getResponse = completableFuture.get();
            if (getResponse.getCount() > 1) {
                logger.warn(MessageFormat.format("more than one kv found by {0}", key));
                return null;
            }
            if (getResponse.getCount() == 0) {
                logger.warn(MessageFormat.format("no kv found by {0}", key));
                return null;
            }

            return getResponse.getKvs().get(0).getValue().toStringUtf8().trim();
        } catch (Exception e) {
            logger.error(MessageFormat.format("fetch value by {0} error", key), e);
        }

        return null;
    }

    private static List<String> fetchEndpoints() {
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        try {
            ETCDEndPoints etcdEndPoints = objectMapper.readValue(ETCDConfigReader.class.getClassLoader().getResourceAsStream("etcd-endpoints.yml"), ETCDEndPoints.class);
            logger.info(MessageFormat.format("etcd env is {0}", ENV));
            return etcdEndPoints.getEndpoint(ENV);
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        return Lists.newArrayList();
    }
}
