package com.tuotiansudai.paywrapper.repository.mapper;

import com.tuotiansudai.paywrapper.repository.model.async.callback.ProjectTransferNotifyRequestModel;
import com.tuotiansudai.paywrapper.repository.model.sync.request.SyncRequestStatus;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectTransferNotifyMapper extends BaseCallbackMapper {

    List<ProjectTransferNotifyRequestModel> getTodoList();

    void updateStatus(@Param(value = "id") Long id, @Param(value = "status") Integer status);
}
