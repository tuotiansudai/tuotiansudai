package com.tuotiansudai.activity.repository.mapper;

import com.tuotiansudai.activity.repository.model.BannerModel;
import com.tuotiansudai.repository.model.Source;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BannerMapper {

    void create(BannerModel bannerModel);

    List<BannerModel> findBannerIsAuthenticatedOrderByOrder(@Param(value = "authenticated") boolean authenticated,
                                                            @Param(value = "source") Source source,
                                                            @Param(value = "currentTime") Date currentTime);

    List<BannerModel> findAllBannerList();

    boolean updateBanner(BannerModel bannerModel);

    BannerModel findById(long id);
}
