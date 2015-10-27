package com.tuotiansudai.service;

import com.tuotiansudai.dto.*;
import com.tuotiansudai.exception.TTSDException;
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
     * @param loginName
     * @return
     * @function 获取借款人或代理人
     */
    List<String> getLoginNames(String loginName);

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

    /**
     * @param loanId
     * @return
     * @function 通过id查找标的
     */

    public List<LoanListWebDto> findLoanListWeb(ActivityType activityType, LoanStatus status, long periodsStart, long periodsEnd, double rateStart, double rateEnd,int currentPageNo);

    public int findLoanListCountWeb(ActivityType activityType, LoanStatus status, long periodsStart, long periodsEnd, double rateStart, double rateEnd);


    LoanModel findLoanById(long loanId);

    boolean loanIsExist(long loanId);
    /**
     * 标的放款
     *
     * @param loanId
     * @param minInvestAmount
     * @param fundraisingEndTime
     * @throws TTSDException
     */
    void loanOut(long loanId, long minInvestAmount, Date fundraisingEndTime) throws TTSDException;

    BaseDto<LoanDto> getLoanDetail(long loanId);

    BaseDto<BasePaginationDataDto> getInvests(long loanId, int index, int pageSize);

    BaseDto<BasePaginationDataDto> getLoanerLoanData(int index, int pageSize, LoanStatus status, Date startTime, Date endTime);

    int findLoanListCount(LoanStatus status,long loanId,String loanName,Date startTime,Date endTime);

    List<LoanListDto> findLoanList(LoanStatus status,long loanId,String loanName,Date startTime,Date endTime,int currentPageNo, int pageSize);
}
