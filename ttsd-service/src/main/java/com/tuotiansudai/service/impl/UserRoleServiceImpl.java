package com.tuotiansudai.service.impl;


import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.mapper.UserRoleMapper;
import com.tuotiansudai.repository.model.Role;
import com.tuotiansudai.repository.model.UserRoleModel;
import com.tuotiansudai.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleServiceImpl implements UserRoleService{

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean judgeUserRoleExist(String loginName,Role role) {
        List<UserRoleModel> userRoleModels = userRoleMapper.findByLoginName(loginName);
        for (UserRoleModel userRoleModel : userRoleModels) {
            if (userRoleModel.getRole() == role) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Role> findRoleNameByLoginName(String loginName) {
        return Lists.transform(userRoleMapper.findByLoginName(loginName), new Function<UserRoleModel, Role>() {
            @Override
            public Role apply(UserRoleModel input) {
                return input.getRole();
            }
        });
    }

    @Override
    public List<String> queryAllAgent() {
        return userMapper.findAllByRole(Maps.newHashMap(ImmutableMap.<String, Object>builder().put("role", Role.AGENT).put("districtName", Lists.newArrayList()).build()));
    }

}
