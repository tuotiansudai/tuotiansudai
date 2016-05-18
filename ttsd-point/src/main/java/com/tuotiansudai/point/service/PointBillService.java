package com.tuotiansudai.point.service;

import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.point.dto.PointBillPaginationItemDataDto;
import com.tuotiansudai.point.repository.model.PointBusinessType;

import java.util.Date;
import java.util.List;

public interface PointBillService {

    void createPointBill(String loginName, Long orderId, PointBusinessType businessType, long point);

    BasePaginationDataDto<PointBillPaginationItemDataDto> getPointBillPagination(String loginName,
                                                                              int index,
                                                                              int pageSize,
                                                                              Date startTime,
                                                                              Date endTime,
                                                                              PointBusinessType businessType);

    List<PointBillPaginationItemDataDto> getPointBillByLoginName(String loginName, int currentPageNo, int pageSize);

    long getPointBillCountByLoginName(String loginName);


}
