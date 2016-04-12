package com.ttsd.special.services;

import com.ttsd.util.ChinaArea;

public interface CachedMobileLocationService {
    public String getProvinceByPhoneNumber(String phoneNumber);

    public ChinaArea getAreaByPhoneNumber(String phoneNumber);
}
