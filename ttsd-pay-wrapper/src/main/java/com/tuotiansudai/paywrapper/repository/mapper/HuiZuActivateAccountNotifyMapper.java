package com.tuotiansudai.paywrapper.repository.mapper;

import com.tuotiansudai.paywrapper.repository.model.NotifyProcessStatus;
import com.tuotiansudai.paywrapper.repository.model.async.callback.HuiZuActivateAccountNotifyRequestModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HuiZuActivateAccountNotifyMapper extends BaseCallbackMapper{

    HuiZuActivateAccountNotifyRequestModel findById(@Param(value = "id") long id);

    void updateStatus(@Param(value = "id") Long id, @Param(value = "status") NotifyProcessStatus status);

    List<HuiZuActivateAccountNotifyRequestModel> findByOrderId(@Param(value = "orderId") String orderId);
}
