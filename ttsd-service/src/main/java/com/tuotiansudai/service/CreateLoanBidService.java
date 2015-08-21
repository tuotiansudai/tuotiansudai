package com.tuotiansudai.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.repository.model.TitleModel;

import java.util.List;
import java.util.Map;

public interface CreateLoanBidService {
    /**
     * @function 新增title
     * @param titleModel
     */
    void createTitle(TitleModel titleModel);

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
    Map<String,List<TitleModel>> findAllTitles();

    /**
     * @function 获取所有的标的类型
     * @return
     */
    List<Map<String,String>> getLoanType();

    /**
     * @function 获取所有的活动类型
     * @return
     */
    List<Map<String,String>> getActivityType();

    /**
     * @function 创建标的
     * @return
     */
    BaseDto<PayFormDataDto> createLoanBid(LoanDto loanDto);
}
