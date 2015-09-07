package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.UserRoleModel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleMapper {

    void create(UserRoleModel userRoleModel);

    List<UserRoleModel> findByLoginName(String loginName);
}
