package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.InvestModel;
<<<<<<< HEAD
import com.tuotiansudai.repository.model.InvestStatus;
=======
>>>>>>> new_version_master
import com.tuotiansudai.repository.model.SortStyle;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface InvestMapper {
    /**
<<<<<<< HEAD
     * 创建投资记录
=======
     * 创建投资
>>>>>>> new_version_master
     *
     * @param investModel
     */
    void create(InvestModel investModel);

    /**
<<<<<<< HEAD
     * 修改投资记录
     *
     * @param investModel
     */
    void update(InvestModel investModel);

    /**
=======
>>>>>>> new_version_master
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
<<<<<<< HEAD
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


=======
    List<InvestModel> findByLoanIdOrderByTime(@Param(value = "loanId") String loanId,
                                              @Param(value = "sortStyle") SortStyle sortStyle);
>>>>>>> new_version_master
}
