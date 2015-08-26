package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.LoanTitleRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by tuotian on 15/8/18.
 */
public interface LoanTitleMapper {
    void create(@Param("loanTitleModels") List<LoanTitleRelation> loanTitleRelations);
    List<LoanTitleRelation> findLoanTitleByLoanId(@Param("loanId")long loanId);
}
