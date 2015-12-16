package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.SystemBillBusinessType;
import com.tuotiansudai.repository.model.SystemBillModel;
import com.tuotiansudai.repository.model.SystemBillOperationType;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SystemBillMapper {

    void create(SystemBillModel model);

    SystemBillModel findByOrderId(Long orderId);

    List<SystemBillModel> findSystemBillPagination(@Param(value = "startTime") Date startTime,
                                                   @Param(value = "endTime") Date endTime,
                                                   @Param(value = "operationType") SystemBillOperationType operationType,
                                                   @Param(value = "businessType") SystemBillBusinessType businessType,
                                                   @Param(value = "index") int index,
                                                   @Param(value = "pageSize") int pageSize);

    int findSystemBillCount(@Param(value = "startTime") Date startTime,
                            @Param(value = "endTime") Date endTime,
                            @Param(value = "operationType") SystemBillOperationType operationType,
                            @Param(value = "businessType") SystemBillBusinessType businessType);


    long findSumSystemIncome(@Param(value = "startTime") Date startTime,
                             @Param(value = "endTime") Date endTime,
                             @Param(value = "operationType") SystemBillOperationType operationType,
                             @Param(value = "businessType") SystemBillBusinessType businessType);

    long findSumSystemExpend(@Param(value = "startTime") Date startTime,
                             @Param(value = "endTime") Date endTime,
                             @Param(value = "operationType") SystemBillOperationType operationType,
                             @Param(value = "businessType") SystemBillBusinessType businessType);

}
