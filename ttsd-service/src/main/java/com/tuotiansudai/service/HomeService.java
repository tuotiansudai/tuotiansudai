package com.tuotiansudai.service;

import com.tuotiansudai.dto.HomeLoanDto;

import java.util.List;

public interface HomeService {

    List<HomeLoanDto> getNormalLoans();

    List<HomeLoanDto> getNewbieLoans();

    List<HomeLoanDto> getEnterpriseLoans();
}
