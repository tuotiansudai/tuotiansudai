package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.PayrollDetailModel;
import com.tuotiansudai.repository.model.PayrollModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PayrollDetailMapper {

    void create(PayrollDetailModel payrollDetailModel);

    void deleteByPayrollId(@Param(value = "payrollId") long payrollId);

    List<PayrollDetailModel> findByPayrollId(@Param(value = "payrollId") long payrollId,
                                             @Param(value = "index") int index,
                                             @Param(value = "pageSize") int pageSize);

    long countByPayrollId(@Param(value = "payrollId") long payrollId);
}
