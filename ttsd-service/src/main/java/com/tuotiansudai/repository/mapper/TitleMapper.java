package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.TitleModel;

import java.util.List;

public interface TitleMapper {
    void createTitle(TitleModel title);
    List<TitleModel> findAllTitles();
}
