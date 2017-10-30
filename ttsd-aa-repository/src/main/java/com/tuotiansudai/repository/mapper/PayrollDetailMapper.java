package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.PayrollDetailModel;
import com.tuotiansudai.repository.model.PayrollPayStatus;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PayrollDetailMapper {

    int create(@Param("payrollDetails") List<PayrollDetailModel> payrollDetailModels);

    void deleteByPayrollId(@Param(value = "payrollId") long payrollId);

    PayrollDetailModel findById(@Param(value = "id") long id);

    List<PayrollDetailModel> findByPayrollId(@Param(value = "payrollId") long payrollId);

    int updateStatus(@Param(value = "id") long id,
                     @Param(value = "status") PayrollPayStatus status);
}
