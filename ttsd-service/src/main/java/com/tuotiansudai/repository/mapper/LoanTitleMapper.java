package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.LoanTitleModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LoanTitleMapper {
    void create(LoanTitleModel title);
    List<LoanTitleModel> findAll();
    LoanTitleModel findById(@Param(value = "id") long id);
}
