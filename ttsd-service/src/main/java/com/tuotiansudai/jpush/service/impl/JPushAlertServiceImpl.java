package com.tuotiansudai.jpush.service.impl;


import cn.jpush.api.report.ReceivedsResult;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.job.JPushReportFetchingJob;
import com.tuotiansudai.job.JobType;
import com.tuotiansudai.job.ManualJPushAlertJob;
import com.tuotiansudai.jpush.client.MobileAppJPushClient;
import com.tuotiansudai.jpush.dto.JPushAlertDto;
import com.tuotiansudai.jpush.dto.JpushReportDto;
import com.tuotiansudai.jpush.repository.mapper.JPushAlertMapper;
import com.tuotiansudai.jpush.repository.model.*;
import com.tuotiansudai.jpush.service.JPushAlertService;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.AccountService;
import com.tuotiansudai.service.AuditLogService;
import com.tuotiansudai.task.OperationType;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.DistrictUtil;
import com.tuotiansudai.util.JobManager;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private AccountService accountService;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    private static final String JPUSH_ID_KEY = "api:jpushId:store";

    private static final String NO_INVEST_LOGIN_NAME = "job:noInvest:loginName";

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private ReferrerRelationMapper referrerRelationMapper;

    @Autowired
    private JobManager jobManager;

    @Autowired
    private AuditLogService auditLogService;

    private static final ObjectMapper objectMapper = new ObjectMapper();
    @Value("${web.server}")
    private String domainName;

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
            jPushAlertDto.setId(String.valueOf(jPushAlertModel.getId()));
        }
    }


    @Override
    public int findMaxSerialNumByType(PushType pushType) {
        return jPushAlertMapper.findMaxSerialNumByType(pushType) == null ? 0 : jPushAlertMapper.findMaxSerialNumByType(pushType);
    }

    @Override
    public int findPushAlertCount(PushType pushType,
                                  PushSource pushSource, PushUserType pushUserType,PushStatus pushStatus,
                                  Date startTime, Date endTime, boolean isAutomatic) {
        if (endTime != null) {
            endTime = new DateTime(endTime).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate();
        }
        return jPushAlertMapper.findPushAlertCount(pushType, pushSource, pushUserType, pushStatus, startTime, endTime, false);
    }

    @Override
    public List<JPushAlertModel> findPushAlerts(int index, int pageSize, PushType pushType,
                   PushSource pushSource, PushUserType pushUserType,PushStatus pushStatus,
                   Date startTime, Date endTime, boolean isAutomatic){
        if (endTime != null) {
            endTime = new DateTime(endTime).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate();
        }
        return jPushAlertMapper.findPushAlerts((index - 1) * pageSize, pageSize, pushType, pushSource, pushUserType, pushStatus, startTime, endTime, isAutomatic);
    }

    @Override
    public JPushAlertModel findJPushAlertModelById(long id) {
        return jPushAlertMapper.findJPushAlertModelById(id);
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
    public void manualJPushAlert(long id) {
        JPushAlertModel jPushAlertModel = jPushAlertMapper.findJPushAlertModelById(id);
        if (jPushAlertModel.isAutomatic()) {
            logger.debug("JPush is failed, this JPush is not manual, id = " + id);
            return;
        }
        if (jPushAlertModel != null) {
            List<String> districtCode = jPushAlertModel.getPushObjects();
            List<String> districtName = null;
            if (CollectionUtils.isNotEmpty(districtCode)) {
                districtName = Lists.transform(districtCode, new Function<String, String>() {
                    @Override
                    public String apply(String input) {
                        return DistrictUtil.convertCodeToName(input);
                    }
                });
            }
            List<String> loginNames = findManualJPushAlertUserLoginName(jPushAlertModel.getPushUserType(), districtName);
            if (CollectionUtils.isEmpty(loginNames)) {
                logger.debug("this JPush without data, id = " + id);
                return;
            }
            if (jPushAlertModel.getPushUserType().contains(PushUserType.ALL) && jPushAlertModel.getPushObjects() == null) {
                String[] jumpToOrLink = chooseJumpToOrLink(new JPushAlertDto(jPushAlertModel));
                mobileAppJPushClient.sendPushAlertByAll(String.valueOf(jPushAlertModel.getId()), jPushAlertModel.getContent(), jumpToOrLink[0], jumpToOrLink[1], jPushAlertModel.getPushSource());
            } else {
                autoJPushByBatchRegistrationId(jPushAlertModel, loginNames, jPushAlertModel.getPushSource());
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
                    List<UserModel> users = userMapper.findAllUsers(Maps.newHashMap(ImmutableMap.<String, Object>builder().put("districtName", districtName).build()));
                    loginNames = Lists.transform(users, new Function<UserModel, String>() {
                        @Override
                        public String apply(UserModel input) {
                            return input.getLoginName();
                        }
                    });
                    break;
                case STAFF:
                    List<UserRoleModel> staffs = userRoleMapper.findAllByRole(Maps.newHashMap(ImmutableMap.<String, Object>builder().put("role", Role.STAFF).put("districtName", districtName).build()));
                    loginNames = Lists.transform(staffs, new Function<UserRoleModel, String>() {
                        @Override
                        public String apply(UserRoleModel input) {
                            return input.getLoginName();
                        }
                    });
                    break;
                case AGENT:
                    List<UserRoleModel> agents = userRoleMapper.findAllByRole(Maps.newHashMap(ImmutableMap.<String, Object>builder().put("role", Role.AGENT).put("districtName", districtName).build()));
                    loginNames = Lists.transform(agents, new Function<UserRoleModel, String>() {
                        @Override
                        public String apply(UserRoleModel input) {
                            return input.getLoginName();
                        }
                    });
                    break;
                case RECOMMENDATION:
                    List<ReferrerRelationModel> recommendations = referrerRelationMapper.findAllRecommendation(Maps.newHashMap(ImmutableMap.<String, Object>builder().put("districtName", districtName).build()));
                    loginNames = Lists.transform(recommendations, new Function<ReferrerRelationModel, String>() {
                        @Override
                        public String apply(ReferrerRelationModel input) {
                            return input.getLoginName();
                        }
                    });
                    break;
                case OTHERS:
                    List<UserModel> others = userMapper.findNaturalUser(Maps.newHashMap(ImmutableMap.<String, Object>builder().put("districtName", districtName).build()));
                    loginNames = Lists.transform(others, new Function<UserModel, String>() {
                        @Override
                        public String apply(UserModel input) {
                            return input.getLoginName();
                        }
                    });
                    break;
            }
            result.addAll(loginNames);
        }
        HashSet<String> hashSet = new HashSet<>(result);
        result.clear();
        result.addAll(hashSet);
        return result;
    }

    @Override
    public void autoJPushAlertBirthMonth() {
        JPushAlertModel jPushAlertModel = jPushAlertMapper.findJPushAlertByPushType(PushType.BIRTHDAY_ALERT_MONTH);
        if (jPushAlertModel != null) {
            List<String> loginNames = accountMapper.findBirthOfAccountInMonth();
            if (CollectionUtils.isEmpty(loginNames)) {
                logger.debug("accountMapper.findBirthOfAccountInMonth() without data");
                return;
            }
            autoJPushByBatchRegistrationId(jPushAlertModel, loginNames, PushSource.ALL);
        } else {
            logger.debug("autoJPushAlertBirthMonthJob is disabled");
        }
    }

    @Override
    public void autoJPushAlertBirthDay() {
        JPushAlertModel jPushAlertModel = jPushAlertMapper.findJPushAlertByPushType(PushType.BIRTHDAY_ALERT_DAY);
        if (jPushAlertModel != null) {
            List<String> loginNames = accountMapper.findBirthOfAccountInDay();
            if (CollectionUtils.isEmpty(loginNames)) {
                logger.debug("accountMapper.findBirthOfAccountInDay() without data");
                return;
            }
            autoJPushByBatchRegistrationId(jPushAlertModel, loginNames, PushSource.ALL);
        } else {
            logger.debug("AutoJPushAlertBirthDayJob is disabled");
        }
    }

    @Override
    public void autoJPushNoInvestAlert() {
        JPushAlertModel jPushAlertModel = jPushAlertMapper.findJPushAlertByPushType(PushType.NO_INVEST_ALERT);
        if (jPushAlertModel != null) {
            try {
                Set<String> jPushAlertSet;
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
                } else {
                    jPushAlertSet = loginNames;
                }

                redisWrapperClient.set(NO_INVEST_LOGIN_NAME, objectMapper.writeValueAsString(loginNames));
                if (CollectionUtils.isNotEmpty(jPushAlertSet)) {
                    autoJPushByBatchRegistrationId(jPushAlertModel, Lists.newArrayList(jPushAlertSet), PushSource.ALL);
                }
            } catch (IOException e) {
                logger.error(e.getLocalizedMessage(), e);
            }
        } else {
            logger.debug("AutoJPushNoInvestAlertJob is disabled");
        }
    }

    @Override
    public void autoJPushLoanAlert(long loanId) {
        List<InvestNotifyInfo> notifyInfos = investMapper.findSuccessInvestMobileEmailAndAmount(loanId);
        JPushAlertModel jPushAlertModel = jPushAlertMapper.findJPushAlertByPushType(PushType.LOAN_ALERT);
        if (jPushAlertModel != null) {
            if (CollectionUtils.isEmpty(notifyInfos)) {
                logger.debug("notifyInfos without data");
                return;
            }
            Map<String, List<String>> loginNameMap = Maps.newHashMap();

            for (InvestNotifyInfo notifyInfo : notifyInfos) {
                List<String> amountLists = Lists.newArrayList(AmountConverter.convertCentToString(notifyInfo.getAmount()));
                loginNameMap.put(notifyInfo.getLoginName(), amountLists);
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
            jumpToOrLink[1] = domainName + jumpToLink;
            return jumpToOrLink;
        }
        if (jumpTo != null) {
            jumpToOrLink[0] = "jumpTo";
            jumpToOrLink[1] = jumpTo.getIndex();
            return jumpToOrLink;
        }
        return jumpToOrLink;
    }


    private void autoJPushByBatchRegistrationId(JPushAlertModel jPushAlertModel, List<String> pushObjects, PushSource pushSource) {
        JPushAlertDto jPushAlertDto = new JPushAlertDto(jPushAlertModel);
        String[] jumpToOrLink = chooseJumpToOrLink(jPushAlertDto);
        List<String> registrationIds = Lists.newArrayList();
        int iosTargetNum = 0;
        int androidTargetNum = 0;

        redisWrapperClient.del(MobileAppJPushClient.APP_PUSH_MSG_ID_KEY + jPushAlertModel.getId());

        for (int i = 0; i < pushObjects.size(); i++) {
            String loginName = pushObjects.get(i);
            if (redisWrapperClient.hexists(JPUSH_ID_KEY, loginName)) {
                String value = redisWrapperClient.hget(JPUSH_ID_KEY, loginName);
                String registrationId;
                String platform;
                if (value.indexOf("-") < 0) {
                    registrationId = value;
                    platform = registrationId.length() == 11 ? "ios" : "android";
                } else {
                    registrationId = value.split("-")[1];
                    platform = value.split("-")[0];
                }
                if ("ios".equals(platform)) {
                    iosTargetNum++;
                } else if ("android".equals(platform)) {
                    androidTargetNum++;
                }
                registrationIds.add(registrationId);
            }
            if (registrationIds.size() == 1000 || (i == pushObjects.size() - 1 && registrationIds.size() > 0)) {
                boolean sendResult = mobileAppJPushClient.sendPushAlertByRegistrationIds("" + jPushAlertModel.getId(), registrationIds, jPushAlertModel.getContent(), jumpToOrLink[0], jumpToOrLink[1], pushSource);
                if (sendResult) {
                    logger.debug(MessageFormat.format("第{0}个用户推送成功", i + 1));
                } else {
                    logger.debug(MessageFormat.format("第{0}个用户推送失败", i + 1));
                }
                registrationIds.clear();
            }
        }
        jPushAlertModel.setAndroidTargetNum(androidTargetNum);
        jPushAlertModel.setIosTargetNum(iosTargetNum);
        jPushAlertMapper.update(jPushAlertModel);
        createGetPushRecordJob(jPushAlertModel.getId());
    }

    private void createGetPushRecordJob(long jpushId) {
        try {
            Date triggerTime = new DateTime().plusHours(25).toDate();
            jobManager.newJob(JobType.GetPushReport, JPushReportFetchingJob.class)
                    .withIdentity(JobType.GetPushReport.name(), "JPush-" + jpushId)
                    .replaceExistingJob(true)
                    .addJobData("JPUSH_ID", jpushId)
                    .runOnceAt(triggerTime).submit();
        } catch (SchedulerException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }

    public BaseDto<JpushReportDto> refreshPushReport(long jpushId) {

        BaseDto<JpushReportDto> baseDto = new BaseDto<>();
        JpushReportDto jpushReportDto = new JpushReportDto();
        baseDto.setData(jpushReportDto);

        Set<String> msgIdList = redisWrapperClient.smembers(MobileAppJPushClient.APP_PUSH_MSG_ID_KEY + jpushId);

        if (msgIdList == null || msgIdList.size() == 0) {
            jpushReportDto.setMessage("此推送为历史推送，无法更新统计数据");
            jpushReportDto.setStatus(false);
            baseDto.setSuccess(false);
            return baseDto;
        }

        String msgIds = Joiner.on(",").join(msgIdList);
        ReceivedsResult result = mobileAppJPushClient.getReportReceived(msgIds);

        int iosArriveNum = 0;
        int androidArriveNum = 0;
        for (ReceivedsResult.Received received : result.received_list) {
            iosArriveNum += received.ios_apns_sent;
            androidArriveNum += received.android_received;
        }

        JPushAlertModel model = jPushAlertMapper.findJPushAlertModelById(jpushId);
        model.setIosArriveNum(iosArriveNum);
        model.setAndroidArriveNum(androidArriveNum);
        jPushAlertMapper.update(model);

        jpushReportDto.setAndroidReceived(androidArriveNum);
        jpushReportDto.setIosReceived(iosArriveNum);
        jpushReportDto.setStatus(true);
        baseDto.setSuccess(true);
        return baseDto;
    }

    private void autoJPushByRegistrationId(JPushAlertModel jPushAlertModel, Map<String, List<String>> pushObjects) {
        JPushAlertDto jPushAlertDto = new JPushAlertDto(jPushAlertModel);
        String[] jumpToOrLink = chooseJumpToOrLink(jPushAlertDto);
        Iterator iterator = pushObjects.entrySet().iterator();
        List<String> registrationIds = Lists.newArrayList();
        while (iterator.hasNext()) {
            Map.Entry<String, List<String>> entry = (Map.Entry<String, List<String>>) iterator.next();
            String loginName = entry.getKey();
            if (redisWrapperClient.hexists(JPUSH_ID_KEY, loginName)) {
                String registrationId = redisWrapperClient.hget(JPUSH_ID_KEY, loginName);
                if (registrationId.indexOf("-") > 0) {
                    registrationId = registrationId.split("-")[1];
                }
                registrationIds.add(registrationId);
                List<String> params = entry.getValue();
                String content = jPushAlertModel.getContent();
                if (CollectionUtils.isNotEmpty(params)) {
                    for (int i = 0; i < params.size(); i++) {
                        content = content.replace("{" + i + "}", params.get(i));

                    }
                }
                boolean sendResult = mobileAppJPushClient.sendPushAlertByRegistrationIds("" + jPushAlertModel.getId(), registrationIds, content, jumpToOrLink[0], jumpToOrLink[1], PushSource.ALL);
                registrationIds.clear();
            }
        }
    }

    private boolean ManualJPushAlertJob(JPushAlertModel jPushAlertModel) {
        if (!jPushAlertModel.getExpectPushTime().after(new Date())) {
            logger.debug("manualJPushAlertJob create failed, expect push time is before now, id = " + jPushAlertModel.getId());
            return false;
        }
        try {
            jobManager.newJob(JobType.ManualJPushAlert, ManualJPushAlertJob.class)
                    .withIdentity(JobType.ManualJPushAlert.name(), "JPush-" + jPushAlertModel.getId())
                    .replaceExistingJob(true)
                    .addJobData("JPUSH_ID", jPushAlertModel.getId())
                    .runOnceAt(jPushAlertModel.getExpectPushTime()).submit();
        } catch (SchedulerException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return true;
    }

    @Override
    public BaseDto<BaseDataDto> pass(String loginName, long id, String ip) {
        logger.debug("JPush audit pass, auditor:" + loginName + ", JPush id:" + id);
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        BaseDataDto baseDataDto = new BaseDataDto();
        baseDto.setData(baseDataDto);

        JPushAlertModel jPushModel = jPushAlertMapper.findJPushAlertModelById(id);
        if (ManualJPushAlertJob(jPushModel)) {
            jPushModel.setStatus(PushStatus.WILL_SEND);
            jPushModel.setAuditor(loginName);
            jPushAlertMapper.update(jPushModel);
            baseDataDto.setStatus(true);
            baseDto.setSuccess(true);

            String auditor = accountService.getRealName(loginName);
            String operator = accountService.getRealName(jPushModel.getCreatedBy());
            String description = auditor + " 审核通过了 " + operator + " 创建的APP推送［" + jPushModel.getName() + "］。";
            auditLogService.createAuditLog(loginName, jPushModel.getCreatedBy(), OperationType.PUSH, String.valueOf(id), description, ip);

            return baseDto;
        } else {
            baseDataDto.setStatus(false);
            baseDto.setSuccess(false);
            baseDataDto.setMessage("审核失败：推送时间已经过期，请核实。");
            return baseDto;
        }
    }

    @Override
    public void reject(String loginName, long id, String ip) {
        logger.debug("JPush audit reject, auditor:" + loginName + ", JPush id:" + id);
        JPushAlertModel jPushModel = jPushAlertMapper.findJPushAlertModelById(id);
        jPushModel.setStatus(PushStatus.REJECTED);
        jPushModel.setAuditor(loginName);
        jPushAlertMapper.update(jPushModel);

        String auditor = accountService.getRealName(loginName);
        String operator = accountService.getRealName(jPushModel.getCreatedBy());
        String description = auditor + " 驳回了 " + operator + " 创建的APP推送［" + jPushModel.getName() + "］。";
        auditLogService.createAuditLog(loginName, jPushModel.getCreatedBy(), OperationType.PUSH, String.valueOf(id), description, ip);
    }

    @Override
    public void delete(String loginName, long id) {
        logger.debug("JPush audit delete, operator:" + loginName + ", JPush id:" + id);
        jobManager.deleteJob(JobType.ManualJPushAlert, JobType.ManualJPushAlert.name(), "JPush-" + id);
        jPushAlertMapper.delete(id);
    }

}
