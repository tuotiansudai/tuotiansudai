package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.LoginLogModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface LoginLogMapper {

    long count(@Param("mobile") String mobile,
               @Param("success") Boolean success,
               @Param("table") String table);

    List<LoginLogModel> getPaginationData(@Param("mobile") String mobile,
                                          @Param("success") Boolean success,
                                          @Param("index") long index,
                                          @Param("pageSize") long pageSize,
                                          @Param("table") String table);

    long countSuccessTimesOnDate(@Param("loginName") String loginName,
                                 @Param("date") Date date,
                                 @Param("table") String table);
}
