package com.tuotiansudai.service;

import com.tuotiansudai.dto.*;
import com.tuotiansudai.exception.BaseException;
import com.tuotiansudai.repository.model.*;

import java.util.Date;
import java.util.List;

public interface LoanService {
    /**
     * @param loanTitleDto
     * @function 新增title
     */
    LoanTitleModel createTitle(LoanTitleDto loanTitleDto);

    /**
     * @return
     * @function 获取所有的标题
     */
    List<LoanTitleModel> findAllTitles();

    /**
     * @return
     * @function 创建标的
     */
    BaseDto<PayDataDto> createLoan(LoanDto loanDto);

    /**
     * @param loanDto
     * @return
     * @function 标的编辑
     */
    BaseDto<PayDataDto> updateLoan(LoanDto loanDto);

    BaseDto<PayDataDto> delayLoan(LoanDto loanDto);

    void startFundraising(long loanId);

    BaseDto<PayDataDto> openLoan(LoanDto loanDto);

    BaseDto<PayDataDto> cancelLoan(LoanDto loanDto);

    List<LoanListWebDto> findLoanListWeb(ProductLineType productLineType, LoanStatus status, long periodsStart, long periodsEnd, double rateStart, double rateEnd, int currentPageNo);

    int findLoanListCountWeb(ProductLineType productLineType, LoanStatus status, long periodsStart, long periodsEnd, double rateStart, double rateEnd);

    LoanModel findLoanById(long loanId);

    boolean loanIsExist(long loanId);

    BaseDto<PayDataDto> loanOut(LoanDto loanDto) throws BaseException;

    BaseDto<LoanDto> getLoanDetail(String loginName, long loanId);

    BaseDto<BasePaginationDataDto> getInvests(String loginName, long loanId, int index, int pageSize);

    BaseDto<BasePaginationDataDto> getLoanerLoanData(String loginName, int index, int pageSize, LoanStatus status, Date startTime, Date endTime);

    int findLoanListCount(LoanStatus status, Long loanId, String loanName, Date startTime, Date endTime);

    List<LoanListDto> findLoanList(LoanStatus status, Long loanId, String loanName, Date startTime, Date endTime, int currentPageNo, int pageSize);
}
