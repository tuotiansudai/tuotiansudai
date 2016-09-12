package com.tuotiansudai.membership.repository.mapper;

import com.tuotiansudai.membership.repository.model.MembershipGiveModel;
import com.tuotiansudai.membership.repository.model.MembershipModel;

import java.util.List;

public interface MembershipGiveMapper {
    void create(MembershipModel membershipModel);

    void update(MembershipModel membershipModel);

    List<MembershipGiveModel> findSome(int index, int pageSize);
}
