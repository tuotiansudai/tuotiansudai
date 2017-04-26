package com.tuotiansudai.activity.service;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.activity.repository.dto.LuxuryPrizeDto;
import com.tuotiansudai.activity.repository.dto.TravelPrizeDto;
import com.tuotiansudai.activity.repository.dto.UserPrizePaginationItemDto;
import com.tuotiansudai.activity.repository.mapper.LuxuryPrizeMapper;
import com.tuotiansudai.activity.repository.mapper.TravelPrizeMapper;
import com.tuotiansudai.activity.repository.mapper.UserLuxuryPrizeMapper;
import com.tuotiansudai.activity.repository.mapper.UserTravelPrizeMapper;
import com.tuotiansudai.activity.repository.model.LuxuryPrizeModel;
import com.tuotiansudai.activity.repository.model.TravelPrizeModel;
import com.tuotiansudai.activity.repository.model.UserLuxuryPrizeModel;
import com.tuotiansudai.activity.repository.model.UserTravelPrizeModel;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.util.MobileEncoder;
import com.tuotiansudai.util.RedisWrapperClient;
import org.apache.commons.collections4.CollectionUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
public class AutumnPrizeService {

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    @Autowired
    private TravelPrizeMapper travelPrizeMapper;

    @Autowired
    private LuxuryPrizeMapper luxuryPrizeMapper;

    @Autowired
    private UserTravelPrizeMapper userTravelPrizeMapper;

    @Autowired
    private UserLuxuryPrizeMapper userLuxuryPrizeMapper;

    @Value(value = "activity.autumn.travel.invest")
    private String activityAutumnTravelInvestKey;

    @Value(value = "activity.autumn.luxury.invest")
    private String activityAutumnLuxuryInvestKey;

    @Autowired
    private UserMapper userMapper;

    public long getTodayInvestAmount(String loginName, String type) {
        UserModel userModel = userMapper.findByLoginName(loginName);
        String mobile = userModel != null ? userModel.getMobile() : "";
        String userName = userModel != null ? userModel.getUserName() : "";
        String secondKey = MessageFormat.format("{0}:{1}:{2}:{3}", loginName, mobile, userName, new DateTime().toString("yyyy-MM-dd"));
        String invests = null;
        if ("travel".equalsIgnoreCase(type)) {
            invests = redisWrapperClient.hget(this.activityAutumnTravelInvestKey, secondKey);
        }
        if ("luxury".equalsIgnoreCase(type)) {
            invests = redisWrapperClient.hget(this.activityAutumnLuxuryInvestKey, secondKey);
        }

        long amount = 0;
        if (Strings.isNullOrEmpty(invests)) {
            return amount;
        }
        List<String> investDetail = Lists.newArrayList(invests.split("\\|"));
        if (CollectionUtils.isNotEmpty(investDetail) && investDetail.size() >= 1) {
            return Long.parseLong(investDetail.get(0));
        }
        return amount;
    }

    public List<TravelPrizeDto> getTravelPrizeItems() {
        List<TravelPrizeModel> models = travelPrizeMapper.findAll();
        return Lists.transform(models, new Function<TravelPrizeModel, TravelPrizeDto>() {
            @Override
            public TravelPrizeDto apply(TravelPrizeModel input) {
                return new TravelPrizeDto(input);
            }
        });
    }

    public List<LuxuryPrizeDto> getLuxuryPrizeItems() {
        List<LuxuryPrizeModel> models = luxuryPrizeMapper.findAll();
        return Lists.transform(models, new Function<LuxuryPrizeModel, LuxuryPrizeDto>() {
            @Override
            public LuxuryPrizeDto apply(LuxuryPrizeModel input) {
                return new LuxuryPrizeDto(input);
            }
        });
    }

    public List<UserPrizePaginationItemDto> getTravelAwardItems(final String loginName) {
        List<UserTravelPrizeModel> models = userTravelPrizeMapper.findByPagination(null, null, null, null, null);
        return Lists.transform(models, new Function<UserTravelPrizeModel, UserPrizePaginationItemDto>() {
            @Override
            public UserPrizePaginationItemDto apply(UserTravelPrizeModel input) {
                if (!input.getLoginName().equalsIgnoreCase(loginName)) {
                    input.setMobile(MobileEncoder.encode(input.getMobile()));
                }
                return new UserPrizePaginationItemDto(input);
            }
        });
    }

    public List<UserPrizePaginationItemDto> getLuxuryAwardItems(final String loginName) {
        List<UserLuxuryPrizeModel> models = userLuxuryPrizeMapper.findByPagination(null, null, null, null, null);
        return Lists.transform(models, new Function<UserLuxuryPrizeModel, UserPrizePaginationItemDto>() {
            @Override
            public UserPrizePaginationItemDto apply(UserLuxuryPrizeModel input) {
                if (!input.getLoginName().equalsIgnoreCase(loginName)) {
                    input.setMobile(MobileEncoder.encode(input.getMobile()));
                }
                return new UserPrizePaginationItemDto(input);
            }
        });
    }

    public List<UserPrizePaginationItemDto> getMyTravelAwardItems(final String mobile) {
        List<UserTravelPrizeModel> models = userTravelPrizeMapper.findByPagination(mobile, null, null, null, null);
        return Lists.transform(models, new Function<UserTravelPrizeModel, UserPrizePaginationItemDto>() {
            @Override
            public UserPrizePaginationItemDto apply(UserTravelPrizeModel input) {
                return new UserPrizePaginationItemDto(input);
            }
        });
    }

    public List<UserPrizePaginationItemDto> getMyLuxuryAwardItems(final String mobile) {
        List<UserLuxuryPrizeModel> models = userLuxuryPrizeMapper.findByPagination(mobile, null, null, null, null);
        return Lists.transform(models, new Function<UserLuxuryPrizeModel, UserPrizePaginationItemDto>() {
            @Override
            public UserPrizePaginationItemDto apply(UserLuxuryPrizeModel input) {
                return new UserPrizePaginationItemDto(input);
            }
        });
    }

}
