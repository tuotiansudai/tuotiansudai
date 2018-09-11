package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.enums.BillOperationType;
import com.tuotiansudai.enums.SystemBillBusinessType;
import com.tuotiansudai.repository.model.SystemBillModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SystemBillMapper {

    void create(SystemBillModel model);

    SystemBillModel findByOrderId(@Param("orderId") Long orderId,
                                  @Param("systemBillBusinessType") SystemBillBusinessType systemBillBusinessType);

    List<SystemBillModel> findSystemBillPagination(@Param(value = "startTime") Date startTime,
                                                   @Param(value = "endTime") Date endTime,
                                                   @Param(value = "operationType") BillOperationType operationType,
                                                   @Param(value = "businessType") SystemBillBusinessType businessType,
                                                   @Param(value = "index") int index,
                                                   @Param(value = "pageSize") int pageSize);

    int findSystemBillCount(@Param(value = "startTime") Date startTime,
                            @Param(value = "endTime") Date endTime,
                            @Param(value = "operationType") BillOperationType operationType,
                            @Param(value = "businessType") SystemBillBusinessType businessType);


    long findSumSystemBill(@Param(value = "startTime") Date startTime,
                           @Param(value = "endTime") Date endTime,
                           @Param(value = "operationType") BillOperationType operationType,
                           @Param(value = "businessType") SystemBillBusinessType businessType);
}
