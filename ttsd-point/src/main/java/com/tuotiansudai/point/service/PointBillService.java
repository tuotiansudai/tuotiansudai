package com.tuotiansudai.point.service;

import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.point.repository.dto.PointBillPaginationItemDataDto;
import com.tuotiansudai.point.repository.model.PointBusinessType;

import java.util.Date;
import java.util.List;

public interface PointBillService {

    void createPointBill(String loginName, Long orderId, PointBusinessType businessType, long point);

    void createTaskPointBill(String loginName, long pointTaskId, long point, String note);

    BasePaginationDataDto<PointBillPaginationItemDataDto> getPointBillPagination(String loginName,
                                                                                 int index,
                                                                                 int pageSize,
                                                                                 Date startTime,
                                                                                 Date endTime,
                                                                                 List<PointBusinessType> businessTypes);

    List<PointBillPaginationItemDataDto> getPointBillByLoginName(String loginName, int currentPageNo, int pageSize);

    long getPointBillCountByLoginName(String loginName);
}
