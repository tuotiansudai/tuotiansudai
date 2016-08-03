package com.tuotiansudai.dto;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Doubles;
import com.tuotiansudai.repository.model.ExtraLoanRateModel;
import org.apache.commons.collections.CollectionUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class ExtraLoanRateDto implements Serializable {

    private List<ExtraLoanRateItemDto> items = Lists.newArrayList();

    private String minExtraRate;

    private String maxExtraRate;

    public ExtraLoanRateDto(List<ExtraLoanRateModel> models) {
        if (CollectionUtils.isNotEmpty(models)) {
            Ordering<ExtraLoanRateModel> ordering = new Ordering<ExtraLoanRateModel>() {
                @Override
                public int compare(ExtraLoanRateModel left, ExtraLoanRateModel right) {
                    return Doubles.compare(left.getRate(), right.getRate());
                }
            };

            List<ExtraLoanRateModel> orderingModels = ordering.sortedCopy(models);

            this.items = Lists.transform(orderingModels, new Function<ExtraLoanRateModel, ExtraLoanRateItemDto>() {
                @Override
                public ExtraLoanRateItemDto apply(ExtraLoanRateModel input) {
                    return new ExtraLoanRateItemDto(input);
                }
            });

            this.minExtraRate = new BigDecimal(ordering.min(models).getRate()).multiply(new BigDecimal(100)).setScale(1, BigDecimal.ROUND_HALF_UP).toString();
            this.maxExtraRate = new BigDecimal(ordering.max(models).getRate()).multiply(new BigDecimal(100)).setScale(1, BigDecimal.ROUND_HALF_UP).toString();
        }
    }

    public List<ExtraLoanRateItemDto> getItems() {
        return items;
    }

    public String getMinExtraRate() {
        return minExtraRate;
    }

    public String getMaxExtraRate() {
        return maxExtraRate;
    }
}
