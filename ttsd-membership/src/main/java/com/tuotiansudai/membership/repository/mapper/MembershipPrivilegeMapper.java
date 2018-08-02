package com.tuotiansudai.membership.repository.mapper;

import com.tuotiansudai.membership.repository.model.MembershipPrivilegeExpiredUsersView;
import com.tuotiansudai.membership.repository.model.MembershipPrivilegeModel;
import com.tuotiansudai.repository.model.Source;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface MembershipPrivilegeMapper {
    int create(MembershipPrivilegeModel membershipPrivilegeModel);

    int update(MembershipPrivilegeModel membershipPrivilegeModel);

    MembershipPrivilegeModel findById(long id);

    MembershipPrivilegeModel findValidPrivilegeModelByLoginName(@Param(value = "loginName") String loginName,
                                                                @Param(value = "currentDate") Date currentDate);

    List<MembershipPrivilegeExpiredUsersView> findMembershipPrivilegeExpiredUsers();

}
