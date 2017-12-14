package com.tuotiansudai.point.service;

import com.tuotiansudai.dto.AccountItemDataDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.point.repository.dto.PointBillPaginationItemDataDto;
import com.tuotiansudai.point.repository.model.PointBusinessType;
import com.tuotiansudai.point.repository.model.PointChangingResult;

import java.util.Date;
import java.util.List;

public interface PointBillService {

    PointChangingResult createPointBill(String loginName, Long orderId, PointBusinessType businessType, long point);

    PointChangingResult createPointBill(String loginName, Long orderId, PointBusinessType businessType, long point, String note);

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

    List<AccountItemDataDto> findUsersAccountPoint(String loginName, String userName, String mobile, Integer currentPageNo, Integer pageSize);

    int findUsersAccountPointCount(String loginName, String userName, String mobile);

    PointChangingResult pointChanging(String loginName,long point);
}
