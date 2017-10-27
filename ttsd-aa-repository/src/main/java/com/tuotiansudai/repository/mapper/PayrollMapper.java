package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.PayrollModel;
import com.tuotiansudai.repository.model.PayrollStatusType;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PayrollMapper {

    void create(PayrollModel payrollModel);

    void update(PayrollModel payrollModel);

    PayrollModel findById(
            @Param(value = "id") long id);

    int updateStatus(
            @Param(value = "id") long id,
            @Param(value = "status") PayrollStatusType status);

}
