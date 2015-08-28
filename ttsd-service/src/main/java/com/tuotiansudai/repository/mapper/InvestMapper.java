package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.InvestModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface InvestMapper {
    /**
     * 创建投资
     * @param investModel
     */
    void create(InvestModel investModel);

    /**
     * 根据ID查找对应的投资
     * @param id
     * @return
     */
    InvestModel findById(@Param(value = "id") long id);

    /**
     * 查找用户的投资记录
     * 如果有分页插件的话，需要修改此返回类型（另外，如何传入排序参数）
     * @param loginName
     * @return
     */
    List<InvestModel> findByLoginName(@Param(value = "loginName") String loginName);

    /**
     * 查找标的的所有投资情况
     * 如果有分页插件的话，需要修改此返回类型（另外，如何传入排序参数）
     * @param loanId
     * @return
     */
    List<InvestModel> findByLoanId(@Param(value = "loanId") String loanId);
}
