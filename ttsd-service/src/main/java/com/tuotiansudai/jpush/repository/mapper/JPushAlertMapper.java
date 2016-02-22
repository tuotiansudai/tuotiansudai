package com.tuotiansudai.jpush.repository.mapper;

import com.tuotiansudai.jpush.repository.model.JPushAlertModel;
import com.tuotiansudai.jpush.repository.model.PushStatus;
import com.tuotiansudai.jpush.repository.model.PushType;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JPushAlertMapper {

    void create(JPushAlertModel jPushAlertModel);

    void update(JPushAlertModel jPushAlertModel);

    int findPushTypeCount(@Param("pushType") PushType pushType);

    int findPushAlertCount(@Param(value = "name") String name, @Param(value = "isAutomatic") boolean isAutomatic);

    List<JPushAlertModel> findPushAlerts(@Param(value = "index") int index, @Param(value = "pageSize") int pageSize,
                                         @Param(value = "name") String name, @Param(value = "isAutomatic") boolean isAutomatic);

    JPushAlertModel findJPushAlertModelById(long id);

    JPushAlertModel findJPushAlertByPushType(@Param("pushType") PushType pushType);

    void updateStatus(@Param("status") PushStatus status, @Param("id") long id);

    void delete(@Param("id") long id);
}
