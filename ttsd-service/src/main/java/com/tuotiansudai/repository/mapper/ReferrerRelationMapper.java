package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.ReferrerRelationModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReferrerRelationMapper {

    void create(ReferrerRelationModel model);

    List<ReferrerRelationModel> findByLoginName(String loginName);

    List<ReferrerRelationModel> findByReferrerLoginName(String referrerLoginName);

    ReferrerRelationModel findByReferrerAndLoginName(@Param(value = "referrerLoginName") String referrerLoginName,
                                                     @Param(value = "loginName") String loginName);

    void delete(@Param(value = "referrerLoginName") String referrerLoginName,
                @Param(value = "loginName") String loginName);
}
