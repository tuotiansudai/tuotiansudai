package com.tuotiansudai.membership.repository.mapper;

import com.tuotiansudai.membership.repository.model.MembershipGiveModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MembershipGiveMapper {
    void create(MembershipGiveModel membershipGiveModel);

    void update(MembershipGiveModel membershipGiveModel);

    MembershipGiveModel findById(@Param(value = "id") long id);

    List<MembershipGiveModel> findSome(@Param(value = "index") int index, @Param(value = "pageSize") int pageSize);

    void delete(@Param(value = "id") long id);
}
