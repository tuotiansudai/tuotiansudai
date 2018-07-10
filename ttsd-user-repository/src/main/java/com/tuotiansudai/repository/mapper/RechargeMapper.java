package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.enums.BankRechargeStatus;
import com.tuotiansudai.repository.model.RechargeModel;
import com.tuotiansudai.repository.model.Source;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by qduljs2011 on 2018/7/10.
 */
@Repository
public interface RechargeMapper {
    void create(RechargeModel model);

    void updateStatus(@Param("id") long id, @Param("status") BankRechargeStatus status);

    RechargeModel findById(long id);

    List<RechargeModel> findRechargePagination(@Param(value = "rechargeId") String rechargeId,
                                               @Param(value = "mobile") String mobile,
                                               @Param(value = "source") Source source,
                                               @Param(value = "status") BankRechargeStatus status,
                                               @Param(value = "channel") String channel,
                                               @Param(value = "index") int index,
                                               @Param(value = "pageSize") int pageSize,
                                               @Param(value = "startTime") Date startTime,
                                               @Param(value = "endTime") Date endTime);


    long findSumRechargeAmount(@Param(value = "rechargeId") String rechargeId,
                               @Param(value = "mobile") String mobile,
                               @Param(value = "source") Source source,
                               @Param(value = "status") BankRechargeStatus status,
                               @Param(value = "channel") String channel,
                               @Param(value = "startTime") Date startTime,
                               @Param(value = "endTime") Date endTime);

    int findRechargeCount(@Param(value = "rechargeId") String rechargeId,
                          @Param(value = "mobile") String mobile,
                          @Param(value = "source") Source source,
                          @Param(value = "status") BankRechargeStatus status,
                          @Param(value = "channel") String channel,
                          @Param(value = "startTime") Date startTime,
                          @Param(value = "endTime") Date endTime);

    long findSumSuccessRechargeByLoginName(String loginName);

    List<String> findAllChannels();
}
