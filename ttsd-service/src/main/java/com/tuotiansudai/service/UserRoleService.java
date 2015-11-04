package com.tuotiansudai.service;


import com.tuotiansudai.repository.model.Role;

public interface UserRoleService {

    boolean judgeUserRoleExist(String loginName,Role role);
}
