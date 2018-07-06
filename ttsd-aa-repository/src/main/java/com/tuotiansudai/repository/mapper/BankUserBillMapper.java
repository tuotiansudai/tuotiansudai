package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.enums.BankUserBillBusinessType;
import com.tuotiansudai.enums.BankUserBillOperationType;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.repository.model.BankUserBillModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BankUserBillMapper {

    void create(BankUserBillModel bankUserBillModel);

    List<BankUserBillModel> findUserBills(@Param(value = "loginName") String loginName,
                                          @Param(value = "mobile") String mobile,
                                          @Param(value = "businessTypes") List<BankUserBillBusinessType> businessTypes,
                                          @Param(value = "operationType") BankUserBillOperationType operationType,
                                          @Param(value = "startTime") Date startTime,
                                          @Param(value = "endTime") Date endTime,
                                          @Param(value = "offset") int offset,
                                          @Param(value = "pageSize") int pageSize,
                                          @Param(value = "role") Role role);

    long countBills(@Param(value = "loginName") String loginName,
                    @Param(value = "mobile") String mobile,
                    @Param(value = "businessTypes") List<BankUserBillBusinessType> businessTypes,
                    @Param(value = "operationType") BankUserBillOperationType operationType,
                    @Param(value = "startTime") Date startTime,
                    @Param(value = "endTime") Date endTime,
                    @Param(value = "role") Role role);
}
