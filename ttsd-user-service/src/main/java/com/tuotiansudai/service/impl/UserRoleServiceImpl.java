package com.tuotiansudai.service.impl;


import com.tuotiansudai.enums.Role;
import com.tuotiansudai.repository.mapper.UserRoleMapper;
import com.tuotiansudai.repository.model.UserRoleModel;
import com.tuotiansudai.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public boolean judgeUserRoleExist(String loginName, Role role) {
        List<UserRoleModel> userRoleModels = userRoleMapper.findByLoginName(loginName);
        return userRoleModels.stream().anyMatch(userRoleModel -> userRoleModel.getRole() == role);
    }

    @Override
    public List<Role> findRoleNameByLoginName(String loginName) {
        return userRoleMapper.findByLoginName(loginName).stream().map(UserRoleModel::getRole).collect(Collectors.toList());
    }
}
