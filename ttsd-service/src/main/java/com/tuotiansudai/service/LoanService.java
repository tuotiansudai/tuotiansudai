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
    public List<LoanListDto> findLoanList(String status,String loanId,String loanName,String startTime,String endTime,String currentPageNo);

    public int findLoanListCount(String status,String loanId,String loanName,String startTime,String endTime);

    public List<LoanListWebDto> findLoanListWeb(String activityType,String status,String periodsStart,String periodsEnd,String rateStart,String rateEnd,String currentPageNo);

    public int findLoanListCountWeb(String activityType,String status,String periodsStart,String periodsEnd,String rateStart,String rateEnd);


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
}
