package com.tuotiansudai.jpush.service.impl;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.jpush.client.MobileAppJPushClient;
import com.tuotiansudai.jpush.dto.JPushAlertDto;
import com.tuotiansudai.jpush.repository.mapper.JPushAlertMapper;
import com.tuotiansudai.jpush.repository.model.*;
import com.tuotiansudai.jpush.service.JPushAlertService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.model.InvestNotifyInfo;
import com.tuotiansudai.util.AmountConverter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;

@Service
public class JPushAlertServiceImpl implements JPushAlertService {
    static Logger logger = Logger.getLogger(JPushAlertServiceImpl.class);
    @Autowired
    private JPushAlertMapper jPushAlertMapper;

    @Autowired
    private MobileAppJPushClient mobileAppJPushClient;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    private static final String JPUSH_ID_KEY = "api:jpushId:store";

    private static final String NO_INVEST_LOGIN_NAME = "job:noInvest:loginName";

    @Autowired
    private InvestMapper investMapper;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @Transactional
    public void buildJPushAlert(String loginName, JPushAlertDto jPushAlertDto) {
        JPushAlertModel jPushAlertModel = new JPushAlertModel(jPushAlertDto);
        if (StringUtils.isNotEmpty(jPushAlertDto.getId())) {
            jPushAlertModel.setUpdatedBy(loginName);
            jPushAlertModel.setUpdatedTime(new Date());
            jPushAlertMapper.update(jPushAlertModel);

        } else {
            jPushAlertModel.setCreatedBy(loginName);
            jPushAlertModel.setCreatedTime(new Date());
            jPushAlertModel.setIsAutomatic(false);
            jPushAlertMapper.create(jPushAlertModel);
        }
    }


    @Override
    public int findPushTypeCount(PushType pushType) {
        return jPushAlertMapper.findPushTypeCount(pushType);
    }

    @Override
    public int findPushAlertCount(String name, boolean isAutomatic) {
        return jPushAlertMapper.findPushAlertCount(name, false);
    }

    @Override
    public List<JPushAlertModel> findPushAlerts(int index, int pageSize, String name, boolean isAutomatic) {
        return jPushAlertMapper.findPushAlerts((index - 1) * pageSize, pageSize, name, isAutomatic);
    }

    @Override
    public JPushAlertModel findJPushAlertModelById(long id) {
        return jPushAlertMapper.findJPushAlertModelById(id);
    }

    @Override
    @Transactional
    public void send(String loginName, long id) {
        JPushAlertModel jPushAlertModel = jPushAlertMapper.findJPushAlertModelById(id);
        JPushAlertDto jPushAlertDto = new JPushAlertDto(jPushAlertModel);
        PushSource pushSource = jPushAlertDto.getPushSource();
        String[] jumpToOrLink = chooseJumpToOrLink(jPushAlertDto);
        String alert = jPushAlertDto.getContent();
        boolean sendResult = false;
        List<String> pushObjects = jPushAlertDto.getPushObjects();

        if (pushSource == PushSource.ALL) {

            if (CollectionUtils.isNotEmpty(pushObjects)) {
                sendResult = mobileAppJPushClient.sendPushAlertByTags(jPushAlertDto.getId(), pushObjects, alert, jumpToOrLink[0], jumpToOrLink[1]);
            } else {
                sendResult = mobileAppJPushClient.sendPushAlertByAll(jPushAlertDto.getId(), alert, jumpToOrLink[0], jumpToOrLink[1]);
            }

        } else if (pushSource == PushSource.ANDROID) {
            if (CollectionUtils.isNotEmpty(pushObjects)) {
                sendResult = mobileAppJPushClient.sendPushAlertByAndroidTags(jPushAlertDto.getId(), pushObjects, alert, jumpToOrLink[0], jumpToOrLink[1]);
            } else {
                sendResult = mobileAppJPushClient.sendPushAlertByAndroid(jPushAlertDto.getId(), alert, jumpToOrLink[0], jumpToOrLink[1]);
            }
        } else if (pushSource == PushSource.IOS) {
            if (CollectionUtils.isNotEmpty(pushObjects)) {
                sendResult = mobileAppJPushClient.sendPushAlertByIosTags(jPushAlertDto.getId(), pushObjects, alert, jumpToOrLink[0], jumpToOrLink[1]);
            } else {
                sendResult = mobileAppJPushClient.sendPushAlertByIos(jPushAlertDto.getId(), alert, jumpToOrLink[0], jumpToOrLink[1]);
            }
        }

        if (sendResult) {
            jPushAlertModel.setUpdatedBy(loginName);
            jPushAlertModel.setUpdatedTime(new Date());
            jPushAlertModel.setStatus(PushStatus.SEND_SUCCESS);
            jPushAlertMapper.update(jPushAlertModel);
        } else {
            jPushAlertModel.setUpdatedBy(loginName);
            jPushAlertModel.setUpdatedTime(new Date());
            jPushAlertModel.setStatus(PushStatus.SEND_FAIL);
            jPushAlertMapper.update(jPushAlertModel);
        }
    }

    @Override
    @Transactional
    public void changeJPushAlertStatus(long id, PushStatus status, String loginName) {
        JPushAlertModel jPushAlertModel = jPushAlertMapper.findJPushAlertModelById(id);
        jPushAlertModel.setStatus(status);
        jPushAlertModel.setUpdatedBy(loginName);
        jPushAlertModel.setUpdatedTime(new Date());
        jPushAlertMapper.update(jPushAlertModel);
    }

    @Override
    @Transactional
    public void changeJPushAlertContent(long id, String content, String loginName) {
        JPushAlertModel jPushAlertModel = jPushAlertMapper.findJPushAlertModelById(id);
        jPushAlertModel.setContent(content);
        jPushAlertModel.setUpdatedBy(loginName);
        jPushAlertModel.setUpdatedTime(new Date());
        jPushAlertMapper.update(jPushAlertModel);
    }

    @Override
    public void autoJPushAlertBirthMonth() {
        JPushAlertModel jPushAlertModel = jPushAlertMapper.findJPushAlertByPushType(PushType.BIRTHDAY_ALERT_MONTH);
        if(jPushAlertModel != null){
            List<String> loginNames = accountMapper.findBirthOfAccountInMonth();
            if(CollectionUtils.isEmpty(loginNames)){
                logger.debug("accountMapper.findBirthOfAccountInMonth() without data");
                return;
            }
            autoJPushByBatchRegistrationId(jPushAlertModel, loginNames);
        }else{
            logger.debug("autoJPushAlertBirthMonthJob is disabled");
        }

    }

    @Override
    public void autoJPushAlertBirthDay() {
        JPushAlertModel jPushAlertModel = jPushAlertMapper.findJPushAlertByPushType(PushType.BIRTHDAY_ALERT_DAY);
        if(jPushAlertModel != null){
            List<String> loginNames = accountMapper.findBirthOfAccountInDay();
            if(CollectionUtils.isEmpty(loginNames)){
                logger.debug("accountMapper.findBirthOfAccountInDay() without data");
                return;
            }
            autoJPushByBatchRegistrationId(jPushAlertModel, loginNames);
        }else{
            logger.debug("AutoJPushAlertBirthDayJob is disabled");
        }
    }

    @Override
    public void autoJPushNoInvestAlert()  {
        JPushAlertModel jPushAlertModel = jPushAlertMapper.findJPushAlertByPushType(PushType.NO_INVEST_ALERT);
        if(jPushAlertModel != null){
            try {
                Set<String> jPushAlertSet ;
                Set<String> loginNames = investMapper.findNoInvestInThirtyDay();
                if (CollectionUtils.isEmpty(loginNames)) {
                    logger.debug("investMapper.findNoInvestInThirtyDay() without data");
                    return;
                }
                if (redisWrapperClient.exists(NO_INVEST_LOGIN_NAME)) {
                    String redisValue = redisWrapperClient.get(NO_INVEST_LOGIN_NAME);
                    Set<String> oldLoginNames = objectMapper.readValue(redisValue, new TypeReference<Set<String>>() {
                    });
                    Sets.SetView<String> diffSetHandle = Sets.difference(loginNames, oldLoginNames);
                    jPushAlertSet = diffSetHandle.immutableCopy();
                }else{
                    jPushAlertSet = loginNames;
                }

                redisWrapperClient.set(NO_INVEST_LOGIN_NAME, objectMapper.writeValueAsString(loginNames));
                if(CollectionUtils.isNotEmpty(jPushAlertSet)){
                    autoJPushByBatchRegistrationId(jPushAlertModel, Lists.newArrayList(jPushAlertSet));
                }
            }catch (IOException e){
                logger.error(e.getLocalizedMessage(),e);
            }
        }else{
            logger.debug("AutoJPushNoInvestAlertJob is disabled");
        }
    }
    @Override
    public void autoJPushLoanAlert(List<InvestNotifyInfo> notifyInfos) {
        JPushAlertModel jPushAlertModel = jPushAlertMapper.findJPushAlertByPushType(PushType.LOAN_ALERT);
        if (jPushAlertModel != null) {
            if (CollectionUtils.isEmpty(notifyInfos)) {
                logger.debug("notifyInfos without data");
                return;
            }
            Map<String, List<String>> loginNameMap = Maps.newHashMap();

            for (InvestNotifyInfo notifyInfo : notifyInfos) {
                List<String> amountLists = Lists.newArrayList(AmountConverter.convertCentToString(notifyInfo.getAmount()));
                loginNameMap.put(notifyInfo.getLoanName(),amountLists);
                autoJPushByRegistrationId(jPushAlertModel, loginNameMap);
                loginNameMap.clear();
            }
        } else {
            logger.debug("LOAN_ALERT is disabled");
        }
    }

    private String[] chooseJumpToOrLink(JPushAlertDto jPushAlertDto) {
        String[] jumpToOrLink = new String[]{"", ""};
        JumpTo jumpTo = jPushAlertDto.getJumpTo();
        String jumpToLink = jPushAlertDto.getJumpToLink();
        if (StringUtils.isNotEmpty(jumpToLink)) {
            jumpToOrLink[0] = "jumpToLink";
            jumpToOrLink[1] = jumpToLink;
            return jumpToOrLink;
        }
        if (jumpTo != null) {
            jumpToOrLink[0] = "jumpTo";
            jumpToOrLink[1] = jumpTo.getIndex();
            return jumpToOrLink;
        }

        return jumpToOrLink;

    }


    private void autoJPushByBatchRegistrationId(JPushAlertModel jPushAlertModel, List<String> pushObjects) {
        JPushAlertDto jPushAlertDto = new JPushAlertDto(jPushAlertModel);
        String[] jumpToOrLink = chooseJumpToOrLink(jPushAlertDto);
        List<String> registrationIds = Lists.newArrayList();
        for (int i = 0; i < pushObjects.size(); i++) {
            String loginName = pushObjects.get(i);
            if (redisWrapperClient.hexists(JPUSH_ID_KEY, loginName)) {
                String registrationId = redisWrapperClient.hget(JPUSH_ID_KEY, loginName);
                registrationIds.add(registrationId);
            }
            if (registrationIds.size() == 1000 || (i == pushObjects.size() - 1 && registrationIds.size() > 0)) {
                boolean sendResult = mobileAppJPushClient.sendPushAlertByRegistrationIds("" + jPushAlertModel.getId(), registrationIds, jPushAlertModel.getContent(), jumpToOrLink[0], jumpToOrLink[1]);
                if (sendResult) {
                    logger.debug(MessageFormat.format("第{0}个用户推送成功", i + 1));
                }else{
                    logger.debug(MessageFormat.format("第{0}个用户推送失败", i + 1));
                }
                registrationIds.clear();
            }

        }
    }

    private void autoJPushByRegistrationId(JPushAlertModel jPushAlertModel, Map<String, List<String>> pushObjects) {
        JPushAlertDto jPushAlertDto = new JPushAlertDto(jPushAlertModel);
        String[] jumpToOrLink = chooseJumpToOrLink(jPushAlertDto);
        Iterator iterator = pushObjects.entrySet().iterator();
        List<String> registrationIds = Lists.newArrayList();
        while (iterator.hasNext()){
            Map.Entry<String, List<String>> entry = (Map.Entry<String, List<String>>) iterator.next();
            String loginName = entry.getKey();
            if (redisWrapperClient.hexists(JPUSH_ID_KEY, loginName)) {
                String registrationId = redisWrapperClient.hget(JPUSH_ID_KEY, loginName);
                registrationIds.add(registrationId);
                List<String> params = entry.getValue();
                String content = jPushAlertModel.getContent();
                if (CollectionUtils.isNotEmpty(params)) {
                    for (int i = 0; i < params.size(); i++) {
                        content = content.replace("{" + i + "}", params.get(i));

                    }
                }
                boolean sendResult = mobileAppJPushClient.sendPushAlertByRegistrationIds("" + jPushAlertModel.getId(), registrationIds, content, jumpToOrLink[0], jumpToOrLink[1]);
                registrationIds.clear();
            }
        }

    }




}
