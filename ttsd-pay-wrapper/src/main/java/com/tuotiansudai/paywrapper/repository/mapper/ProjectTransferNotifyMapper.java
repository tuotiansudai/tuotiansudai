package com.tuotiansudai.paywrapper.repository.mapper;

import com.tuotiansudai.paywrapper.repository.model.async.callback.ProjectTransferNotifyRequestModel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectTransferNotifyMapper extends BaseCallbackMapper {

    List<ProjectTransferNotifyRequestModel> getTodoList();

}
