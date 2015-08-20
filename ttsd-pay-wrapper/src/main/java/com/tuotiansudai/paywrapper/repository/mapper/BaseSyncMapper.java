package com.tuotiansudai.paywrapper.repository.mapper;

import com.tuotiansudai.paywrapper.repository.model.sync.request.BaseSyncRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.request.RequestStatus;
import com.tuotiansudai.paywrapper.repository.model.sync.response.BaseSyncResponseModel;
import org.apache.ibatis.annotations.Param;

public interface BaseSyncMapper {

    BaseSyncRequestModel findRequestById(Long id);

    void createRequest(BaseSyncRequestModel model);

    void updateRequestStatus(@Param(value = "id") Long id, @Param(value = "status") RequestStatus status);

    void createResponse(BaseSyncResponseModel model);
}
