package com.tuotiansudai.log.repository.mapper;

import com.tuotiansudai.log.repository.model.LoginLogModel;
import com.tuotiansudai.repository.model.Source;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoginLogMapper {

    void create(@Param("table") String table,
                @Param("model") LoginLogModel model);

    long count(@Param("loginName") String loginName,
               @Param("mobile") String mobile,
               @Param("success") Boolean success,
               @Param("table") String table,
               @Param("source") Source source);

    List<LoginLogModel> getPaginationData(@Param("loginName") String loginName,
                                          @Param("mobile") String mobile,
                                          @Param("success") Boolean success,
                                          @Param("source") Source source,
                                          @Param("index") long index,
                                          @Param("pageSize") long pageSize,
                                          @Param("table") String table);

    void createLoginLogTable(@Param("newTableName") String newTableName);

}
