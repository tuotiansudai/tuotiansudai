package com.tuotiansudai.point.service.impl;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.tuotiansudai.point.dto.PointTaskDto;
import com.tuotiansudai.point.repository.mapper.PointTaskMapper;
import com.tuotiansudai.point.repository.mapper.UserPointTaskMapper;
import com.tuotiansudai.point.repository.model.PointBusinessType;
import com.tuotiansudai.point.repository.model.PointTask;
import com.tuotiansudai.point.repository.model.PointTaskModel;
import com.tuotiansudai.point.repository.model.UserPointTaskModel;
import com.tuotiansudai.point.service.PointBillService;
import com.tuotiansudai.point.service.PointTaskService;
import com.tuotiansudai.repository.mapper.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;

@Service
public class PointTaskServiceImpl implements PointTaskService {

    static Logger logger = Logger.getLogger(PointTaskServiceImpl.class);

    @Autowired
    private PointTaskMapper pointTaskMapper;

    @Autowired
    private UserPointTaskMapper userPointTaskMapper;

    @Autowired
    private PointBillService pointBillService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private BankCardMapper bankCardMapper;

    @Autowired
    private RechargeMapper rechargeMapper;

    @Autowired
    private InvestMapper investMapper;

    @Override
    @Transactional
    public void completeTask(PointTask pointTask, String loginName) {
        if (isCompletedTaskConditions(pointTask, loginName)) {
            PointTaskModel pointTaskModel = pointTaskMapper.findByName(pointTask);
            userPointTaskMapper.create(new UserPointTaskModel(loginName,  pointTaskModel.getId()));
            pointBillService.createPointBill(loginName, pointTaskModel.getId(), PointBusinessType.TASK, pointTaskModel.getPoint());
            logger.debug(MessageFormat.format("{0} has completed task {1}", loginName, pointTask.name()));
        }
    }

    @Override
    public List<PointTaskDto> displayPointTask(int index, int pageSize,final String loginName) {
        List<PointTaskModel> pointTaskModels = pointTaskMapper.find((index-1) * pageSize,pageSize);
        if(CollectionUtils.isEmpty(pointTaskModels)){
            return Lists.newArrayList();
        }
        List<PointTaskDto> pointTaskDtos = Lists.transform(pointTaskModels, new Function<PointTaskModel, PointTaskDto>() {
            @Override
            public PointTaskDto apply(PointTaskModel pointTaskModel) {
                PointTaskDto pointTaskDto = new PointTaskDto(pointTaskModel);
                UserPointTaskModel userPointTaskModel = userPointTaskMapper.findByLoginNameAndId(pointTaskModel.getId(),loginName);
                pointTaskDto.setCompleted(userPointTaskModel != null ? true : false);
                return pointTaskDto;
            }
        });

        return pointTaskDtos;
    }

    private boolean isCompletedTaskConditions(final PointTask pointTask, String loginName) {
        List<UserPointTaskModel> completedTask = userPointTaskMapper.findByLoginName(loginName);
        boolean isCompleted = Iterators.tryFind(completedTask.iterator(), new Predicate<UserPointTaskModel>() {
            @Override
            public boolean apply(UserPointTaskModel input) {
                return input.getPointTask().getName() == pointTask;
            }
        }).isPresent();

        if (isCompleted) {
            return false;
        }

        switch (pointTask) {
            case REGISTER:
                return accountMapper.findByLoginName(loginName) != null;
            case BIND_EMAIL:
                return !Strings.isNullOrEmpty(userMapper.findByLoginName(loginName).getEmail());
            case BIND_BANK_CARD:
                return bankCardMapper.findPassedBankCardByLoginName(loginName) != null;
            case FIRST_RECHARGE:
                return rechargeMapper.findSumSuccessRechargeByLoginName(loginName) > 0;
            case FIRST_INVEST:
                return investMapper.sumSuccessInvestAmountByLoginName(null, loginName) > 0;
            case SUM_INVEST_10000:
                return investMapper.sumSuccessInvestAmountByLoginName(null, loginName) > 1000000;
        }

        return false;
    }
}
