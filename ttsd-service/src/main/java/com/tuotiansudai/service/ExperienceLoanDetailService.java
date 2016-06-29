package com.tuotiansudai.service;


import com.tuotiansudai.repository.model.ExperienceLoanDto;

public interface ExperienceLoanDetailService {

    ExperienceLoanDto findExperienceLoanDtoDetail(long loanId, String loginName);
}
