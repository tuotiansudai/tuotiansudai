package com.tuotiansudai.activity.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.activity.repository.dto.RedEnvelopSplitActivityDto;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.enums.AppUrl;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserChannel;
import com.tuotiansudai.util.AmountConverter;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class RedEnvelopSplitActivityService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    private static List<Long> coupons = Lists.newArrayList(333L, 334L, 335L, 336L, 337L, 338L);

    private static String REFERRER_TITLE = "您的好友%s送你三重好礼";

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

    public String getShareReferrerUrl(String loginName){
        RedEnvelopSplitActivityDto redEnvelopSplitActivityDto = new RedEnvelopSplitActivityDto(REFERRER_TITLE, REFERRER_DESCRIPTION, "");
        StringBuffer url = new StringBuffer(AppUrl.RED_ENVELOP_SPLIT.name());


        return url.toString();
    }

}
