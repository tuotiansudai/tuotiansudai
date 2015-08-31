package com.tuotiansudai.paywrapper.repository.mapper;

import com.tuotiansudai.paywrapper.repository.model.async.request.BaseAsyncModel;
import com.tuotiansudai.paywrapper.repository.model.sync.request.SyncRequestStatus;
import org.apache.ibatis.annotations.Param;

public interface BaseAsyncMapper {

    BaseAsyncModel findById(Long id);

    void create(BaseAsyncModel model);
}
