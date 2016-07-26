package com.tuotiansudai.point.service.impl;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableMap;
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
import com.tuotiansudai.repository.model.ProductType;
import com.tuotiansudai.repository.model.ReferrerRelationModel;
import com.tuotiansudai.util.AmountConverter;
import org.apache.commons.collections.CollectionUtils;
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
            PointTask.EACH_REFERRER_INVEST,
            PointTask.FIRST_INVEST_180,
            PointTask.FIRST_INVEST_360,
            PointTask.FIRST_TURN_ON_NO_PASSWORD_INVEST,
            PointTask.FIRST_TURN_ON_AUTO_INVEST);

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
        if (this.isCompletedAdvancedTaskConditions(pointTask, loginName)) {
            PointTaskModel pointTaskModel = pointTaskMapper.findByName(pointTask);
            long maxTaskLevel = userPointTaskMapper.findMaxTaskLevelByLoginName(loginName, pointTask);
            String pointBillNote;
            switch (pointTask) {
                case EACH_SUM_INVEST:
                    long eachSumInvestTaskLevel = this.getEachSumInvestTaskLevel(investMapper.sumSuccessInvestAmountByLoginName(null, loginName));
                    for (long level = maxTaskLevel + 1; level <= eachSumInvestTaskLevel; level++) {
                        userPointTaskMapper.create(new UserPointTaskModel(loginName, pointTaskModel.getId(), this.getEachSumInvestTaskPointByLevel(level), level));
                        pointBillNote = MessageFormat.format("累计投资满{0}元奖励{1}财豆", AmountConverter.convertCentToString(this.getEachSumInvestTaskAmountByLevel(level)), String.valueOf(this.getEachSumInvestTaskPointByLevel(level)));
                        pointBillService.createTaskPointBill(loginName, pointTaskModel.getId(), this.getEachSumInvestTaskPointByLevel(level), pointBillNote);
                    }
                    break;
                case FIRST_SINGLE_INVEST:
                    int eachSingleInvestTaskLevel = this.getFirstSingleInvestTaskLevel(investMapper.findLatestSuccessInvest(loginName).getAmount());
                    for (long level = maxTaskLevel + 1; level <= eachSingleInvestTaskLevel; level++) {
                        userPointTaskMapper.create(new UserPointTaskModel(loginName, pointTaskModel.getId(), this.getFirstSingleInvestTaskPointByLevel(level), level));
                        pointBillNote = MessageFormat.format("单笔投资满{0}元奖励{1}财豆", AmountConverter.convertCentToString(this.getFirstSingleInvestTaskAmountByLevel(level)), String.valueOf(this.getFirstSingleInvestTaskPointByLevel(level)));
                        pointBillService.createTaskPointBill(loginName, pointTaskModel.getId(), this.getFirstSingleInvestTaskPointByLevel(level), pointBillNote);
                    }
                    break;
                case EACH_RECOMMEND:
                case FIRST_REFERRER_INVEST:
                case EACH_REFERRER_INVEST:
                    String referrer = userMapper.findByLoginName(loginName).getReferrer();
                    long referrerMaxTaskLevel = userPointTaskMapper.findMaxTaskLevelByLoginName(referrer, pointTask);
                    userPointTaskMapper.create(new UserPointTaskModel(referrer, pointTaskModel.getId(), pointTaskModel.getPoint(), referrerMaxTaskLevel + 1));
                    pointBillNote = MessageFormat.format("{0}奖励{1}财豆", pointTask.getTitle(), String.valueOf(pointTaskModel.getPoint()));
                    pointBillService.createTaskPointBill(referrer, pointTaskModel.getId(), pointTaskModel.getPoint(), pointBillNote);
                    break;
                default:
                    userPointTaskMapper.create(new UserPointTaskModel(loginName, pointTaskModel.getId(), pointTaskModel.getPoint(), maxTaskLevel + 1));
                    pointBillNote = MessageFormat.format("{0}奖励{1}财豆", pointTask.getTitle(), String.valueOf(pointTaskModel.getPoint()));
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
                        long nextLevelSumSuccessInvestAmount = this.getEachSumInvestTaskAmountByLevel(completedMaxTaskLevel + 1);
                        pointTaskDto.setTitle(MessageFormat.format(pointTask.getTitle(), AmountConverter.convertCentToString(nextLevelSumSuccessInvestAmount)));
                        pointTaskDto.setPoint(this.getEachSumInvestTaskPointByLevel(completedMaxTaskLevel + 1));
                        pointTaskDto.setDescription(MessageFormat.format("还差<span class='color-key'>{0}元</span>即可获得奖励", AmountConverter.convertCentToString(nextLevelSumSuccessInvestAmount - sumSuccessInvestAmount)));
                        break;
                    case FIRST_SINGLE_INVEST:
                        pointTaskDto.setTitle(MessageFormat.format(pointTask.getTitle(),
                                AmountConverter.convertCentToString(this.getFirstSingleInvestTaskAmountByLevel(completedMaxTaskLevel + 1))));
                        pointTaskDto.setPoint(this.getFirstSingleInvestTaskPointByLevel(completedMaxTaskLevel + 1));
                        break;
                    case EACH_RECOMMEND:
                        pointTaskDto.setTitle(pointTask.getTitle());
                        pointTaskDto.setPoint(pointTaskModel.getPoint());
                        pointTaskDto.setDescription(MessageFormat.format("已邀请{0}名好友注册",
                                String.valueOf(referrerRelationMapper.findByReferrerLoginNameAndLevel(loginName, 1).size()),
                                String.valueOf(pointTaskModel.getPoint() * completedMaxTaskLevel)));
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
            long taskLevel = userPointTaskModel.getTaskLevel();
            PointTaskDto pointTaskDto = new PointTaskDto();
            pointTaskDto.setPoint(userPointTaskModel.getPoint());
            pointTaskDto.setCompleted(true);
            PointTask pointTask = userPointTaskModel.getPointTask().getName();
            switch (pointTask) {
                case EACH_SUM_INVEST:
                    pointTaskDto.setTitle(MessageFormat.format(pointTask.getTitle(), AmountConverter.convertCentToString(this.getEachSumInvestTaskAmountByLevel(taskLevel))));
                    break;
                case FIRST_SINGLE_INVEST:
                    pointTaskDto.setTitle(MessageFormat.format(pointTask.getTitle(), AmountConverter.convertCentToString(this.getFirstSingleInvestTaskAmountByLevel(taskLevel))));
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
                long sumInvestAmount = investMapper.sumSuccessInvestAmountByLoginName(null, loginName);
                return userPointTaskMapper.findMaxTaskLevelByLoginName(loginName, pointTask) < this.getEachSumInvestTaskLevel(sumInvestAmount);
            case FIRST_SINGLE_INVEST:
                long firstSingleInvestTaskLevel = this.getFirstSingleInvestTaskLevel(investMapper.findLatestSuccessInvest(loginName).getAmount());
                return userPointTaskMapper.findMaxTaskLevelByLoginName(loginName, pointTask) < firstSingleInvestTaskLevel;
            case EACH_RECOMMEND:
                return accountMapper.findByLoginName(referrer) != null;
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

    private long getEachSumInvestTaskLevel(long amount) {
        if (amount <= 0) {
            return 0;
        }
        if (amount < 100000000) {
            for (int index = 5; index > 0; index--) {
                if (amount >= Lists.newArrayList(500000L, 1000000L, 5000000L, 10000000L, 50000000L).get(index - 1)) {
                    return index;
                }
            }
            return 0;
        }
        return amount / 100000000 + 5;
    }

    private long getEachSumInvestTaskAmountByLevel(long level) {
        if (level <= 0) {
            return 0;
        }
        if (level <= 5) {
            return Lists.newArrayList(500000L, 1000000L, 5000000L, 10000000L, 50000000L).get((int) level - 1);
        }
        return 100000000 * (level - 5);
    }


    private long getEachSumInvestTaskPointByLevel(long level) {
        if (level < 1) {
            return 0;
        }
        if (level <= 5) {
            return ImmutableMap.<Long, Long>builder().put(1L, 1000L).put(2L, 2000L).put(3L, 5000L).put(4L, 10000L).put(5L, 50000L).build().get(level);
        }
        return (level - 5) * 100000;
    }

    private int getFirstSingleInvestTaskLevel(long amount) {
        if (amount <= 0) {
            return 0;
        }
        for (int index = 5; index > 0; index--) {
            if (amount >= Lists.newArrayList(1000000L, 5000000L, 10000000L, 20000000L, 50000000L).get(index - 1)) {
                return index;
            }
        }
        return 0;
    }

    private long getFirstSingleInvestTaskAmountByLevel(long level) {
        if (level < 1 || level > 5) {
            return 0;
        }
        return Lists.newArrayList(1000000L, 5000000L, 10000000L, 20000000L, 50000000L).get((int) level - 1);
    }

    private long getFirstSingleInvestTaskPointByLevel(long level) {
        if (level < 1) {
            return 0;
        }
        return ImmutableMap.<Long, Long>builder().put(1L, 2000L).put(2L, 5000L).put(3L, 10000L).put(4L, 20000L).put(5L, 50000L).build().get(level);
    }
}
