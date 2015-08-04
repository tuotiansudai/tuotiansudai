package com.esoft.archer.user.service.impl;

import com.esoft.archer.system.controller.LoginUserInfo;
import com.esoft.archer.user.model.AdminOperationLog;
import com.esoft.archer.user.model.User;
import com.esoft.archer.user.model.UserBill;
import com.esoft.archer.user.service.AdminOperationLogService;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.SpringBeanUtil;
import org.springframework.stereotype.Service;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;

/**
 * Created by zzg on 15/8/4.
 */
@Service(value = "adminOperationLogService")
public class AdminOperationLogServiceImpl implements AdminOperationLogService {
    LoginUserInfo loginUserInfo = (LoginUserInfo) SpringBeanUtil.getBeanByName("loginUserInfo");

    private void LogOperation( String objId, String operationType, String description, boolean isSuccess) {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

        //ull.setLoginIp(FacesUtil.getRequestIp(request));
        //ull.setLoginTime(new Timestamp(System.currentTimeMillis()));
        System.out.println(description);
    }

    @Override
    public String generateUserInfoString(User user) {
        return user.getUsername();
    }

    @Override
    public String generateFinanceInfoString(UserBill userBill) {
        return userBill.getDetail();
    }

    @Override
    public void logUserOperation(String objId, String description, boolean isSuccess) {
        LogOperation(objId, AdminOperationLog.OPERATE_TYPE_USER, description, isSuccess);
    }

    @Override
    public void logFinanceOperation(String objId, String description, boolean isSuccess) {
        LogOperation(objId, AdminOperationLog.OPERATE_TYPE_FINANCE, description, isSuccess);
    }
}
