package com.tuotiansudai.paywrapper.repository.mapper;

import com.tuotiansudai.paywrapper.repository.model.CouponRepayNotifyProcessStatus;
import com.tuotiansudai.paywrapper.repository.model.async.callback.CouponRepayNotifyRequestModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponRepayNotifyRequestMapper extends BaseCallbackMapper {

    List<CouponRepayNotifyRequestModel> getTodoList(@Param(value = "limitCount") int limitCount);

    void updateStatus(@Param(value = "id") Long id, @Param(value = "status") CouponRepayNotifyProcessStatus status);
}
