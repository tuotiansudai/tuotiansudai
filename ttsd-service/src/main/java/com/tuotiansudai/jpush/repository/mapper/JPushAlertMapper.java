package com.tuotiansudai.jpush.repository.mapper;

import com.tuotiansudai.jpush.repository.model.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface JPushAlertMapper {

    void create(JPushAlertModel jPushAlertModel);

    void update(JPushAlertModel jPushAlertModel);

    int findMaxSerialNumByType(@Param("pushType") PushType pushType);

    int findPushAlertCount(@Param(value = "pushType") PushType pushType,
                           @Param(value = "pushSource") PushSource pushSource,
                           @Param(value = "pushUserType") PushUserType pushUserType,
                           @Param(value = "pushStatus") PushStatus pushStatus,
                           @Param(value = "startTime") Date startTime,
                           @Param(value = "endTime") Date endTime, @Param(value = "isAutomatic") boolean isAutomatic);

    List<JPushAlertModel> findPushAlerts(@Param(value = "index") int index,
                                         @Param(value = "pageSize") int pageSize,
                                         @Param(value = "pushType") PushType pushType,
                                         @Param(value = "pushSource") PushSource pushSource,
                                         @Param(value = "pushUserType") PushUserType pushUserType,
                                         @Param(value = "pushStatus") PushStatus pushStatus,
                                         @Param(value = "startTime") Date startTime,
                                         @Param(value = "endTime") Date endTime,
                                         @Param(value = "isAutomatic") boolean isAutomatic);


    JPushAlertModel findJPushAlertModelById(long id);

    JPushAlertModel findJPushAlertByPushType(@Param("pushType") PushType pushType);

    void delete(@Param("id") long id);
}
