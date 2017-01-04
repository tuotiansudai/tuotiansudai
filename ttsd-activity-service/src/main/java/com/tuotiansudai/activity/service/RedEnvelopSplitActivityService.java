package com.tuotiansudai.activity.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.activity.repository.dto.RedEnvelopSplitActivityDto;
import com.tuotiansudai.activity.repository.dto.RedEnvelopSplitReferrerDto;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.enums.AppUrl;
import com.tuotiansudai.repository.mapper.PrepareUserMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.PrepareUserModel;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.UserChannel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.JsonConverter;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RedEnvelopSplitActivityService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private PrepareUserMapper prepareUserMapper;

    private static List<Long> coupons = Lists.newArrayList(333L, 334L, 335L, 336L, 337L, 338L);

    @Value("${web.server}")
    private String domainName;

    private static String REFERRER_TITLE = "您的好友%s送你三重好礼";

    private static String REFERRER_DESCRIPTION = "完成注册即可领取8.88元现金红包+5888元体验金+588元优惠券";

    private static Integer DEFAULT_PAGE_SIZE = 10;

    @Value("#{'${activity.weiXin.red.envelop.period}'.split('\\~')}")
    private List<String> weiXinPeriod = Lists.newArrayList();

    public int getReferrerCount(String loginName) {
        if(Strings.isNullOrEmpty(loginName)){
            return 0;
        }
        Date startTime = DateTime.parse(weiXinPeriod.get(0), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        Date endTime = DateTime.parse(weiXinPeriod.get(1), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        return userMapper.findUserModelByChannel(loginName, Arrays.asList(UserChannel.values()), startTime, endTime, null).size();
    }

    public String getReferrerRedEnvelop(String loginName) {
        if(Strings.isNullOrEmpty(loginName)){
            return "";
        }
        return AmountConverter.convertCentToString(userCouponMapper.findSumRedEnvelopeByLoginNameAndCouponId(loginName, coupons));
    }

    public String getShareReferrerUrl(String loginName) {
        if(Strings.isNullOrEmpty(loginName)){
            return "";
        }

        UserModel userModel = userMapper.findByLoginName(loginName);
        RedEnvelopSplitActivityDto redEnvelopSplitActivityDto = new RedEnvelopSplitActivityDto(String.format(REFERRER_TITLE, userModel.getUserName()),
                REFERRER_DESCRIPTION, domainName + "activity/red-envelop-split/referrer");
        String paramJson = "";
        try {
            paramJson = JsonConverter.writeValueAsString(redEnvelopSplitActivityDto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        String base64 = "";
        try {
            base64 = Base64.getEncoder().encodeToString(paramJson.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return String.format(AppUrl.RED_ENVELOP_SPLIT.getPath(), base64);
    }

    public void beforeRegisterUser(String loginName, String referrerMobile, String channel){

        if(prepareUserMapper.findByMobile(referrerMobile) != null){
            return;
        }

        UserModel userModel = userMapper.findByLoginName(loginName);
        PrepareUserModel prepareUserModel = new PrepareUserModel();
        prepareUserModel.setReferrerLoginName(loginName);
        prepareUserModel.setReferrerMobile(userModel != null ? userModel.getMobile() : null);
        prepareUserModel.setMobile(referrerMobile);
        prepareUserModel.setCreatedTime(new Date());
        prepareUserModel.setChannel(channel);
        prepareUserMapper.create(prepareUserModel);
    }

    public List<RedEnvelopSplitReferrerDto> getReferrerList(String loginName){
        Date startTime = DateTime.parse(weiXinPeriod.get(0), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        Date endTime = DateTime.parse(weiXinPeriod.get(1), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        List<UserModel> userModels = userMapper.findUserModelByChannel(loginName, Lists.newArrayList(UserChannel.values()), startTime, endTime, DEFAULT_PAGE_SIZE);
        return userModels.stream().map(userModel -> new RedEnvelopSplitReferrerDto(userModel.getMobile(), userModel.getRegisterTime())).collect(Collectors.toList());
    }

}
