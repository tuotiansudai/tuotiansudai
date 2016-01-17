package com.tuotiansudai.console.jpush.service;


import com.tuotiansudai.console.jpush.dto.JPushAlertDto;
import com.tuotiansudai.console.jpush.repository.model.JPushAlertModel;
import com.tuotiansudai.console.jpush.repository.model.PushType;

import java.util.List;

public interface JPushAlertService {
    void buildJPushAlert(String loginName,JPushAlertDto jPushAlertDto);

    int findPushTypeCount(PushType pushType);

    int findPushAlertCount(String name);

    List<JPushAlertModel> findPushAlerts(int index,int pageSize,String name);

    JPushAlertModel findJPushAlertModelById(long id);

    void send(String loginName,long id);
}
