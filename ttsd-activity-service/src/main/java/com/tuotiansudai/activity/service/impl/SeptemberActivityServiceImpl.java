package com.tuotiansudai.activity.service.impl;


import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.activity.dto.UserLuxuryPrizeDto;
import com.tuotiansudai.activity.repository.mapper.LuxuryPrizeMapper;
import com.tuotiansudai.activity.repository.mapper.UserLuxuryPrizeMapper;
import com.tuotiansudai.activity.repository.model.LuxuryPrizeModel;
import com.tuotiansudai.activity.repository.model.UserLuxuryPrizeModel;
import com.tuotiansudai.activity.service.SeptemberActivityService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.AccountModel;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

public class SeptemberActivityServiceImpl implements SeptemberActivityService{
    @Autowired
    private UserLuxuryPrizeMapper userLuxuryPrizeMapper;
    @Autowired
    private LuxuryPrizeMapper luxuryPrizeMapper;
    @Autowired
    private AccountMapper accountMapper;
    @Override
    public List<UserLuxuryPrizeDto> obtainLuxuryPrizeList(String mobile, Date startTime, Date endTime, Integer index, Integer pageSize) {
        List<UserLuxuryPrizeModel> userLuxuryPrizeModels = userLuxuryPrizeMapper.getUserLuxuryPrizeList(mobile,startTime,endTime,index,pageSize);

        List<UserLuxuryPrizeDto> userLuxuryPrizeDtos = Lists.transform(userLuxuryPrizeModels, new Function<UserLuxuryPrizeModel, UserLuxuryPrizeDto>() {
            @Override
            public UserLuxuryPrizeDto apply(UserLuxuryPrizeModel input) {
                LuxuryPrizeModel luxuryPrizeModel = luxuryPrizeMapper.findByPrizeId(input.getPrizeId());
                String name = luxuryPrizeModel != null?luxuryPrizeModel.getName():"";
                AccountModel accountModel = accountMapper.findByLoginName(input.getLoginName());
                String userName = accountMapper.findByLoginName(input.getLoginName()) != null?accountModel.getUserName():"";
                return new UserLuxuryPrizeDto(input,name,userName);
            }
        });
        return userLuxuryPrizeDtos;
    }
}
