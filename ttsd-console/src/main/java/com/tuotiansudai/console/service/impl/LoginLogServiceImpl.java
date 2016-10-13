package com.tuotiansudai.console.service.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.LoginLogPaginationItemDataDto;
import com.tuotiansudai.repository.mapper.LoginLogMapper;
import com.tuotiansudai.repository.model.LoginLogModel;
import com.tuotiansudai.console.service.LoginLogService;
import com.tuotiansudai.util.PaginationUtil;
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

        List<LoginLogModel> data = loginLogMapper.getPaginationData(mobile, success, PaginationUtil.calculateOffset(index, pageSize, count), pageSize, loginLogTableName);

        List<LoginLogPaginationItemDataDto> records = Lists.transform(data, new Function<LoginLogModel, LoginLogPaginationItemDataDto>() {
            @Override
            public LoginLogPaginationItemDataDto apply(LoginLogModel input) {
                return new LoginLogPaginationItemDataDto(input.getLoginName(), input.getSource(), input.getIp(), input.getDevice(), input.getLoginTime(), input.isSuccess());
            }
        });

        return new BasePaginationDataDto<>(PaginationUtil.validateIndex(index, pageSize, count), pageSize, count, records);
    }

    private String getLoginLogTableName(Date date) {
        return MessageFormat.format(LOGIN_LOG_TABLE_TEMPLATE, new DateTime(date).toString("yyyyMM"));
    }
}
