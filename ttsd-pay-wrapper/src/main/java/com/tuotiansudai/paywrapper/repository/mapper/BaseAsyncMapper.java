package com.tuotiansudai.paywrapper.repository.mapper;

import com.tuotiansudai.paywrapper.repository.model.async.request.BaseAsyncRequestModel;

public interface BaseAsyncMapper {

    BaseAsyncRequestModel findById(Long id);

    void create(BaseAsyncRequestModel model);
}
