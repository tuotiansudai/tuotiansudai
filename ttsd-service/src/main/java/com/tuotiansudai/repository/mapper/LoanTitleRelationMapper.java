package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.LoanTitleRelationModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanTitleRelationMapper {
    void create(@Param("loanTitleModels") List<LoanTitleRelationModel> loanTitleRelationModels);

    List<LoanTitleRelationModel> findByLoanId(@Param("loanId") long loanId);

    void delete(@Param("loanId") long loanId);
}
