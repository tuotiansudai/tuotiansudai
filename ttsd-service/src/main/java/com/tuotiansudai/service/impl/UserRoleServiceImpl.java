package com.tuotiansudai.service.impl;


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

}
