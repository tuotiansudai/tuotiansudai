package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.LoanApplicationModel;
import com.tuotiansudai.repository.model.LoanApplicationView;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LoanApplicationMapper {
    void create(LoanApplicationModel loanApplicationModel);

    void update(LoanApplicationModel loanApplicationModel);

    LoanApplicationModel findById(@Param(value = "id") long id);

    List<LoanApplicationView> findViewPagination(@Param(value = "index") int index, @Param(value = "pageSize") int pageSize);

    long findCount();
}
