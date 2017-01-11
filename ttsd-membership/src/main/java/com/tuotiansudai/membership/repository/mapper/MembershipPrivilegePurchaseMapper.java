package com.tuotiansudai.membership.repository.mapper;

import com.tuotiansudai.membership.repository.model.MembershipPrivilegePurchaseModel;
import org.springframework.stereotype.Repository;

@Repository
public interface MembershipPrivilegePurchaseMapper {
    int create(MembershipPrivilegePurchaseModel membershipPrivilegePurchaseModel);

    int update(MembershipPrivilegePurchaseModel membershipPrivilegePurchaseModel);

    MembershipPrivilegePurchaseModel findById(long id);
}
