package com.tuotiansudai.membership.service.impl;

import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.membership.dto.MembershipGiveDto;
import com.tuotiansudai.membership.repository.mapper.MembershipGiveMapper;
import com.tuotiansudai.membership.repository.mapper.MembershipMapper;
import com.tuotiansudai.membership.repository.mapper.UserMembershipMapper;
import com.tuotiansudai.membership.repository.model.*;
import com.tuotiansudai.membership.service.ImportUtils;
import com.tuotiansudai.membership.service.MembershipGiveService;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MembershipGiveServiceImpl implements MembershipGiveService {

    static Logger logger = Logger.getLogger(MembershipGiveServiceImpl.class);

    @Autowired
    MembershipGiveMapper membershipGiveMapper;

    @Autowired
    MembershipMapper membershipMapper;

    @Autowired
    UserMembershipMapper userMembershipMapper;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Override
    public void createAndEditMembershipGive(MembershipGiveDto membershipGiveDto, long importUsersId) {
        long membershipGiveId = membershipGiveDto.getId();

        MembershipGiveModel membershipGiveModel = membershipGiveMapper.findById(membershipGiveId);

        if (null != membershipGiveModel) {
            editMembershipGive(membershipGiveModel, membershipGiveDto, importUsersId);
        } else {
            createMembershipGive(membershipGiveDto, importUsersId);
        }
    }

    @SuppressWarnings(value = "unchecked")
    private void createMembershipGive(MembershipGiveDto membershipGiveDto, long importUsersId) {
        MembershipModel membershipModel = membershipMapper.findByLevel(membershipGiveDto.getMembershipLevel());
        if (null == membershipModel) {
            logger.error(MessageFormat.format("[MembershipGiveServiceImpl][createMembershipGive]Membership level {0} not exist.", membershipGiveDto.getMembershipLevel()));
            return;
        }
        MembershipGiveModel membershipGiveModel = new MembershipGiveModel(membershipGiveDto, membershipModel);
        membershipGiveMapper.create(membershipGiveModel);

        if (membershipGiveDto.getUserGroup().equals(MembershipUserGroup.IMPORT_USER)) {
            String membershipGiveId = String.valueOf(membershipGiveModel.getId());
            List<String> importUsers = (List<String>) redisWrapperClient.hgetSeri(ImportUtils.redisMembershipGiveReceivers, String.valueOf(importUsersId));
            redisWrapperClient.hdelSeri(ImportUtils.redisMembershipGiveReceivers, String.valueOf(importUsersId));
            redisWrapperClient.hsetSeri(ImportUtils.redisMembershipGiveReceivers, membershipGiveId, importUsers);
        }
    }

    @SuppressWarnings(value = "unchecked")
    private void editMembershipGive(MembershipGiveModel originMembershipGiveModel, MembershipGiveDto membershipGiveDto, long importUsersId) {
        MembershipModel membershipModel = membershipMapper.findByLevel(membershipGiveDto.getMembershipLevel());
        if (null == membershipModel) {
            logger.error(MessageFormat.format("[MembershipGiveServiceImpl][createMembershipGive]Membership level {0} not exist.", membershipGiveDto.getMembershipLevel()));
            return;
        }

        if (originMembershipGiveModel.isValid()) {
            return;
        }

        originMembershipGiveModel.setMembershipId(membershipModel.getId());
        originMembershipGiveModel.setValidPeriod(membershipGiveDto.getValidPeriod());
        originMembershipGiveModel.setReceiveStartTime(membershipGiveDto.getReceiveStartTime());
        originMembershipGiveModel.setReceiveEndTime(membershipGiveDto.getReceiveEndTime());
        originMembershipGiveModel.setUserGroup(membershipGiveDto.getUserGroup());
        originMembershipGiveModel.setSmsNotify(membershipGiveDto.isSmsNotify());
        originMembershipGiveModel.setUpdatedTime(new Date());
        originMembershipGiveModel.setUpdatedLoginName(membershipGiveDto.getUpdatedLoginName());

        membershipGiveMapper.update(originMembershipGiveModel);

        if (!membershipGiveDto.getUserGroup().equals(MembershipUserGroup.IMPORT_USER)) {
            redisWrapperClient.hdelSeri(ImportUtils.redisMembershipGiveReceivers, String.valueOf(importUsersId));
        }
    }

    @SuppressWarnings(value = "unchecked")
    @Transactional
    @Override
    public BaseDto<BaseDataDto> approveMembershipGive(long id, String validLoginName) {
        MembershipGiveModel membershipGiveModel = membershipGiveMapper.findById(id);
        if (null == membershipGiveModel) {
            return new BaseDto<>(new BaseDataDto(false, "会员发放计划不存在"));
        }
        if (membershipGiveModel.isValid()) {
            return new BaseDto<>(new BaseDataDto(false, "会员发放计划已生效"));
        }

        membershipGiveModel.setValid(true);
        membershipGiveModel.setValidLoginName(validLoginName);
        membershipGiveModel.setUpdatedLoginName(validLoginName);
        membershipGiveModel.setUpdatedTime(new Date());

        membershipGiveMapper.update(membershipGiveModel);

        if (membershipGiveModel.getUserGroup().equals(MembershipUserGroup.IMPORT_USER)) {
            List<UserMembershipModel> userMembershipModels = new ArrayList<>();
            List<String> importUsers = (List<String>) redisWrapperClient.hgetSeri(ImportUtils.redisMembershipGiveReceivers, String.valueOf(membershipGiveModel.getId()));
            for (String loginName : importUsers) {
                UserMembershipModel userMembershipModel = new UserMembershipModel(loginName, membershipGiveModel.getMembershipId(),
                        DateTime.now().plusDays(membershipGiveModel.getValidPeriod()).toDate(), UserMembershipType.GIVEN);
                userMembershipModel.setMembershipGiveId(membershipGiveModel.getId());
                userMembershipModels.add(userMembershipModel);
            }
            userMembershipMapper.createMass(userMembershipModels);
        }

        return new BaseDto<>(new BaseDataDto(true));
    }

    @Override
    public BaseDto<BaseDataDto> cancelMembershipGive(long id, String validLoginName) {
        MembershipGiveModel membershipGiveModel = membershipGiveMapper.findById(id);
        if (null == membershipGiveModel) {
            return new BaseDto<>(new BaseDataDto(false, "会员发放计划不存在"));
        }
        if (!membershipGiveModel.isValid()) {
            return new BaseDto<>(new BaseDataDto(false, "会员发放计划未生效"));
        }
        if (!membershipGiveModel.getUserGroup().equals(MembershipUserGroup.NEW_REGISTERED_USER)) {
            return new BaseDto<>(new BaseDataDto(false, "只有新注册用户的会员发放计划可以取消"));
        }

        membershipGiveModel.setValid(false);
        membershipGiveModel.setValidLoginName(validLoginName);
        membershipGiveModel.setUpdatedLoginName(validLoginName);
        membershipGiveModel.setUpdatedTime(new Date());

        membershipGiveMapper.update(membershipGiveModel);

        return new BaseDto<>(new BaseDataDto(true));
    }
}
