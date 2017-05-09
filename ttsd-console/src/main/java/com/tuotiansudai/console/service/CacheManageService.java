package com.tuotiansudai.console.service;

import com.tuotiansudai.util.MybatisRedisCacheWrapperClient;
import org.springframework.stereotype.Service;

@Service
public class CacheManageService {

    private MybatisRedisCacheWrapperClient mybatisRedisCacheWrapperClient = new MybatisRedisCacheWrapperClient();

    public String clearMybatisCache() {
        return mybatisRedisCacheWrapperClient.clearMybatisCache();
    }

}
