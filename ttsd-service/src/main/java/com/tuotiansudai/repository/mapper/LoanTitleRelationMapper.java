package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.LoanTitleRelationModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by tuotian on 15/8/18.
 */
public interface LoanTitleRelationMapper {
    void create(@Param("loanTitleModels") List<LoanTitleRelationModel> loanTitleRelationModels);
    List<LoanTitleRelationModel> findLoanTitleByLoanId(@Param("loanId")long loanId);
}
