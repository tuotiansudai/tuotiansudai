package com.tuotiansudai.membership.repository.mapper;


import com.tuotiansudai.membership.repository.model.MembershipExperienceBillModel;
import com.tuotiansudai.membership.repository.model.UserMembershipModel;
import org.springframework.stereotype.Repository;

@Repository
public interface MembershipExperienceBillMapper {

    void create(MembershipExperienceBillModel membershipExperienceBillModel);

    void update(MembershipExperienceBillModel membershipExperienceBillModel);

    void delete(MembershipExperienceBillModel membershipExperienceBillModel);

    MembershipExperienceBillModel findById(long id);
}
