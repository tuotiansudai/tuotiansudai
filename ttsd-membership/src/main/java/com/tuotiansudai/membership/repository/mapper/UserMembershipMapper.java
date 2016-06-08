package com.tuotiansudai.membership.repository.mapper;


import com.tuotiansudai.membership.repository.model.UserMembershipItemModel;
import com.tuotiansudai.membership.repository.model.UserMembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipType;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface UserMembershipMapper {

    void create(UserMembershipModel userMembershipModel);

    void update(UserMembershipModel userMembershipModel);

    UserMembershipModel findById(long id);

    List<UserMembershipModel> findByLoginName(String loginName);

    List<UserMembershipItemModel> findUserMembershipItemsByLoginNameAndMobileAndRegisterTimeAndTypeAndVipLevel(@Param(value = "loginName") String loginName,
                                                                                                               @Param(value = "mobile") String mobile,
                                                                                                               @Param(value = "registerStartTime") Date registerStartTime,
                                                                                                               @Param(value = "registerEndTime") Date registerEndTime,
                                                                                                               @Param(value = "type") UserMembershipType userMembershipType,
                                                                                                               @Param(value = "levels") List<Integer> levels);
}
