package com.tuotiansudai.smswrapper.repository.mapper;

import com.tuotiansudai.smswrapper.repository.model.NeteaseCallbackRequestModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface NeteaseCallbackRequestMapper {

    NeteaseCallbackRequestModel findByMobileAndSendid(@Param(value = "mobile") String mobile,
                                                      @Param(value = "sendid") String sendid);

    List<NeteaseCallbackRequestModel> findFailedRequests(@Param(value = "now") Date now);

    void create(NeteaseCallbackRequestModel model);

    void update(NeteaseCallbackRequestModel model);
}
