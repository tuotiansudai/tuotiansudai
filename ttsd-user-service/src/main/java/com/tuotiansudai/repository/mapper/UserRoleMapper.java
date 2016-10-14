package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.Role;
import com.tuotiansudai.repository.model.UserRoleModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleMapper {

    int create(@Param("userRoles") List<UserRoleModel> userRoleModels);

    List<UserRoleModel> findByLoginName(String loginName);

    List<UserRoleModel> findByMobile(String mobile);

    UserRoleModel findByLoginNameAndRole(@Param("loginName") String loginName,
                                         @Param("role") Role role);

    void deleteByLoginName(@Param("loginName") String loginName);

}
