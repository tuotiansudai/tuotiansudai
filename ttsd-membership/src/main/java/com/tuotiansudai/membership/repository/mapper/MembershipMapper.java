package com.tuotiansudai.membership.repository.mapper;


import com.tuotiansudai.membership.repository.model.MembershipModel;
import org.springframework.stereotype.Repository;

@Repository
public interface MembershipMapper {

    void create(MembershipModel membershipModel);

    void update(MembershipModel membershipModel);

    MembershipModel findById(long id);
}
