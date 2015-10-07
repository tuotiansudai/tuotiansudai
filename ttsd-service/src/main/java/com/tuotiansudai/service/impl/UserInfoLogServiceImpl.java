package com.tuotiansudai.service.impl;

import com.tuotiansudai.repository.mapper.UserInfoLogMapper;
import com.tuotiansudai.repository.model.UserInfoLogModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserRoleModel;
import com.tuotiansudai.service.UserInfoLogService;
import com.tuotiansudai.utils.LoginUserInfo;
import com.tuotiansudai.utils.RequestIPParser;
import com.tuotiansudai.utils.UUIDGenerator;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserInfoLogServiceImpl implements UserInfoLogService {
    @Autowired
    private UserInfoLogMapper userInfoLogMapper;

    @Override
    public String generateUserInfoString(UserModel userModel,List<UserRoleModel> userRoles, UserModel oldUserModel,List<UserRoleModel> oldUserRoles) {
        StringBuffer sb = new StringBuffer();
        sb.append("UserId:"+userModel.getId()+"; ");
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
    public void logUserOperation(String objId, String description, boolean isSuccess,HttpServletRequest request) {
        String userIp = RequestIPParser.getRequestIp(request);
        Timestamp operateTime = new Timestamp(System.currentTimeMillis());
        String userId = LoginUserInfo.getLoginName();
        UserInfoLogModel log = new UserInfoLogModel();
        log.setId(UUIDGenerator.generate());
        log.setDescription(description);
        log.setIp(userIp);
        log.setSuccess(isSuccess);
        log.setObjId(objId);
        log.setOperateTime(operateTime);
        log.setUserId(userId);
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
