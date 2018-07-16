package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.enums.BankRechargeStatus;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.repository.model.BankRechargeModel;
import com.tuotiansudai.repository.model.BankRechargePaginationView;
import com.tuotiansudai.repository.model.Source;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BankRechargeMapper {

    void createInvestor(BankRechargeModel userRechargeModel);

    void createLoaner(BankRechargeModel userRechargeModel);

    void update(BankRechargeModel userRechargeModel);

    BankRechargeModel findById(@Param(value = "id") long id);

    int sumRechargeSuccessAmountByLoginNameAndRole(@Param(value = "loginName") String loginName,
                                                   @Param(value = "roleType") Role roleType);

    List<BankRechargePaginationView> findRechargePagination(@Param(value = "role")Role role,
                                                            @Param(value = "rechargeId") String rechargeId,
                                                            @Param(value = "mobile") String mobile,
                                                            @Param(value = "source") Source source,
                                                            @Param(value = "status") BankRechargeStatus status,
                                                            @Param(value = "channel") String channel,
                                                            @Param(value = "index") int index,
                                                            @Param(value = "pageSize") int pageSize,
                                                            @Param(value = "startTime") Date startTime,
                                                            @Param(value = "endTime") Date endTime);


    long findSumRechargeAmount(@Param(value = "role")Role role,
                               @Param(value = "rechargeId") String rechargeId,
                               @Param(value = "mobile") String mobile,
                               @Param(value = "source") Source source,
                               @Param(value = "status") BankRechargeStatus status,
                               @Param(value = "channel") String channel,
                               @Param(value = "startTime") Date startTime,
                               @Param(value = "endTime") Date endTime);

    int findRechargeCount(@Param(value = "role")Role role,
                          @Param(value = "rechargeId") String rechargeId,
                          @Param(value = "mobile") String mobile,
                          @Param(value = "source") Source source,
                          @Param(value = "status") BankRechargeStatus status,
                          @Param(value = "channel") String channel,
                          @Param(value = "startTime") Date startTime,
                          @Param(value = "endTime") Date endTime);

    List<String> findAllChannels();

}
