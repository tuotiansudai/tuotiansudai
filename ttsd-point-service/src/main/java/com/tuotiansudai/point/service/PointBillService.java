package com.tuotiansudai.point.service;

import com.tuotiansudai.point.repository.dto.AccountItemDataDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.point.repository.dto.PointBillPaginationItemDataDto;
import com.tuotiansudai.point.repository.model.PointBusinessType;

import java.util.Date;
import java.util.List;

public interface PointBillService {

    void createPointBill(String loginName, Long orderId, PointBusinessType businessType, long point);

    void createPointBill(String loginName, Long orderId, PointBusinessType businessType, long point, String note);

    void createTaskPointBill(String loginName, long pointTaskId, long point, String note);

    BasePaginationDataDto<PointBillPaginationItemDataDto> getPointBillPagination(String loginName,
                                                                                 String pointType,
                                                                                 int index,
                                                                                 int pageSize,
                                                                                 Date startTime,
                                                                                 Date endTime,
                                                                                 List<PointBusinessType> businessTypes);

    List<PointBillPaginationItemDataDto> getPointBillByLoginName(String loginName, int currentPageNo, int pageSize);

    long getPointBillCountByLoginName(String loginName);

    BasePaginationDataDto<AccountItemDataDto> findUsersAccountPoint(String loginName, String mobile, int currentPageNo, int pageSize);

    Long getFrozenPointByLoginName(String loginName);

}
