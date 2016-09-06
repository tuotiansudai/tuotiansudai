package com.tuotiansudai.service.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.LoginLogPaginationItemDataDto;
import com.tuotiansudai.repository.mapper.LoginLogMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.LoginLogView;
import com.tuotiansudai.service.LoginLogService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Service
public class LoginLogServiceImpl implements LoginLogService {

    private final static String LOGIN_LOG_TABLE_TEMPLATE = "login_log_{0}";

    @Autowired
    private LoginLogMapper loginLogMapper;

    @Override
    public BasePaginationDataDto<LoginLogPaginationItemDataDto> getLoginLogPaginationData(String mobile, Boolean success, int index, int pageSize, int year, int month) {
        String loginLogTableName = this.getLoginLogTableName(new DateTime(year, month, 1, 0, 0).toDate());
        long count = loginLogMapper.count(mobile, success, loginLogTableName);

        List<LoginLogView> data = Lists.newArrayList();
        if (count > 0 ) {
            int totalPages = (int) (count % pageSize > 0 || count == 0 ? count / pageSize + 1 : count / pageSize);
            index = index > totalPages ? totalPages : index;
            data = loginLogMapper.getPaginationData(mobile, success, (index - 1) * pageSize, pageSize, loginLogTableName);
        }

        List<LoginLogPaginationItemDataDto> records = Lists.transform(data, new Function<LoginLogView, LoginLogPaginationItemDataDto>() {
            @Override
            public LoginLogPaginationItemDataDto apply(LoginLogView input) {
                return new LoginLogPaginationItemDataDto(input.getMobile(), input.getSource(), input.getIp(), input.getDevice(), input.getLoginTime(), input.isSuccess());
            }
        });

        return new BasePaginationDataDto<>(index, pageSize, count, records);
    }

    @Override
    public long countSuccessTimesOnDate(String loginName, Date date) {
        String loginLogTableName = this.getLoginLogTableName(date);
        return loginLogMapper.countSuccessTimesOnDate(loginName, date, loginLogTableName);
    }

    private String getLoginLogTableName(Date date) {
        return MessageFormat.format(LOGIN_LOG_TABLE_TEMPLATE, new DateTime(date).toString("yyyyMM"));
    }
}
