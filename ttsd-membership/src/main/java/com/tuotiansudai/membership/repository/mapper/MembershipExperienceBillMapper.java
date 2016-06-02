package com.tuotiansudai.membership.repository.mapper;

import com.tuotiansudai.membership.repository.model.MembershipExperienceBillModel;
import org.springframework.stereotype.Repository;

@Repository
public interface MembershipExperienceBillMapper {

    void create(MembershipExperienceBillModel membershipExperienceBillModel);

    void update(MembershipExperienceBillModel membershipExperienceBillModel);

    MembershipExperienceBillModel findById(long id);
}
