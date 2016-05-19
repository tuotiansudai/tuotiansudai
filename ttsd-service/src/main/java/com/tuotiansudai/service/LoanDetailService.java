package com.tuotiansudai.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.LoanDetailDto;

public interface LoanDetailService {

    LoanDetailDto getLoanDetail(String loginName, long loanId);

    BaseDto<BasePaginationDataDto> getInvests(String loginName, long loanId, int index, int pageSize);
}
