package com.ttsd.special.services.impl;

import com.ttsd.redis.RedisClient;
import com.ttsd.special.services.CachedMobileLocationService;
import com.ttsd.util.ChinaArea;
import com.ttsd.util.MobileLocationUtils;
import com.ttsd.util.ProvinceUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CachedMobileLocationServiceImpl implements CachedMobileLocationService{
    /**
     * redis 里的手机号缓存的 key
     */
    private static final String MOBILE_LOCATION_CACHE_KEY = "mobile_location_cache";

    @Autowired
    private RedisClient redis;

    @Override
    public String getProvinceByPhoneNumber(String phoneNumber) {
        String province = redis.hget(MOBILE_LOCATION_CACHE_KEY, phoneNumber);
        if(StringUtils.isBlank(province)){
            province = MobileLocationUtils.locateMobileNumber(phoneNumber);
            if(StringUtils.isBlank(province)){
                province = "None";
            }
            redis.hset(MOBILE_LOCATION_CACHE_KEY, phoneNumber, province);
        }
        return province;
    }

    @Override
    public ChinaArea getAreaByPhoneNumber(String phoneNumber) {
        String province = getProvinceByPhoneNumber(phoneNumber);
        ChinaArea area = ProvinceUtils.getAreaByProvince(province);
        return area;
    }
}
