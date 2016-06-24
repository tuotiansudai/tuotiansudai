package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.dto.BannerDto;
import com.tuotiansudai.repository.model.BannerModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BannerMapper {

    void create(BannerModel bannerModel);

    List<BannerDto> findBannerList(@Param(value = "index") int index,
                                   @Param(value = "pageSize") int pageSize);

    int findBannerCount();

    boolean updateBanner(BannerModel bannerModel);

    BannerModel findById(long id);

}
