package com.tuotiansudai.service;

import com.tuotiansudai.dto.*;
import com.tuotiansudai.repository.model.ActivityType;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.LoanType;
import com.tuotiansudai.repository.model.LoanTitleModel;

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
     * @function 获取所有的标类型
     */
    List<LoanType> getLoanType();

    /**
     * @return
     * @function 获取所有的活动类型
     */
    List<ActivityType> getActivityType();

    /**
     * @return
     * @function 创建标的
     */
    BaseDto<PayDataDto> createLoan(LoanDto loanDto);

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

    BaseDto<LoanDto> getLoanDetail(long loanId);

    String getExpectedTotalIncome(long loanId, double investAmount);

    BasePaginationDto<InvestPaginationDataDto> getInvests(long loanId, int index, int pageSize);
}
