package com.tuotiansudai.paywrapper.repository.mapper;

import com.tuotiansudai.paywrapper.repository.model.NotifyProcessStatus;
import com.tuotiansudai.paywrapper.repository.model.async.callback.ExtraRateNotifyRequestModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExtraRateNotifyRequestMapper extends BaseCallbackMapper {

    ExtraRateNotifyRequestModel findById(@Param(value = "id") Long id);

    void updateStatus(@Param(value = "id") Long id, @Param(value = "status") NotifyProcessStatus status);
}
