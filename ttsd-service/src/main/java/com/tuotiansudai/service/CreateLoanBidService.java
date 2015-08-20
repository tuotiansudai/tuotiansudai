package com.tuotiansudai.service;

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
    List<TitleModel> findAllTitles();

    List<Map<String,String>> getLoanType();

    List<Map<String,String>> getActivityType();
}
