package com.tuotiansudai.service;

import com.tuotiansudai.dto.*;
import com.tuotiansudai.repository.model.*;

import java.util.Date;
import java.util.List;

public interface LoanService {
    /**
     * @function 新增title
     * @param loanTitleDto
     */
    LoanTitleModel createTitle(LoanTitleDto loanTitleDto);

    /**
     * @function 获取借款人或代理人
     * @param loginName
     * @return
     */
    List<String> getLoginNames(String loginName);

    /**
     * @function 获取所有的标题
     * @return
     */
    List<LoanTitleModel> findAllTitles();

    /**
     * @function 获取所有的标类型
     * @return
     */
    List<LoanType> getLoanType();

    /**
     * @function 获取所有的活动类型
     * @return
     */
    List<ActivityType> getActivityType();

    /**
     * @function 创建标的
     * @return
     */
    BaseDto<PayDataDto> createLoan(LoanDto loanDto);

    public List<LoanListDto> findLoanList(LoanStatus status,long loanId,String loanName,Date startTime,Date endTime,int currentPageNo);

    public int findLoanListCount(LoanStatus status,long loanId,String loanName,Date startTime,Date endTime);

    /**
     * @function 标的编辑
     * @param loanDto
     * @return
     */
    BaseDto<PayDataDto> updateLoan(LoanDto loanDto);

    /**
     * @function 通过id查找标的
     * @param loanId
     * @return
     */
    LoanModel findLoanById(long loanId);

    boolean loanIsExist(long loanId);
}
