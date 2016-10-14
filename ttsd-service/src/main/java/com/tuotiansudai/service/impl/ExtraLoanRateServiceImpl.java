package com.tuotiansudai.service.impl;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.ExtraLoanRateRuleDto;
import com.tuotiansudai.repository.mapper.ExtraLoanRateRuleMapper;
import com.tuotiansudai.repository.model.ExtraLoanRateRuleModel;
import com.tuotiansudai.repository.model.ProductType;
import com.tuotiansudai.service.ExtraLoanRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExtraLoanRateServiceImpl implements ExtraLoanRateService {

    @Autowired
    private ExtraLoanRateRuleMapper extraLoanRateRuleMapper;

    public BaseDto<ExtraLoanRateRuleDto> findExtraLoanRateRuleByNameAndProductType(String loanName, ProductType productType) {
        ExtraLoanRateRuleDto extraLoanRateRuleDto = new ExtraLoanRateRuleDto();
        BaseDto<ExtraLoanRateRuleDto> baseDto = new BaseDto<>(extraLoanRateRuleDto);
        List<ExtraLoanRateRuleModel> extraLoanRateRuleModels = extraLoanRateRuleMapper.findExtraLoanRateRuleByNameAndProductType(loanName, productType);
        extraLoanRateRuleDto.setStatus(true);
        extraLoanRateRuleDto.setExtraLoanRateRuleModels(extraLoanRateRuleModels);
        return baseDto;
    }

}
