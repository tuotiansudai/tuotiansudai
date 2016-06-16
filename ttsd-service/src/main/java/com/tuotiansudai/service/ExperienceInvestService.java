package com.tuotiansudai.service;

import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.InvestDto;

public interface ExperienceInvestService {

    BaseDto<BaseDataDto> invest(InvestDto investDto);
}
