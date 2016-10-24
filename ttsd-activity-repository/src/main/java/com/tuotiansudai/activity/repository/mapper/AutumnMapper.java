package com.tuotiansudai.activity.repository.mapper;

import com.tuotiansudai.activity.model.AutumnReferrerRelationView;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;

@Repository
public interface AutumnMapper {


    List<AutumnReferrerRelationView> findByLoginNameAndLevel(@Param(value = "loginName") String loginName,
                                                             @Param(value = "level") int level);

    List<AutumnReferrerRelationView> findByReferrerLoginNameAndLevelAndRegisterTime(@Param(value = "referrerLoginName") String referrerLoginName,
                                                                                    @Param(value = "startTime") Date startTime,
                                                                                    @Param(value = "endTime") Date endTime);

    AutumnReferrerRelationView findByReferrerAndLoginName(@Param(value = "referrerLoginName") String referrerLoginName,
                                                          @Param(value = "loginName") String loginName);

}
