package com.tuotiansudai.console.activity.service;

import com.tuotiansudai.activity.repository.mapper.CashSnowballActivityMapper;
import com.tuotiansudai.activity.repository.model.CashSnowballActivityModel;
import com.tuotiansudai.activity.repository.model.CashSnowballActivityView;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.model.ActivityAmountGrade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActivityConsoleCashSnowballService {

    @Autowired
    private CashSnowballActivityMapper cashSnowballActivityMapper;

    public BasePaginationDataDto<CashSnowballActivityView> list(int index, int pageSize, String mobile, Long startInvestAmount, Long endInvestAmount) {
        List<CashSnowballActivityModel> cashSnowballActivityModels = cashSnowballActivityMapper.findAll(mobile, startInvestAmount, endInvestAmount);
        cashSnowballActivityModels.stream().forEach(cashSnowballActivityModel -> cashSnowballActivityModel.setCashAmount(cashSnowballActivityModel.getCashAmount() + ActivityAmountGrade.getAwardAmount("CASH_SNOWBALL", cashSnowballActivityModel.getAnnualizedAmount())));
        List<CashSnowballActivityView> list = cashSnowballActivityModels.stream().map(CashSnowballActivityView::new).collect(Collectors.toList());
        int count = list.size();
        int endIndex = pageSize * index;
        int startIndex = (index - 1) * 10;
        if (count <= endIndex) {
            endIndex = count;
        }
        if (count < startIndex) {
            startIndex = count;
        }
        BasePaginationDataDto basePaginationDataDto = new BasePaginationDataDto(index, pageSize, count, list.subList(startIndex, endIndex));
        return basePaginationDataDto;
    }
}
