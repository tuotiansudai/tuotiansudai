package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.BannerModel;
import com.tuotiansudai.repository.model.Source;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BannerMapper {

    void create(BannerModel bannerModel);

    List<BannerModel> findBannerIsAuthenticatedOrderByOrder(@Param(value = "authenticated") boolean authenticated,
                                                            @Param(value = "source") Source source);

    List<BannerModel> findAllBannerList();

    boolean updateBanner(BannerModel bannerModel);

    BannerModel findById(long id);
}
