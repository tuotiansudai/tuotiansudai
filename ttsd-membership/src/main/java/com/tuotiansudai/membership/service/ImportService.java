package com.tuotiansudai.membership.service;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.tuotiansudai.util.RedisWrapperClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

@Service
public class ImportService {

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    private final static Set<String> redisKeys = new HashSet<>();

    public final static String redisMembershipGiveReceivers = "membership:membership-give:receivers";

    static {
        redisKeys.add("membership:membership-give:receivers");
    }

    private boolean redisKeyIsLegal(String redisKey) {
        return redisKeys.contains(redisKey);
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
                line = (line.trim() + ",");
                stringBuilder.append(line);
            }
        }

        List<String> Strings = Lists.newArrayList();
        for (String string : Splitter.on(',').trimResults().splitToList(stringBuilder.toString())) {
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

        Object storeStrings = redisWrapperClient.hgetSeri(redisKey, String.valueOf(importId));

        List<String> importStrings = new ArrayList<>();

        if (null != storeStrings) {
            importStrings = (List<String>) storeStrings;
        }

        return importStrings;
    }
}
