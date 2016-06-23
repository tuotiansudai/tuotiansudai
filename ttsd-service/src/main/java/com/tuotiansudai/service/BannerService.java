package com.tuotiansudai.service;


import com.tuotiansudai.dto.BannerDto;

public interface BannerService {

    void create(BannerDto bannerDto, String loginName);

}
