package com.tuotiansudai.paywrapper.repository.mapper;

import com.tuotiansudai.paywrapper.repository.model.RepayNotifyProcessStatus;
import com.tuotiansudai.paywrapper.repository.model.async.callback.NormalRepayNotifyRequestModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NormalRepayNotifyMapper extends BaseCallbackMapper {

    List<NormalRepayNotifyRequestModel> getNormalTodoList(@Param(value = "limitCount") int limitCount);

    void updateStatus(@Param(value = "id") Long id, @Param(value = "status") RepayNotifyProcessStatus status);

}
