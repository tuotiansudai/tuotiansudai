package com.tuotiansudai.membership.repository.mapper;

import com.tuotiansudai.membership.repository.model.MembershipExperienceBillModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface MembershipExperienceBillMapper {

    void create(MembershipExperienceBillModel membershipExperienceBillModel);

    void createBatch(List<MembershipExperienceBillModel> membershipExperienceBillModels);

    void update(MembershipExperienceBillModel membershipExperienceBillModel);

    MembershipExperienceBillModel findById(long id);

    List<MembershipExperienceBillModel> findMembershipExperienceBillByLoginName(@Param(value = "loginName") String loginName,
                                                                                @Param(value = "startTime") Date startTime,
                                                                                @Param(value = "endTime") Date endTime,
                                                                                @Param(value = "index") Integer index,
                                                                                @Param(value = "pageSize") Integer pageSize);

    long findMembershipExperienceBillCountByLoginName(@Param(value = "loginName") String loginName,
                                                      @Param(value = "startTime") Date startTime,
                                                      @Param(value = "endTime") Date endTime);

}
