package com.tuotiansudai.console.service;

import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.enums.riskestimation.*;
import com.tuotiansudai.repository.mapper.RiskEstimateMapper;
import com.tuotiansudai.repository.model.RiskEstimateViewModel;
import com.tuotiansudai.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsoleRiskEstimateService {

    @Autowired
    private RiskEstimateMapper riskEstimateMapper;

    public BasePaginationDataDto<RiskEstimateViewModel> listRiskEstimate(Estimate estimate, Income income, Rate rate, Duration duration, Age age, int index) {

        long count = riskEstimateMapper.countByConsole(estimate, income, rate, duration, age);

        int offset = PaginationUtil.calculateOffset(index, 10, count);

        List<RiskEstimateViewModel> models = riskEstimateMapper.findByConsole(estimate, income, rate, duration, age, offset);

        return new BasePaginationDataDto<>(index, 10, count, models);
    }
}
