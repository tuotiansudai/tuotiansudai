package com.ttsd.api.service;


import com.esoft.jdp2p.invest.model.Invest;
import com.esoft.jdp2p.loan.model.Loan;
import com.ttsd.api.dto.*;

import java.util.List;

public interface MobileLoanDetailAppService {

    BaseResponseDto generateLoanDetail(LoanDetailRequestDto loanDetailRequestDto);

    LoanDetailResponseDataDto convertLoanDetailFromLoan(Loan loan,List<EvidenceDto> evidence);

    List<InvestRecordDto> convertInvestRecordDtoFromInvest(List<Invest> invests);

    List<String> getImageUrl(String guaranteeCompanyDescription);


}
