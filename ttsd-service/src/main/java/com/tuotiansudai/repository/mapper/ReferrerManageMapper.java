package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.ReferrerManageView;
import com.tuotiansudai.repository.model.Role;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ReferrerManageMapper {

    int findReferrerManageCount(@Param("referrerLoginName") String referrerLoginName,@Param("investLoginName") String investLoginName,
                                @Param("investStartTime") Date investStartTime,@Param("investEndTime") Date investEndTime,
                                @Param("level") Integer level,@Param("rewardStartTime") Date rewardStartTime,
                                @Param("rewardEndTime") Date rewardEndTime,@Param("role") Role role);

    List<ReferrerManageView> findReferrerManage(@Param("referrerLoginName") String referrerLoginName,@Param("investLoginName") String investLoginName,
                                                @Param("investStartTime") Date investStartTime,@Param("investEndTime") Date investEndTime,
                                                @Param("level") Integer level,@Param("rewardStartTime") Date rewardStartTime,
                                                @Param("rewardEndTime") Date rewardEndTime,@Param("role") Role role,
                                                @Param("startLimit") int startLimit,@Param("endLimit") int endLimit);

}
