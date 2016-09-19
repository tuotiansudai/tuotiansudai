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

    void createMass(List<UserMembershipModel> userMembershipModels);

    void update(UserMembershipModel userMembershipModel);

    UserMembershipModel findById(long id);

    List<UserMembershipModel> findByLoginName(String loginName);

    List<UserMembershipItemView> findUserMembershipItemViews(@Param(value = "loginName") String loginName,
                                                             @Param(value = "mobile") String mobile,
                                                             @Param(value = "registerStartTime") Date registerStartTime,
                                                             @Param(value = "registerEndTime") Date registerEndTime,
                                                             @Param(value = "type") UserMembershipType userMembershipType,
                                                             @Param(value = "levels") List<Integer> levels,
                                                             @Param(value = "pageIndex") int pageIndex,
                                                             @Param(value = "pageSize") int pageSize);

    UserMembershipModel findActiveByLoginName(String loginName);

    UserMembershipModel findCurrentMaxByLoginName(String loginName);

    Double findRateByLoginName(String loginName);

    Integer findRealLevelByLoginName(String loginName);

    Long findMembershipPointByLoginName(String loginName);

    long countMembershipByLevel(int level);

    void updateMembershipPoint(@Param(value = "loginName") String loginName,
                               @Param(value = "membershipPoint") long membershipPoint);

    UserMembershipModel findByLoginNameByType(@Param(value = "loginName") String loginName,
                                              @Param(value = "type") UserMembershipType type);

    List<UserMembershipModel> findByLoginNameByMembershipId(@Param(value = "loginName") String loginName,
                                                      @Param(value = "membershipId") long membershipId);

    int findCountUserMembershipItemViews(@Param(value = "loginName") String loginName,
                                         @Param(value = "mobile") String mobile,
                                         @Param(value = "registerStartTime") Date registerStartTime,
                                         @Param(value = "registerEndTime") Date registerEndTime,
                                         @Param(value = "type") UserMembershipType userMembershipType,
                                         @Param(value = "levels") List<Integer> levels,
                                         @Param(value = "index") int index,
                                         @Param(value = "pageSize") int pageSize);

    long findByLoginNameOrInvestTime(@Param(value = "loginName") String loginName, @Param(value = "investTime") Date investTime);
}
