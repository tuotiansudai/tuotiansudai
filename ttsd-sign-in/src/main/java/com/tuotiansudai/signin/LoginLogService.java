package com.tuotiansudai.signin;

import com.tuotiansudai.repository.mapper.LoginLogMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.LoginLogModel;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.UserModel;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Date;

@Service
public class LoginLogService {

    private final static String LOGIN_LOG_TABLE_TEMPLATE = "login_log_{0}";

    @Autowired
    private LoginLogMapper loginLogMapper;

    @Autowired
    private UserMapper userMapper;

    @Transactional
    public void generateLoginLog(String loginNameOrMobile, Source source, String ip, String device, boolean loginSuccess) {
        UserModel userModel = userMapper.findByLoginNameOrMobile(loginNameOrMobile);
        if(userModel == null)
            return;
        LoginLogModel model = new LoginLogModel(userModel.getLoginName(), source, ip, device, loginSuccess);

        loginLogMapper.create(model, this.getLoginLogTableName(new Date()));

    }

    private String getLoginLogTableName(Date date) {
        return MessageFormat.format(LOGIN_LOG_TABLE_TEMPLATE, new DateTime(date).toString("yyyyMM"));
    }
}
