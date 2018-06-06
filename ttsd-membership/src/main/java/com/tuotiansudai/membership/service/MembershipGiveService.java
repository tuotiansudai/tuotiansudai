package com.tuotiansudai.membership.service;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.membership.dto.MembershipGiveDto;
import com.tuotiansudai.membership.dto.MembershipGiveReceiveDto;
import com.tuotiansudai.membership.repository.mapper.MembershipExperienceBillMapper;
import com.tuotiansudai.membership.repository.mapper.MembershipGiveMapper;
import com.tuotiansudai.membership.repository.mapper.MembershipMapper;
import com.tuotiansudai.membership.repository.mapper.UserMembershipMapper;
import com.tuotiansudai.membership.repository.model.*;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.util.PaginationUtil;
import com.tuotiansudai.util.RedisWrapperClient;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.*;

@Service
public class MembershipGiveService {

    static Logger logger = Logger.getLogger(MembershipGiveService.class);

    private RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    @Autowired
    private MembershipGiveMapper membershipGiveMapper;

    @Autowired
    private MembershipMapper membershipMapper;

    @Autowired
    private UserMembershipMapper userMembershipMapper;

    @Autowired
    private MembershipExperienceBillMapper membershipExperienceBillMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private ImportService importService;

    @Value("#{'${web.heroRanking.activity.period}'.split('\\~')}")
    private List<String> heroRankingActivityPeriod = Lists.newArrayList();

    public void createAndEditMembershipGive(MembershipGiveDto membershipGiveDto, long importUsersId) {
        long membershipGiveId = membershipGiveDto.getId();

        MembershipGiveModel membershipGiveModel = membershipGiveMapper.findById(membershipGiveId);

        if (null != membershipGiveModel) {
            editMembershipGive(membershipGiveModel, membershipGiveDto, importUsersId);
        } else {
            createMembershipGive(membershipGiveDto, importUsersId);
        }
    }

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

    public BasePaginationDataDto<MembershipGiveDto> getMembershipGiveDtos(int index, int pageSize) {
        long totalCount = membershipGiveMapper.findAllCount();
        List<MembershipGiveModel> membershipGiveModels = membershipGiveMapper.findPagination(PaginationUtil.calculateOffset(index, pageSize, totalCount), pageSize);

        final Map<Long, Integer> idLevelMap = getIdLevelMap();
        List<MembershipGiveDto> membershipGiveDtos = Lists.transform(membershipGiveModels, new Function<MembershipGiveModel, MembershipGiveDto>() {
            @Override
            public MembershipGiveDto apply(MembershipGiveModel membershipGiveModel) {
                MembershipGiveDto membershipGiveDto = new MembershipGiveDto(membershipGiveModel);
                membershipGiveDto.setMembershipLevel(idLevelMap.get(membershipGiveModel.getMembershipId()));
                return membershipGiveDto;
            }
        });
        return new BasePaginationDataDto<>(index, pageSize, totalCount, membershipGiveDtos);
    }

    public BasePaginationDataDto<MembershipGiveReceiveDto> getMembershipGiveReceiveDtosByMobile(long membershipGiveId, String mobile,
                                                                                                int index, int pageSize) {
        UserModel userModel = userMapper.findByMobile(mobile);
        String loginName = null;
        if (null != userModel) {
            loginName = userModel.getLoginName();
        }

        long totalCount = userMembershipMapper.findCountGiveMembershipsByLoginNameAndGiveId(membershipGiveId, loginName);

        List<UserMembershipModel> userMembershipModels = userMembershipMapper.findGiveMembershipsByLoginNameAndGiveId(membershipGiveId, loginName, PaginationUtil.calculateOffset(index, pageSize, totalCount), pageSize);
        List<MembershipGiveReceiveDto> membershipGiveReceiveDtos = Lists.transform(userMembershipModels, new Function<UserMembershipModel, MembershipGiveReceiveDto>() {
            @Override
            public MembershipGiveReceiveDto apply(UserMembershipModel userMembershipModel) {
                MembershipGiveReceiveDto membershipGiveReceiveDto = new MembershipGiveReceiveDto();
                membershipGiveReceiveDto.setLoginName(userMembershipModel.getLoginName());
                membershipGiveReceiveDto.setMobile(userMapper.findByLoginName(userMembershipModel.getLoginName()).getMobile());
                membershipGiveReceiveDto.setReceiveTime(userMembershipModel.getCreatedTime());
                return membershipGiveReceiveDto;
            }
        });

        return new BasePaginationDataDto<>(index, pageSize, totalCount, membershipGiveReceiveDtos);
    }

    public void newUserReceiveMembership(String loginName) {
        List<MembershipGiveModel> membershipGiveModels = membershipGiveMapper.findAllCurrentNewUserGivePlans();
        giveUsersMemberships(Lists.newArrayList(loginName), membershipGiveModels);
    }

    @Transactional
    public BaseDto<BaseDataDto> approveMembershipGive(long id, String activeBy) {
        MembershipGiveModel membershipGiveModel = membershipGiveMapper.findById(id);
        if (null == membershipGiveModel) {
            return new BaseDto<>(new BaseDataDto(false, "会员发放计划不存在"));
        }
        if (membershipGiveModel.isActive()) {
            return new BaseDto<>(new BaseDataDto(false, "会员发放计划已生效"));
        }

        if (membershipGiveModel.getUserGroup().equals(MembershipUserGroup.IMPORT_USER)) {
            List<String> importUsers = importService.getImportStrings(ImportService.redisMembershipGiveReceivers, membershipGiveModel.getId());
            if (0 == importUsers.size()) {
                return new BaseDto<>(new BaseDataDto(false, "导入用户名单未导入或已丢失"));
            }
            List<String> mobiles = new ArrayList<>();
            for (String loginName : importUsers) {
                UserModel userModel = userMapper.findByLoginName(loginName);
                if (null == userModel) {
                    return new BaseDto<>(new BaseDataDto(false, "导入用户名单中含有不存在的用户名,需要重新导入"));
                }
                mobiles.add(userModel.getMobile());
            }
            giveUsersMemberships(importUsers, Lists.newArrayList(membershipGiveModel));
        }

        membershipGiveModel.setActive(true);
        membershipGiveModel.setActiveBy(activeBy);
        membershipGiveModel.setUpdatedBy(activeBy);
        membershipGiveModel.setUpdatedTime(new Date());

        membershipGiveMapper.update(membershipGiveModel);

        return new BaseDto<>(new BaseDataDto(true));
    }

    public BaseDto<BaseDataDto> cancelMembershipGive(long id, String validLoginName) {
        MembershipGiveModel membershipGiveModel = membershipGiveMapper.findById(id);
        if (null == membershipGiveModel) {
            return new BaseDto<>(new BaseDataDto(false, "会员发放计划不存在"));
        }
        if (!membershipGiveModel.isActive()) {
            return new BaseDto<>(new BaseDataDto(false, "会员发放计划未生效"));
        }
        if (!membershipGiveModel.getUserGroup().equals(MembershipUserGroup.NEW_REGISTERED_USER)) {
            return new BaseDto<>(new BaseDataDto(false, "只有新注册用户的会员发放计划可以取消"));
        }

        membershipGiveModel.setActive(false);
        membershipGiveModel.setActiveBy(validLoginName);
        membershipGiveModel.setUpdatedBy(validLoginName);
        membershipGiveModel.setUpdatedTime(new Date());

        membershipGiveMapper.update(membershipGiveModel);

        return new BaseDto<>(new BaseDataDto(true));
    }

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

    private void createMembershipGive(MembershipGiveDto membershipGiveDto, long importUsersId) {
        MembershipModel membershipModel = membershipMapper.findByLevel(membershipGiveDto.getMembershipLevel());
        MembershipGiveModel membershipGiveModel = new MembershipGiveModel(membershipGiveDto, membershipModel);
        membershipGiveMapper.create(membershipGiveModel);

        List<String> importUsers = importService.getImportStrings(ImportService.redisMembershipGiveReceivers, importUsersId);
        redisWrapperClient.hdelSeri(ImportService.redisMembershipGiveReceivers, String.valueOf(importUsersId));
        if (membershipGiveDto.getUserGroup().equals(MembershipUserGroup.IMPORT_USER)) {
            redisWrapperClient.hsetSeri(ImportService.redisMembershipGiveReceivers, String.valueOf(membershipGiveModel.getId()), importUsers);
        }
    }

    private void editMembershipGive(MembershipGiveModel originMembershipGiveModel, MembershipGiveDto membershipGiveDto, long importUsersId) {
        if (originMembershipGiveModel.isActive()) {
            return;
        }

        MembershipModel membershipModel = membershipMapper.findByLevel(membershipGiveDto.getMembershipLevel());

        originMembershipGiveModel.setMembershipId(membershipModel.getId());
        originMembershipGiveModel.setDeadline(membershipGiveDto.getDeadline());
        if (StringUtils.isEmpty(membershipGiveDto.getStartTime())) {
            originMembershipGiveModel.setStartTime(null);
        } else {
            originMembershipGiveModel.setStartTime(DateTime.parse(membershipGiveDto.getStartTime().replace(' ', 'T')).toDate());
        }
        if (StringUtils.isEmpty(membershipGiveDto.getEndTime())) {
            originMembershipGiveModel.setEndTime(null);
        } else {
            originMembershipGiveModel.setEndTime(DateTime.parse(membershipGiveDto.getEndTime().replace(' ', 'T')).toDate());
        }
        originMembershipGiveModel.setUserGroup(membershipGiveDto.getUserGroup());
        originMembershipGiveModel.setSmsNotify(membershipGiveDto.isSmsNotify());
        originMembershipGiveModel.setUpdatedTime(new Date());
        originMembershipGiveModel.setUpdatedBy(membershipGiveDto.getUpdatedBy());

        membershipGiveMapper.update(originMembershipGiveModel);

        List<String> importUsers = importService.getImportStrings(ImportService.redisMembershipGiveReceivers, importUsersId);
        redisWrapperClient.hdelSeri(ImportService.redisMembershipGiveReceivers, String.valueOf(importUsersId));
        if (membershipGiveDto.getUserGroup().equals(MembershipUserGroup.IMPORT_USER)) {
            redisWrapperClient.hsetSeri(ImportService.redisMembershipGiveReceivers, String.valueOf(originMembershipGiveModel.getId()), importUsers);
        }
    }

    private Map<Long, Integer> getIdLevelMap() {
        List<MembershipModel> membershipModels = membershipMapper.findAllMembership();
        Map<Long, Integer> map = new HashMap<>();
        for (MembershipModel membershipModel : membershipModels) {
            map.put(membershipModel.getId(), membershipModel.getLevel());
        }
        return map;
    }

    private List<UserMembershipModel> combineUserMembershipModels(List<String> loginNames, List<MembershipGiveModel> membershipGiveModels) {
        List<UserMembershipModel> userMembershipModels = new ArrayList<>();
        for (String loginName : loginNames) {
            for (MembershipGiveModel membershipGiveModel : membershipGiveModels) {
                userMembershipModels.add(new UserMembershipModel(loginName, membershipGiveModel.getMembershipId(),
                        DateTime.now().withTimeAtStartOfDay().plusDays(1).plusDays(membershipGiveModel.getDeadline()).plusSeconds(-1).toDate(),
                        UserMembershipType.GIVEN, membershipGiveModel.getId()));
            }
        }
        return userMembershipModels;
    }

    private List<MembershipExperienceBillModel> combineMembershipExperienceBillModels(List<String> loginNames, List<MembershipGiveModel> membershipGiveModels) {
        Map<Long, Integer> map = getIdLevelMap();
        List<MembershipExperienceBillModel> membershipExperienceBillModels = new ArrayList<>();
        for (String loginName : loginNames) {
            for (MembershipGiveModel membershipGiveModel : membershipGiveModels) {
                long totalExperience = 0;
                AccountModel accountModel = accountMapper.findByLoginName(loginName);
                if (null != accountModel) {
                    totalExperience = accountModel.getMembershipPoint();
                }
                membershipExperienceBillModels.add(new MembershipExperienceBillModel(loginName, null, 0L, totalExperience,
                        MessageFormat.format("获赠期限为{0}天的V{1}会员", membershipGiveModel.getDeadline(), map.get(membershipGiveModel.getMembershipId()))));
            }
        }
        return membershipExperienceBillModels;
    }

    private void giveUsersMemberships(List<String> users, List<MembershipGiveModel> membershipGiveModels) {
        List<UserMembershipModel> userMembershipModels = combineUserMembershipModels(users, membershipGiveModels);
        List<MembershipExperienceBillModel> membershipExperienceBillModels = combineMembershipExperienceBillModels(users, membershipGiveModels);

        if (userMembershipModels.size() != 0 && membershipExperienceBillModels.size() != 0) {
            userMembershipMapper.createBatch(userMembershipModels);
            membershipExperienceBillMapper.createBatch(membershipExperienceBillModels);
        }
    }

    public GivenMembership receiveMembership(String loginName) {
        if (DateTime.parse(heroRankingActivityPeriod.get(0), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate().after(DateTime.now().toDate())) {
            return GivenMembership.NO_TIME;
        }

        if (DateTime.parse(heroRankingActivityPeriod.get(1), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate().before(DateTime.now().toDate())) {
            return GivenMembership.END_TIME;
        }

        if (loginName == null || loginName.equals("")) {
            return GivenMembership.NO_LOGIN;
        }

        if (accountMapper.findByLoginName(loginName) == null) {
            return GivenMembership.NO_REGISTER;
        }

        if (userMembershipMapper.findByLoginNameByType(loginName, UserMembershipType.GIVEN) != null) {
            return GivenMembership.ALREADY_RECEIVED;
        }

        long investAmount = userMembershipMapper.sumSuccessInvestAmountByLoginName(null, loginName);
        Date registerTime = accountMapper.findByLoginName(loginName).getRegisterTime();
        if (registerTime != null && DateTime.parse(heroRankingActivityPeriod.get(0), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate().after(registerTime) && investAmount < 100000) {
            return GivenMembership.ALREADY_REGISTER_NOT_INVEST_1000;
        }

        if (registerTime != null && DateTime.parse(heroRankingActivityPeriod.get(0), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate().after(registerTime) && investAmount >= 100000) {
            createUserMembershipModel(loginName, MembershipLevel.V5.getLevel());
            return GivenMembership.ALREADY_REGISTER_ALREADY_INVEST_1000;
        }

        createUserMembershipModel(loginName, MembershipLevel.V5.getLevel());
        return GivenMembership.AFTER_START_ACTIVITY_REGISTER;
    }

    private void createUserMembershipModel(String loginName, int level) {
        UserMembershipModel userMembershipModel = new UserMembershipModel(loginName,
                membershipMapper.findByLevel(level).getId(),
                DateTime.now().plusMonths(1).toDate(),
                UserMembershipType.GIVEN);
        userMembershipMapper.create(userMembershipModel);
    }
}