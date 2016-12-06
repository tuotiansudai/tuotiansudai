package com.tuotiansudai.push.service;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.push.client.PushClient;
import com.tuotiansudai.push.repository.mapper.PushAlertMapper;
import com.tuotiansudai.push.repository.model.PushAlertModel;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JPushAlertNewService {

    static Logger logger = Logger.getLogger(JPushAlertNewService.class);

    @Autowired
    private PushAlertMapper pushAlertMapper;

    @Autowired
    private PushClient pushClient;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    private static final String JPUSH_ID_KEY = "api:jpushId:store";

    public void autoJPushAlertSendToAll(PushAlertModel pushAlertModel) {
        if (null != pushAlertModel) {
            pushClient.sendJPush(null, pushAlertModel.getContent());
            pushAlertMapper.update(pushAlertModel);
        } else {
            logger.debug("Auto JPush is disabled.");
        }
    }

    public PushAlertModel findJPushAlertModelByMessageId(long id) {
        return pushAlertMapper.findById(id);
    }

    public void autoJPushBatchByLoginNames(PushAlertModel pushAlertModel, List<String> loginNames) {
        List<String> registrationIds = Lists.newArrayList();
        for (String loginName : loginNames) {
            String value = redisWrapperClient.hget(JPUSH_ID_KEY, loginName);
            if (Strings.isNullOrEmpty(value) && value.split("-").length == 2) {
                registrationIds.add(value.split("-")[1]);
            }
        }

        for (int batch = 0; batch < registrationIds.size() / 1000 + (registrationIds.size() % 1000 > 0 ? 1 : 0); batch++) {
            pushClient.sendJPush(registrationIds.subList(batch * 1000,
                    (batch + 1) * 1000 > registrationIds.size() ? registrationIds.size() : (batch + 1) * 1000),
                    pushAlertModel.getContent());
        }
    }
}
