package com.tuotiansudai.util;

import com.coreos.jetcd.Client;
import com.coreos.jetcd.KV;
import com.coreos.jetcd.data.ByteSequence;
import com.coreos.jetcd.kv.GetResponse;
import org.apache.log4j.Logger;

import java.text.MessageFormat;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ETCDConfigReader {

    private static Logger logger = Logger.getLogger(ETCDConfigReader.class);

    private static KV kvClient;

    static {
        kvClient = Client.builder().endpoints("http://192.168.1.139:23791").build().getKVClient();
    }

    public static String getValue(String key) {
        CompletableFuture<GetResponse> completableFuture = kvClient.get(ByteSequence.fromString(key));

        try {
            GetResponse getResponse = completableFuture.get();
            if (getResponse.getCount() > 1) {
                logger.error(MessageFormat.format("more than one kv found by {0}", key));
                return "";
            }
            if (getResponse.getCount() == 0) {
                logger.error(MessageFormat.format("no kv found by {0}", key));
                return "";
            }

            return getResponse.getKvs().get(0).getValue().toStringUtf8();
        } catch (Exception e) {
            logger.error(MessageFormat.format("fetch value by {0} error", key), e);
        }

        return "";
    }
}
