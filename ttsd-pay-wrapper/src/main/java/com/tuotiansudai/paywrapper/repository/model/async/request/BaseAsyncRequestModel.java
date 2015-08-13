package com.tuotiansudai.paywrapper.repository.model.async.request;

import com.tuotiansudai.paywrapper.repository.model.sync.request.BaseSyncRequestModel;

public abstract class BaseAsyncRequestModel extends BaseSyncRequestModel{
    private String retUrl;

    private String notifyUrl;
}
