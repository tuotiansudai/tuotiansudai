package com.tuotiansudai.service.impl;


import com.tuotiansudai.dto.BannerDto;
import com.tuotiansudai.repository.mapper.BannerMapper;
import com.tuotiansudai.repository.model.BannerModel;
import com.tuotiansudai.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class BannerServiceImpl implements BannerService{

    @Autowired
    private BannerMapper bannerMapper;

    @Override
    public void create(BannerDto bannerDto, String loginName) {
        BannerModel bannerModel = new BannerModel(bannerDto);
        bannerModel.setActive(true);
        bannerModel.setCreatedTime(new Date());
        bannerModel.setCreatedBy(loginName);
        bannerModel.setDeleted(false);
        bannerMapper.create(bannerModel);
    }

}
