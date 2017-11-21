package com.tuotiansudai.etcd;

import com.coreos.jetcd.Client;
import com.coreos.jetcd.KV;
import com.coreos.jetcd.data.ByteSequence;
import com.coreos.jetcd.kv.GetResponse;
import com.google.common.base.Strings;
import org.apache.log4j.Logger;

import java.text.MessageFormat;
import java.util.concurrent.CompletableFuture;

public class ETCDConfigReader {

    private static Logger logger = Logger.getLogger(ETCDConfigReader.class);

    private static KV kvClient;

    static {
        kvClient = Client.builder().endpoints("http://localhost:2379").build().getKVClient();
    }

    public String getProperties(String key) {
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
}
