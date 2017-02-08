package com.tuotiansudai.console.service;


import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.console.bi.dto.RoleStage;
import com.tuotiansudai.console.dto.UserItemDataDto;
import com.tuotiansudai.console.repository.mapper.UserMapperConsole;
import com.tuotiansudai.console.repository.model.UserOperation;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.enums.OperationType;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.exception.EditUserException;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.BindBankCardService;
import com.tuotiansudai.task.TaskConstant;
import com.tuotiansudai.util.AmountConverter;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ConsoleUserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private BindBankCardService bindBankCardService;

    @Autowired
    private AutoInvestPlanMapper autoInvestPlanMapper;

    @Autowired
    private UserMapperConsole userMapperConsole;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private ReferrerRelationMapper referrerRelationMapper;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Transactional(rollbackFor = Exception.class)
    public void editUser(String operatorLoginName, EditUserDto editUserDto, String ip) throws EditUserException {
        this.checkUpdateUserData(editUserDto);

        final String loginName = editUserDto.getLoginName();

        String mobile = editUserDto.getMobile();
        UserModel userModel = userMapper.findByLoginName(loginName);
        String beforeUpdateUserMobile = userModel.getMobile();

        // update role
        userRoleMapper.deleteByLoginName(loginName);
        if (CollectionUtils.isNotEmpty(editUserDto.getRoles())) {
            List<UserRoleModel> afterUpdateUserRoleModels = Lists.transform(editUserDto.getRoles(), role -> new UserRoleModel(loginName, role));
            userRoleMapper.create(afterUpdateUserRoleModels);
        }

        userModel.setStatus(editUserDto.getStatus());
        userModel.setMobile(mobile);
        userModel.setEmail(editUserDto.getEmail());
        userModel.setReferrer(Strings.isNullOrEmpty(editUserDto.getReferrer()) ? null : editUserDto.getReferrer());
        userModel.setLastModifiedTime(new Date());
        userModel.setLastModifiedUser(operatorLoginName);
        userMapper.updateUser(userModel);

        if (!mobile.equals(beforeUpdateUserMobile) && accountMapper.findByLoginName(loginName) != null) {
            RegisterAccountDto registerAccountDto = new RegisterAccountDto(userModel.getLoginName(),
                    mobile,
                    userModel.getUserName(),
                    userModel.getIdentityNumber());
            BaseDto<PayDataDto> baseDto = payWrapperClient.register(registerAccountDto);
            if (!baseDto.getData().getStatus()) {
                throw new EditUserException(baseDto.getData().getMessage());
            }
        }

        // update referrer relationship
        mqWrapperClient.sendMessage(MessageQueue.GenerateReferrerRelation, userModel.getLoginName());
    }

    public EditUserDto getEditUser(String loginName) {
        UserModel userModel = userMapper.findByLoginName(loginName);
        List<UserRoleModel> userRoleModels = userRoleMapper.findByLoginName(loginName);
        List<Role> roles = Lists.newArrayList();
        for (UserRoleModel userRoleModel : userRoleModels) {
            roles.add(userRoleModel.getRole());
        }
        AutoInvestPlanModel autoInvestPlanModel = autoInvestPlanMapper.findByLoginName(loginName);

        EditUserDto editUserDto = new EditUserDto(userModel, roles, autoInvestPlanModel != null && autoInvestPlanModel.isEnabled());

        BankCardModel bankCard = bindBankCardService.getPassedBankCard(loginName);
        if (bankCard != null) {
            editUserDto.setBankCardNumber(bankCard.getCardNumber());
        }

        if (userRoleMapper.findByLoginNameAndRole(userModel.getReferrer(), Role.STAFF) != null) {
            editUserDto.setReferrerStaff(true);
        }
        return editUserDto;
    }

    public List<String> findAllUserChannels() {
        return userMapper.findAllUserChannels();
    }

    public UserModel findByLoginName(String loginName) {
        return userMapper.findByLoginName(loginName);
    }

    public BaseDto<BasePaginationDataDto<UserItemDataDto>> findAllUser(String loginName, String email, String mobile,
                                                                       Date beginTime, Date endTime, Source source,
                                                                       RoleStage roleStage, String referrerMobile,
                                                                       String channel, UserOperation userOperation,
                                                                       Integer index, Integer pageSize) {
        BaseDto<BasePaginationDataDto<UserItemDataDto>> baseDto = new BaseDto<>();
        List<UserView> userViews = userMapperConsole.findAllUser(loginName, email, mobile, beginTime, endTime, source, roleStage, referrerMobile, channel, userOperation, (index - 1) * pageSize, pageSize);
        List<UserItemDataDto> userItems = Lists.newArrayList();
        for (UserView userView : userViews) {
            UserItemDataDto userItemDataDto = new UserItemDataDto(userView);
            List<UserRoleModel> userRoleModels = userRoleMapper.findByLoginName(userView.getLoginName());
            userItemDataDto.setUserRoles(userRoleModels);
            String taskId = OperationType.USER + "-" + userView.getLoginName();
            userItemDataDto.setModify(redisWrapperClient.hexistsSeri(TaskConstant.TASK_KEY + Role.OPERATOR_ADMIN, taskId));
            userItems.add(userItemDataDto);
        }
        int count = userMapperConsole.findAllUserCount(loginName, email, mobile, beginTime, endTime, source, roleStage, referrerMobile, channel, userOperation);
        BasePaginationDataDto<UserItemDataDto> basePaginationDataDto = new BasePaginationDataDto<>(index, pageSize, count, userItems);
        basePaginationDataDto.setStatus(true);
        baseDto.setData(basePaginationDataDto);
        return baseDto;
    }

    public List<UserView> searchAllUsers(String loginName, String referrerMobile, String mobile, String identityNumber) {
        return userMapperConsole.searchAllUsers(loginName, referrerMobile, mobile, identityNumber);
    }

    public List<UserItemDataDto> findUsersAccountBalance(String mobile, String balanceMin, String balanceMax, Integer index, Integer pageSize) {
        List<Long> balance = parseBalanceInt(balanceMin, balanceMax);
        List<UserView> userViews = userMapperConsole.findUsersAccountBalance(mobile, balance.get(0), balance.get(1),
                index != null && pageSize != null ? (index - 1) * pageSize : null, pageSize);

        List<UserItemDataDto> userItemDataDtoList = Lists.newArrayList();
        for (UserView userView : userViews) {
            UserItemDataDto userItemDataDto = new UserItemDataDto(userView);
            userItemDataDtoList.add(userItemDataDto);
        }
        return userItemDataDtoList;
    }

    public long findUsersAccountBalanceCount(String mobile, String balanceMin, String balanceMax) {
        List<Long> balance = parseBalanceInt(balanceMin, balanceMax);
        return userMapperConsole.findUsersAccountBalanceCount(mobile, balance.get(0), balance.get(1));
    }

    public long findUsersAccountBalanceSum(String mobile, String balanceMin, String balanceMax) {
        List<Long> balance = parseBalanceInt(balanceMin, balanceMax);
        return userMapperConsole.findUsersAccountBalanceSum(mobile, balance.get(0), balance.get(1));
    }


    public List<String> findStaffNameFromUserLike(String loginName) {
        return userMapperConsole.findStaffByLikeLoginName(loginName);
    }

    public List<String> findLoginNameLike(String loginName) {
        return userMapperConsole.findLoginNameLike(loginName);
    }

    public List<String> findMobileLike(String mobile) {
        return userMapperConsole.findMobileLike(mobile);
    }

    public long findUsersCountByChannel(String channel) {
        return userMapperConsole.findUsersCountByChannel(channel);
    }

    public List<String> findAllLoanerLikeLoginName(String loginName) {
        return userMapperConsole.findAllLoanerLikeLoginName(loginName);
    }

    public List<String> findAccountLikeLoginName(String loginName) {
        return userMapperConsole.findAccountLikeLoginName(loginName);
    }

    private void checkUpdateUserData(EditUserDto editUserDto) throws EditUserException {
        String loginName = editUserDto.getLoginName();
        UserModel editUserModel = userMapper.findByLoginName(loginName);

        if (editUserModel == null) {
            throw new EditUserException("该用户不存在");
        }

        String newEmail = editUserDto.getEmail();
        if (!Strings.isNullOrEmpty(newEmail) && userMapper.findByEmail(newEmail) != null && !editUserModel.getLoginName().equalsIgnoreCase(userMapper.findByEmail(newEmail).getLoginName())) {
            throw new EditUserException("该邮箱已经存在");
        }

        String mobile = editUserDto.getMobile();
        UserModel userModelByMobile = userMapper.findByMobile(mobile);
        if (!Strings.isNullOrEmpty(mobile) && userModelByMobile != null && !editUserModel.getLoginName().equalsIgnoreCase(userModelByMobile.getLoginName())) {
            throw new EditUserException("该手机号已经存在");
        }

        if (editUserDto.getRoles().contains(Role.STAFF) && !Strings.isNullOrEmpty(editUserDto.getReferrer())) {
            throw new EditUserException("业务员不能设置推荐人");
        }

        String newReferrerLoginName = editUserDto.getReferrer();
        UserModel newReferrerModel = userMapper.findByLoginName(newReferrerLoginName);
        if (!Strings.isNullOrEmpty(newReferrerLoginName) && newReferrerModel == null) {
            throw new EditUserException("推荐人不存在");
        }

        if (loginName.equalsIgnoreCase(newReferrerLoginName)) {
            throw new EditUserException("不能将推荐人设置为自己");
        }

        // 是否新推荐人是该用户推荐的
        if (this.isNewReferrerReferree(loginName, newReferrerLoginName)) {
            throw new EditUserException("推荐人与该用户存在间接推荐关系");
        }
    }

    private List<Long> parseBalanceInt(String balanceMin, String balanceMax) {
        long min = AmountConverter.convertStringToCent(balanceMin);
        long max = AmountConverter.convertStringToCent(balanceMax);
        return Lists.newArrayList(min, max);
    }

    private boolean isNewReferrerReferree(String loginName, String newReferrerLoginName) {
        Map<Integer, List<String>> allLowerUsers = Maps.newHashMap(ImmutableMap.<Integer, List<String>>builder()
                .put(0, Lists.newArrayList(loginName))
                .put(1, Lists.<String>newArrayList())
                .put(2, Lists.<String>newArrayList())
                .put(3, Lists.<String>newArrayList())
                .put(4, Lists.<String>newArrayList())
                .build());

        for (int level = 1; level <= 4; level++) {
            List<String> lowerLoginNames = allLowerUsers.get(level - 1);
            for (String lowerLoginName : lowerLoginNames) {
                List<ReferrerRelationModel> referrerRelationModels = referrerRelationMapper.findByReferrerLoginNameAndLevel(lowerLoginName, 1);
                for (ReferrerRelationModel referrerRelationModel : referrerRelationModels) {
                    allLowerUsers.get(level).add(referrerRelationModel.getLoginName());
                    if (referrerRelationModel.getLoginName().equalsIgnoreCase(newReferrerLoginName)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
