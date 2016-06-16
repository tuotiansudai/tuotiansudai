package com.tuotiansudai.service.impl;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.ExtraLoanRateRuleDto;
import com.tuotiansudai.repository.mapper.ExtraLoanRateRuleMapper;
import com.tuotiansudai.repository.model.ExtraLoanRateRuleModel;
import com.tuotiansudai.repository.model.ProductType;
import com.tuotiansudai.service.ExtraLoanRateRuleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExtraLoanRateRuleServiceImpl implements ExtraLoanRateRuleService {

    @Autowired
    private ExtraLoanRateRuleMapper extraLoanRateRuleMapper;

    public BaseDto<ExtraLoanRateRuleDto> findExtraLoanRateRuleByNameAndProductType(String loanName, ProductType productType) {
        BaseDto baseDto = new BaseDto();
        ExtraLoanRateRuleDto extraLoanRateRuleDto = new ExtraLoanRateRuleDto();
        baseDto.setData(extraLoanRateRuleDto);
        if (StringUtils.isEmpty(loanName) || productType == null) {
            extraLoanRateRuleDto.setStatus(false);
        } else {
            List<ExtraLoanRateRuleModel> extraLoanRateRuleModels = extraLoanRateRuleMapper.findExtraLoanRateRuleByNameAndProductType(loanName, productType);
            extraLoanRateRuleDto.setStatus(true);
            extraLoanRateRuleDto.setExtraLoanRateRuleModels(extraLoanRateRuleModels);
        }
        return baseDto;
    }

}
