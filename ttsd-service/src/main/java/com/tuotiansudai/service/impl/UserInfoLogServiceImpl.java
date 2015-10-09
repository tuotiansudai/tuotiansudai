package com.tuotiansudai.service.impl;

import com.tuotiansudai.repository.mapper.UserInfoLogMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.mapper.UserRoleMapper;
import com.tuotiansudai.repository.model.UserInfoLogModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserRoleModel;
import com.tuotiansudai.service.UserInfoLogService;
import com.tuotiansudai.utils.IdGenerator;
import com.tuotiansudai.utils.LoginUserInfo;
import com.tuotiansudai.utils.UUIDGenerator;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserInfoLogServiceImpl implements UserInfoLogService {
    @Autowired
    private UserInfoLogMapper userInfoLogMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;

    private String generateDescription(UserModel userModel,List<UserRoleModel> userRoles) {
        UserModel oldUserModel = userMapper.findByLoginName(userModel.getLoginName());
        List<UserRoleModel> oldUserRoles = userRoleMapper.findByLoginName(userModel.getLoginName());
        StringBuffer sb = new StringBuffer();
        sb.append("UserId:"+userModel.getLoginName()+"; ");
        sb.append(buildDiffInfo("Email",userModel.getEmail(),oldUserModel.getEmail()));
        sb.append(buildDiffInfo("Status", userModel.getStatus().name(), oldUserModel.getStatus().name()));
        sb.append(buildDiffInfo("Mobile", userModel.getMobile(), oldUserModel.getMobile()));
        sb.append(buildDiffInfo("Referrer",userModel.getReferrer(), oldUserModel.getReferrer()));
        sb.append(buildDiffInfo("Roles",getRoleNames(userRoles), getRoleNames(oldUserRoles)));
        return sb.toString();

    }

    private String buildDiffInfo(String label, String newValue, String oldValue){
        if(StringUtils.isBlank(newValue)){ newValue = ""; }
        if(StringUtils.isBlank(oldValue)){ oldValue = ""; }
        if(!StringUtils.equalsIgnoreCase(newValue, oldValue)) {
            return MessageFormat.format("{0}: {1} => {2};", label, oldValue, newValue);
        } else {
            return "";
        }
    }

    @Override
    @Transactional
    public void logUserOperation(UserModel userModel,List<UserRoleModel> userRoles,String userIp) {
        String description = generateDescription(userModel, userRoles);
        Timestamp operateTime = new Timestamp(System.currentTimeMillis());
        String loginName = LoginUserInfo.getLoginName();
        UserInfoLogModel log = new UserInfoLogModel();
        log.setId(new IdGenerator().generate());
        log.setDescription(description);
        log.setIp(userIp);
        log.setOperateTime(operateTime);
        log.setLoginName(loginName);
        userInfoLogMapper.create(log);

    }
    private String getRoleNames(List<UserRoleModel> roles){
        if(roles ==null || roles.size() ==0){
            return "[]";
        }
        List<String> roleNames = new ArrayList<>(roles.size());
        for(UserRoleModel r:roles){
            roleNames.add(r.getRole().name());
        }
        String s = ArrayUtils.toString(roleNames);
        return s;
    }

}
