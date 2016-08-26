package com.tuotiansudai.activity.service;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.activity.dto.LuxuryPrizeDto;
import com.tuotiansudai.activity.dto.TravelPrizeDto;
import com.tuotiansudai.activity.dto.UserPrizePaginationItemDto;
import com.tuotiansudai.activity.repository.mapper.LuxuryPrizeMapper;
import com.tuotiansudai.activity.repository.mapper.TravelPrizeMapper;
import com.tuotiansudai.activity.repository.mapper.UserLuxuryPrizeMapper;
import com.tuotiansudai.activity.repository.mapper.UserTravelPrizeMapper;
import com.tuotiansudai.activity.repository.model.LuxuryPrizeModel;
import com.tuotiansudai.activity.repository.model.TravelPrizeModel;
import com.tuotiansudai.activity.repository.model.UserLuxuryPrizeModel;
import com.tuotiansudai.activity.repository.model.UserTravelPrizeModel;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.util.MobileEncoder;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
public class AutumnPrizeService {

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Autowired
    private TravelPrizeMapper travelPrizeMapper;

    @Autowired
    private LuxuryPrizeMapper luxuryPrizeMapper;

    @Autowired
    private UserTravelPrizeMapper userTravelPrizeMapper;

    @Autowired
    private UserLuxuryPrizeMapper userLuxuryPrizeMapper;

    @Autowired
    private InvestMapper investMapper;

    @Value(value = "activity.autumn.travel.invest")
    private String activityAutumnTravelInvestKey;

    @Value(value = "activity.autumn.luxury.invest")
    private String activityAutumnLuxuryInvestKey;

    public long getTodayInvestAmount(String loginName, String type) {
        String secondKey = MessageFormat.format("{0}:{1}", loginName, new DateTime().toString("yyyy-MM-dd"));
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

        List<String> ids = Lists.newArrayList(invests.split("\\|"));

        List<InvestModel> investModels = investMapper.findByIds(Lists.transform(ids, new Function<String, Long>() {
            @Override
            public Long apply(String input) {
                return Long.parseLong(input);
            }
        }));

        for (InvestModel investModel : investModels) {
            amount += investModel.getAmount();
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
