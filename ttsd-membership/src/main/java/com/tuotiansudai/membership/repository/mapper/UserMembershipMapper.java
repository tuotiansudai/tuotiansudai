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

    int findCountUserMembershipItemViews(@Param(value = "loginName") String loginName,
                                         @Param(value = "mobile") String mobile,
                                         @Param(value = "registerStartTime") Date registerStartTime,
                                         @Param(value = "registerEndTime") Date registerEndTime,
                                         @Param(value = "type") UserMembershipType userMembershipType,
                                         @Param(value = "levels") List<Integer> levels,
                                         @Param(value = "index") int index,
                                         @Param(value = "pageSize") int pageSize);

    List<UserMembershipModel> findExpiredUserMembership(@Param(value = "expiredDate") Date expiredDate);
}
