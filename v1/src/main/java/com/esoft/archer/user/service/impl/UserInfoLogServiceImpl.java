package com.esoft.archer.user.service.impl;

import com.esoft.archer.system.controller.LoginUserInfo;
import com.esoft.archer.user.model.*;
import com.esoft.archer.user.service.UserInfoLogService;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.IdGenerator;
import com.esoft.core.util.SpringBeanUtil;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzg on 15/8/4.
 */
@Service(value = "userInfoLogService")
public class UserInfoLogServiceImpl implements UserInfoLogService {

    @Resource
    private LoginUserInfo loginUserInfo;

    @Resource
    private HibernateTemplate ht;

    @Override
    public String generateUserInfoString(User user) {
        StringBuffer sb = new StringBuffer();
        sb.append("UserId:"+user.getId()+"; ");
        sb.append("UserName:"+user.getUsername()+"; ");
        sb.append("RealName:"+user.getRealname()+"; ");
        sb.append("Email:"+user.getEmail()+"; ");
        sb.append("Sex:"+user.getSex()+"; ");
        sb.append("Status:"+user.getStatus()+"; ");
        sb.append("MobileNumber:"+user.getMobileNumber()+"; ");
        sb.append("BindIp:"+user.getBindIP()+"; ");
        sb.append("IdCard:"+user.getIdCard()+"; ");
        sb.append("CurrAddress:"+user.getCurrentAddress()+"; ");
        sb.append("NickName:" + user.getNickname() + "; ");
        sb.append("Roles:" + getRoleNames(user.getRoles()));
        return sb.toString();
    }

    private String getRoleNames(List<Role> roles){
        if(roles ==null || roles.size() ==0){
            return "[]";
        }
        List<String> roleNames = new ArrayList<>(roles.size());
        for(Role r:roles){
            roleNames.add(r.getName());
        }
        String s = ArrayUtils.toString(roleNames);
        return s;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void logUserOperation(String objId, String description, boolean isSuccess) {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String userIp = FacesUtil.getRequestIp(request);
        Timestamp operateTime = new Timestamp(System.currentTimeMillis());
        String userId = loginUserInfo.getLoginUserId();

        UserInfoLog log = new UserInfoLog();
        log.setId(IdGenerator.randomUUID());
        log.setDescription(description);
        log.setIp(userIp);
        log.setSuccess(isSuccess);
        log.setObjId(objId);
        log.setOperateTime(operateTime);
        log.setUserId(userId);

        ht.save(log);
    }

}
