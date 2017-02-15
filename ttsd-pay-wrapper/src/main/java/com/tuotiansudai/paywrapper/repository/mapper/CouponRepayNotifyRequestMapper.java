package com.tuotiansudai.paywrapper.repository.mapper;

import com.tuotiansudai.paywrapper.repository.model.NotifyProcessStatus;
import com.tuotiansudai.paywrapper.repository.model.async.callback.CouponRepayNotifyRequestModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepayNotifyRequestMapper extends BaseCallbackMapper {

    CouponRepayNotifyRequestModel findById(@Param(value = "id") long id);

    void updateStatus(@Param(value = "id") Long id, @Param(value = "status") NotifyProcessStatus status);
}
