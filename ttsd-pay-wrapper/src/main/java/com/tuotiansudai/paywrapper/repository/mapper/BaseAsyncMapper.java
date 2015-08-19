package com.tuotiansudai.paywrapper.repository.mapper;

import com.tuotiansudai.paywrapper.repository.model.async.request.BaseAsyncRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.request.RequestStatus;
import org.apache.ibatis.annotations.Param;

public interface BaseAsyncMapper {

    BaseAsyncRequestModel findRequestById(Long id);

    void createRequest(BaseAsyncRequestModel model);

    void updateRequestStatus(@Param(value = "id") Long id, @Param(value = "status") RequestStatus status);
}
