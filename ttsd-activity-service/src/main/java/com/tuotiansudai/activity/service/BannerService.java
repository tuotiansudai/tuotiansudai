package com.tuotiansudai.activity.service;


import com.tuotiansudai.activity.repository.dto.BannerDto;
import com.tuotiansudai.activity.repository.mapper.BannerMapper;
import com.tuotiansudai.activity.repository.model.BannerModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BannerService {

    @Autowired
    private BannerMapper bannerMapper;

    public void create(BannerDto bannerDto, String loginName, String ip) {
        BannerModel bannerModel = new BannerModel(bannerDto);
        bannerModel.setCreatedTime(new Date());
        bannerModel.setCreatedBy(loginName);
        bannerModel.setActivatedBy(loginName);
        bannerModel.setDeleted(false);
        bannerModel.setAppUrl(bannerDto.getAppUrl().trim());
        bannerMapper.create(bannerModel);
    }

    public List<BannerModel> findAllBannerList() {
        return bannerMapper.findAllBannerList();
    }

    public boolean updateBanner(BannerModel bannerModel, String loginName, String ip) {
        return bannerMapper.updateBanner(bannerModel);
    }

    public BannerModel findById(long id) {
        return bannerMapper.findById(id);
    }
}
