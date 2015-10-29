package com.tuotiansudai.api.service.impl;


import com.tuotiansudai.api.dto.*;
import com.tuotiansudai.api.service.MobileAppLoanDetailService;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.LoanModel;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class MobileAppLoanDetailServiceImpl implements MobileAppLoanDetailService {


    @Override
    public BaseResponseDto generateLoanDetail(LoanDetailRequestDto loanDetailRequestDto) {
        throw new NotImplementedException(getClass().getName());
    }

    @Override
    public LoanDetailResponseDataDto convertLoanDetailFromLoan(LoanModel loan, List<EvidenceResponseDataDto> evidence) {
        throw new NotImplementedException(getClass().getName());
    }

    @Override
    public List<InvestRecordResponseDataDto> convertInvestRecordDtoFromInvest(List<InvestModel> invests) {
        throw new NotImplementedException(getClass().getName());
    }

    @Override
    public List<String> getImageUrl(String guaranteeCompanyDescription) {
        throw new NotImplementedException(getClass().getName());
    }
}
