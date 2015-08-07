package com.tuotiansudai.paywrapper.repository.mapper;

import com.tuotiansudai.paywrapper.repository.model.request.BaseRequestModel;
import com.tuotiansudai.paywrapper.repository.model.request.RequestStatus;
import com.tuotiansudai.paywrapper.repository.model.response.BaseResponseModel;
import org.apache.ibatis.annotations.Param;

public interface BaseMapper {

    BaseRequestModel findRequestById(Long id);

    void createRequest(BaseRequestModel model);

    void updateRequestStatus(@Param(value = "id") Long id, @Param(value = "status") RequestStatus status);

    void createResponse(BaseResponseModel model);
}
