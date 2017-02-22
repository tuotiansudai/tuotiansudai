package com.tuotiansudai.console.service;

import com.tuotiansudai.console.dto.ExperienceBalancePaginationItemDto;
import com.tuotiansudai.console.dto.ExperienceBillPaginationItemDto;
import com.tuotiansudai.console.dto.InvestRepayExperiencePaginationItemDto;
import com.tuotiansudai.console.repository.mapper.ExperienceBillMapperConsole;
import com.tuotiansudai.console.repository.mapper.InvestRepayMapperConsole;
import com.tuotiansudai.console.repository.mapper.UserMapperConsole;
import com.tuotiansudai.console.repository.model.ExperienceBillView;
import com.tuotiansudai.console.repository.model.InvestRepayExperienceView;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.enums.ExperienceBillOperationType;
import com.tuotiansudai.enums.ExperienceBusinessType;
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
//    @Autowired
//    private InvestRepayMapperConsole investRepayMapperConsole;
//    @Autowired
//    private ExperienceBillMapperConsole experienceBillMapperConsole;

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

//    public BasePaginationDataDto<InvestRepayExperiencePaginationItemDto> investRepayExperience(String mobile,
//                                                                                               Date repayDateMin,
//                                                                                               Date repayDateMax,
//                                                                                               RepayStatus repayStatus,
//                                                                                               int index,
//                                                                                               int pageSize){
//        int count = investRepayMapperConsole.findCountInvestRepayExperience(mobile,repayDateMin,repayDateMax,repayStatus);
//        int offset = PaginationUtil.calculateOffset(index,pageSize,count);
//        List<InvestRepayExperienceView> views = investRepayMapperConsole.findInvestRepayExperience(mobile,repayDateMin,repayDateMax,repayStatus,offset,pageSize);
//        return new BasePaginationDataDto<>(index,pageSize,count,views.stream().map(investRepayExperienceView -> new InvestRepayExperiencePaginationItemDto(investRepayExperienceView))
//                .collect(Collectors.toList()));
//    }
//
//    public long findSumExpectedInterestExperience(String mobile,Date repayDateMin,Date repayDateMax,RepayStatus repayStatus){
//        return investRepayMapperConsole.findSumExpectedInterestExperience(mobile,repayDateMin,repayDateMax,repayStatus);
//    }
//    public long findSumActualInterestExperience(String mobile,Date repayDateMin,Date repayDateMax,RepayStatus repayStatus){
//        return investRepayMapperConsole.findSumActualInterestExperience(mobile,repayDateMin,repayDateMax,repayStatus);
//    }
//
//    public BasePaginationDataDto<ExperienceBillPaginationItemDto> experienceBill(String mobile,
//                                                                                 Date startTime,
//                                                                                 Date endTime,
//                                                                                 ExperienceBillOperationType operationType,
//                                                                                 ExperienceBusinessType businessType,
//                                                                                 int index,
//                                                                                 int pageSize){
//        int count = experienceBillMapperConsole.findCountExperienceBill(mobile,startTime,endTime,operationType,businessType);
//        int offset = PaginationUtil.calculateOffset(index,pageSize,count);
//        List<ExperienceBillView> views = experienceBillMapperConsole.findExperienceBill(mobile,startTime,endTime,operationType,businessType,offset,pageSize);
//        return new BasePaginationDataDto<>(index,pageSize,count,views.stream().map(experienceBillView -> new ExperienceBillPaginationItemDto(experienceBillView))
//                .collect(Collectors.toList()));
//
//    }
//
//    public long findSumExperienceBillAmount(String mobile,
//                  Date startTime,
//                  Date endTime,
//                  ExperienceBillOperationType operationType,
//                  ExperienceBusinessType businessType){
//        return experienceBillMapperConsole.findSumExperienceBillAmount(mobile,startTime,endTime,operationType,businessType);
//    }



}
