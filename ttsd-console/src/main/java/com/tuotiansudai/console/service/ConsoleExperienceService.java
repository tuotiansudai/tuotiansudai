package com.tuotiansudai.console.service;

import com.tuotiansudai.console.dto.ExperienceBalancePaginationItemDto;
import com.tuotiansudai.console.dto.InvestRepayExperiencePaginationItemDto;
import com.tuotiansudai.console.repository.mapper.InvestRepayMapperConsole;
import com.tuotiansudai.console.repository.mapper.UserMapperConsole;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.mapper.ExperienceBillMapper;
import com.tuotiansudai.repository.model.RepayStatus;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConsoleExperienceService {

    @Autowired
    private UserMapperConsole userMapperConsole;
    @Autowired
    private ExperienceBillMapper experienceBillMapper;
    @Autowired
    private InvestRepayMapperConsole investRepayMapperConsole;

    public BasePaginationDataDto<ExperienceBalancePaginationItemDto> balance(String mobile, String balanceMin, String balanceMax, int index, int pageSize){
        int count = userMapperConsole.findCountExperienceBalance(mobile,balanceMin,balanceMax);
        int offset = PaginationUtil.calculateOffset(index,pageSize,count);
        List<UserModel> userModels = userMapperConsole.findExperienceBalance(mobile,balanceMin,balanceMax,offset,pageSize);
        return new BasePaginationDataDto<>(index,pageSize,count,
                userModels.stream().map(userModel -> new ExperienceBalancePaginationItemDto(userModel,experienceBillMapper.findLastExchangeTimeByLoginName(userModel.getLoginName())))
                        .collect(Collectors.toList()));
    }

    public long sumExperienceBalance(String mobile, String balanceMin, String balanceMax){
        return userMapperConsole.sumExperienceBalance(mobile,balanceMin,balanceMax);
    }

    public BasePaginationDataDto<InvestRepayExperiencePaginationItemDto> investRepayExperience(String mobile,
                                                                                               Date repayDateMin,
                                                                                               Date repayDateMax,
                                                                                               RepayStatus repayStatus,
                                                                                               int index,
                                                                                               int pageSize){

    }



}
