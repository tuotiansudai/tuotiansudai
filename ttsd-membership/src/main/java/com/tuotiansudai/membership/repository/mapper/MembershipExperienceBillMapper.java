package com.tuotiansudai.membership.repository.mapper;

import com.tuotiansudai.membership.repository.model.MembershipExperienceBillModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MembershipExperienceBillMapper {

    void create(MembershipExperienceBillModel membershipExperienceBillModel);

    void update(MembershipExperienceBillModel membershipExperienceBillModel);

    MembershipExperienceBillModel findById(long id);

    List<MembershipExperienceBillModel> findMembershipExperienceBillByLoginName(@Param(value = "loginName") String loginName,
                                                                                @Param(value = "index") int index,
                                                                                @Param(value = "pageSize") int pageSize);

    long findMembershipExperienceBillCountByLoginName(@Param(value = "loginName") String loginName);

}
