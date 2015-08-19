package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.TitleModel;

import java.util.List;

/**
 * Created by tuotian on 15/8/18.
 */
public interface TitleMapper {
    List<TitleModel> findAllTitles();
}
