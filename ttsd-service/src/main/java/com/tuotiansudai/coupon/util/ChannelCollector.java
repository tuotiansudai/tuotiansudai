package com.tuotiansudai.coupon.util;


import com.google.common.base.Strings;
import com.tuotiansudai.repository.mapper.CouponUserGroupMapper;
import com.tuotiansudai.repository.model.CouponModel;
import com.tuotiansudai.repository.model.CouponUserGroupModel;
import com.tuotiansudai.repository.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChannelCollector implements UserCollector {

    @Autowired
    private CouponUserGroupMapper couponUserGroupMapper;

    @Override
    public boolean contains(CouponModel couponModel, UserModel userModel) {
        //该优惠券如果指定发给渠道用户,那么只发放给优惠券在活动期限内 且 来源渠道的用户注册时间也在活动期限内
        if (couponModel == null
                || userModel == null
                || Strings.isNullOrEmpty(userModel.getChannel())
                || userModel.getRegisterTime().before(couponModel.getStartTime())
                || userModel.getRegisterTime().after(couponModel.getEndTime())) {
            return false;
        }

        CouponUserGroupModel couponUserGroupModel = couponUserGroupMapper.findByCouponId(couponModel.getId());
        return couponUserGroupModel != null
                && couponUserGroupModel.getUserGroupItems().contains(userModel.getChannel());
    }
}
