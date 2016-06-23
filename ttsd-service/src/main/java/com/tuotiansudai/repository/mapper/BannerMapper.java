package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.BannerModel;
import org.springframework.stereotype.Repository;

@Repository
public interface BannerMapper {

    void create(BannerModel bannerModel);

}
