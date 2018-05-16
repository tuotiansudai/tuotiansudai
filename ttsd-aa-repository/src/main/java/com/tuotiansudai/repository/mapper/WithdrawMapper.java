package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.enums.WithdrawStatus;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.WithdrawModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface WithdrawMapper {

    void create(WithdrawModel withdrawModel);

    WithdrawModel findById(long id);

    void update(WithdrawModel withdrawModel);

    long findSumSuccessWithdrawByLoginName(String loginName);

    List<WithdrawModel> findWithdrawPagination(@Param(value = "withdrawId") String withdrawId,
                                               @Param(value = "mobile") String mobile,
                                               @Param(value = "status") WithdrawStatus status,
                                               @Param(value = "source") Source source,
                                               @Param(value = "index") int index,
                                               @Param(value = "pageSize") int pageSize,
                                               @Param(value = "startTime") Date startTime,
                                               @Param(value = "endTime") Date endTime,
                                               @Param(value = "role") String role);


    long findSumWithdrawAmount(@Param(value = "withdrawId") String withdrawId,
                               @Param(value = "mobile") String mobile,
                               @Param(value = "status") WithdrawStatus status,
                               @Param(value = "source") Source source,
                               @Param(value = "role") String role,
                               @Param(value = "startTime") Date startTime,
                               @Param(value = "endTime") Date endTime);


    long findSumWithdrawFee(@Param(value = "withdrawId") String withdrawId,
                            @Param(value = "mobile") String mobile,
                            @Param(value = "status") WithdrawStatus status,
                            @Param(value = "source") Source source,
                            @Param(value = "startTime") Date startTime,
                            @Param(value = "endTime") Date endTime,
                            @Param(value = "role") String role);

    int findWithdrawCount(@Param(value = "withdrawId") String withdrawId,
                          @Param(value = "mobile") String mobile,
                          @Param(value = "status") WithdrawStatus status,
                          @Param(value = "source") Source source,
                          @Param(value = "startTime") Date startTime,
                          @Param(value = "endTime") Date endTime,
                          @Param(value = "role") String role);
}
