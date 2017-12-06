package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.enums.Role;
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

    void deleteByLoginNameAndRole(@Param("loginName") String loginName,
                                  @Param("role") Role role);

    List<String> findAllLoginNameByRole(@Param(value = "role") Role role);

    long findCountByRole(@Param(value = "role") Role role);
}
