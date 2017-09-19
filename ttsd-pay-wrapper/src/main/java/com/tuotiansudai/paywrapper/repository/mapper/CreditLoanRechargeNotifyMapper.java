package com.tuotiansudai.paywrapper.repository.mapper;

import com.tuotiansudai.paywrapper.repository.model.NotifyProcessStatus;
import com.tuotiansudai.paywrapper.repository.model.async.callback.InvestNotifyRequestModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CreditLoanRechargeNotifyMapper extends BaseCallbackMapper {

    List<InvestNotifyRequestModel> getLastRequest(@Param(value = "limitCount") int limitCount);

    InvestNotifyRequestModel findById(@Param(value = "id") long id);

    void updateStatus(@Param(value = "id") Long id, @Param(value = "status") NotifyProcessStatus status);
}
