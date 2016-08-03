package com.tuotiansudai.service;

import com.tuotiansudai.dto.*;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.repository.model.LoanTitleModel;

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
     * @function 创建标的
     * @param loanDto
     * @param loanDetailsDto
     * @param loanerDetailsDto
     * @param pledgeDetailsDto
     * @return
     */
    BaseDto<BaseDataDto> createLoan(LoanDto loanDto, LoanDetailsDto loanDetailsDto, LoanerDetailsDto loanerDetailsDto,
                                    AbstractPledgeDetailsDto pledgeDetailsDto);

    /**
     * @param loanDto
     * @return
     * @function 标的编辑
     */
    BaseDto<BaseDataDto> updateLoan(LoanDto loanDto, LoanDetailsDto loanDetailsDto, LoanerDetailsDto loanerDetailsDto,
                                    AbstractPledgeDetailsDto pledgeDetailsDto);

    BaseDto<PayDataDto> delayLoan(LoanDto loanDto);

    void startFundraising(long loanId);

    BaseDto<PayDataDto> openLoan(LoanDto loanDto, String ip);

    BaseDto<PayDataDto> cancelLoan(LoanDto loanDto);

    List<LoanItemDto> findLoanItems(String name, LoanStatus status, double rateStart, double rateEnd,int durationStart,int durationEnd, int currentPageNo);

    int findLoanListCountWeb(String name, LoanStatus status, double rateStart, double rateEnd,int durationStart,int durationEnd);

    LoanModel findLoanById(long loanId);

    AbstractCreateLoanDto findCreateLoanDto(long loanId);

    boolean loanIsExist(long loanId);

    BaseDto<PayDataDto> loanOut(LoanDto loanDto);

    BaseDto<BasePaginationDataDto> getLoanerLoanData(String loginName, int index, int pageSize, LoanStatus status, Date startTime, Date endTime);

    int findLoanListCount(LoanStatus status, Long loanId, String loanName, Date startTime, Date endTime);

    List<LoanListDto> findLoanList(LoanStatus status, Long loanId, String loanName, Date startTime, Date endTime, int currentPageNo, int pageSize);

    BaseDto<PayDataDto> applyAuditLoan(LoanDto loanDto);

}
