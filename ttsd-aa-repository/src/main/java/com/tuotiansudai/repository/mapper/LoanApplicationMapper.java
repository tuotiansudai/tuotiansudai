package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.enums.LoanApplicationStatus;
import com.tuotiansudai.repository.model.LoanApplicationMaterialsModel;
import com.tuotiansudai.repository.model.LoanApplicationModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface LoanApplicationMapper {
    void create(LoanApplicationModel loanApplicationModel);

    void update(LoanApplicationModel loanApplicationModel);

    LoanApplicationModel findById(@Param(value = "id") long id);

    List<LoanApplicationModel> findPagination(@Param(value = "index") int index, @Param(value = "pageSize") int pageSize);

    long findCount();

    void createMaterials(LoanApplicationMaterialsModel loanApplicationMaterialsModel);

    List<LoanApplicationModel> findConsumeApply(@Param(value = "keyWord") String keyWord,
                                                @Param(value = "status") LoanApplicationStatus status,
                                                @Param(value = "startTime") Date startTime,
                                                @Param(value = "endTime") Date endTime,
                                                @Param(value = "index") int index,
                                                @Param(value = "pageSize") int pageSize);

    long findConsumeApplyCount(@Param(value = "keyWord") String keyWord,
                               @Param(value = "status") LoanApplicationStatus status,
                               @Param(value = "startTime") Date startTime,
                               @Param(value = "endTime") Date endTime);
}
