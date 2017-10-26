package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.PayrollModel;
import org.springframework.stereotype.Repository;

@Repository
public interface PayrollMapper {

    void create(PayrollModel payrollModel);

    void update(PayrollModel payrollModel);

    PayrollModel findById(long id);
}
