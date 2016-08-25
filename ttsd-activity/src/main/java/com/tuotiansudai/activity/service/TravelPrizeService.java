package com.tuotiansudai.activity.service;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.activity.dto.TravelPrizeDto;
import com.tuotiansudai.activity.dto.UserTravelPrizePaginationItemDto;
import com.tuotiansudai.activity.repository.mapper.TravelPrizeMapper;
import com.tuotiansudai.activity.repository.mapper.UserTravelPrizeMapper;
import com.tuotiansudai.activity.repository.model.TravelPrizeModel;
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
public class TravelPrizeService {

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Autowired
    private TravelPrizeMapper travelPrizeMapper;

    @Autowired
    private UserTravelPrizeMapper userTravelPrizeMapper;

    @Autowired
    private InvestMapper investMapper;

    @Value(value = "activity.autumn.travel.invest")
    private String activityAutumnTravelInvestKey;

    @Value(value = "activity.autumn.luxury.invest")
    private String activityAutumnLuxuryInvestKey;

    public long getTodayTravelInvestAmount(String loginName) {
        String secondKey = MessageFormat.format("{0}:{1}", loginName, new DateTime().toString("yyyy-MM-dd"));
        String invests = redisWrapperClient.hget(this.activityAutumnTravelInvestKey, secondKey);
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

    public List<UserTravelPrizePaginationItemDto> getTravelAwardItems(final String loginName) {
        List<UserTravelPrizeModel> models = userTravelPrizeMapper.findByPagination(null, null, null, null, null);

        return Lists.transform(models, new Function<UserTravelPrizeModel, UserTravelPrizePaginationItemDto>() {
            @Override
            public UserTravelPrizePaginationItemDto apply(UserTravelPrizeModel input) {
                if (!input.getLoginName().equalsIgnoreCase(loginName)) {
                    input.setMobile(MobileEncoder.encode(input.getMobile()));
                }
                return new UserTravelPrizePaginationItemDto(input);
            }
        });
    }

    public List<UserTravelPrizePaginationItemDto> getMyTravelAwardItems(final String mobile) {
        List<UserTravelPrizeModel> models = userTravelPrizeMapper.findByPagination(mobile, null, null, null, null);
        return Lists.transform(models, new Function<UserTravelPrizeModel, UserTravelPrizePaginationItemDto>() {
            @Override
            public UserTravelPrizePaginationItemDto apply(UserTravelPrizeModel input) {
                return new UserTravelPrizePaginationItemDto(input);
            }
        });
    }

}
