package com.esoft.archer.user.service.impl;

import com.esoft.archer.user.model.Role;
import com.esoft.archer.user.model.User;
import com.esoft.archer.user.model.UserInfoLog;
import com.esoft.archer.user.service.UserInfoLogService;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.IdGenerator;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@Service(value = "userInfoLogService")
public class UserInfoLogServiceImpl implements UserInfoLogService {

    @Resource
    private HibernateTemplate ht;

    @Override
    public String generateUserInfoString(User user, User oldUserInfo) {
        StringBuffer sb = new StringBuffer();
        sb.append("UserId:"+user.getId()+"; ");
        sb.append(buildDiffInfo("RealName",user.getRealname(),oldUserInfo.getRealname()));
        sb.append(buildDiffInfo("Email", user.getEmail(), oldUserInfo.getEmail()));
        sb.append(buildDiffInfo("Sex", user.getSex(), oldUserInfo.getSex()));
        sb.append(buildDiffInfo("Status", user.getStatus(), oldUserInfo.getStatus()).replace("0", "禁用").replace("1", "正常"));
        sb.append(buildDiffInfo("MobileNumber", user.getMobileNumber(), oldUserInfo.getMobileNumber()));
        sb.append(buildDiffInfo("BindIp",user.getBindIP(), oldUserInfo.getBindIP()));
        sb.append(buildDiffInfo("IdCard",user.getIdCard(), oldUserInfo.getIdCard()));
        sb.append(buildDiffInfo("CurrAddress", user.getCurrentAddress(), oldUserInfo.getCurrentAddress()));
        sb.append(buildDiffInfo("NickName",user.getNickname(), oldUserInfo.getNickname()));
        sb.append(buildDiffInfo("Roles",getRoleNames(user.getRoles()), getRoleNames(oldUserInfo.getRoles())));
        sb.append(buildDiffInfo("Referrer",user.getReferrer(), oldUserInfo.getReferrer()));
        sb.append(buildDiffInfo("Source",user.getSource(), oldUserInfo.getSource()));
        return sb.toString();
    }

    private String buildDiffInfo(String label, String newValue, String oldValue){
        if(StringUtils.isBlank(newValue)){ newValue = ""; }
        if(StringUtils.isBlank(oldValue)){ oldValue = ""; }
        if(!StringUtils.equalsIgnoreCase(newValue, oldValue)) {
            return MessageFormat.format("{0}: {1} => {2};",label, oldValue, newValue);
        } else {
            return "";
        }
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
        String userId = String.valueOf(FacesUtil.getExpressionValue("#{loginUserInfo.loginUserId}"));
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
