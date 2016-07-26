package com.tuotiansudai.service;

import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.LoginLogPaginationItemDataDto;
import com.tuotiansudai.repository.model.Source;

import java.util.Date;

public interface LoginLogService {

    void generateLoginLog(String loginNameOrMobile, Source source, String ip, String device, boolean loginSuccess);

    BasePaginationDataDto<LoginLogPaginationItemDataDto> getLoginLogPaginationData(String loginName, Boolean success, int index, int pageSize, int year, int month);

    long countSuccessTimesOnDate(String loginName, Date date);
}
