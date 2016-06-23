package com.tuotiansudai.service;


import com.tuotiansudai.dto.BannerDto;

import java.util.List;

public interface BannerService {

    void create(BannerDto bannerDto, String loginName);

    List<BannerDto> findBannerList(int index, int pageSize);

    int  findBannerCount();
}
