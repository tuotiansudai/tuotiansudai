package com.tuotiansudai.jpush.service.impl;


import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.enums.PushSource;
import com.tuotiansudai.jpush.client.MobileAppJPushClient;
import com.tuotiansudai.jpush.dto.JPushAlertDto;
import com.tuotiansudai.jpush.repository.mapper.JPushAlertMapper;
import com.tuotiansudai.jpush.repository.model.JPushAlertModel;
import com.tuotiansudai.jpush.repository.model.JumpTo;
import com.tuotiansudai.jpush.repository.model.PushStatus;
import com.tuotiansudai.jpush.repository.model.PushUserType;
import com.tuotiansudai.jpush.service.JPushAlertService;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.Role;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Service
public class JPushAlertServiceImpl implements JPushAlertService {
    static Logger logger = Logger.getLogger(JPushAlertServiceImpl.class);
    @Autowired
    private JPushAlertMapper jPushAlertMapper;

    @Autowired
    private MobileAppJPushClient mobileAppJPushClient;

    private static final String JPUSH_ID_KEY = "api:jpushId:store";

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Override
    @Transactional
    public void buildJPushAlert(String editBy, JPushAlertDto jPushAlertDto) {
        JPushAlertModel jPushAlertModel = new JPushAlertModel(jPushAlertDto);
        if (StringUtils.isNotEmpty(jPushAlertDto.getId())) {
            jPushAlertModel.setUpdatedBy(editBy);
            jPushAlertModel.setUpdatedTime(new Date());
            if (jPushAlertModel.getJumpTo() != JumpTo.OTHER) {
                jPushAlertModel.setJumpToLink("");
            }
            jPushAlertMapper.update(jPushAlertModel);
        } else {
            jPushAlertModel.setCreatedBy(editBy);
            jPushAlertModel.setCreatedTime(new Date());
            jPushAlertModel.setJumpToLink(jPushAlertModel.getJumpTo() != JumpTo.OTHER ? "" : jPushAlertModel.getJumpToLink());
            jPushAlertModel.setIsAutomatic(false);
            jPushAlertMapper.create(jPushAlertModel);
            jPushAlertDto.setId(String.valueOf(jPushAlertModel.getId()));
        }
    }


    @Override
    public void manualJPushAlert(long id) {
        JPushAlertModel jPushAlertModel = jPushAlertMapper.findJPushAlertModelById(id);
        if (jPushAlertModel != null) {
            if (jPushAlertModel.isAutomatic()) {
                logger.debug("JPush is failed, this JPush is not manual, id = " + id);
                return;
            }
            if (jPushAlertModel.getPushUserType().contains(PushUserType.ALL) && jPushAlertModel.getPushDistricts() == null) {
                Map extras = chooseJumpToOrLink(new JPushAlertDto(jPushAlertModel));
                mobileAppJPushClient.sendPushAlertByAll(String.valueOf(jPushAlertModel.getId()), jPushAlertModel.getContent(), extras, jPushAlertModel.getPushSource());
            } else {
                List<String> loginNames = findManualJPushAlertUserLoginName(jPushAlertModel.getPushUserType(), null);
                if (CollectionUtils.isEmpty(loginNames)) {
                    logger.debug("this JPush without data, id = " + id);
                    return;
                }
            }
            jPushAlertModel.setStatus(PushStatus.SEND_SUCCESS);
            jPushAlertMapper.update(jPushAlertModel);
        } else {
            logger.debug("this JPush is disabled, id = " + id);
        }
    }

    private List<String> findManualJPushAlertUserLoginName(List<PushUserType> pushUserTypes, List<String> districtName) {
        if (CollectionUtils.isEmpty(districtName)) {
            districtName = Lists.newArrayList();
        }
        List<String> loginNames = null;
        List<String> result = Lists.newArrayList();
        for (PushUserType pushUserType : pushUserTypes) {
            switch (pushUserType) {
                case ALL:
                    loginNames = userMapper.findAllUsersByProvinces(Maps.newHashMap(ImmutableMap.<String, Object>builder().put("districtName", districtName).build()));
                    break;
                case STAFF:
                    loginNames = userMapper.findAllByRole(Maps.newHashMap(ImmutableMap.<String, Object>builder().put("role", Role.STAFF).put("districtName", districtName).build()));
                    break;
                case AGENT:
                    loginNames = userMapper.findAllByRole(Maps.newHashMap(ImmutableMap.<String, Object>builder().put("role", Role.AGENT).put("districtName", districtName).build()));
                    break;
                case RECOMMENDATION:
                    loginNames = userMapper.findAllRecommendation(Maps.newHashMap(ImmutableMap.<String, Object>builder().put("districtName", districtName).build()));
                    break;
                case OTHERS:
                    loginNames = userMapper.findNaturalUser(Maps.newHashMap(ImmutableMap.<String, Object>builder().put("districtName", districtName).build()));
                    break;
            }
            result.addAll(loginNames);
        }
        HashSet<String> hashSet = new HashSet<>(result);
        result.clear();
        result.addAll(hashSet);
        return result;
    }

    private Map chooseJumpToOrLink(JPushAlertDto jPushAlertDto) {
        Map<String, String> extras = Maps.newHashMap();
        JumpTo jumpTo = jPushAlertDto.getJumpTo();
        for (String key : jumpTo.getParams()) {
            switch (key) {
                case "jumpTo":
                    extras.put("jumpTo", jumpTo.getIndex());
                    break;
                case "jumpToLink":
                    extras.put("jumpToLink", jPushAlertDto.getJumpToLink());
                    break;
                case "investId":
                    extras.put("investId", jPushAlertDto.getInvestId());
                    break;
                case "loanId":
                    extras.put("loanId", jPushAlertDto.getLoanId());
                    break;
                case "isCompleted":
                    extras.put("isCompleted", jPushAlertDto.getIsCompleted());
                    break;
            }
        }
        return extras;
    }


    @Override
    public void reject(String loginName, long id, String ip) {
        logger.debug("JPush audit reject, auditor:" + loginName + ", JPush id:" + id);
        JPushAlertModel jPushModel = jPushAlertMapper.findJPushAlertModelById(id);
        jPushModel.setStatus(PushStatus.REJECTED);
        jPushModel.setAuditor(loginName);
        jPushAlertMapper.update(jPushModel);
    }

    @Override
    public void delete(String loginName, long id) {
        logger.debug("JPush audit delete, operator:" + loginName + ", JPush id:" + id);
        jPushAlertMapper.delete(id);
    }

    @Override
    public void storeJPushId(String loginName, String platform, String jPushId) {
        String value = platform == null ? jPushId : platform.toLowerCase() + "-" + jPushId;
        logger.debug(MessageFormat.format("jpushId:{0} begin", value));
        redisWrapperClient.hset(JPUSH_ID_KEY, loginName, value);
        logger.debug(MessageFormat.format("jpushId:{0} end", value));
    }

    @Override
    public void delStoreJPushId(String loginName) {
        redisWrapperClient.hdel(JPUSH_ID_KEY, loginName);
    }
}
