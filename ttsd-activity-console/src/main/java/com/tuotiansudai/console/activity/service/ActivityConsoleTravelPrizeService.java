package com.tuotiansudai.console.activity.service;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.activity.repository.dto.TravelPrizeDto;
import com.tuotiansudai.activity.repository.dto.UserPrizePaginationItemDto;
import com.tuotiansudai.activity.repository.model.TravelPrizeModel;
import com.tuotiansudai.activity.repository.model.UserTravelPrizeModel;
import com.tuotiansudai.activity.repository.mapper.TravelPrizeMapper;
import com.tuotiansudai.activity.repository.mapper.UserTravelPrizeMapper;
import com.tuotiansudai.activity.repository.dto.TravelPrizeRequestDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ActivityConsoleTravelPrizeService {

    @Autowired
    private TravelPrizeMapper travelPrizeMapper;

    @Autowired
    private UserTravelPrizeMapper userTravelPrizeMapper;

    public BaseDto<BasePaginationDataDto> getTravelAwardItems(String mobile, Date startTime, Date endTime, int index, int pageSize) {
        long count = userTravelPrizeMapper.countByPagination(mobile, startTime, endTime);
        List<UserTravelPrizeModel> models = userTravelPrizeMapper.findByPagination(mobile, startTime, endTime, PaginationUtil.calculateOffset(index, pageSize, count), pageSize);
        List<UserPrizePaginationItemDto> items = Lists.transform(models, new Function<UserTravelPrizeModel, UserPrizePaginationItemDto>() {
            @Override
            public UserPrizePaginationItemDto apply(UserTravelPrizeModel input) {
                return new UserPrizePaginationItemDto(input);
            }
        });

        BasePaginationDataDto<UserPrizePaginationItemDto> dataDto = new BasePaginationDataDto<>(PaginationUtil.validateIndex(index, pageSize, count), pageSize, count, items);
        dataDto.setStatus(true);
        return new BaseDto<>(dataDto);
    }

    public BaseDto<BasePaginationDataDto> getTravelPrizeItems() {
        List<TravelPrizeModel> models = travelPrizeMapper.findAll();
        List<TravelPrizeDto> items = Lists.transform(models, new Function<TravelPrizeModel, TravelPrizeDto>() {
            @Override
            public TravelPrizeDto apply(TravelPrizeModel input) {
                return new TravelPrizeDto(input);
            }
        });

        BasePaginationDataDto<TravelPrizeDto> dataDto = new BasePaginationDataDto<>(1, 10, 3, items);

        dataDto.setStatus(true);
        return new BaseDto<>(dataDto);
    }

    public TravelPrizeDto getTravelPrize(long id) {
        TravelPrizeModel model = travelPrizeMapper.findById(id);

        return new TravelPrizeDto(model);
    }

    public void update(String loginName, TravelPrizeRequestDto dto) {
        TravelPrizeModel model = travelPrizeMapper.findById(dto.getId());
        model.setName(dto.getName());
        model.setPrice(dto.getPrice());
        model.setImage(dto.getImage());
        model.setIntroduce(dto.getIntroduce());
        model.setUpdatedBy(loginName);
        model.setUpdatedTime(new Date());
        model.setIntroduce(dto.getIntroduce());
        model.setInvestAmount(AmountConverter.convertStringToCent(dto.getInvestAmount()));
        travelPrizeMapper.update(model);
    }
}
