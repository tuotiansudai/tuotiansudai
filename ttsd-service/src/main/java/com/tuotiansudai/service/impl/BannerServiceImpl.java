package com.tuotiansudai.service.impl;


import com.tuotiansudai.dto.BannerDto;
import com.tuotiansudai.repository.mapper.BannerMapper;
import com.tuotiansudai.repository.model.BannerModel;
import com.tuotiansudai.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BannerServiceImpl implements BannerService{

    @Autowired
    private BannerMapper bannerMapper;

    @Override
    public void create(BannerDto bannerDto, String loginName, String ip) {
        BannerModel bannerModel = new BannerModel(bannerDto);
        bannerModel.setActive(true);
        bannerModel.setCreatedTime(new Date());
        bannerModel.setCreatedBy(loginName);
        bannerModel.setActivatedBy(loginName);
        bannerModel.setActivatedTime(new Date());
        bannerModel.setDeleted(false);
        bannerMapper.create(bannerModel);
    }

    @Override
    public List<BannerModel> findAllBannerList(){
        return bannerMapper.findAllBannerList();
    }

    @Override
    public boolean updateBanner(BannerModel bannerModel, String loginName, String ip){
        return bannerMapper.updateBanner(bannerModel);
    }

    @Override
    public BannerModel findById(long id){
        return bannerMapper.findById(id);
    }
}
