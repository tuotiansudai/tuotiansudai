package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.LoginLogModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoginLogMapper {

    void create(@Param(value = "model") LoginLogModel loginLogModel,
                @Param(value = "table") String table);

    long count(@Param("loginName") String loginName,
               @Param("success") Boolean success,
               @Param("table") String table);

    List<LoginLogModel> getPaginationData(@Param("loginName") String loginName,
                                          @Param("success") Boolean success,
                                          @Param("index") long index,
                                          @Param("pageSize") long pageSize,
                                          @Param("table") String table);
}
