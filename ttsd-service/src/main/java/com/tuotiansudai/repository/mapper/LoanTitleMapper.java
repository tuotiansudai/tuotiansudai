package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.LoanTitleModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanTitleMapper {
    void create(LoanTitleModel title);

    List<LoanTitleModel> findAll();

    LoanTitleModel findById(@Param(value = "id") long id);
}
