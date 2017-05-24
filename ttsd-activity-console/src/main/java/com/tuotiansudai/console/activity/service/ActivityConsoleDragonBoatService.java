package com.tuotiansudai.console.activity.service;

import com.tuotiansudai.activity.repository.mapper.DragonBoatFestivalMapper;
import com.tuotiansudai.activity.repository.model.DragonBoatFestivalModel;
import com.tuotiansudai.dto.BasePaginationDataDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ActivityConsoleDragonBoatService {

    @Autowired
    private DragonBoatFestivalMapper dragonBoatFestivalMapper;

    public BasePaginationDataDto<DragonBoatFestivalModel> getList(int index, int pageSize) {
        long count = dragonBoatFestivalMapper.countDragonBoatFestival();
        List<DragonBoatFestivalModel> list = dragonBoatFestivalMapper.getDragonBoatFestivalList((index - 1) * pageSize, pageSize);
        return new BasePaginationDataDto<>(index, pageSize, count, list);
    }

}
