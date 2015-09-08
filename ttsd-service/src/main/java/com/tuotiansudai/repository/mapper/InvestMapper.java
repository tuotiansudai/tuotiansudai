package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.SortStyle;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface InvestMapper {
    /**
     * 创建投资
     *
     * @param investModel
     */
    void create(InvestModel investModel);

    /**
     * 根据ID查找对应的投资
     *
     * @param id
     * @return
     */
    InvestModel findById(@Param(value = "id") long id);

    /**
     * 查找用户的投资记录
     * 如果有分页插件的话，需要修改此返回类型
     *
     * @param loginName
     * @return
     */
    List<InvestModel> findByLoginNameOrderByTime(@Param(value = "loginName") String loginName,
                                                 @Param(value = "sortStyle") SortStyle sortStyle);

    /**
     * 查找标的的所有投资情况
     * 如果有分页插件的话，需要修改此返回类型
     *
     * @param loanId
     * @return
     */
    List<InvestModel> findByLoanIdOrderByTime(@Param(value = "loanId") String loanId,
                                              @Param(value = "sortStyle") SortStyle sortStyle);

    List<InvestModel> getSuccessInvests(@Param(value = "loanId") long loanId);
}
