package com.tuotiansudai.console.activity.service;

import com.google.common.base.Strings;
import com.tuotiansudai.activity.repository.mapper.IPhone7InvestLotteryMapper;
import com.tuotiansudai.activity.repository.mapper.IPhone7LotteryConfigMapper;
import com.tuotiansudai.activity.repository.model.IPhone7InvestLotteryStatView;
import com.tuotiansudai.activity.repository.model.IPhone7InvestLotteryWinnerView;
import com.tuotiansudai.activity.repository.model.IPhone7LotteryConfigModel;
import com.tuotiansudai.activity.repository.model.IPhone7LotteryConfigStatus;
import com.tuotiansudai.console.activity.dto.IPhone7InvestLotteryStatDto;
import com.tuotiansudai.console.activity.dto.IPhone7InvestLotteryWinnerDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.AuditLogMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.AuditLogModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.task.OperationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IPhone7LotteryService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private IPhone7InvestLotteryMapper mapper;

    @Autowired
    private IPhone7LotteryConfigMapper configMapper;

    @Autowired
    private AuditLogMapper auditLogMapper;

    public BaseDto<BasePaginationDataDto> listStat(String mobile, int pageIndex, int pageSize) {
        String loginName = null;
        if (!Strings.isNullOrEmpty(mobile)) {
            UserModel userModel = userMapper.findByMobile(mobile);
            if (userModel == null) {
                BasePaginationDataDto<IPhone7InvestLotteryStatDto> paginationDataDto = new BasePaginationDataDto<>(
                        pageIndex, pageSize, 0, new ArrayList<>());
                return new BaseDto<>(paginationDataDto);
            }
            loginName = userModel.getLoginName();
        }

        pageIndex = pageIndex < 1 ? 1 : pageIndex;
        pageSize = pageSize <= 0 ? 10 : pageSize;

        int rowIndex = (pageIndex - 1) * pageSize;

        List<IPhone7InvestLotteryStatView> records = mapper.statInvest(loginName, rowIndex, pageSize);
        int userCount = records.size();
        if (Strings.isNullOrEmpty(mobile)) {
            userCount = mapper.statUserCount();
        }
        List<IPhone7InvestLotteryStatDto> dtoList = records.stream().map(r -> {
            UserModel userModel = userMapper.findByLoginName(r.getLoginName());
            AccountModel accountModel = accountMapper.findByLoginName(r.getLoginName());
            return new IPhone7InvestLotteryStatDto(r, userModel.getMobile(), accountModel.getUserName());
        }).collect(Collectors.toList());

        BasePaginationDataDto<IPhone7InvestLotteryStatDto> paginationDataDto = new BasePaginationDataDto<>(
                pageIndex, pageSize, userCount, dtoList);
        return new BaseDto<>(paginationDataDto);
    }

    @Transactional(transactionManager = "transactionManager")
    public void approveConfig(long id, String loginName, String ip) {
        IPhone7LotteryConfigModel configModel = configMapper.findById(id);
        if (configModel != null) {
            List<IPhone7LotteryConfigModel> configModels = configMapper.findByInvestAmount(configModel.getInvestAmount());
            if (configModels.stream().anyMatch(c -> c.getStatus() == IPhone7LotteryConfigStatus.EFFECTIVE)) {
                throw new RuntimeException("此阶段已开奖完毕，不能进行修改中奖号码");
            }
            configMapper.removeApprovedConfig(configModel.getInvestAmount());
            configMapper.approve(id, loginName, new Date());

            logOperation(configModel, true, loginName, ip);
        }
    }

    @Transactional(transactionManager = "transactionManager")
    public void refuseConfig(long id, String loginName, String ip) {
        IPhone7LotteryConfigModel configModel = configMapper.findById(id);
        if (configModel != null) {
            configMapper.refuse(id, loginName, new Date());
            logOperation(configModel, false, loginName, ip);
        }
    }

    public List<IPhone7InvestLotteryWinnerDto> listWinner() {
        List<IPhone7InvestLotteryWinnerView> winnerViews = mapper.listWinner();
        return winnerViews.stream().map(w -> {
            UserModel userModel = userMapper.findByLoginName(w.getLoginName());
            AccountModel accountModel = accountMapper.findByLoginName(w.getLoginName());
            return new IPhone7InvestLotteryWinnerDto(w, userModel.getMobile(), accountModel.getUserName());
        }).collect(Collectors.toList());
    }

    private void logOperation(IPhone7LotteryConfigModel configModel, boolean passed, String loginName, String ip) {
        String auditor = accountMapper.findByLoginName(loginName).getUserName();
        String operator = accountMapper.findByLoginName(configModel.getCreatedBy()).getUserName();
        String operation = passed ? "通过" : "驳回";
        String activityName = "老板出差，运营汪闭眼送iphone7";
        String description = MessageFormat.format("{0}{1}了{2}在“{3}”活动中修改的中奖配置({4}万：{5}, 手机号:{6})",
                auditor, operation, operator, activityName, configModel.getInvestAmount(),
                configModel.getLotteryNumber(), configModel.getMobile());
        createAuditLog(loginName, configModel.getCreatedBy(),
                OperationType.ACTIVITY, activityName, description, ip);
    }

    private void createAuditLog(String auditorLoginName, String operatorLoginName, OperationType operationType, String targetId, String description, String auditorIp) {
        AuditLogModel log = new AuditLogModel();
        log.setOperatorLoginName(operatorLoginName);
        log.setAuditorLoginName(auditorLoginName);
        log.setTargetId(targetId);
        log.setOperationType(operationType);
        log.setIp(auditorIp);
        log.setDescription(description);
        auditLogMapper.create(log);
    }
}
