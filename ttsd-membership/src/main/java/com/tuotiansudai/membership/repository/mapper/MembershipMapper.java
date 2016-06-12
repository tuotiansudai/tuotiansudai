package com.tuotiansudai.membership.repository.mapper;


import com.tuotiansudai.membership.repository.model.MembershipModel;
import org.springframework.stereotype.Repository;

@Repository
public interface MembershipMapper {

    MembershipModel findById(long id);

    MembershipModel findByExperience(long experience);

    MembershipModel findByLevel(int level);
}
