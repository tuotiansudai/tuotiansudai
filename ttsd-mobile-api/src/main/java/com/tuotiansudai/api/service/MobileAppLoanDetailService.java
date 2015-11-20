package com.tuotiansudai.api.service;


import com.tuotiansudai.api.dto.*;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.LoanModel;

import java.util.List;

public interface MobileAppLoanDetailService {

    BaseResponseDto generateLoanDetail(LoanDetailRequestDto loanDetailRequestDto);


}
