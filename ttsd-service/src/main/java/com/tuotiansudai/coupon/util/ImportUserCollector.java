package com.tuotiansudai.coupon.util;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
public class ImportUserCollector implements UserCollector {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    private final static String IMPORT_COUPON_USER_REDIS_KEY_TEMPLATE = "console:{0}:importcouponuser";

    @Override
    public List<String> collect(long couponId) {
        String values = redisWrapperClient.hget(MessageFormat.format(IMPORT_COUPON_USER_REDIS_KEY_TEMPLATE, String.valueOf(couponId)), "success");
        if (Strings.isNullOrEmpty(values)) {
            return Lists.newArrayList();
        }

        return Lists.transform(Lists.newArrayList(values.split(",")), new Function<String, String>() {
            @Override
            public String apply(String value) {
                UserModel userModel = userMapper.findByLoginNameOrMobile(value);
                return userModel.getLoginName();
            }
        });
    }

    @Override
    public long count(long couponId) {
        String values = redisWrapperClient.hget(MessageFormat.format(IMPORT_COUPON_USER_REDIS_KEY_TEMPLATE, String.valueOf(couponId)), "success");
        if (Strings.isNullOrEmpty(values)) {
            return 0;
        }

        return values.split(",").length;
    }

    @Override
    public boolean contains(long couponId, String loginName) {
        String values = redisWrapperClient.hget(MessageFormat.format(IMPORT_COUPON_USER_REDIS_KEY_TEMPLATE, String.valueOf(couponId)), "success");

        return !Strings.isNullOrEmpty(values) && Lists.transform(Lists.newArrayList(values.split(",")), new Function<String, String>() {
            @Override
            public String apply(String value) {
                UserModel userModel = userMapper.findByLoginNameOrMobile(value);
                return userModel.getLoginName();
            }
        }).contains(loginName);
    }
}
