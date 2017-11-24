package com.tuotiansudai.etcd;

import com.coreos.jetcd.Client;
import com.coreos.jetcd.KV;
import com.coreos.jetcd.data.ByteSequence;
import com.coreos.jetcd.kv.GetResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class ETCDConfigReader {

    private static Logger logger = Logger.getLogger(ETCDConfigReader.class);

    private static final String ENDPOINTS_ENV_VAR = "TTSD_ETCD_ENDPOINT";

    private static KV kvClient;

    static {
        kvClient = Client.builder().endpoints(fetchEndpoints()).build().getKVClient();
    }

    String getProperties(String key) {
        return getValue(key);
    }


    public static String getValue(String key) {
        if (Strings.isNullOrEmpty(key)) {
            return null;
        }
        CompletableFuture<GetResponse> completableFuture = kvClient.get(ByteSequence.fromString(key));

        try {
            GetResponse getResponse = completableFuture.get();
            if (getResponse.getCount() > 1) {
                logger.error(MessageFormat.format("more than one kv found by {0}", key));
                return null;
            }
            if (getResponse.getCount() == 0) {
                logger.error(MessageFormat.format("no kv found by {0}", key));
                return null;
            }

            return getResponse.getKvs().get(0).getValue().toStringUtf8().trim();
        } catch (Exception e) {
            logger.error(MessageFormat.format("fetch value by {0} error", key), e);
        }

        return null;
    }

    private static String fetchEndpoints() {
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        try {
            ETCDEndPoints etcdEndPoints = objectMapper.readValue(ETCDConfigReader.class.getClassLoader().getResourceAsStream("etcd-endpoints.yml"), ETCDEndPoints.class);
            String env = System.getenv(ENDPOINTS_ENV_VAR);
            logger.info(MessageFormat.format("etcd env var is {0}", env));
            if (!Strings.isNullOrEmpty(env)) {
                ETCDEndPoint endpoint = etcdEndPoints.getEndpoint(env.toLowerCase());

                String endpointUrl = MessageFormat.format("http://{0}:{1}",
                        endpoint.getHost().get(new Random().nextInt(endpoint.getHost().size())),
                        endpoint.getPort().get(new Random().nextInt(endpoint.getPort().size())));

                logger.info(MessageFormat.format("etcd endpoint is {0}", endpointUrl));
                return endpointUrl;
            }

        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        return "http://localhost:2379";
    }
}
