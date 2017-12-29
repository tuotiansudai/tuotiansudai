package com.tuotiansudai.point.service;

import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.point.repository.dto.PointBillPaginationItemDataDto;
import com.tuotiansudai.point.repository.dto.UserPointItemDataDto;
import com.tuotiansudai.point.repository.model.PointBusinessType;

import java.awt.*;
import java.util.Date;
import java.util.List;

public interface PointBillService {
    String CHANNEL_SUDAI = "SUDAI";

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

//    BasePaginationDataDto<PointBillPaginationItemDataDto> getPointBillPaginationConosle(Date startTime,Date endTime,PointBusinessType businessType);

    List<PointBillPaginationItemDataDto> getPointBillByLoginName(String loginName, int currentPageNo, int pageSize);

    long getPointBillCountByLoginName(String loginName);

    BasePaginationDataDto<UserPointItemDataDto> findUsersAccountPoint(String loginNameOrMobile, String channel, Long minPoint, Long maxPoint, int currentPageNo, int pageSize);

}
