package com.tuotiansudai.message.util;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.tuotiansudai.message.repository.mapper.MessageMapper;
import com.tuotiansudai.message.repository.model.MessageModel;
import com.tuotiansudai.message.repository.model.MessageUserGroup;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.ReferrerRelationMapper;
import com.tuotiansudai.repository.mapper.UserRoleMapper;
import com.tuotiansudai.repository.model.ReferrerRelationModel;
import com.tuotiansudai.repository.model.Role;
import com.tuotiansudai.repository.model.UserRoleModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageUserGroupDecisionManager {

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private ReferrerRelationMapper referrerRelationMapper;

    @Autowired
    private InvestMapper investMapper;

    public boolean decide(final String loginName, long messageId) {
        MessageModel messageModel = this.messageMapper.findById(messageId);
        List<MessageUserGroup> userGroups = messageModel.getUserGroups();

        return Iterators.any(userGroups.iterator(), new Predicate<MessageUserGroup>() {
            @Override
            public boolean apply(MessageUserGroup userGroup) {
                return MessageUserGroupDecisionManager.this.contains(loginName, userGroup);
            }
        });
    }

    private boolean contains(String loginName, MessageUserGroup messageUserGroup) {
        switch (messageUserGroup) {
            case ALL_USER:
                return true;
            case STAFF:
                List<UserRoleModel> userRoleModelList = userRoleMapper.findByLoginName(loginName);
                return Iterators.tryFind(userRoleModelList.iterator(), new Predicate<UserRoleModel>() {
                    @Override
                    public boolean apply(UserRoleModel input) {
                        return input.getRole() == Role.STAFF;
                    }
                }).isPresent();
            case STAFF_RECOMMEND_LEVEL_ONE:
                List<ReferrerRelationModel> referrerRelationModels = referrerRelationMapper.findByLoginNameAndLevel(loginName, 1);
                return Iterators.tryFind(referrerRelationModels.iterator(), new Predicate<ReferrerRelationModel>() {
                    @Override
                    public boolean apply(ReferrerRelationModel input) {
                        List<UserRoleModel> userRoleModelList = userRoleMapper.findByLoginName(input.getReferrerLoginName());
                        return Iterators.tryFind(userRoleModelList.iterator(), new Predicate<UserRoleModel>() {
                            @Override
                            public boolean apply(UserRoleModel input) {
                                return input.getRole() == Role.STAFF;
                            }
                        }).isPresent();
                    }
                }).isPresent();
            case REGISTERED_NOT_INVESTED_USER:
                return investMapper.sumSuccessInvestAmountByLoginName(null, loginName) == 0;
        }
        return false;
    }
}
