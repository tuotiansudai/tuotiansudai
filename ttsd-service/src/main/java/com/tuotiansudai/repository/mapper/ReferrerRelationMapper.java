package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.ReferrerRelationModel;

import java.util.List;

public interface ReferrerRelationMapper {

    void create(ReferrerRelationModel model);

    List<ReferrerRelationModel> findByLoginName(String loginName);

}
