package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.ReconciliationModel;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface ReconciliationMapper {

    @MapKey("orderNo")
    Map<String, ReconciliationModel> recharge(@Param(value = "queryDate") String queryDate);

    @MapKey("orderNo")
    Map<String, ReconciliationModel> withdraw(@Param(value = "queryDate") String queryDate);

    @MapKey("orderNo")
    Map<String, ReconciliationModel> invest(@Param(value = "queryDate") String queryDate);

    @MapKey("orderNo")
    Map<String, ReconciliationModel> transferInvest(@Param(value = "queryDate") String queryDate);

    @MapKey("orderNo")
    Map<String, ReconciliationModel> loanFull(@Param(value = "queryDate") String queryDate);

    @MapKey("orderNo")
    Map<String, ReconciliationModel> loanRepay(@Param(value = "queryDate") String queryDate);

    @MapKey("orderNo")
    Map<String, ReconciliationModel> loanCallBack(@Param(value = "queryDate") String queryDate);

}
