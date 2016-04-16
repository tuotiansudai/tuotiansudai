package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.ReferrerRelationModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReferrerRelationMapper {

    void create(ReferrerRelationModel model);

    List<ReferrerRelationModel> findByLoginName(String loginName);

    List<ReferrerRelationModel> findByLoginNameAndLevel(@Param(value = "loginName") String loginName,
                                                        @Param(value = "level") int level);

    List<ReferrerRelationModel> findByReferrerLoginNameAndLevel(@Param(value = "referrerLoginName") String referrerLoginName,
                                                                @Param(value = "level") int level);

    ReferrerRelationModel findByReferrerAndLoginName(@Param(value = "referrerLoginName") String referrerLoginName,
                                                     @Param(value = "loginName") String loginName);

    void delete(@Param(value = "referrerLoginName") String referrerLoginName,
                @Param(value = "loginName") String loginName);

    int findReferrerCountByReferrerLoginName(@Param(value = "referrerLoginName") String referrerLoginName);



}
