package com.tuotiansudai.service;


import com.tuotiansudai.dto.BannerDto;
import com.tuotiansudai.repository.model.BannerModel;

import java.util.List;

public interface BannerService {

    void create(BannerDto bannerDto, String loginName);

    List<BannerDto> findBannerList(int index, int pageSize);

    int  findBannerCount();

    boolean updateBanner(BannerModel bannerModel);

    BannerModel findById(long id);
}
