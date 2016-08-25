package com.tuotiansudai.console.service.impl;


import com.google.common.collect.Lists;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.console.bi.dto.RoleStage;
import com.tuotiansudai.console.repository.mapper.UserMapperConsole;
import com.tuotiansudai.console.service.UserServiceConsole;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.UserItemDataDto;
import com.tuotiansudai.repository.mapper.UserRoleMapper;
import com.tuotiansudai.repository.model.Role;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.UserRoleModel;
import com.tuotiansudai.repository.model.UserView;
import com.tuotiansudai.service.BindBankCardService;
import com.tuotiansudai.task.OperationType;
import com.tuotiansudai.task.TaskConstant;
import com.tuotiansudai.util.AmountConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceConsoleImpl implements UserServiceConsole {

    @Autowired
    private UserMapperConsole userMapperConsole;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private RedisWrapperClient redisWrapperClient;


    @Override
    public BaseDto<BasePaginationDataDto<UserItemDataDto>> findAllUser(String loginName,
                                                                       String email,
                                                                       String mobile,
                                                                       Date beginTime,
                                                                       Date endTime,
                                                                       Source source,
                                                                       RoleStage roleStage,
                                                                       String referrerMobile,
                                                                       String channel,
                                                                       Integer index,
                                                                       Integer pageSize) {
        BaseDto<BasePaginationDataDto<UserItemDataDto>> baseDto = new BaseDto<>();
        List<UserView> userViews = userMapperConsole.findAllUser(loginName, email, mobile, beginTime, endTime, source, roleStage, referrerMobile, channel, (index - 1) * pageSize, pageSize);
        List<UserItemDataDto> userItems = Lists.newArrayList();
        for (UserView userView : userViews) {
            UserItemDataDto userItemDataDto = new UserItemDataDto(userView);
            List<UserRoleModel> userRoleModels = userRoleMapper.findByLoginName(userView.getLoginName());
            userItemDataDto.setUserRoles(userRoleModels);
            String taskId = OperationType.USER + "-" + userView.getLoginName();
            userItemDataDto.setModify(redisWrapperClient.hexistsSeri(TaskConstant.TASK_KEY + Role.OPERATOR_ADMIN, taskId));
            userItems.add(userItemDataDto);
        }
        int count = userMapperConsole.findAllUserCount(loginName, email, mobile, beginTime, endTime, source, roleStage, referrerMobile, channel);
        BasePaginationDataDto<UserItemDataDto> basePaginationDataDto = new BasePaginationDataDto<>(index, pageSize, count, userItems);
        basePaginationDataDto.setStatus(true);
        baseDto.setData(basePaginationDataDto);
        return baseDto;
    }

    @Override
    public List<UserView> searchAllUsers(String loginName, String referrerMobile, String mobile, String identityNumber) {
        return userMapperConsole.searchAllUsers(loginName, referrerMobile, mobile, identityNumber);
    }

    @Override
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

    @Override
    public long findUsersAccountBalanceCount(String mobile, String balanceMin, String balanceMax) {
        List<Long> balance = parseBalanceInt(balanceMin, balanceMax);
        return userMapperConsole.findUsersAccountBalanceCount(mobile, balance.get(0), balance.get(1));
    }


    @Override
    public long findUsersAccountBalanceSum(String mobile, String balanceMin, String balanceMax) {
        List<Long> balance = parseBalanceInt(balanceMin, balanceMax);
        return userMapperConsole.findUsersAccountBalanceSum(mobile, balance.get(0), balance.get(1));
    }

    @Override
    public List<String> findStaffNameFromUserLike(String loginName) {
        return userMapperConsole.findStaffByLikeLoginName(loginName);
    }

    @Override
    public List<String> findLoginNameLike(String loginName) {
        return userMapperConsole.findLoginNameLike(loginName);
    }

    @Override
    public List<String> findMobileLike(String mobile) {
        return userMapperConsole.findMobileLike(mobile);
    }

    @Override
    public long findUsersCountByChannel(String channel) {
        return userMapperConsole.findUsersCountByChannel(channel);
    }

    @Override
    public List<String> findAllLoanerLikeLoginName(String loginName) {
        return userMapperConsole.findAllLoanerLikeLoginName(loginName);
    }

    @Override
    public List<String> findAccountLikeLoginName(String loginName) {
        return userMapperConsole.findAccountLikeLoginName(loginName);
    }

    private List<Long> parseBalanceInt(String balanceMin, String balanceMax) {
        long min = AmountConverter.convertStringToCent(balanceMin);
        long max = AmountConverter.convertStringToCent(balanceMax);
        return Lists.newArrayList(min, max);
    }
}
