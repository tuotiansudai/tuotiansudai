package com.tuotiansudai.log.repository.mapper;

import com.tuotiansudai.enums.UserOpType;
import com.tuotiansudai.log.repository.model.UserOpLogModel;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface UserOpLogMapper {

    void create(UserOpLogModel model);

    UserOpLogModel findById(@Param("id") long id);

    long count(@Param("mobile") String mobile,
               @Param("opType") UserOpType opType,
               @Param("startTime") Date startTime,
               @Param("endTime") Date endTime);

    List<UserOpLogModel> getPaginationData(@Param("mobile") String mobile,
                                           @Param("opType") UserOpType opType,
                                           @Param("startTime") Date startTime,
                                           @Param("endTime") Date endTime,
                                           @Param("index") int index,
                                           @Param("pageSize") int pageSize);
}
