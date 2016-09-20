package com.tuotiansudai.membership.service;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.RedisWrapperClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class ImportUtils {

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    private final static Map<String, String> redisKeys = new HashMap<>();

    public final static String redisMembershipGiveReceivers = "membership:membership-give:receivers";

    static {
        redisKeys.put("redisMembershipGiveReceivers", "membership:membership-give:receivers");
    }

    private ImportUtils() {
    }

    public static ImportUtils getInstance() {
        return new ImportUtils();
    }

    private boolean redisKeyIsLegal(String redisKey) {
        return redisKeys.containsKey(redisKey);
    }

    public long importStrings(final String redisKey, long oldImportId, InputStream inputStream) throws IOException {
        if (!redisKeyIsLegal(redisKey)) {
            throw new IllegalArgumentException();
        }

        StringBuilder stringBuilder = new StringBuilder();
        try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            String line;
            while (null != (line = bufferedReader.readLine())) {
                stringBuilder.append(line);
            }
        }

        List<String> Strings = Lists.newArrayList();
        for (String string : Splitter.on(',').splitToList(stringBuilder.toString())) {
            if (!StringUtils.isEmpty(string)) {
                Strings.add(string);
            }
        }

        if (redisWrapperClient.hexists(redisKey, String.valueOf(oldImportId))) {
            redisWrapperClient.hdel(redisKey, String.valueOf(oldImportId));
        }
        long importId = new Date().getTime();
        redisWrapperClient.hsetSeri(redisKey, String.valueOf(importId), Strings);

        return importId;
    }

    @SuppressWarnings(value = "unchecked")
    public List<String> getImportStrings(final String redisKey, final long importId) {
        if (!redisKeyIsLegal(redisKey)) {
            throw new IllegalArgumentException();
        }

        List<String> importStrings = (List<String>) redisWrapperClient.hgetSeri(redisKey, String.valueOf(importId));

        if(null == importStrings) {
            importStrings = new ArrayList<>();
        }

        return importStrings;
    }
}
