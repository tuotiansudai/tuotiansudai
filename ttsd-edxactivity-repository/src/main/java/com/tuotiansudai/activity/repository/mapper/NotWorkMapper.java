package com.tuotiansudai.activity.repository.mapper;

import com.tuotiansudai.activity.repository.model.NotWorkModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NotWorkMapper {
    void create(NotWorkModel notWorkModel);

    void update(NotWorkModel notWorkModel);

    NotWorkModel findByLoginName(@Param(value = "loginName") String loginName);

    List<NotWorkModel> findPagination(@Param(value = "index") int index, @Param(value = "pageSize") int pageSize);

    long findAllCount();
}
