package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.LoanRiskManagementTitleModel;
import com.tuotiansudai.repository.model.LoanTitleModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRiskManagementTitleMapper {
    void create(LoanRiskManagementTitleModel titleModel);

    List<LoanRiskManagementTitleModel> findAll();

}
