package com.tuotiansudai.service;


import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.ExtraLoanRateRuleDto;
import com.tuotiansudai.repository.model.ProductType;

public interface ExtraLoanRateRuleService {

    BaseDto<ExtraLoanRateRuleDto> findExtraLoanRateRuleByNameAndProductType(String loanName, ProductType productType);

}
