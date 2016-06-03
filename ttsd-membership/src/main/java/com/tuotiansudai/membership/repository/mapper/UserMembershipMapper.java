package com.tuotiansudai.membership.repository.mapper;


import com.tuotiansudai.membership.repository.model.UserMembershipModel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMembershipMapper {

    void create(UserMembershipModel userMembershipModel);

    void update(UserMembershipModel userMembershipModel);

    UserMembershipModel findById(long id);

    List<UserMembershipModel> findByLoginName(String loginName);

    UserMembershipModel findActiveByLoginName(String loginName);

    Double findRateByLoginName(String loginName);
}
