package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.UserOpLogModel;
import com.tuotiansudai.repository.model.UserOpLogView;
import com.tuotiansudai.repository.model.UserOpType;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface UserOpLogMapper {

    void create(UserOpLogModel model);

    long count(@Param("mobile") String mobile,
               @Param("opType") UserOpType opType,
               @Param("startTime") Date startTime,
               @Param("endTime") Date endTime);

    List<UserOpLogView> getPaginationData(@Param("mobile") String mobile,
                                           @Param("opType") UserOpType opType,
                                           @Param("startTime") Date startTime,
                                           @Param("endTime") Date endTime,
                                           @Param("index") int index,
                                           @Param("pageSize") int pageSize);
}
