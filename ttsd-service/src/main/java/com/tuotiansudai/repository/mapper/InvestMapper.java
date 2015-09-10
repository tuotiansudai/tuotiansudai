package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.repository.model.SortStyle;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
@Repository
public interface InvestMapper {
    /**
     * 创建投资记录
     *
     * @param investModel
     */
    void create(InvestModel investModel);

    /**
     * 更新投资记录的状态
     * @param id
     * @param status
     */
    void updateStatus(@Param(value = "id") long id,
                      @Param(value = "status") InvestStatus status);

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
    List<InvestModel> findByLoanIdOrderByTime(@Param(value = "loanId") long loanId,
                                              @Param(value = "sortStyle") SortStyle sortStyle);

    /**
     * 计算标的的投资总额
     *
     * @param loanId
     * @return
     */
    long sumSuccessInvestAmount(@Param(value = "loanId") long loanId);

    List<InvestModel> getInvests(@Param(value = "loanId") long loanId,
                                 @Param(value = "index") Integer index,
                                 @Param(value = "pageSize") Integer pageSize,
                                 @Param(value = "status") InvestStatus status);

    int getTotalCount(@Param(value = "loanId") long loanId,
                                    @Param(value = "status") InvestStatus status);

    List<InvestModel> getSuccessInvests(@Param(value = "loanId") long loanId);

}
