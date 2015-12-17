package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.ReferrerManageView;
import com.tuotiansudai.repository.model.ReferrerRelationView;
import com.tuotiansudai.repository.model.Role;
import com.tuotiansudai.repository.model.Source;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ReferrerManageMapper {

    int findReferrerManageCount(@Param("referrerLoginName") String referrerLoginName, @Param("investLoginName") String investLoginName,
                                @Param("investStartTime") Date investStartTime, @Param("investEndTime") Date investEndTime,
                                @Param("level") Integer level, @Param("rewardStartTime") Date rewardStartTime,
                                @Param("rewardEndTime") Date rewardEndTime, @Param("role") Role role, @Param("source") Source source);

    long findReferrerManageInvestAmountSum(@Param("referrerLoginName") String referrerLoginName, @Param("investLoginName") String investLoginName,
                                @Param("investStartTime") Date investStartTime, @Param("investEndTime") Date investEndTime,
                                @Param("level") Integer level, @Param("rewardStartTime") Date rewardStartTime,
                                @Param("rewardEndTime") Date rewardEndTime, @Param("role") Role role, @Param("source") Source source);

    long findReferrerManageRewardAmountSum(@Param("referrerLoginName") String referrerLoginName, @Param("investLoginName") String investLoginName,
                                @Param("investStartTime") Date investStartTime, @Param("investEndTime") Date investEndTime,
                                @Param("level") Integer level, @Param("rewardStartTime") Date rewardStartTime,
                                @Param("rewardEndTime") Date rewardEndTime, @Param("role") Role role, @Param("source") Source source);

    List<ReferrerManageView> findReferrerManage(@Param("referrerLoginName") String referrerLoginName, @Param("investLoginName") String investLoginName,
                                                @Param("investStartTime") Date investStartTime, @Param("investEndTime") Date investEndTime,
                                                @Param("level") Integer level, @Param("rewardStartTime") Date rewardStartTime,
                                                @Param("rewardEndTime") Date rewardEndTime, @Param("role") Role role, @Param("source") Source source,
                                                @Param("startLimit") int startLimit, @Param("endLimit") int endLimit);

    List<ReferrerRelationView> findReferRelationList(@Param("referrerLoginName") String referrerLoginName,
                                                     @Param("loginName") String loginName,
                                                     @Param("referStartTime") Date referStartTime,
                                                     @Param("referEndTime") Date referEndTime,
                                                     @Param("level") String level,
                                                     @Param("startLimit") int startLimit,
                                                     @Param("endLimit") int endLimit);

    int findReferRelationCount(@Param("referrerLoginName") String referrerLoginName,
                               @Param("loginName") String loginName,
                               @Param("referStartTime") Date referStartTime,
                               @Param("referEndTime") Date referEndTime,
                               @Param("level") String level);

    List<ReferrerManageView> findReferInvestList(@Param("referrerLoginName") String referrerLoginName,
                                                 @Param("loginName") String loginName,
                                                 @Param("investStartTime") Date referStartTime,
                                                 @Param("investEndTime") Date referEndTime,
                                                 @Param("level") String level,
                                                 @Param("startLimit") int startLimit,
                                                 @Param("endLimit") int endLimit);

    int findReferInvestCount(@Param("referrerLoginName") String referrerLoginName,
                             @Param("loginName") String loginName,
                             @Param("investStartTime") Date referStartTime,
                             @Param("investEndTime") Date referEndTime,
                             @Param("level") String level);

    long findReferInvestTotalAmount(@Param("referrerLoginName") String referrerLoginName,
                                    @Param("loginName") String loginName,
                                    @Param("investStartTime") Date referStartTime,
                                    @Param("investEndTime") Date referEndTime,
                                    @Param("level") String level);
}
