package com.tuotiansudai.console.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.ExtraLoanRateRuleDto;
import com.tuotiansudai.repository.mapper.ExtraLoanRateRuleMapper;
import com.tuotiansudai.repository.model.ExtraLoanRateRuleModel;
import com.tuotiansudai.repository.model.ProductType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExtraLoanRateService {

    @Autowired
    private ExtraLoanRateRuleMapper extraLoanRateRuleMapper;

    public BaseDto<ExtraLoanRateRuleDto> findExtraLoanRateRuleByNameAndProductType(String loanName, ProductType productType) {
        ExtraLoanRateRuleDto extraLoanRateRuleDto = new ExtraLoanRateRuleDto();
        BaseDto<ExtraLoanRateRuleDto> baseDto = new BaseDto<>(extraLoanRateRuleDto);
        //标的名称添加的序号规则为年的后两位加3位数字（如17019）
        loanName = (loanName.indexOf("1") >= 0 || loanName.indexOf("2") >= 0) ? loanName.substring(0, loanName.length() - 5) : loanName;
        List<ExtraLoanRateRuleModel> extraLoanRateRuleModels = extraLoanRateRuleMapper.findExtraLoanRateRuleByNameAndProductType(loanName, productType);
        extraLoanRateRuleDto.setStatus(true);
        extraLoanRateRuleDto.setExtraLoanRateRuleModels(extraLoanRateRuleModels);
        return baseDto;
    }

}
