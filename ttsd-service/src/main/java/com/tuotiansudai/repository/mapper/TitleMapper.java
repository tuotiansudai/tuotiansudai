package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.TitleModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TitleMapper {
    void create(TitleModel title);
    List<TitleModel> findAllTitles();
    TitleModel findTitleById(@Param(value = "id") long id);
}
