package com.tuotiansudai.jpush.service;

import com.google.common.collect.Maps;
import com.tuotiansudai.jpush.client.MobileAppJPushClient;
import com.tuotiansudai.jpush.dto.JPushAlertDto;
import com.tuotiansudai.jpush.repository.mapper.JPushAlertMapper;
import com.tuotiansudai.jpush.repository.model.JPushAlertModel;
import com.tuotiansudai.jpush.repository.model.JumpTo;
import com.tuotiansudai.jpush.repository.model.PushStatus;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class JPushAlertNewService {

    static Logger logger = Logger.getLogger(JPushAlertNewService.class);

    @Autowired
    private JPushAlertMapper jPushAlertMapper;

    @Autowired
    private MobileAppJPushClient mobileAppJPushClient;

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

    public void autoJPushAlertSend(JPushAlertModel jPushAlertModel) {
        if (null != jPushAlertModel) {
            Map extras = chooseJumpToOrLink(new JPushAlertDto(jPushAlertModel));
            mobileAppJPushClient.sendPushAlertByAll(String.valueOf(jPushAlertModel.getId()), jPushAlertModel.getContent(), extras, jPushAlertModel.getPushSource());
            jPushAlertModel.setStatus(PushStatus.SEND_SUCCESS);
            jPushAlertMapper.update(jPushAlertModel);
        } else {
            logger.debug("Auto JPush is disabled.");
        }
    }

    public JPushAlertModel findJPushAlertModelByMessageId(long messageId) {
        return jPushAlertMapper.findByMessageId(messageId);
    }
}
