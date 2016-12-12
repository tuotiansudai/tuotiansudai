package com.tuotiansudai.activity.repository.mapper;

import com.tuotiansudai.activity.repository.model.ActivityCategory;
import com.tuotiansudai.activity.repository.model.NotWorkModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NotWorkMapper {

    void create(NotWorkModel notWorkModel);

    void update(NotWorkModel notWorkModel);

    NotWorkModel findByLoginName(@Param(value = "loginName") String loginName, @Param(value = "activityCategory") ActivityCategory activityCategory);

    List<NotWorkModel> findPagination(@Param(value = "mobile") String mobile,
                                      @Param(value = "activityCategory") ActivityCategory activityCategory,
                                      @Param(value = "index") Integer index,
                                      @Param(value = "pageSize") Integer pageSize);

    long findAllCount();
}
