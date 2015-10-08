package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.ReferrerRelationModel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReferrerRelationMapper {

    void create(ReferrerRelationModel model);

    List<ReferrerRelationModel> findByLoginName(String loginName);

}
