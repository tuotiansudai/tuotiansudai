package com.tuotiansudai.service;


import com.tuotiansudai.dto.BannerDto;
import com.tuotiansudai.repository.model.BannerModel;

import java.util.List;

public interface BannerService {

    void create(BannerDto bannerDto, String loginName, String ip);

    List<BannerModel> findAllBannerList();

    boolean updateBanner(BannerModel bannerModel, String loginName, String ip);

    BannerModel findById(long id);
}
