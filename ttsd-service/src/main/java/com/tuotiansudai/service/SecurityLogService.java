package com.tuotiansudai.service;

import com.tuotiansudai.dto.AuditLogPaginationItemDataDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.LoginLogPaginationItemDataDto;

public interface SecurityLogService {

    BasePaginationDataDto<LoginLogPaginationItemDataDto> getLoginLogPaginationData(String loginName, Boolean success, int index, int pageSize, int year, int month);

//    BasePaginationDataDto<AuditLogPaginationItemDataDto> getAuditLogPaginationData(String loginName, Boolean success, long index, long pageSize, int year, int month);

}
