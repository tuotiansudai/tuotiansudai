package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.RechargeModel;
import com.tuotiansudai.repository.model.RechargeSource;
import com.tuotiansudai.repository.model.RechargeStatus;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface RechargeMapper {

    void create(RechargeModel model);

    void updateStatus(@Param("id") long id, @Param("status") RechargeStatus status);

    RechargeModel findById(long id);

    long findSumRechargeByLoginName(String loginName);

    List<RechargeModel> findRechargePagination(@Param(value = "rechargeId") String rechargeId,
                                               @Param(value = "loginName") String loginName,
                                               @Param(value = "source") RechargeSource source,
                                               @Param(value = "status") RechargeStatus status,
                                               @Param(value = "index") int index,
                                               @Param(value = "pageSize") int pageSize,
                                               @Param(value = "startTime") Date startTime,
                                               @Param(value = "endTime") Date endTime);


    int findRechargeCount(@Param(value = "rechargeId") String rechargeId,
                          @Param(value = "loginName") String loginName,
                          @Param(value = "source") RechargeSource source,
                          @Param(value = "status") RechargeStatus status,
                          @Param(value = "startTime") Date startTime,
                          @Param(value = "endTime") Date endTime);
}
