package com.tuotiansudai.jpush.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.jpush.client.MobileAppJPushClient;
import com.tuotiansudai.jpush.dto.JPushAlertDto;
import com.tuotiansudai.jpush.repository.mapper.JPushAlertMapper;
import com.tuotiansudai.jpush.repository.model.JPushAlertModel;
import com.tuotiansudai.jpush.repository.model.JumpTo;
import com.tuotiansudai.jpush.repository.model.PushStatus;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

@Service
public class JPushAlertNewService {

    static Logger logger = Logger.getLogger(JPushAlertNewService.class);

    @Autowired
    private JPushAlertMapper jPushAlertMapper;

    @Autowired
    private MobileAppJPushClient mobileAppJPushClient;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    private static final String JPUSH_ID_KEY = "api:jpushId:store";

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

    public void autoJPushAlertSendToAll(JPushAlertModel jPushAlertModel) {
        if (null != jPushAlertModel) {
            Map extras = chooseJumpToOrLink(new JPushAlertDto(jPushAlertModel));
            mobileAppJPushClient.sendPushAlertByAll(jPushAlertModel.getContent(), extras, jPushAlertModel.getPushSource());
            jPushAlertModel.setStatus(PushStatus.SEND_SUCCESS);
            jPushAlertMapper.update(jPushAlertModel);
        } else {
            logger.info("Auto JPush is disabled.");
        }
    }

    public JPushAlertModel findJPushAlertModelByMessageId(long messageId) {
        return jPushAlertMapper.findByMessageId(messageId);
    }

    public void autoJPushBatchByLoginNames(JPushAlertModel jPushAlertModel, List<String> loginNames) {
        JPushAlertDto jPushAlertDto = new JPushAlertDto(jPushAlertModel);
        Map extras = chooseJumpToOrLink(jPushAlertDto);
        List<String> registrationIds = Lists.newArrayList();

        redisWrapperClient.del(MobileAppJPushClient.APP_PUSH_MSG_ID_KEY + jPushAlertModel.getId());

        for (int i = 0; i < loginNames.size(); i++) {
            String loginName = loginNames.get(i);
            if (redisWrapperClient.hexists(JPUSH_ID_KEY, loginName)) {
                String value = redisWrapperClient.hget(JPUSH_ID_KEY, loginName);
                String registrationId;
                if (!value.contains("-")) {
                    registrationId = value;
                } else {
                    registrationId = value.split("-")[1];
                }
                registrationIds.add(registrationId);
            }
            if (registrationIds.size() == 1000 || (i == loginNames.size() - 1 && registrationIds.size() > 0)) {
                boolean sendResult = mobileAppJPushClient.sendPushAlertByRegistrationIds(registrationIds, jPushAlertModel.getContent(), extras, jPushAlertModel.getPushSource());
                if (sendResult) {
                    logger.info(MessageFormat.format("第{0}个用户推送成功", i + 1));
                } else {
                    logger.info(MessageFormat.format("第{0}个用户推送失败", i + 1));
                }
                registrationIds.clear();
            }
        }
    }
}
