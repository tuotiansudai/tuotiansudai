package com.tuotiansudai.console.service;

import com.tuotiansudai.console.bi.dto.RoleStage;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.model.Source;

import java.util.Date;

public interface UserServiceConsole {

    BaseDto<BasePaginationDataDto> findAllUser(String loginName, String email, String mobile, Date beginTime, Date endTime,
                                               Source source, RoleStage roleStage, String referrerMobile, String channel,
                                               Integer pageIndex, Integer pageSize);
}
