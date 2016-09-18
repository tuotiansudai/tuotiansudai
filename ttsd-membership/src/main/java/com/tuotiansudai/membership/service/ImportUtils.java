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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public ImportUtils getInstance() {
        return new ImportUtils();
    }

    private boolean redisKeyIsLegal(String redisKey) {
        return redisKeys.containsKey(redisKey);
    }

    public long importStrings(final String redisKey, long oldImportUsersId, InputStream inputStream) throws IOException {
        if (!redisKeyIsLegal(redisKey)) {
            throw new IllegalArgumentException();
        }

        if (redisWrapperClient.hexists(redisKey, String.valueOf(oldImportUsersId))) {
            redisWrapperClient.hdel(redisKey, String.valueOf(oldImportUsersId));
        }

        long importUsersId = new Date().getTime();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while (null != (line = bufferedReader.readLine())) {
            stringBuilder.append(line);
        }

        List<String> importUsers = Lists.newArrayList();
        for (String loginName : Splitter.on(',').splitToList(stringBuilder.toString())) {
            if (!StringUtils.isEmpty(loginName)) {
                importUsers.add(loginName);
            }
        }
        redisWrapperClient.hsetSeri(redisKey, String.valueOf(importUsersId), importUsers);
        return importUsersId;
    }
}
