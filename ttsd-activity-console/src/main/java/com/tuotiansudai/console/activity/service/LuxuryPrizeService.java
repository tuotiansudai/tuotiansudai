package com.tuotiansudai.console.activity.service;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.activity.dto.LuxuryPrizeDto;
import com.tuotiansudai.activity.dto.UserPrizePaginationItemDto;
import com.tuotiansudai.activity.repository.mapper.LuxuryPrizeMapper;
import com.tuotiansudai.activity.repository.mapper.UserLuxuryPrizeMapper;
import com.tuotiansudai.activity.repository.model.LuxuryPrizeModel;
import com.tuotiansudai.activity.repository.model.UserLuxuryPrizeModel;
import com.tuotiansudai.console.activity.dto.LuxuryPrizeRequestDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.util.AmountConverter;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

@Service
public class LuxuryPrizeService {
    @Autowired
    private UserLuxuryPrizeMapper userLuxuryPrizeMapper;
    @Autowired
    private LuxuryPrizeMapper luxuryPrizeMapper;

    public BaseDto<BasePaginationDataDto> obtainUserLuxuryPrizeList(String mobile, Date startTime, Date endTime, Integer index, Integer pageSize) {
        if (startTime != null) {
            startTime = new DateTime(startTime).withTimeAtStartOfDay().toDate();
        }
        if (endTime != null) {
            endTime = new DateTime(endTime).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate();
        }

        long count = userLuxuryPrizeMapper.countByPagination(mobile, startTime, endTime);
        int totalPages = (int) (count % pageSize > 0 || count == 0 ? count / pageSize + 1 : count / pageSize);
        index = index > totalPages ? totalPages : index;
        List<UserPrizePaginationItemDto> items = Lists.newArrayList();
        List<UserLuxuryPrizeModel> userLuxuryPrizeModels = userLuxuryPrizeMapper.findByPagination(mobile, startTime, endTime, (index-1) * pageSize, pageSize);
        if (count > 0) {
            items = Lists.transform(userLuxuryPrizeModels, new Function<UserLuxuryPrizeModel, UserPrizePaginationItemDto>() {
                @Override
                public UserPrizePaginationItemDto apply(UserLuxuryPrizeModel input) {
                    return new UserPrizePaginationItemDto(input);
                }
            });
        }

        BasePaginationDataDto basePaginationDataDto = new BasePaginationDataDto(index, pageSize, count, items);
        basePaginationDataDto.setStatus(true);
        BaseDto<BasePaginationDataDto> baseDto = new BaseDto<BasePaginationDataDto>();
        baseDto.setData(basePaginationDataDto);
        return baseDto;
    }

    public BaseDto<BasePaginationDataDto> obtainLuxuryPrizeList() {
        List<LuxuryPrizeModel> luxuryPrizeModels = luxuryPrizeMapper.findAll();
        List<LuxuryPrizeDto> items = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(luxuryPrizeModels)) {
            items = Lists.transform(luxuryPrizeModels, new Function<LuxuryPrizeModel, LuxuryPrizeDto>() {
                @Override
                public LuxuryPrizeDto apply(LuxuryPrizeModel input) {
                    return new LuxuryPrizeDto(input);
                }
            });
        }
        BasePaginationDataDto<LuxuryPrizeDto> dataDto = new BasePaginationDataDto<>(1, 10, 3, items);

        dataDto.setStatus(true);
        return new BaseDto<BasePaginationDataDto>(dataDto);

    }

    public LuxuryPrizeDto obtainLuxuryPrizeDto(long luxuryPrizeId){
        LuxuryPrizeModel luxuryPrizeModel = luxuryPrizeMapper.findById(luxuryPrizeId);
        return new LuxuryPrizeDto(luxuryPrizeModel);

    }
    public void editLuxuryPrize(LuxuryPrizeRequestDto luxuryPrizeRequestDto,String loginName){
        long luxuryPrizeId = luxuryPrizeRequestDto.getLuxuryPrizeId();
        LuxuryPrizeModel luxuryPrizeModel = luxuryPrizeMapper.findById(luxuryPrizeId);
        luxuryPrizeModel.setBrand(luxuryPrizeRequestDto.getBrand());
        luxuryPrizeModel.setUpdatedTime(new Date());
        luxuryPrizeModel.setInvestAmount(AmountConverter.convertStringToCent(luxuryPrizeRequestDto.getInvestAmount()));
        luxuryPrizeModel.setUpdatedBy(loginName);
        luxuryPrizeModel.setImage(luxuryPrizeRequestDto.getImage());
        luxuryPrizeModel.setIntroduce(luxuryPrizeRequestDto.getIntroduce());
        luxuryPrizeModel.setName(luxuryPrizeRequestDto.getName());
        luxuryPrizeModel.setPrice(luxuryPrizeRequestDto.getPrice());
        luxuryPrizeModel.setTenPercentOffInvestAmount(AmountConverter.convertStringToCent(luxuryPrizeRequestDto.getTenPercentOffInvestAmount()));
        luxuryPrizeModel.setTwentyPercentOffInvestAmount(AmountConverter.convertStringToCent(luxuryPrizeRequestDto.getTwentyPercentOffInvestAmount()));
        luxuryPrizeModel.setThirtyPercentOffInvestAmount(AmountConverter.convertStringToCent(luxuryPrizeRequestDto.getThirtyPercentOffInvestAmount()));
        luxuryPrizeMapper.update(luxuryPrizeModel);

    }
}
