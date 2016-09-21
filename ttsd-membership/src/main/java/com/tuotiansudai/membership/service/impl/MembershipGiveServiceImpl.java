package com.tuotiansudai.membership.service.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.membership.dto.MembershipGiveDto;
import com.tuotiansudai.membership.dto.MembershipGiveReceiveDto;
import com.tuotiansudai.membership.repository.mapper.MembershipGiveMapper;
import com.tuotiansudai.membership.repository.mapper.MembershipMapper;
import com.tuotiansudai.membership.repository.mapper.UserMembershipMapper;
import com.tuotiansudai.membership.repository.model.*;
import com.tuotiansudai.membership.service.ImportService;
import com.tuotiansudai.membership.service.MembershipGiveService;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.*;

@Service
public class MembershipGiveServiceImpl implements MembershipGiveService {

    static Logger logger = Logger.getLogger(MembershipGiveServiceImpl.class);

    @Autowired
    private MembershipGiveMapper membershipGiveMapper;

    @Autowired
    private MembershipMapper membershipMapper;

    @Autowired
    private UserMembershipMapper userMembershipMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Autowired
    private ImportService importService;

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

    @Override
    public MembershipGiveDto getMembershipGiveDtoById(long membershipGiveId) {
        MembershipGiveModel membershipGiveModel = membershipGiveMapper.findById(membershipGiveId);
        if (null == membershipGiveModel) {
            return null;
        } else {
            MembershipModel membershipModel = membershipMapper.findById(membershipGiveModel.getMembershipId());
            if (null == membershipModel) {
                return null;
            } else {
                MembershipGiveDto membershipGiveDto = new MembershipGiveDto(membershipGiveModel);
                membershipGiveDto.setMembershipLevel(membershipModel.getLevel());
                return membershipGiveDto;
            }
        }
    }

    @Override
    public List<MembershipGiveDto> getMembershipGiveDtos(int index, int pageSize) {
        List<MembershipModel> membershipModels = membershipMapper.findAllMembership();
        Map<Long, Integer> map = new HashMap<>();
        for (MembershipModel membershipModel : membershipModels) {
            map.put(membershipModel.getId(), membershipModel.getLevel());
        }

        List<MembershipGiveModel> membershipGiveModels = membershipGiveMapper.findSome((index - 1) * pageSize, pageSize);
        List<MembershipGiveDto> membershipGiveDtos = new ArrayList<>();
        for (MembershipGiveModel membershipGiveModel : membershipGiveModels) {
            MembershipGiveDto membershipGiveDto = new MembershipGiveDto(membershipGiveModel);
            membershipGiveDto.setMembershipLevel(map.get(membershipGiveModel.getMembershipId()));
            membershipGiveDtos.add(membershipGiveDto);
        }
        return membershipGiveDtos;
    }

    @Override
    public long getMembershipGiveCount() {
        return membershipGiveMapper.findAllCount();
    }

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
            List<String> importUsers = importService.getImportStrings(ImportService.redisMembershipGiveReceivers, importUsersId);
            redisWrapperClient.hdelSeri(ImportService.redisMembershipGiveReceivers, String.valueOf(importUsersId));
            redisWrapperClient.hsetSeri(ImportService.redisMembershipGiveReceivers, membershipGiveId, importUsers);
        }
    }

    private void editMembershipGive(MembershipGiveModel originMembershipGiveModel, MembershipGiveDto membershipGiveDto, long importUsersId) {
        if (originMembershipGiveModel.isValid()) {
            return;
        }

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

        List<String> importUsers = importService.getImportStrings(ImportService.redisMembershipGiveReceivers, importUsersId);
        redisWrapperClient.hdelSeri(ImportService.redisMembershipGiveReceivers, String.valueOf(importUsersId));
        if (membershipGiveDto.getUserGroup().equals(MembershipUserGroup.IMPORT_USER)) {
            redisWrapperClient.hsetSeri(ImportService.redisMembershipGiveReceivers, String.valueOf(originMembershipGiveModel.getId()), importUsers);
        }
    }

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
        if (membershipGiveModel.getUserGroup().equals(MembershipUserGroup.IMPORT_USER)) {
            List<String> importUsers = importService.getImportStrings(ImportService.redisMembershipGiveReceivers, membershipGiveModel.getId());
            if (0 == importUsers.size()) {
                return new BaseDto<>(new BaseDataDto(false, "导入用户名单未导入或已丢失"));
            }
            for (String loginName : importUsers) {
                UserModel userModel = userMapper.findByLoginName(loginName);
                if (null == userModel) {
                    return new BaseDto<>(new BaseDataDto(false, "导入用户名单中含有不存在的用户名,需要重新导入"));
                }
            }
        }

        membershipGiveModel.setValid(true);
        membershipGiveModel.setValidLoginName(validLoginName);
        membershipGiveModel.setUpdatedLoginName(validLoginName);
        membershipGiveModel.setUpdatedTime(new Date());

        membershipGiveMapper.update(membershipGiveModel);

        if (membershipGiveModel.getUserGroup().equals(MembershipUserGroup.IMPORT_USER)) {
            List<UserMembershipModel> userMembershipModels = new ArrayList<>();
            List<String> importUsers = importService.getImportStrings(ImportService.redisMembershipGiveReceivers, membershipGiveModel.getId());
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

    @Override
    public BaseDto<BaseDataDto> importGiveUsers(long importUsersId, InputStream inputStream) {
        if (null == inputStream) {
            return new BaseDto<>(new BaseDataDto(false, "没有导入名单"));
        }
        long newImportUsersId;
        try {
            newImportUsersId = importService.importStrings(ImportService.redisMembershipGiveReceivers, importUsersId, inputStream);
        } catch (IOException e) {
            return new BaseDto<>(new BaseDataDto(false, "用户导入失败"));
        } catch (IllegalArgumentException e) {
            return new BaseDto<>(new BaseDataDto(false, "无法存储导入名单"));
        } finally {
            try {
                inputStream.close();
            } catch (Exception e) {
                logger.error("[MembershipGiveServiceImpl][importGiveUsers] importUsers stream close fail!");
            }
        }
        return new BaseDto<>(new BaseDataDto(true, String.valueOf(newImportUsersId)));
    }

    @Override
    public List<MembershipGiveReceiveDto> getMembershipGiveReceiveDtosByMobile(long membershipGiveId, String mobile,
                                                                               int index, int pageSize) {
        UserModel userModel = userMapper.findByMobile(mobile);
        String loginName = null;
        if (null != userModel) {
            loginName = userModel.getLoginName();
        }

        List<UserMembershipModel> userMembershipModels = userMembershipMapper.findGiveMembershipsByLoginNameAndGiveId(membershipGiveId, loginName, (index - 1) * pageSize, pageSize);
        return Lists.transform(userMembershipModels, new Function<UserMembershipModel, MembershipGiveReceiveDto>() {
            @Override
            public MembershipGiveReceiveDto apply(UserMembershipModel userMembershipModel) {
                MembershipGiveReceiveDto membershipGiveReceiveDto = new MembershipGiveReceiveDto();
                membershipGiveReceiveDto.setLoginName(userMembershipModel.getLoginName());
                membershipGiveReceiveDto.setMobile(userMapper.findByLoginName(userMembershipModel.getLoginName()).getMobile());
                membershipGiveReceiveDto.setReceiveTime(userMembershipModel.getCreatedTime());
                return membershipGiveReceiveDto;
            }
        });
    }

    @Override
    public long getCountMembershipGiveReceiveDtosByMobile(long membershipGiveId, String mobile) {
        UserModel userModel = userMapper.findByMobile(mobile);
        String loginName = null;
        if (null != userModel) {
            loginName = userModel.getLoginName();
        }

        return userMembershipMapper.findCountGiveMembershipsByLoginNameAndGiveId(membershipGiveId, loginName);
    }
}
