package com.tuotiansudai.console.activity.service;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.activity.repository.mapper.UserTravelPrizeMapper;
import com.tuotiansudai.activity.repository.model.UserTravelPrizeModel;
import com.tuotiansudai.console.activity.dto.UserTravelPrizePaginationItemDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TravelPrizeService {

    @Autowired
    private UserTravelPrizeMapper userTravelPrizeMapper;

    public BaseDto<BasePaginationDataDto> getTravelAwardItems(String mobile, Date startTime, Date endTime, int index, int pageSize) {
        long count = userTravelPrizeMapper.countByPagination(mobile, startTime, endTime);
        List<UserTravelPrizeModel> models = userTravelPrizeMapper.findByPagination(mobile, startTime, endTime, PaginationUtil.calculateOffset(index, pageSize, count), pageSize);
        List<UserTravelPrizePaginationItemDto> items = Lists.transform(models, new Function<UserTravelPrizeModel, UserTravelPrizePaginationItemDto>() {
            @Override
            public UserTravelPrizePaginationItemDto apply(UserTravelPrizeModel input) {
                return new UserTravelPrizePaginationItemDto(input);
            }
        });

        BasePaginationDataDto<UserTravelPrizePaginationItemDto> dataDto = new BasePaginationDataDto<>(PaginationUtil.validateIndex(index, pageSize, count), pageSize, count, items);
        dataDto.setStatus(true);
        return new BaseDto<BasePaginationDataDto>(dataDto);
    }
}
