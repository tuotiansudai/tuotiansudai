package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.BankRechargeModel;
import com.tuotiansudai.repository.model.BankRechargePaginationView;
import com.tuotiansudai.repository.model.BankRechargeStatus;
import com.tuotiansudai.repository.model.Source;
import feign.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BankRechargeMapper {

    void create(BankRechargeModel userRechargeModel);

    void update(BankRechargeModel userRechargeModel);

    BankRechargeModel findById(@Param(value = "id") long id);

    int sumRechargeSuccessAmountByLoginName(@Param(value = "loginName") String loginName);

    List<BankRechargePaginationView> findRechargePagination(@Param(value = "rechargeId") String rechargeId,
                                                            @Param(value = "mobile") String mobile,
                                                            @Param(value = "source") Source source,
                                                            @Param(value = "status") BankRechargeStatus status,
                                                            @Param(value = "channel") String channel,
                                                            @Param(value = "index") int index,
                                                            @Param(value = "pageSize") int pageSize,
                                                            @Param(value = "startTime") Date startTime,
                                                            @Param(value = "endTime") Date endTime,
                                                            @Param(value = "role") String role);


    long findSumRechargeAmount(@Param(value = "rechargeId") String rechargeId,
                               @Param(value = "mobile") String mobile,
                               @Param(value = "source") Source source,
                               @Param(value = "status") BankRechargeStatus status,
                               @Param(value = "channel") String channel,
                               @Param(value = "role") String role,
                               @Param(value = "startTime") Date startTime,
                               @Param(value = "endTime") Date endTime);

    int findRechargeCount(@Param(value = "rechargeId") String rechargeId,
                          @Param(value = "mobile") String mobile,
                          @Param(value = "source") Source source,
                          @Param(value = "status") BankRechargeStatus status,
                          @Param(value = "channel") String channel,
                          @Param(value = "startTime") Date startTime,
                          @Param(value = "endTime") Date endTime,
                          @Param(value = "role") String role);

    List<String> findAllChannels();

}
