package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.enums.Role;
import com.tuotiansudai.enums.WithdrawStatus;
import com.tuotiansudai.repository.model.BankWithdrawModel;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.WithdrawPaginationView;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BankWithdrawMapper {

    void createInvestor(BankWithdrawModel bankWithdrawModel);

    void createLoaner(BankWithdrawModel bankWithdrawModel);

    BankWithdrawModel findById(long id);

    void update(BankWithdrawModel bankWithdrawModel);

    long sumSuccessWithdrawByLoginNameAndRole(@Param(value = "loginName") String loginName,
                                              @Param(value = "roleType") Role roleType);

    long sumWithdrawAmount(@Param(value = "role") Role role,
                           @Param(value = "withdrawId") Long withdrawId,
                           @Param(value = "mobile") String mobile,
                           @Param(value = "status") WithdrawStatus status,
                           @Param(value = "source") Source source,
                           @Param(value = "startTime") Date startTime,
                           @Param(value = "endTime") Date endTime);

    long sumWithdrawFee(@Param(value = "role") Role role,
                        @Param(value = "withdrawId") Long withdrawId,
                        @Param(value = "mobile") String mobile,
                        @Param(value = "status") WithdrawStatus status,
                        @Param(value = "source") Source source,
                        @Param(value = "startTime") Date startTime,
                        @Param(value = "endTime") Date endTime);

    int findWithdrawCount(@Param(value = "role") Role role,
                          @Param(value = "withdrawId") Long withdrawId,
                          @Param(value = "mobile") String mobile,
                          @Param(value = "status") WithdrawStatus status,
                          @Param(value = "source") Source source,
                          @Param(value = "startTime") Date startTime,
                          @Param(value = "endTime") Date endTime);

    List<WithdrawPaginationView> findWithdrawPagination(@Param(value = "role") Role role,
                                                        @Param(value = "withdrawId") Long withdrawId,
                                                        @Param(value = "mobile") String mobile,
                                                        @Param(value = "status") WithdrawStatus status,
                                                        @Param(value = "source") Source source,
                                                        @Param(value = "index") int index,
                                                        @Param(value = "pageSize") int pageSize,
                                                        @Param(value = "startTime") Date startTime,
                                                        @Param(value = "endTime") Date endTime);
}
