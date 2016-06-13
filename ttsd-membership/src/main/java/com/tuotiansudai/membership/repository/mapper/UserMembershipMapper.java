package com.tuotiansudai.membership.repository.mapper;


import com.tuotiansudai.membership.repository.model.UserMembershipModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface UserMembershipMapper {

    void create(UserMembershipModel userMembershipModel);

    void update(UserMembershipModel userMembershipModel);

    UserMembershipModel findById(long id);

    List<UserMembershipModel> findByLoginName(String loginName);

    UserMembershipModel findActiveByLoginName(String loginName);

    Double findRateByLoginName(String loginName);

    Integer findRealLevelByLoginName(String loginName);

    UserMembershipModel findByMembershipId(long membershipId);

    Long findMembershipPointByLoginName(String loginName);

    void updateMembershipPoint(@Param(value = "loginName") String loginName,
                               @Param(value = "membershipPoint") long membershipPoint);

    String findAccountIdentityNumberByLoginName(String loginName);

    long sumSuccessInvestAmountByLoginName(@Param(value = "loginName") String loginName);

    Date findAccountRegisterTimeByLoginName(String loginName);
}
