package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.enums.Role;
import com.tuotiansudai.repository.model.UserFundView;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFundMapper {

    UserFundView findByLoginName(@Param(value = "loginName") String loginName,
                                 @Param(value = "roleType") Role roleType);

    UserFundView findUmpByLoginName(@Param(value = "loginName") String loginName);
}
