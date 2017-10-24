package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.PayrollModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PayrollDetailMapper {

    void create(PayrollModel payrollModel);

    void deleteByPayrollId(@Param(value = "payrollId") long payrollId);
}
