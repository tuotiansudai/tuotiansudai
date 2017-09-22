package com.tuotiansudai.paywrapper.repository.mapper;

import com.tuotiansudai.paywrapper.repository.model.NotifyProcessStatus;
import com.tuotiansudai.paywrapper.repository.model.async.callback.HuiZuRepayNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.InvestNotifyRequestModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HuiZuRepayNotifyRequestMapper extends BaseCallbackMapper {

    HuiZuRepayNotifyRequestModel findById(@Param(value = "id") long id);

    void updateStatus(@Param(value = "id") Long id, @Param(value = "status") NotifyProcessStatus status);

    List<HuiZuRepayNotifyRequestModel> findByOrderId(@Param(value = "orderId") String orderId);
}
