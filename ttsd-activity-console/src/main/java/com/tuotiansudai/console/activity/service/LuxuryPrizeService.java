package com.tuotiansudai.console.activity.service;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.activity.repository.mapper.LuxuryPrizeMapper;
import com.tuotiansudai.activity.repository.mapper.UserLuxuryPrizeMapper;
import com.tuotiansudai.activity.repository.model.LuxuryPrizeModel;
import com.tuotiansudai.activity.repository.model.UserLuxuryPrizeModel;
import com.tuotiansudai.console.activity.dto.LuxuryPrizeDto;
import com.tuotiansudai.console.activity.dto.UserLuxuryPrizeDto;
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

    public BasePaginationDataDto<UserLuxuryPrizeDto> obtainUserLuxuryPrizeList(String mobile, Date startTime, Date endTime, Integer index, Integer pageSize) {
        if (startTime != null) {
            startTime = new DateTime(startTime).withTimeAtStartOfDay().toDate();
        }
        if (endTime != null) {
            endTime = new DateTime(endTime).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate();
        }

        long count = userLuxuryPrizeMapper.getCountUserLuxuryPrize(mobile, startTime, endTime);
        int totalPages = (int) (count % pageSize > 0 || count == 0 ? count / pageSize + 1 : count / pageSize);
        index = index > totalPages ? totalPages : index;
        List<UserLuxuryPrizeDto> items = Lists.newArrayList();
        List<UserLuxuryPrizeModel> userLuxuryPrizeModels = userLuxuryPrizeMapper.getUserLuxuryPrizeList(mobile, startTime, endTime, index, pageSize);
        if (count > 0) {
            items = Lists.transform(userLuxuryPrizeModels, new Function<UserLuxuryPrizeModel, UserLuxuryPrizeDto>() {
                @Override
                public UserLuxuryPrizeDto apply(UserLuxuryPrizeModel input) {
                    LuxuryPrizeModel luxuryPrizeModel = luxuryPrizeMapper.findByPrizeId(input.getPrizeId());
                    return new UserLuxuryPrizeDto(input);
                }
            });
        }

        BasePaginationDataDto basePaginationDataDto = new BasePaginationDataDto(index, pageSize, count, items);
        basePaginationDataDto.setStatus(true);
        return basePaginationDataDto;
    }

    public List<LuxuryPrizeDto> obtainLuxuryPrizeList() {
        List<LuxuryPrizeModel> luxuryPrizeModels = luxuryPrizeMapper.findAllLuxuryPrize();
        List<LuxuryPrizeDto> items = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(luxuryPrizeModels)) {
            items = Lists.transform(luxuryPrizeModels, new Function<LuxuryPrizeModel, LuxuryPrizeDto>() {
                @Override
                public LuxuryPrizeDto apply(LuxuryPrizeModel input) {
                    return new LuxuryPrizeDto(input);
                }
            });
        }
        return items;
    }

    public LuxuryPrizeDto obtainLuxuryPrizeDto(long luxuryPrizeId){
        LuxuryPrizeModel luxuryPrizeModel = luxuryPrizeMapper.findById(luxuryPrizeId);
        return new LuxuryPrizeDto(luxuryPrizeModel);

    }
    public void editLuxuryPrize(LuxuryPrizeDto luxuryPrizeDto,String loginName){
        long luxuryPrizeId = luxuryPrizeDto.getLuxuryPrizeId();
        LuxuryPrizeModel luxuryPrizeModel = luxuryPrizeMapper.findById(luxuryPrizeId);
        luxuryPrizeModel.setBrand(luxuryPrizeDto.getBrand());
        luxuryPrizeModel.setUpdatedTime(new Date());
        luxuryPrizeModel.setInvestAmount(AmountConverter.convertStringToCent(luxuryPrizeDto.getInvestAmount()));
        luxuryPrizeModel.setUpdatedBy(loginName);
        luxuryPrizeModel.setImage(luxuryPrizeDto.getImage());
        luxuryPrizeModel.setIntroduce(luxuryPrizeDto.getIntroduce());
        luxuryPrizeModel.setName(luxuryPrizeDto.getName());
        luxuryPrizeModel.setPrice(luxuryPrizeDto.getPrice());
        luxuryPrizeModel.setTenPercentOffInvestAmount(AmountConverter.convertStringToCent(luxuryPrizeDto.getTenPercentOffInvestAmount()));
        luxuryPrizeModel.setTwentyPercentOffInvestAmount(AmountConverter.convertStringToCent(luxuryPrizeDto.getTwentyPercentOffInvestAmount()));
        luxuryPrizeModel.setThirtyPercentOffInvestAmount(AmountConverter.convertStringToCent(luxuryPrizeDto.getThirtyPercentOffInvestAmount()));
        luxuryPrizeMapper.update(luxuryPrizeModel);

    }
}
