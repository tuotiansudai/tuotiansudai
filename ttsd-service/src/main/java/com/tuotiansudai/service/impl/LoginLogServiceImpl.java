package com.tuotiansudai.service.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.LoginLogPaginationItemDataDto;
import com.tuotiansudai.repository.mapper.LoginLogMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.LoginLogModel;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.service.LoginLogService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;

@Service
public class LoginLogServiceImpl implements LoginLogService {

    private static String LOGIN_LOG_TABLE_TEMPLATE = "login_log_{0}{1}";

    @Autowired
    private LoginLogMapper loginLogMapper;

    @Autowired
    private UserMapper userMapper;


    @Transactional
    @Override
    public void generateLoginLog(String loginNameOrMobile, Source source, String ip, String device, boolean loginSuccess) {
        UserModel userModel = userMapper.findByLoginNameOrMobile(loginNameOrMobile);
        if(userModel == null)
            return;
        LoginLogModel model = new LoginLogModel(userModel.getLoginName(), source, ip, device, loginSuccess);
        DateTime now = new DateTime();
        loginLogMapper.create(model, MessageFormat.format(LOGIN_LOG_TABLE_TEMPLATE, String.valueOf(now.getYear()), String.valueOf(now.getMonthOfYear())));
    }

    @Override
    public BasePaginationDataDto<LoginLogPaginationItemDataDto> getLoginLogPaginationData(String loginName, Boolean success, int index, int pageSize, int year, int month) {
        String table = MessageFormat.format(LOGIN_LOG_TABLE_TEMPLATE, String.valueOf(year), String.valueOf(month));

        long count = loginLogMapper.count(loginName, success, table);

        List<LoginLogModel> data = Lists.newArrayList();
        if (count > 0 ) {
            int totalPages = (int) (count % pageSize > 0 ? count / pageSize + 1 : count / pageSize);
            index = index > totalPages ? totalPages : index;
            data = loginLogMapper.getPaginationData(loginName, success, (index - 1) * pageSize, pageSize, table);
        }


        List<LoginLogPaginationItemDataDto> records = Lists.transform(data, new Function<LoginLogModel, LoginLogPaginationItemDataDto>() {
            @Override
            public LoginLogPaginationItemDataDto apply(LoginLogModel input) {
                return new LoginLogPaginationItemDataDto(input.getLoginName(), input.getSource(), input.getIp(), input.getDevice(), input.getLoginTime(), input.isSuccess());
            }
        });

        return new BasePaginationDataDto<>(index, pageSize, count, records);
    }
}
