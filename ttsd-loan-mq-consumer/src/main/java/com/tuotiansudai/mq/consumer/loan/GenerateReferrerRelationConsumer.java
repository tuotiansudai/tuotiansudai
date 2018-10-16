package com.tuotiansudai.mq.consumer.loan;

import com.google.common.base.Strings;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.mq.consumer.loan.service.ReferrerRelationService;
import com.tuotiansudai.mq.consumer.loan.service.StaffRecommendRoleService;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class GenerateReferrerRelationConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(GenerateReferrerRelationConsumer.class);

    private final UserMapper userMapper;

    private final ReferrerRelationService referrerRelationService;

    private final StaffRecommendRoleService staffRecommendRoleService;

    @Autowired
    public GenerateReferrerRelationConsumer(UserMapper userMapper, ReferrerRelationService referrerRelationService, StaffRecommendRoleService staffRecommendRoleService) {
        this.userMapper = userMapper;
        this.referrerRelationService = referrerRelationService;
        this.staffRecommendRoleService = staffRecommendRoleService;
    }

    @Override
    public MessageQueue queue() {
        return MessageQueue.GenerateReferrerRelation;
    }

    @Override
    public void consume(String message) {
        logger.info("[GenerateReferrerRelation MQ] receive message: '{}'", message);
        if (Strings.isNullOrEmpty(message)) {
            logger.error("[GenerateReferrerRelation MQ] message is empty");
            return;
        }

        UserModel userModel = userMapper.findByLoginNameOrMobile(message);
        if(userModel == null){
            logger.error("[GenerateReferrerRelation MQ] login name({}) not found ", message);
            return;
        }

        try {
            this.referrerRelationService.generateRelation(userModel.getReferrer(), userModel.getLoginName());
            String staffReferrerMobile = this.referrerRelationService.findStaffReferrerMobileByLoginName(userModel.getLoginName());
            if (!Strings.isNullOrEmpty(staffReferrerMobile)){
                userMapper.updateStaffReferrerMobile(userModel.getLoginName(), staffReferrerMobile);
            }
            this.staffRecommendRoleService.generateStaffRole(userModel.getReferrer(), userModel.getLoginName());
        } catch (Exception e) {
            logger.error(MessageFormat.format("[GenerateReferrerRelation MQ] receive message: {0}, user is {1} new referrer is {2}", message, userModel.getLoginName(), userModel.getReferrer()), e);
        }
        logger.info("[GenerateReferrerRelation MQ] receive message: '{}'. finished", message);
    }
}
