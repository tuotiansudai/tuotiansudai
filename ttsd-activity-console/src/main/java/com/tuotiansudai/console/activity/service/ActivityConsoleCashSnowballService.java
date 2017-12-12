package com.tuotiansudai.console.activity.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.activity.repository.mapper.CashSnowballActivityMapper;
import com.tuotiansudai.activity.repository.model.CashSnowballActivityModel;
import com.tuotiansudai.activity.repository.model.CashSnowballActivityView;
import com.tuotiansudai.dto.BasePaginationDataDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ActivityConsoleCashSnowballService {

    @Autowired
    private CashSnowballActivityMapper cashSnowballActivityMapper;

    private final List<ActivityConsoleCashSnowballService.AnnualizedAmount> annualizedAmounts = Lists.newArrayList(
            new ActivityConsoleCashSnowballService.AnnualizedAmount(10000000l, 20000000l, 10000l),
            new ActivityConsoleCashSnowballService.AnnualizedAmount(20000000l, 30000000l, 30000l),
            new ActivityConsoleCashSnowballService.AnnualizedAmount(30000000l, 50000000l, 60000l),
            new ActivityConsoleCashSnowballService.AnnualizedAmount(50000000l, 60000000l, 120000l),
            new ActivityConsoleCashSnowballService.AnnualizedAmount(60000000l, Long.MAX_VALUE, 201800));

    public BasePaginationDataDto<CashSnowballActivityView> list(int index, int pageSize, String mobile, Long startInvestAmount, Long endInvestAmount) {
        List<CashSnowballActivityModel> cashSnowballActivityModels = cashSnowballActivityMapper.findAll(mobile, startInvestAmount == null ? null : startInvestAmount * 100, endInvestAmount == null ? null : endInvestAmount * 100);
        for (CashSnowballActivityModel cashSnowballActivityModel : cashSnowballActivityModels) {
            Optional<AnnualizedAmount> optional = annualizedAmounts.stream().filter(annualizedAmount -> annualizedAmount.getMinAmount() <= cashSnowballActivityModel.getAnnualizedAmount() && cashSnowballActivityModel.getAnnualizedAmount() < annualizedAmount.getMaxAmount()).findAny();
            cashSnowballActivityModel.setCashAmount(cashSnowballActivityModel.getCashAmount() + optional.map(o -> o.getCashAmount()).orElse(0l));
        }
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

    class AnnualizedAmount {
        private long minAmount;
        private long maxAmount;
        private long cashAmount;

        public AnnualizedAmount() {
        }

        public AnnualizedAmount(long minAmount, long maxAmount, long cashAmount) {
            this.minAmount = minAmount;
            this.maxAmount = maxAmount;
            this.cashAmount = cashAmount;
        }

        public long getMinAmount() {
            return minAmount;
        }

        public long getMaxAmount() {
            return maxAmount;
        }

        public long getCashAmount() {
            return cashAmount;
        }
    }
}
