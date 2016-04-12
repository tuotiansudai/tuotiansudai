package com.ttsd.api.service;


import com.esoft.jdp2p.invest.model.Invest;
import com.esoft.jdp2p.loan.model.Loan;
import com.ttsd.api.dto.*;

import java.util.List;

public interface MobileAppLoanDetailService {

    BaseResponseDto generateLoanDetail(LoanDetailRequestDto loanDetailRequestDto);

    LoanDetailResponseDataDto convertLoanDetailFromLoan(Loan loan,List<EvidenceResponseDataDto> evidence);

    List<InvestRecordResponseDataDto> convertInvestRecordDtoFromInvest(List<Invest> invests);

    List<String> getImageUrl(String guaranteeCompanyDescription);


}
