package com.tuotiansudai.api.service.impl;

import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.LoanListRequestDto;
import com.tuotiansudai.api.dto.LoanResponseDataDto;
import com.tuotiansudai.api.service.MobileAppLoanListService;
import com.tuotiansudai.repository.model.LoanModel;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class MobileAppLoanListServiceImpl implements MobileAppLoanListService {
    @Override
    public BaseResponseDto generateLoanList(LoanListRequestDto investListRequestDt) {
        throw new NotImplementedException(getClass().getName());
    }

    @Override
    public List<LoanResponseDataDto> convertLoanDto(List<LoanModel> loanList) {
        throw new NotImplementedException(getClass().getName());
    }
}
