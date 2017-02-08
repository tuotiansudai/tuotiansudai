package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.enums.Role;
import com.tuotiansudai.repository.model.RechargeModel;
import com.tuotiansudai.repository.model.RechargeSource;
import com.tuotiansudai.repository.model.RechargeStatus;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface RechargeMapper {

    void create(RechargeModel model);

    void updateStatus(@Param("id") long id, @Param("status") RechargeStatus status);

    RechargeModel findById(long id);

    List<RechargeModel> findRechargePagination(@Param(value = "rechargeId") String rechargeId,
                                               @Param(value = "mobile") String mobile,
                                               @Param(value = "source") RechargeSource source,
                                               @Param(value = "status") RechargeStatus status,
                                               @Param(value = "channel") String channel,
                                               @Param(value = "index") int index,
                                               @Param(value = "pageSize") int pageSize,
                                               @Param(value = "startTime") Date startTime,
                                               @Param(value = "endTime") Date endTime);


    long findSumRechargeAmount(@Param(value = "rechargeId") String rechargeId,
                               @Param(value = "mobile") String mobile,
                               @Param(value = "source") RechargeSource source,
                               @Param(value = "status") RechargeStatus status,
                               @Param(value = "channel") String channel,
                               @Param(value = "role") Role role,
                               @Param(value = "startTime") Date startTime,
                               @Param(value = "endTime") Date endTime);

    int findRechargeCount(@Param(value = "rechargeId") String rechargeId,
                          @Param(value = "mobile") String mobile,
                          @Param(value = "source") RechargeSource source,
                          @Param(value = "status") RechargeStatus status,
                          @Param(value = "channel") String channel,
                          @Param(value = "startTime") Date startTime,
                          @Param(value = "endTime") Date endTime);

    long findSumSuccessRechargeByLoginName(String loginName);

    List<String> findAllChannels();

}
