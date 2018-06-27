package com.tuotiansudai.repository.mapper;

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

    void create(BankWithdrawModel bankWithdrawModel);

    BankWithdrawModel findById(long id);

    void update(BankWithdrawModel bankWithdrawModel);

    long sumSuccessWithdrawByLoginName(String loginName);

    long sumWithdrawAmount(@Param(value = "withdrawId") Long withdrawId,
                           @Param(value = "mobile") String mobile,
                           @Param(value = "status") WithdrawStatus status,
                           @Param(value = "source") Source source,
                           @Param(value = "startTime") Date startTime,
                           @Param(value = "endTime") Date endTime);

    long sumWithdrawFee(@Param(value = "withdrawId") Long withdrawId,
                        @Param(value = "mobile") String mobile,
                        @Param(value = "status") WithdrawStatus status,
                        @Param(value = "source") Source source,
                        @Param(value = "startTime") Date startTime,
                        @Param(value = "endTime") Date endTime);

    int findWithdrawCount(@Param(value = "withdrawId") Long withdrawId,
                          @Param(value = "mobile") String mobile,
                          @Param(value = "status") WithdrawStatus status,
                          @Param(value = "source") Source source,
                          @Param(value = "startTime") Date startTime,
                          @Param(value = "endTime") Date endTime);

    List<WithdrawPaginationView> findWithdrawPagination(@Param(value = "withdrawId") Long withdrawId,
                                                        @Param(value = "mobile") String mobile,
                                                        @Param(value = "status") WithdrawStatus status,
                                                        @Param(value = "source") Source source,
                                                        @Param(value = "index") int index,
                                                        @Param(value = "pageSize") int pageSize,
                                                        @Param(value = "startTime") Date startTime,
                                                        @Param(value = "endTime") Date endTime);

    List<BankWithdrawModel> findSuccessByDate(@Param(value = "queryDate") String queryDate);
}
