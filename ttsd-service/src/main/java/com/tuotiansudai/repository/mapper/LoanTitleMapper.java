package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.LoanTitleModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by tuotian on 15/8/18.
 */
public interface LoanTitleMapper {
    void createLoanTitle(@Param("loanTitleModels")List<LoanTitleModel> loanTitleModels);
    List<LoanTitleMapper> findLoanTitleByLoanId(@Param("loanId")long loanId);
}
