package com.tuotiansudai.console.activity.service;

import com.tuotiansudai.activity.repository.mapper.ZeroShoppingPrizeConfigMapper;
import com.tuotiansudai.activity.repository.mapper.ZeroShoppingPrizeSelectMapper;
import com.tuotiansudai.activity.repository.model.ZeroShoppingPrizeConfigModel;
import com.tuotiansudai.activity.repository.model.ZeroShoppingPrizeSelectModel;
import com.tuotiansudai.dto.BasePaginationDataDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ActivityConsoleZeroShoppingService {

    @Autowired
    private ZeroShoppingPrizeConfigMapper zeroShoppingPrizeConfigMapper;

    @Autowired
    private ZeroShoppingPrizeSelectMapper zeroShoppingPrizeSelectMapper;

    public List<ZeroShoppingPrizeConfigModel> getAllPrize(){
        return zeroShoppingPrizeConfigMapper.findAll();
    }

    public void updatePrizeCount(ZeroShoppingPrizeConfigModel zeroShoppingPrizeConfigModel){
        zeroShoppingPrizeConfigMapper.update(zeroShoppingPrizeConfigModel);
    }

    public BasePaginationDataDto<ZeroShoppingPrizeSelectModel> userPrizeList(int index, int pageSize, String mobile, Date startTime, Date endTime) {
        List<ZeroShoppingPrizeSelectModel> zeroShoppingPrizeSelectModels = zeroShoppingPrizeSelectMapper.findByMobileAndDate(mobile, startTime, endTime);
        int count = zeroShoppingPrizeSelectModels.size();
        int endIndex = pageSize * index;
        int startIndex = (index - 1) * 10;
        if (count <= endIndex) {
            endIndex = count;
        }
        if (count < startIndex) {
            startIndex = count;
        }
        BasePaginationDataDto basePaginationDataDto = new BasePaginationDataDto(index, pageSize, count, zeroShoppingPrizeSelectModels.subList(startIndex, endIndex));
        return basePaginationDataDto;
    }
}
