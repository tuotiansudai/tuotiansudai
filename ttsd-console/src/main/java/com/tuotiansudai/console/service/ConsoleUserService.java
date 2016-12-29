package com.tuotiansudai.console.service;


import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.console.bi.dto.RoleStage;
import com.tuotiansudai.console.dto.UserItemDataDto;
import com.tuotiansudai.console.repository.mapper.UserMapperConsole;
import com.tuotiansudai.console.repository.model.UserOperation;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.enums.OperationType;
import com.tuotiansudai.exception.EditUserException;
import com.tuotiansudai.exception.ReferrerRelationException;
import com.tuotiansudai.enums.OperationType;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.AutoInvestPlanMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.mapper.UserRoleMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.BindBankCardService;
import com.tuotiansudai.service.ReferrerRelationService;
import com.tuotiansudai.task.TaskConstant;
import com.tuotiansudai.util.AmountConverter;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class ConsoleUserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private ReferrerRelationService referrerRelationService;

    @Autowired
    private BindBankCardService bindBankCardService;

    @Autowired
    private AutoInvestPlanMapper autoInvestPlanMapper;

    @Autowired
    private UserMapperConsole userMapperConsole;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Transactional(rollbackFor = Exception.class)
    public void editUser(String operatorLoginName, EditUserDto editUserDto, String ip) throws EditUserException, ReferrerRelationException {
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

        // update referrer
        this.referrerRelationService.generateRelation(editUserDto.getReferrer(), editUserDto.getLoginName());

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
    }

    public EditUserDto getEditUser(String loginName) {
        UserModel userModel = userMapper.findByLoginName(loginName);
        List<UserRoleModel> userRoleModels = userRoleMapper.findByLoginName(loginName);
        List<Role> roles = Lists.newArrayList();
        for (UserRoleModel userRoleModel : userRoleModels) {
            roles.add(userRoleModel.getRole());
        }
        AutoInvestPlanModel autoInvestPlanModel = autoInvestPlanMapper.findByLoginName(loginName);

        EditUserDto editUserDto = new EditUserDto(userModel, roles, autoInvestPlanModel);

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
    }

    private List<Long> parseBalanceInt(String balanceMin, String balanceMax) {
        long min = AmountConverter.convertStringToCent(balanceMin);
        long max = AmountConverter.convertStringToCent(balanceMax);
        return Lists.newArrayList(min, max);
    }
}
