package com.tuotiansudai.push.service.impl;


import com.google.common.base.Strings;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.push.dto.PushCreateDto;
import com.tuotiansudai.push.repository.mapper.PushAlertMapper;
import com.tuotiansudai.push.repository.model.PushAlertModel;
import com.tuotiansudai.push.service.PushService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Date;

@Service
public class PushServiceImpl implements PushService {

    private static Logger logger = Logger.getLogger(PushServiceImpl.class);

    @Autowired
    private PushAlertMapper pushAlertMapper;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    private static final String PUSH_ID_KEY = "api:jpushId:store";

    @Override
    @Transactional
    public Long createOrUpdate(String createdOrUpdatedBy, PushCreateDto pushCreateDto) {
        if (pushCreateDto == null) {
            return null;
        }

        if (pushCreateDto.getId() == null) {
            PushAlertModel pushAlertModel = new PushAlertModel(createdOrUpdatedBy, pushCreateDto);
            pushAlertMapper.create(pushAlertModel);
            return pushAlertModel.getId();
        }

        PushAlertModel pushAlertModel = pushAlertMapper.findById(pushCreateDto.getId());
        pushAlertModel.setUpdatedBy(createdOrUpdatedBy);
        pushAlertModel.setUpdatedTime(new Date());
        pushAlertModel.setPushSource(pushCreateDto.getPushSource());
        pushAlertModel.setPushType(pushCreateDto.getPushType());
        pushAlertMapper.update(pushAlertModel);
        return pushAlertModel.getId();
    }

    @Override
    public void delete(long id) {
        pushAlertMapper.deleteById(id);
    }

    @Override
    public void storeJPushId(String loginName, String platform, String jPushId) {
        if (Strings.isNullOrEmpty(loginName) || Strings.isNullOrEmpty(platform) || Strings.isNullOrEmpty(jPushId)) {
            return;
        }
        redisWrapperClient.hset(PUSH_ID_KEY, loginName, MessageFormat.format("{0}-{1}", platform.toLowerCase(), jPushId));
    }

    @Override
    public void removeJPushId(String loginName) {
        redisWrapperClient.hdel(PUSH_ID_KEY, loginName);
    }
}
