package com.tuotiansudai.paywrapper.repository.mapper;

import com.tuotiansudai.paywrapper.repository.model.async.callback.BaseCallbackRequestModel;

public interface BaseCallbackMapper {

    void create(BaseCallbackRequestModel model);

}
