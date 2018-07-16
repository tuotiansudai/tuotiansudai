package com.tuotiansudai.console.service;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.client.BankWrapperClient;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.console.bi.dto.RoleStage;
import com.tuotiansudai.console.dto.RemainUserDto;
import com.tuotiansudai.console.dto.UserItemDataDto;
import com.tuotiansudai.console.repository.mapper.UserMapperConsole;
import com.tuotiansudai.console.repository.model.RemainUserView;
import com.tuotiansudai.console.repository.model.UserMicroModelView;
import com.tuotiansudai.console.repository.model.UserOperation;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.dto.request.UpdateUserInfoRequestDto;
import com.tuotiansudai.enums.OperationType;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.exception.EditUserException;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.rest.client.UserRestClient;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.service.BankBindCardService;
import com.tuotiansudai.task.TaskConstant;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.PaginationUtil;
import com.tuotiansudai.util.RedisWrapperClient;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ConsoleUserService {

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRestClient userRestClient;

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Autowired
    private BankAccountMapper bankAccountMapper;

    @Autowired
    private BankBindCardService bankBindCardService;

    @Autowired
    private AutoInvestPlanMapper autoInvestPlanMapper;

    @Autowired
    private UserMapperConsole userMapperConsole;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private ReferrerRelationMapper referrerRelationMapper;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Autowired
    private BankCardMapper bankCardMapper;

    @Autowired
    private UserBankCardMapper userBankCardMapper;

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

        UpdateUserInfoRequestDto updateDto = new UpdateUserInfoRequestDto(loginName);
        if (userModel.getStatus() != editUserDto.getStatus()) {
            updateDto.setStatus(editUserDto.getStatus());
        }
        if (!StringUtils.equalsIgnoreCase(userModel.getMobile(), editUserDto.getMobile())) {
            updateDto.setMobile(editUserDto.getMobile());
        }
        if (!StringUtils.equalsIgnoreCase(userModel.getEmail(), editUserDto.getEmail())) {
            updateDto.setEmail(editUserDto.getEmail());
        }
        if (!StringUtils.equalsIgnoreCase(userModel.getReferrer(), editUserDto.getReferrer())) {
            updateDto.setReferrer(Strings.isNullOrEmpty(editUserDto.getReferrer()) ? "" : editUserDto.getReferrer());
        }
        updateDto.setLastModifiedTime(new Date());
        updateDto.setLastModifiedUser(operatorLoginName);
        userRestClient.update(updateDto);

        if (!mobile.equals(beforeUpdateUserMobile) && bankAccountMapper.findByLoginNameAndRole(loginName, Role.INVESTOR) != null) {
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
        if (userRoleMapper.findByLoginNameAndRole(userModel.getReferrer(), Role.SD_STAFF) != null) {
            editUserDto.setReferrerStaff(true);
        }
        editUserDto = setUserBankCardNumberByLoginName(loginName, editUserDto);
        return editUserDto;
    }

    public List<String> findAllUserChannels() {
        return userMapperConsole.findAllUserChannels();
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
            userRoleModels = userRoleModels.stream().filter(userRoleModel -> {
                return userRoleModel.getRole() != Role.INVESTOR && userRoleModel.getRole() != Role.LOANER;
            }).collect(Collectors.toList());
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

    public List<String> findAllAgents() {
        return userRoleMapper.findAllLoginNameByRole(Role.AGENT);
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

        if ((editUserDto.getRoles().contains(Role.SD_STAFF) || editUserDto.getRoles().contains(Role.ZC_STAFF)) && !Strings.isNullOrEmpty(editUserDto.getReferrer())) {
            throw new EditUserException("业务员不能设置推荐人");
        }

        String newReferrerLoginName = editUserDto.getReferrer();
        if (!Strings.isNullOrEmpty(newReferrerLoginName)) {
            UserModel newReferrerModel = userMapper.findByLoginName(newReferrerLoginName);
            if (newReferrerModel == null) {
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

        if (editUserDto.getRoles().contains(Role.SD_STAFF) && editUserDto.getRoles().contains(Role.ZC_STAFF)) {
            throw new EditUserException("不能同时设置速贷业务员和资产业务员");
        }


        String afterChangeUser = MessageFormat.format("{0}:{1}:{2}:{3}:{4}",
                editUserDto.getMobile(),
                Strings.isNullOrEmpty(editUserDto.getEmail()) ? "" : editUserDto.getEmail(),
                Strings.isNullOrEmpty(editUserDto.getReferrer()) ? "" : editUserDto.getReferrer(),
                editUserDto.getStatus(),
                Joiner.on("").join(editUserDto.getRoles().stream().sorted().collect(Collectors.toList())));

        String beforeChangeUser = MessageFormat.format("{0}:{1}:{2}:{3}:{4}",
                editUserModel.getMobile(),
                Strings.isNullOrEmpty(editUserModel.getEmail()) ? "" : editUserModel.getEmail(),
                Strings.isNullOrEmpty(editUserModel.getReferrer()) ? "" : editUserModel.getReferrer(),
                editUserModel.getStatus(),
                Joiner.on("").join(userRoleMapper.findByLoginName(editUserModel.getLoginName()).stream().map(UserRoleModel::getRole).sorted().collect(Collectors.toList())));


        if (afterChangeUser.equals(beforeChangeUser)) {
            throw new EditUserException("用户未做任何修改");
        }
    }

    private List<Long> parseBalanceInt(String balanceMin, String balanceMax) {
        long min = AmountConverter.convertStringToCent(balanceMin);
        long max = AmountConverter.convertStringToCent(balanceMax);
        return Lists.newArrayList(min, max);
    }

    public BaseDto<BasePaginationDataDto<UserMicroModelView>> queryUserMicroView(String mobile,
                                                                                 Role role,
                                                                                 Date registerTimeStart,
                                                                                 Date registerTimeEnd,
                                                                                 String hasCertify,
                                                                                 String invested,
                                                                                 Long totalInvestAmountStart,
                                                                                 Long totalInvestAmountEnd,
                                                                                 Long totalWithdrawAmountStart,
                                                                                 Long totalWithdrawAmountEnd,
                                                                                 Long userBalanceStart,
                                                                                 Long userBalanceEnd,
                                                                                 Integer investCountStart,
                                                                                 Integer investCountEnd,
                                                                                 Integer loanCountStart,
                                                                                 Integer loanCountEnd,
                                                                                 Integer transformPeriodStart,
                                                                                 Integer transformPeriodEnd,
                                                                                 Integer invest1st2ndTimingStart,
                                                                                 Integer invest1st2ndTimingEnd,
                                                                                 Integer invest1st3ndTimingStart,
                                                                                 Integer invest1st3ndTimingEnd,
                                                                                 Date lastInvestTimeStart,
                                                                                 Date lastInvestTimeEnd,
                                                                                 Long repayingAmountStart,
                                                                                 Long repayingAmountEnd,
                                                                                 Date lastLoginTimeStart,
                                                                                 Date lastLoginTimeEnd,
                                                                                 Source lastLoginSource,
                                                                                 Date lastRepayTimeStart,
                                                                                 Date lastRepayTimeEnd,
                                                                                 Date lastWithdrawTimeStart,
                                                                                 Date lastWithdrawTimeEnd,
                                                                                 int index,
                                                                                 int pageSize) {


        int count = userMapperConsole.findUserMicroModelCount(mobile,
                role,
                registerTimeStart,
                registerTimeEnd,
                hasCertify,
                invested,
                totalInvestAmountStart,
                totalInvestAmountEnd,
                totalWithdrawAmountStart,
                totalWithdrawAmountEnd,
                userBalanceStart,
                userBalanceEnd,
                investCountStart,
                investCountEnd,
                loanCountStart,
                loanCountEnd,
                transformPeriodStart,
                transformPeriodEnd,
                invest1st2ndTimingStart,
                invest1st2ndTimingEnd,
                invest1st3ndTimingStart,
                invest1st3ndTimingEnd,
                lastInvestTimeStart,
                lastInvestTimeEnd,
                repayingAmountStart,
                repayingAmountEnd,
                lastLoginTimeStart,
                lastLoginTimeEnd,
                lastLoginSource,
                lastRepayTimeStart,
                lastRepayTimeEnd,
                lastWithdrawTimeStart,
                lastWithdrawTimeEnd);

        List<UserMicroModelView> userMicroModelViewList = userMapperConsole.queryUserMicroModel(mobile,
                role,
                registerTimeStart,
                registerTimeEnd,
                hasCertify,
                invested,
                totalInvestAmountStart,
                totalInvestAmountEnd,
                totalWithdrawAmountStart,
                totalWithdrawAmountEnd,
                userBalanceStart,
                userBalanceEnd,
                investCountStart,
                investCountEnd,
                loanCountStart,
                loanCountEnd,
                transformPeriodStart,
                transformPeriodEnd,
                invest1st2ndTimingStart,
                invest1st2ndTimingEnd,
                invest1st3ndTimingStart,
                invest1st3ndTimingEnd,
                lastInvestTimeStart,
                lastInvestTimeEnd,
                repayingAmountStart,
                repayingAmountEnd,
                lastLoginTimeStart,
                lastLoginTimeEnd,
                lastLoginSource,
                lastRepayTimeStart,
                lastRepayTimeEnd,
                lastWithdrawTimeStart,
                lastWithdrawTimeEnd,
                (index - 1) * pageSize,
                pageSize);

        for (UserMicroModelView view : userMicroModelViewList) {
            view.setAverageInvestAmount(view.getInvestCount() == 0 ? 0 : ((double) view.getTotalInvestAmount()) / view.getInvestCount());
            view.setAverageLoanInvestAmount(view.getLoanCount() == 0 ? 0 : (double) view.getTotalInvestAmount() / view.getLoanCount());
            if (view.getLastLoginTime() != null) {
                view.setLastLoginToNow((int) ((DateTime.now().withTimeAtStartOfDay().getMillis()
                        - new DateTime(view.getLastLoginTime()).withTimeAtStartOfDay().getMillis()) / (1000 * 60 * 60 * 24)));
            }
        }

        BaseDto<BasePaginationDataDto<UserMicroModelView>> baseDto = new BaseDto<>();
        BasePaginationDataDto<UserMicroModelView> basePaginationDataDto = new BasePaginationDataDto<>(index, pageSize, count, userMicroModelViewList);
        basePaginationDataDto.setStatus(true);
        baseDto.setData(basePaginationDataDto);

        return baseDto;
    }

    public BasePaginationDataDto<RemainUserDto> findRemainUsers(String loginName, String mobile, Date registerStartTime, Date registerEndTime,
                                                                Boolean useExperienceCoupon, Date experienceStartTime, Date experienceEndTime,
                                                                Integer investCountLowLimit, Integer investCountHighLimit, Long investSumLowLimit,
                                                                Long investSumHighLimit, Date firstInvestStartTime, Date firstInvestEndTime,
                                                                Date secondInvestStartTime, Date secondInvestEndTime, int index, int pageSize) {
        long count = userMapperConsole.findRemainUsersCount(loginName, mobile, registerStartTime,
                registerEndTime, useExperienceCoupon, experienceStartTime, experienceEndTime, investCountLowLimit,
                investCountHighLimit, investSumLowLimit, investSumHighLimit, firstInvestStartTime, firstInvestEndTime,
                secondInvestStartTime, secondInvestEndTime);

        List<RemainUserView> remainUserViews = userMapperConsole.findRemainUsers(loginName, mobile, registerStartTime,
                registerEndTime, useExperienceCoupon, experienceStartTime, experienceEndTime, investCountLowLimit,
                investCountHighLimit, investSumLowLimit, investSumHighLimit, firstInvestStartTime, firstInvestEndTime,
                secondInvestStartTime, secondInvestEndTime, PaginationUtil.calculateOffset(index, pageSize, count), pageSize);

        List<RemainUserDto> remainUserDtos = remainUserViews.stream().map(RemainUserDto::new).collect(Collectors.toList());

        BasePaginationDataDto<RemainUserDto> basePaginationDataDto = new BasePaginationDataDto<>(index, pageSize, count, remainUserDtos);

        return basePaginationDataDto;
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

    public EditUserDto setUserBankCardNumberByLoginName(String loginName, EditUserDto editUserDto) {
        editUserDto.setBankCardNumberUMP(bankCardMapper.findPassedBankCardNumberByLoginName(loginName));
        List<UserBankCardModel> userBankCardModelList = userBankCardMapper.findBankCardNumberByloginName(loginName);

        String bankCardNumberInvestor = userBankCardModelList.stream().filter(userItem -> {
            return Role.BANK_INVESTOR.equals(userItem.getRoleType());
        }).findAny().map(UserBankCardModel::getCardNumber).orElse(null);
        editUserDto.setBankCardNumberInvestor(bankCardNumberInvestor);
        String bankCardNumberLoaner = userBankCardModelList.stream().filter(userItem -> {
            return Role.BANK_LOANER.equals(userItem.getRoleType());
        }).findAny().map(UserBankCardModel::getCardNumber).orElse(null);
        editUserDto.setBankCardNumberLoaner(bankCardNumberLoaner);
        return editUserDto;
    }

}
