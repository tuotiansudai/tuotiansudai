package com.tuotiansudai.service;


import com.tuotiansudai.repository.model.Role;

import java.util.List;

public interface UserRoleService {

    boolean judgeUserRoleExist(String loginName,Role role);

    List<Role> findRoleNameByLoginName(String loginName);
}
