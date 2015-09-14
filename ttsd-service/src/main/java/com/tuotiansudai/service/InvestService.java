package com.tuotiansudai.service;

import com.tuotiansudai.dto.*;

public interface InvestService {

    /**
     * 进行一次投资
     * @param investDto
     */
    BaseDto<PayFormDataDto> invest(InvestDto investDto);

    /**
     * 查找投资记录
     * @param queryDto
     * @return
     */
    BasePaginationDto<InvestDetailDto> queryInvests(InvestDetailQueryDto queryDto);
}
