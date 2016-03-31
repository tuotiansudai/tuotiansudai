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
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserRoleModel;
import com.tuotiansudai.service.BindBankCardService;
import com.tuotiansudai.task.OperationType;
import com.tuotiansudai.task.TaskConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserServiceConsoleImpl implements UserServiceConsole {

    @Autowired
    private UserMapperConsole userMapperConsole;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private BindBankCardService bindBankCardService;
    @Autowired
    private RedisWrapperClient redisWrapperClient;


    @Override
    public BaseDto<BasePaginationDataDto> findAllUser(String loginName, String email, String mobile, Date beginTime, Date endTime,
                                                      Source source,
                                                      RoleStage roleStage, String referrer, String channel, Integer index, Integer pageSize) {
        BaseDto<BasePaginationDataDto> baseDto = new BaseDto<>();
        List<UserModel> userModels = userMapperConsole.findAllUser(loginName, email, mobile, beginTime, endTime,
                source,
                roleStage, referrer, channel, (index - 1) * pageSize, pageSize);
        List<UserItemDataDto> userItemDataDtos = Lists.newArrayList();
        for (UserModel userModel : userModels) {

            UserItemDataDto userItemDataDto = new UserItemDataDto(userModel);
            List<UserRoleModel> userRoleModels = userRoleMapper.findByLoginName(userModel.getLoginName());
            userItemDataDto.setUserRoles(userRoleModels);

            List<UserRoleModel> referrerRoleModels = userRoleMapper.findByLoginName(userModel.getReferrer());
            for (UserRoleModel referrerRoleModel : referrerRoleModels) {
                if (referrerRoleModel.getRole() == Role.STAFF) {
                    userItemDataDto.setReferrerStaff(true);
                    break;
                }
            }
            userItemDataDto.setBankCard(bindBankCardService.getPassedBankCard(userModel.getLoginName()) != null);
            String taskId = OperationType.USER + "-" + userModel.getLoginName();
            userItemDataDto.setModify(redisWrapperClient.hexistsSeri(TaskConstant.TASK_KEY + Role.OPERATOR_ADMIN, taskId));
            userItemDataDtos.add(userItemDataDto);
        }
        int count = userMapperConsole.findAllUserCount(loginName, email, mobile, beginTime, endTime, source, roleStage, referrer, channel);
        BasePaginationDataDto<UserItemDataDto> basePaginationDataDto = new BasePaginationDataDto<>(index, pageSize, count, userItemDataDtos);
        basePaginationDataDto.setStatus(true);
        baseDto.setData(basePaginationDataDto);
        return baseDto;

    }
}
