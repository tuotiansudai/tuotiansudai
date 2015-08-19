package com.tuotiansudai.paywrapper.repository.mapper;

import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackResponseModel;

public interface BaseCallbackMapper {

    void createRequest(BaseCallbackRequestModel model);

    void createResponse(BaseCallbackResponseModel model);
}
