package com.tuotiansudai.membership.repository.mapper;


import com.tuotiansudai.membership.repository.model.UserMembershipModel;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMembershipMapper {

    void create(UserMembershipModel userMembershipModel);

    void update(UserMembershipModel userMembershipModel);

    void delete(UserMembershipModel userMembershipModel);

    UserMembershipModel findById(long id);
}
