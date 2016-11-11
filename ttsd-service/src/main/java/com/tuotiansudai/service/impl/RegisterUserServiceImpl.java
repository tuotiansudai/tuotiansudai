package com.tuotiansudai.service.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.exception.ReferrerRelationException;
import com.tuotiansudai.membership.repository.mapper.MembershipMapper;
import com.tuotiansudai.membership.repository.mapper.UserMembershipMapper;
import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipModel;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.mapper.UserRoleMapper;
import com.tuotiansudai.repository.model.Role;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserRoleModel;
import com.tuotiansudai.service.ReferrerRelationService;
import com.tuotiansudai.service.RegisterUserService;
import com.tuotiansudai.util.TransactionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegisterUserServiceImpl implements RegisterUserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private MembershipMapper membershipMapper;

    @Autowired
    private UserMembershipMapper userMembershipMapper;

    @Autowired
    private ReferrerRelationService referrerRelationService;

    @Autowired
    private MQClient mqClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean register(UserModel userModel) throws ReferrerRelationException {
        this.userMapper.create(userModel);

        this.userRoleMapper.create(Lists.newArrayList(new UserRoleModel(userModel.getLoginName(), Role.USER)));

        if (!Strings.isNullOrEmpty(userModel.getReferrer())) {
            this.referrerRelationService.generateRelation(userModel.getReferrer(), userModel.getLoginName());
        }

        MembershipModel membershipModel = membershipMapper.findByLevel(0);
        UserMembershipModel userMembershipModel = UserMembershipModel.createUpgradeUserMembershipModel(userModel.getLoginName(), membershipModel.getId());
        userMembershipMapper.create(userMembershipModel);

        TransactionUtil.runAfterCommit(() -> mqClient.publishMessage(MessageTopic.UserRegistered, dto.getMobile()));
        return true;
    }
}
