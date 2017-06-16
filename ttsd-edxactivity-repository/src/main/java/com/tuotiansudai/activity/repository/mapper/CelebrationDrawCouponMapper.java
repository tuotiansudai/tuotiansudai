package com.tuotiansudai.activity.repository.mapper;


import com.tuotiansudai.activity.repository.model.CelebrationDrawCouponModel;
import org.springframework.stereotype.Repository;

@Repository
public interface CelebrationDrawCouponMapper {

    void create(CelebrationDrawCouponModel celebrationDrawCouponModel);

    CelebrationDrawCouponModel findByLoginName(String loginName);

}
