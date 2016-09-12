package com.tuotiansudai.point.service.impl;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.tuotiansudai.point.dto.PointTaskDto;
import com.tuotiansudai.point.repository.mapper.PointTaskMapper;
import com.tuotiansudai.point.repository.mapper.UserPointTaskMapper;
import com.tuotiansudai.point.repository.model.PointTask;
import com.tuotiansudai.point.repository.model.PointTaskModel;
import com.tuotiansudai.point.repository.model.UserPointTaskModel;
import com.tuotiansudai.point.service.PointBillService;
import com.tuotiansudai.point.service.PointTaskService;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.ProductType;
import com.tuotiansudai.util.AmountConverter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;

@Service
public class PointTaskServiceImpl implements PointTaskService {

    private static Logger logger = Logger.getLogger(PointTaskServiceImpl.class);

    private final static List<PointTask> NEWBIE_TASKS = Lists.newArrayList(PointTask.REGISTER, PointTask.BIND_BANK_CARD, PointTask.FIRST_RECHARGE, PointTask.FIRST_INVEST);

    private final static List<PointTask> ADVANCED_TASKS = Lists.newArrayList(PointTask.EACH_SUM_INVEST,
            PointTask.FIRST_SINGLE_INVEST,
            PointTask.EACH_RECOMMEND,
            PointTask.FIRST_REFERRER_INVEST,
            PointTask.FIRST_INVEST_180,
            PointTask.FIRST_TURN_ON_NO_PASSWORD_INVEST,
            PointTask.FIRST_TURN_ON_AUTO_INVEST,
            PointTask.FIRST_INVEST_360);

    final static long SUM_INVEST_5000_AMOUNT = 500000L;
    final static long SUM_INVEST_5000_POINT = 100;
    final static long FIRST_INVEST_10000_AMOUNT = 1000000L;
    final static long FIRST_INVEST_10000_POINT = 200;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private PointTaskMapper pointTaskMapper;

    @Autowired
    private UserPointTaskMapper userPointTaskMapper;

    @Autowired
    private PointBillService pointBillService;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private BankCardMapper bankCardMapper;

    @Autowired
    private RechargeMapper rechargeMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private ReferrerRelationMapper referrerRelationMapper;

    @Override
    @Transactional
    public void completeNewbieTask(PointTask pointTask, String loginName) {
        if (this.isCompletedNewbieTaskConditions(pointTask, loginName)) {
            PointTaskModel pointTaskModel = pointTaskMapper.findByName(pointTask);
            long maxTaskLevel = userPointTaskMapper.findMaxTaskLevelByLoginName(loginName, pointTask);
            userPointTaskMapper.create(new UserPointTaskModel(loginName, pointTaskModel.getId(), pointTaskModel.getPoint(), maxTaskLevel + 1));
            pointBillService.createTaskPointBill(loginName, pointTaskModel.getId(), pointTaskModel.getPoint(), pointTask.getDescription());
        }

        logger.debug(MessageFormat.format("[Point Task] {0} has completed newbie task {1}", loginName, pointTask.name()));
    }

    @Override
    public void completeAdvancedTask(PointTask pointTask, String loginName) {
        final int FIRST_TASK_LEVEL = 1;
        if (this.isCompletedAdvancedTaskConditions(pointTask, loginName)) {
            PointTaskModel pointTaskModel = pointTaskMapper.findByName(pointTask);
            long maxTaskLevel = userPointTaskMapper.findMaxTaskLevelByLoginName(loginName, pointTask);
            String pointBillNote;
            switch (pointTask) {
                case EACH_SUM_INVEST:
                    //累计投资满5000元返100积分，只能完成一次
                    userPointTaskMapper.create(new UserPointTaskModel(loginName, pointTaskModel.getId(), SUM_INVEST_5000_POINT, FIRST_TASK_LEVEL));
                    pointBillNote = MessageFormat.format("累计投资满{0}元奖励{1}积分", AmountConverter.convertCentToString(SUM_INVEST_5000_AMOUNT), String.valueOf(SUM_INVEST_5000_POINT));
                    pointBillService.createTaskPointBill(loginName, pointTaskModel.getId(), SUM_INVEST_5000_POINT, pointBillNote);
                    break;
                case FIRST_SINGLE_INVEST:
                    //首次投资满10000元返200积分，只能完成一次
                    long firstInvestAmount = investMapper.findLatestSuccessInvest(loginName).getAmount();
                    if (firstInvestAmount >= FIRST_INVEST_10000_AMOUNT) {
                        userPointTaskMapper.create(new UserPointTaskModel(loginName, pointTaskModel.getId(), FIRST_INVEST_10000_POINT, FIRST_TASK_LEVEL));
                        pointBillNote = MessageFormat.format("单笔投资满{0}元奖励{1}积分", AmountConverter.convertCentToString(FIRST_INVEST_10000_AMOUNT), String.valueOf(FIRST_INVEST_10000_POINT));
                        pointBillService.createTaskPointBill(loginName, pointTaskModel.getId(), FIRST_INVEST_10000_POINT, pointBillNote);
                    }
                    break;
                case EACH_RECOMMEND:
                case FIRST_REFERRER_INVEST:
                    String referrer = userMapper.findByLoginName(loginName).getReferrer();
                    long referrerMaxTaskLevel = userPointTaskMapper.findMaxTaskLevelByLoginName(referrer, pointTask);
                    userPointTaskMapper.create(new UserPointTaskModel(referrer, pointTaskModel.getId(), pointTaskModel.getPoint(), referrerMaxTaskLevel + 1));
                    pointBillNote = MessageFormat.format("{0}奖励{1}积分", pointTask.getTitle(), String.valueOf(pointTaskModel.getPoint()));
                    pointBillService.createTaskPointBill(referrer, pointTaskModel.getId(), pointTaskModel.getPoint(), pointBillNote);
                    break;
                case EACH_REFERRER_INVEST:
                    break;
                default:
                    userPointTaskMapper.create(new UserPointTaskModel(loginName, pointTaskModel.getId(), pointTaskModel.getPoint(), maxTaskLevel + 1));
                    pointBillNote = MessageFormat.format("{0}奖励{1}积分", pointTask.getTitle(), String.valueOf(pointTaskModel.getPoint()));
                    pointBillService.createTaskPointBill(loginName, pointTaskModel.getId(), pointTaskModel.getPoint(), pointBillNote);
            }
        }

        logger.debug(MessageFormat.format("[Point Task]{0} has completed task {1}", loginName, pointTask.name()));
    }

    @Override
    public List<PointTaskDto> getNewbiePointTasks(String loginName) {
        List<PointTaskDto> data = Lists.newArrayList();
        for (PointTask pointTask : NEWBIE_TASKS) {
            PointTaskModel pointTaskModel = pointTaskMapper.findByName(pointTask);
            PointTaskDto pointTaskDto = new PointTaskDto();
            pointTaskDto.setName(pointTask);
            pointTaskDto.setTitle(pointTask.getTitle());
            pointTaskDto.setDescription(pointTask.getDescription());
            pointTaskDto.setPoint(pointTaskModel.getPoint());
            pointTaskDto.setCompleted(CollectionUtils.isNotEmpty(userPointTaskMapper.findByLoginNameAndTask(loginName, pointTask)));
            pointTaskDto.setUrl(this.getTaskUrl(pointTask));

            data.add(pointTaskDto);
        }
        return data;
    }

    @Override
    public List<PointTaskDto> getAdvancedPointTasks(String loginName) {
        List<PointTaskDto> data = Lists.newArrayList();
        for (PointTask pointTask : ADVANCED_TASKS) {
            PointTaskModel pointTaskModel = pointTaskMapper.findByName(pointTask);
            long completedMaxTaskLevel = userPointTaskMapper.findMaxTaskLevelByLoginName(loginName, pointTask);
            if (pointTaskModel.getMaxLevel() == 0 || completedMaxTaskLevel < pointTaskModel.getMaxLevel()) {
                PointTaskDto pointTaskDto = new PointTaskDto();
                pointTaskDto.setName(pointTask);
                pointTaskDto.setUrl(this.getTaskUrl(pointTask));
                switch (pointTask) {
                    case EACH_SUM_INVEST:
                        long sumSuccessInvestAmount = investMapper.sumSuccessInvestAmountByLoginName(null, loginName);
                        pointTaskDto.setTitle(MessageFormat.format(pointTask.getTitle(), AmountConverter.convertCentToString(SUM_INVEST_5000_AMOUNT)));
                        pointTaskDto.setPoint(SUM_INVEST_5000_POINT);
                        pointTaskDto.setDescription(MessageFormat.format("还差<span class='color-key'>{0}元</span>即可获得奖励", AmountConverter.convertCentToString(SUM_INVEST_5000_AMOUNT - sumSuccessInvestAmount)));
                        break;
                    case FIRST_SINGLE_INVEST:
                        pointTaskDto.setTitle(MessageFormat.format(pointTask.getTitle(),
                                AmountConverter.convertCentToString(FIRST_INVEST_10000_AMOUNT)));
                        pointTaskDto.setPoint(FIRST_INVEST_10000_POINT);
                        break;
                    case EACH_RECOMMEND:
                        pointTaskDto.setTitle(pointTask.getTitle());
                        pointTaskDto.setPoint(pointTaskModel.getPoint());
                        pointTaskDto.setDescription(MessageFormat.format("已邀请{0}名好友注册",
                                String.valueOf(referrerRelationMapper.findByReferrerLoginNameAndLevel(loginName, 1).size())));
                        break;
                    default:
                        pointTaskDto.setTitle(pointTask.getTitle());
                        pointTaskDto.setPoint(pointTaskModel.getPoint());
                }
                data.add(pointTaskDto);
            }
        }
        return data;
    }

    private String getTaskUrl(PointTask pointTask) {
        switch (pointTask) {
            case REGISTER:
                return "/register/account";
            case BIND_BANK_CARD:
                return "/bind-card";
            case FIRST_RECHARGE:
                return "/recharge";
            case FIRST_INVEST:
            case EACH_SUM_INVEST:
            case FIRST_SINGLE_INVEST:
            case FIRST_INVEST_180:
            case FIRST_INVEST_360:
                return "/loan-list";
            case EACH_RECOMMEND:
            case EACH_REFERRER_INVEST:
            case FIRST_REFERRER_INVEST:
                return "/referrer/refer-list";
            case FIRST_TURN_ON_NO_PASSWORD_INVEST:
                return "/personal-info";
            case FIRST_TURN_ON_AUTO_INVEST:
                return "/auto-invest/agreement";
        }
        return null;
    }

    @Override
    public List<PointTaskDto> getCompletedAdvancedPointTasks(String loginName) {
        List<PointTaskDto> data = Lists.newArrayList();
        List<UserPointTaskModel> userPointTaskModels = userPointTaskMapper.findByLoginName(loginName);
        for (UserPointTaskModel userPointTaskModel : userPointTaskModels) {
            PointTaskDto pointTaskDto = new PointTaskDto();
            pointTaskDto.setPoint(userPointTaskModel.getPoint());
            pointTaskDto.setCompleted(true);
            PointTask pointTask = userPointTaskModel.getPointTask().getName();
            switch (pointTask) {
                case EACH_SUM_INVEST:
                    pointTaskDto.setTitle(MessageFormat.format(pointTask.getTitle(), AmountConverter.convertCentToString(SUM_INVEST_5000_AMOUNT)));
                    break;
                case FIRST_SINGLE_INVEST:
                    pointTaskDto.setTitle(MessageFormat.format(pointTask.getTitle(), AmountConverter.convertCentToString(FIRST_INVEST_10000_AMOUNT)));
                    break;
                case EACH_RECOMMEND:
                    pointTaskDto.setTitle(pointTask.getTitle());
                    break;
                case EACH_REFERRER_INVEST:
                    pointTaskDto.setTitle(pointTask.getTitle());
                    break;
                default:
                    pointTaskDto.setTitle(pointTask.getTitle());
            }
            data.add(pointTaskDto);
        }

        return data;
    }


    private boolean isCompletedNewbieTaskConditions(final PointTask pointTask, String loginName) {
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
            case BIND_BANK_CARD:
                return bankCardMapper.findPassedBankCardByLoginName(loginName) != null;
            case FIRST_RECHARGE:
                return rechargeMapper.findSumSuccessRechargeByLoginName(loginName) > 0;
            case FIRST_INVEST:
                return investMapper.sumSuccessInvestAmountByLoginName(null, loginName) > 0;
        }

        return false;
    }

    private boolean isCompletedAdvancedTaskConditions(final PointTask pointTask, String loginName) {
        String referrer = userMapper.findByLoginName(loginName).getReferrer();
        switch (pointTask) {
            case EACH_SUM_INVEST:
                //只能完成一次
                long sumInvestAmount = investMapper.sumSuccessInvestAmountByLoginName(null, loginName);
                return CollectionUtils.isEmpty(userPointTaskMapper.findByLoginNameAndTask(loginName, pointTask)) && (sumInvestAmount >= SUM_INVEST_5000_AMOUNT);
            case FIRST_SINGLE_INVEST:
                //只能完成一次
                return CollectionUtils.isEmpty(userPointTaskMapper.findByLoginNameAndTask(loginName, pointTask));
            case EACH_RECOMMEND:
                //只能完成一次
                return accountMapper.findByLoginName(referrer) != null && CollectionUtils.isEmpty(userPointTaskMapper.findByLoginNameAndTask(referrer, pointTask));
            case FIRST_REFERRER_INVEST:
                return accountMapper.findByLoginName(referrer) != null && userPointTaskMapper.findMaxTaskLevelByLoginName(referrer, pointTask) == 0;
            case EACH_REFERRER_INVEST:
                return accountMapper.findByLoginName(referrer) != null && investMapper.findLatestSuccessInvest(loginName).getAmount() >= 100000L;
            case FIRST_INVEST_180:
                return userPointTaskMapper.findMaxTaskLevelByLoginName(loginName, pointTask) == 0
                        && loanMapper.findById(investMapper.findLatestSuccessInvest(loginName).getLoanId()).getProductType() == ProductType._180;
            case FIRST_INVEST_360:
                return userPointTaskMapper.findMaxTaskLevelByLoginName(loginName, pointTask) == 0
                        && loanMapper.findById(investMapper.findLatestSuccessInvest(loginName).getLoanId()).getProductType() == ProductType._360;
            case FIRST_TURN_ON_AUTO_INVEST:
            case FIRST_TURN_ON_NO_PASSWORD_INVEST:
                return userPointTaskMapper.findMaxTaskLevelByLoginName(loginName, pointTask) == 0;
        }

        return false;
    }
}
