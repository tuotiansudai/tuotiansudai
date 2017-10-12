package com.tuotiansudai.console.activity.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.activity.repository.mapper.ZeroShoppingPrizeConfigMapper;
import com.tuotiansudai.activity.repository.mapper.ZeroShoppingPrizeSelectMapper;
import com.tuotiansudai.activity.repository.model.ZeroShoppingPrize;
import com.tuotiansudai.activity.repository.model.ZeroShoppingPrizeConfigModel;
import com.tuotiansudai.activity.repository.model.ZeroShoppingPrizeSelectModel;
import com.tuotiansudai.activity.repository.model.ZeroShoppingPrizeSelectView;
import com.tuotiansudai.dto.BasePaginationDataDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActivityConsoleZeroShoppingService {

    @Autowired
    private ZeroShoppingPrizeConfigMapper zeroShoppingPrizeConfigMapper;

    @Autowired
    private ZeroShoppingPrizeSelectMapper zeroShoppingPrizeSelectMapper;

    public List<ZeroShoppingPrizeConfigModel> getAllPrize() {
        return zeroShoppingPrizeConfigMapper.findAll();
    }

    public void updatePrizeCount(ZeroShoppingPrizeConfigModel zeroShoppingPrizeConfigModel) {
        zeroShoppingPrizeConfigMapper.update(zeroShoppingPrizeConfigModel);
    }

    public BasePaginationDataDto<ZeroShoppingPrizeSelectView> userPrizeList(int index, int pageSize, String mobile, Date startTime, Date endTime) {
        List<ZeroShoppingPrizeSelectModel> zeroShoppingPrizeSelectModels = zeroShoppingPrizeSelectMapper.findByMobileAndDate(mobile, startTime, endTime);
        List<ZeroShoppingPrizeSelectView> zeroShoppingPrizeSelectViews = zeroShoppingPrizeSelectModels.stream().map(i -> new ZeroShoppingPrizeSelectView(i)).collect(Collectors.toList());
        int count = zeroShoppingPrizeSelectViews.size();
        int endIndex = pageSize * index;
        int startIndex = (index - 1) * 10;
        if (count <= endIndex) {
            endIndex = count;
        }
        if (count < startIndex) {
            startIndex = count;
        }
        BasePaginationDataDto basePaginationDataDto = new BasePaginationDataDto(index, pageSize, count, zeroShoppingPrizeSelectViews.subList(startIndex, endIndex));
        return basePaginationDataDto;
    }
}
