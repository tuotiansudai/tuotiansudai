package com.tuotiansudai.service;


import com.tuotiansudai.dto.ExperienceLoanDto;

public interface ExperienceLoanDetailService {

    ExperienceLoanDto findExperienceLoanDtoDetail(long loanId, String loginName);
}
