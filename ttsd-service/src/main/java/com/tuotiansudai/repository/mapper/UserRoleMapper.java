package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.UserRoleModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleMapper {

    void create(@Param("userRoles") List<UserRoleModel> userRoleModels);

    List<UserRoleModel> findByLoginName(String loginName);

    void deleteByLoginName(@Param("loginName") String loginName);
}
