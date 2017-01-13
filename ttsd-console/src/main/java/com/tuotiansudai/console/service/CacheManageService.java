package com.tuotiansudai.console.service;

import com.tuotiansudai.client.RedisWrapperClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CacheManageService {

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    public String clearMybatisCache() {
        return redisWrapperClient.clearMybatisCache();
    }

}
