package com.tuotiansudai.console.service;

import com.tuotiansudai.dto.ExperienceBalancePaginationItemDto;
import com.tuotiansudai.repository.mapper.ExperienceBillMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConsoleExperienceService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ExperienceBillMapper experienceBillMapper;

    private List<ExperienceBalancePaginationItemDto> balance(String mobile, String balanceMin, String balanceMax, int index, int pageSize){
        int offset = PaginationUtil.calculateOffset(index,pageSize,userMapper.findCountExperienceBalance(mobile,balanceMin,balanceMax));
        List<UserModel> userModels = userMapper.findExperienceBalance(mobile,balanceMin,balanceMax,offset,pageSize);
        return userModels.stream().map(userModel -> new ExperienceBalancePaginationItemDto(userModel,experienceBillMapper.findLastExchangeTimeByLoginName(userModel.getLoginName()))).collect(Collectors.toList());
    }


}
