package com.tuotiansudai.activity.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.activity.repository.dto.RedEnvelopSplitActivityDto;
import com.tuotiansudai.activity.repository.dto.RedEnvelopSplitReferrerDto;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.coupon.repository.model.UserGroup;
import com.tuotiansudai.coupon.util.UserCollector;
import com.tuotiansudai.enums.AppUrl;
import com.tuotiansudai.repository.mapper.PrepareUserMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.PrepareUserModel;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.UserChannel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.JsonConverter;
import com.tuotiansudai.util.MobileEncryptor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RedEnvelopSplitActivityService {

    private final static Logger logger = Logger.getLogger(RedEnvelopSplitActivityService.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private PrepareUserMapper prepareUserMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Value("${web.server}")
    private String domainName;

    private static List<Long> coupons = Lists.newArrayList(333L, 334L, 335L, 336L, 337L, 338L);

    private static String REFERRER_TITLE = "您的好友%s送你三重好礼";

    private static String REFERRER_DESCRIPTION = "完成注册即可领取8.88元现金红包+5888元体验金+588元优惠券";

    private static Integer DEFAULT_PAGE_SIZE = 10;

    @Value("#{'${activity.weiXin.red.envelop.period}'.split('\\~')}")
    private List<String> weiXinPeriod = Lists.newArrayList();

    public int getReferrerCount(String loginName) {
        if (Strings.isNullOrEmpty(loginName)) {
            return 0;
        }
        Date startTime = DateTime.parse(weiXinPeriod.get(0), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        Date endTime = DateTime.parse(weiXinPeriod.get(1), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();

        return getReferrerCount(loginName, startTime, endTime).size();
    }

    public List<UserModel> getReferrerCount(String loginName, Date startTime, Date endTime) {
        if(Strings.isNullOrEmpty(loginName)){
            return Lists.newArrayList();
        }
        List<UserModel> userModels = userMapper.findUsersByRegisterTimeOrReferrer(startTime, endTime, loginName);
        return userModels.stream().filter(userModel -> asUserChannel(userModel.getChannel())).collect(Collectors.toList());
    }

    public String getReferrerRedEnvelop(int referrerCount) {
        long sumAmount = 0l;

        if(referrerCount >= 1){
            sumAmount += 188l;
        }

        if(referrerCount >= 2){
            sumAmount += 588l;
        }

        if(referrerCount >= 3){
            sumAmount += 988l;
        }

        if(referrerCount >= 7){
            sumAmount += 1388l;
        }

        if(referrerCount >= 10){
            sumAmount += 2088l;
        }

        return AmountConverter.convertCentToString(sumAmount);
    }

    public String getSumRedEnvelopByLoginName(String loginName, List<Long> couponIds){
        long sumAmount = 0l;
        List<Long> couponAmounts = couponIds.stream().filter(id -> CollectionUtils.isNotEmpty(userCouponMapper.findByLoginNameAndCouponId(loginName, id)))
                .map(id -> couponMapper.findById(id).getAmount()).collect(Collectors.toList());
        for(Long amount : couponAmounts){
            sumAmount += amount;
        }
        return AmountConverter.convertCentToString(sumAmount);
    }

    public String getShareReferrerUrl(String loginName) {
        if (Strings.isNullOrEmpty(loginName)) {
            return AppUrl.LOGIN.getPath();
        }

        UserModel userModel = userMapper.findByLoginName(loginName);
        RedEnvelopSplitActivityDto redEnvelopSplitActivityDto = new RedEnvelopSplitActivityDto(String.format(REFERRER_TITLE, userModel.getUserName() == null ? "" : userModel.getUserName()),
                REFERRER_DESCRIPTION, domainName + "/activity/red-envelop-split/referrer");

        logger.info(MessageFormat.format("[redEnvelopSplit] shard url:{0}", redEnvelopSplitActivityDto.getShareUrl()));
        String paramJson = "";
        String base64 = "";
        try {
            paramJson = JsonConverter.writeValueAsString(redEnvelopSplitActivityDto);
            base64 = Base64.getEncoder().encodeToString(paramJson.getBytes("utf-8"));
        } catch (JsonProcessingException e) {
            logger.info(MessageFormat.format("[redEnvelopSplit] json converter fail, userName:{0}, shardUrl:{1}", userModel.getUserName(), redEnvelopSplitActivityDto.getShareUrl()));
        } catch (UnsupportedEncodingException e) {
            logger.info(MessageFormat.format("[redEnvelopSplit] base64 converter fail, json:{0}", paramJson));
        }

        return String.format(AppUrl.SHARE.getPath(), base64);
    }

    public void beforeRegisterUser(String loginName, String referrerMobile, String channel) {

        if (prepareUserMapper.findByMobile(referrerMobile) != null) {
            return;
        }

        UserModel userModel = userMapper.findByLoginName(loginName);
        PrepareUserModel prepareUserModel = new PrepareUserModel();
        prepareUserModel.setReferrerLoginName(loginName);
        prepareUserModel.setReferrerMobile(userModel != null ? userModel.getMobile() : null);
        prepareUserModel.setMobile(referrerMobile);
        prepareUserModel.setCreatedTime(new Date());
        prepareUserModel.setChannel(Source.MOBILE);
        prepareUserModel.setRegisterChannel(UserChannel.valueOf(channel));
        prepareUserMapper.create(prepareUserModel);
    }

    public List<RedEnvelopSplitReferrerDto> getReferrerList(String loginName) {
        Date startTime = DateTime.parse(weiXinPeriod.get(0), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        Date endTime = DateTime.parse(weiXinPeriod.get(1), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();

        List<UserModel> referrerUsers = getReferrerCount(loginName, startTime, endTime);
        if (referrerUsers.size() > DEFAULT_PAGE_SIZE) {
            referrerUsers.subList(0, DEFAULT_PAGE_SIZE);
        }
        return referrerUsers.stream().map(userModel -> new RedEnvelopSplitReferrerDto(MobileEncryptor.encryptWebMiddleMobile(userModel.getMobile()), userModel.getRegisterTime())).collect(Collectors.toList());
    }

    public static boolean asUserChannel(String str) {
        for (UserChannel me : UserChannel.values()) {
            if (me.name().equalsIgnoreCase(str))
                return true;
        }
        return false;
    }

}
