package com.tuotiansudai.membership.repository.mapper;


import com.tuotiansudai.membership.repository.model.UserMembershipItemView;
import com.tuotiansudai.membership.repository.model.UserMembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipType;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface UserMembershipMapper {

    void create(UserMembershipModel userMembershipModel);

    void createBatch(List<UserMembershipModel> userMembershipModels);

    void update(UserMembershipModel userMembershipModel);

    UserMembershipModel findById(long id);

    List<UserMembershipModel> findByLoginName(String loginName);

    List<UserMembershipModel> findByLoginNameAndMembershipId(@Param(value = "loginName") String loginName,
                                                             @Param(value = "membershipId") long membershipId);

    long countMembershipByLevel(int level);

    List<UserMembershipItemView> findUserMembershipItemViews(@Param(value = "loginName") String loginName,
                                                             @Param(value = "mobile") String mobile,
                                                             @Param(value = "registerStartTime") Date registerStartTime,
                                                             @Param(value = "registerEndTime") Date registerEndTime,
                                                             @Param(value = "type") UserMembershipType userMembershipType,
                                                             @Param(value = "levels") List<Integer> levels,
                                                             @Param(value = "pageIndex") int pageIndex,
                                                             @Param(value = "pageSize") int pageSize);

    UserMembershipModel findCurrentMaxByLoginName(String loginName);

    UserMembershipModel findCurrentUpgradeMaxByLoginName(String loginName);

    int findCountUserMembershipItemViews(@Param(value = "loginName") String loginName,
                                         @Param(value = "mobile") String mobile,
                                         @Param(value = "registerStartTime") Date registerStartTime,
                                         @Param(value = "registerEndTime") Date registerEndTime,
                                         @Param(value = "type") UserMembershipType userMembershipType,
                                         @Param(value = "levels") List<Integer> levels,
                                         @Param(value = "index") int index,
                                         @Param(value = "pageSize") int pageSize);

    List<UserMembershipModel> findExpiredUserMembership(@Param(value = "expiredDate") Date expiredDate);

    List<UserMembershipModel> findGiveMembershipsByLoginNameAndGiveId(@Param(value = "membershipGiveId") long membershipGiveId,
                                                                      @Param(value = "loginName") String loginName,
                                                                      @Param(value = "index") int index,
                                                                      @Param(value = "pageSize") int pageSize);

    long findCountGiveMembershipsByLoginNameAndGiveId(@Param(value = "membershipGiveId") long membershipGiveId,
                                                      @Param(value = "loginName") String loginName);

    long sumSuccessInvestAmountByLoginName(@Param(value = "loanId") Long loanId, @Param(value = "loginName") String loginName);

    UserMembershipModel findByLoginNameByType(@Param(value = "loginName") String loginName,
                                              @Param(value = "type") UserMembershipType type);

    long findCountMembershipByLevel(@Param(value = "level") long level);

    List<String> findLoginNameMembershipByLevel(@Param(value = "level") long level);

}
