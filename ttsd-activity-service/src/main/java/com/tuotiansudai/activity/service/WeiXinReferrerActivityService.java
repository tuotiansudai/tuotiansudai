package com.tuotiansudai.activity.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.coupon.repository.model.UserCouponView;
import com.tuotiansudai.enums.AppUrl;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserChannel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.util.AmountConverter;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class WeiXinReferrerActivityService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    private static List<Long> coupons = Lists.newArrayList(333L, 334L, 335L, 336L, 337L, 338L);

    private static String REFERRER_DESCRIPTION = "完成注册即可领取8.88元现金红包+5888元体验金+588元优惠券";

    @Value("#{'${activity.weiXin.red.envelop.period}'.split('\\~')}")
    private List<String> weiXinPeriod = Lists.newArrayList();

    public int getReferrerCount(String loginName){
        Date startTime = DateTime.parse(weiXinPeriod.get(0), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        Date endTime = DateTime.parse(weiXinPeriod.get(1), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        return userMapper.findUserModelByChannel(loginName, Arrays.asList(UserChannel.values()), startTime, endTime).size();
    }

    public String getReferrerRedEnvelop(String loginName){
        return AmountConverter.convertCentToString(userCouponMapper.findSumRedEnvelopeByLoginNameAndCouponId(loginName, coupons));
    }

    public String getShardReferrerurl(){
        StringBuffer url = new StringBuffer(AppUrl.WEIXIN_REFERRER.name());


        return url.toString();
    }

}
