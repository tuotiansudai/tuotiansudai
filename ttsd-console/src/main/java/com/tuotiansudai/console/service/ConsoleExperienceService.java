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
import com.tuotiansudai.enums.ExperienceBillBusinessType;
import com.tuotiansudai.enums.ExperienceBillOperationType;
import com.tuotiansudai.repository.mapper.ExperienceAccountMapper;
import com.tuotiansudai.repository.mapper.ExperienceBillMapper;
import com.tuotiansudai.repository.model.RepayStatus;
import com.tuotiansudai.repository.model.UserView;
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
    private ExperienceAccountMapper experienceAccountMapper;
    @Autowired
    private InvestRepayMapperConsole investRepayMapperConsole;
    @Autowired
    private ExperienceBillMapperConsole experienceBillMapperConsole;

    public BasePaginationDataDto<ExperienceBalancePaginationItemDto> balance(String mobile, String balanceMin, String balanceMax, int index, int pageSize) {
        int count = userMapperConsole.findCountExperienceBalance(mobile, balanceMin, balanceMax);
        int offset = PaginationUtil.calculateOffset(index, pageSize, count);
        List<UserView> userViews = userMapperConsole.findExperienceBalance(mobile, balanceMin, balanceMax, offset, pageSize);
        return new BasePaginationDataDto<>(index, pageSize, count,
                userViews.stream()
                        .map(userView -> new ExperienceBalancePaginationItemDto(userView,
                                experienceAccountMapper.getExperienceBalance(userView.getLoginName()),
                                experienceBillMapper.findLastExchangeTimeByLoginName(userView.getLoginName())))
                        .collect(Collectors.toList()));
    }

    public long sumExperienceBalance(String mobile, String balanceMin, String balanceMax) {
        return userMapperConsole.sumExperienceBalance(mobile, balanceMin, balanceMax);
    }

    public BasePaginationDataDto<InvestRepayExperiencePaginationItemDto> investRepayExperience(String mobile,
                                                                                               Date startTime,
                                                                                               Date endTime,
                                                                                               RepayStatus repayStatus,
                                                                                               int index,
                                                                                               int pageSize) {
        int count = investRepayMapperConsole.findCountInvestRepayExperience(mobile, startTime, endTime, repayStatus);
        int offset = PaginationUtil.calculateOffset(index, pageSize, count);
        List<InvestRepayExperienceView> views = investRepayMapperConsole.findInvestRepayExperience(mobile, startTime, endTime, repayStatus, offset, pageSize);
        return new BasePaginationDataDto<>(index, pageSize, count, views.stream().map(investRepayExperienceView -> new InvestRepayExperiencePaginationItemDto(investRepayExperienceView))
                .collect(Collectors.toList()));
    }

    public long findSumExpectedInterestExperience(String mobile, Date startTime, Date endTime, RepayStatus repayStatus) {
        return investRepayMapperConsole.findSumExpectedInterestExperience(mobile, startTime, endTime, repayStatus);
    }

    public long findSumActualInterestExperience(String mobile, Date startTime, Date endTime, RepayStatus repayStatus) {
        return investRepayMapperConsole.findSumActualInterestExperience(mobile, startTime, endTime, repayStatus);
    }

    public BasePaginationDataDto<ExperienceBillPaginationItemDto> experienceBill(String mobile,
                                                                                 Date startTime,
                                                                                 Date endTime,
                                                                                 ExperienceBillOperationType operationType,
                                                                                 ExperienceBillBusinessType businessType,
                                                                                 int index,
                                                                                 int pageSize) {
        int count = experienceBillMapperConsole.findCountExperienceBill(mobile, startTime, endTime, operationType, businessType);
        int offset = PaginationUtil.calculateOffset(index, pageSize, count);
        List<ExperienceBillView> views = experienceBillMapperConsole.findExperienceBill(mobile, startTime, endTime, operationType, businessType, offset, pageSize);
        return new BasePaginationDataDto<>(index, pageSize, count, views.stream().map(experienceBillView -> new ExperienceBillPaginationItemDto(experienceBillView))
                .collect(Collectors.toList()));

    }

    public long findSumExperienceBillAmount(String mobile,
                                            Date startTime,
                                            Date endTime,
                                            ExperienceBillOperationType operationType, ExperienceBillBusinessType businessType) {
        return experienceBillMapperConsole.findSumExperienceBillAmount(mobile, startTime, endTime, operationType, businessType);
    }


}
