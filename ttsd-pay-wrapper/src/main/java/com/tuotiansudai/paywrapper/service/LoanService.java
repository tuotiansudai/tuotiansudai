package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.repository.model.TitleModel;

import java.util.List;

public interface LoanService {
    /**
     * @function 创建标的
     * @param loanDto
     * @return
     */
    BaseDto createLoan(LoanDto loanDto);

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
    List<TitleModel> findAllTitles();
}
