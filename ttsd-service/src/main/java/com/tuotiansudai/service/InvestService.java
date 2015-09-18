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
     * @param includeNextRepay 是否同时查询下次还款情况
     * @return
     */
    BasePaginationDataDto<InvestDetailDto> queryInvests(InvestDetailQueryDto queryDto, boolean includeNextRepay);
}
