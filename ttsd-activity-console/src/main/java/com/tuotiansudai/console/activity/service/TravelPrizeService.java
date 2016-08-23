package com.tuotiansudai.console.activity.service;

import com.tuotiansudai.activity.repository.mapper.TravelPrizeMapper;
import com.tuotiansudai.activity.repository.mapper.UserTravelPrizeMapper;
import com.tuotiansudai.activity.repository.model.UserTravelPrizeModel;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TravelPrizeService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserTravelPrizeMapper userTravelPrizeMapper;

    public void getTravelAwardItems(String mobile, Date startTime, Date endTime, int index, int pageSize) {
        long countByPagination = userTravelPrizeMapper.countByPagination(mobile, startTime, endTime);
        List<UserTravelPrizeModel> models = userTravelPrizeMapper.findByPagination(mobile, startTime, endTime, PaginationUtil.calculateOffset(index, pageSize, countByPagination), pageSize);
    }
}
