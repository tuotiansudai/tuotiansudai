package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.LoanTitleModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TitleMapper {
    void create(LoanTitleModel title);
    List<LoanTitleModel> find();
    LoanTitleModel findTitleById(@Param(value = "id") long id);
}
