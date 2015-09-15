package com.tuotiansudai.service;

import com.tuotiansudai.dto.BasePaginationDto;
import com.tuotiansudai.dto.LoanRepayDto;
import com.tuotiansudai.repository.model.RepayStatus;

import java.util.List;


public interface LoanRepayService {

    BasePaginationDto findLoanRepayPagination(LoanRepayDto requestDto);

    List<RepayStatus> findAllRepayStatus();

}
