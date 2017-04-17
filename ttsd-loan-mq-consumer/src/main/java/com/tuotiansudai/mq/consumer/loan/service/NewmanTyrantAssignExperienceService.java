package com.tuotiansudai.mq.consumer.loan.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.enums.ExperienceBillBusinessType;
import com.tuotiansudai.enums.ExperienceBillOperationType;
import com.tuotiansudai.enums.MessageEventType;
import com.tuotiansudai.message.EventMessage;
import com.tuotiansudai.message.NewmanTyrantMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.service.ExperienceBillService;
import com.tuotiansudai.util.AmountConverter;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Date;

@Service
public class NewmanTyrantAssignExperienceService {
    private final static Logger logger = Logger.getLogger(NewmanTyrantAssignExperienceService.class);
    @Autowired
    private ExperienceBillService experienceBillService;
    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Transactional
    public void grantExperience(NewmanTyrantMessage newmanTyrantMessage){
        logger.info(String.format("[NewmanTyrantAssignExperienceService %s] grant %s experience  begin ...",
                DateFormatUtils.format(newmanTyrantMessage.getCurrentDate(), "yyyy-MM-dd"),
                newmanTyrantMessage.getLoginName()));
        experienceBillService.updateUserExperienceBalanceByLoginName(588800, newmanTyrantMessage.getLoginName(), ExperienceBillOperationType.IN, ExperienceBillBusinessType.NEWMAN_TYRANT,
                MessageFormat.format(ExperienceBillBusinessType.NEWMAN_TYRANT.getContentTemplate(),
                        AmountConverter.convertCentToString(588800),
                        newmanTyrantMessage.getCurrentDate()));
        mqWrapperClient.sendMessage(MessageQueue.EventMessage, new EventMessage(MessageEventType.NEWMAN_TYRANT,
                Lists.newArrayList(newmanTyrantMessage.getLoginName()),
                MessageEventType.NEWMAN_TYRANT.getTitleTemplate(),
                MessageFormat.format(MessageEventType.NEWMAN_TYRANT.getContentTemplate(),DateFormatUtils.format(newmanTyrantMessage.getCurrentDate(),"yyyy-MM-dd")),
                null));
        logger.info(String.format("[NewmanTyrantAssignExperienceService %s] grant %s experience end ...",
                DateFormatUtils.format(newmanTyrantMessage.getCurrentDate(), "yyyy-MM-dd"),
                newmanTyrantMessage.getLoginName()));
    }
}
