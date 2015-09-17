package com.tuotiansudai.service;


import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.InvestDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.PayFormDataDto;

public interface InvestService {

    /**
     * 进行一次投资
     * @param investDto
     */
    BaseDto<PayFormDataDto> invest(InvestDto investDto);

    /**
     * 进行一次无密投资
     * @para investDto
     * @return
     */
    BaseDto<PayDataDto> investNopwd(InvestDto investDto);
}
