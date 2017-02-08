package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.ExtraLoanRateRuleModel;
import com.tuotiansudai.repository.model.ProductType;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExtraLoanRateRuleMapper {

    List<ExtraLoanRateRuleModel> findExtraLoanRateRuleByNameAndProductType(@Param(value = "loanName") String loanName, @Param(value = "productType") ProductType productType);

    ExtraLoanRateRuleModel findById(@Param(value = "id") long id);

}
