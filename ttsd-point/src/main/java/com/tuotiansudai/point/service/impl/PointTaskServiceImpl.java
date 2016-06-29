package com.tuotiansudai.point.service.impl;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
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

    private final static List<PointTask> NEWBIE_TASK = Lists.newArrayList(PointTask.REGISTER, PointTask.BIND_BANK_CARD, PointTask.FIRST_RECHARGE, PointTask.FIRST_INVEST);

    private final static List<PointTask> ADVANCED_TASK = Lists.newArrayList(PointTask.REGISTER, PointTask.BIND_BANK_CARD, PointTask.FIRST_RECHARGE, PointTask.FIRST_INVEST);

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

    @Override
    @Transactional
    public void completeNewbieTask(PointTask pointTask, String loginName) {
        if (this.isCompletedNewbieTaskConditions(pointTask, loginName)) {
            PointTaskModel pointTaskModel = pointTaskMapper.findByName(pointTask);
            long maxTaskLevel = userPointTaskMapper.findMaxTaskLevelByLoginName(loginName, pointTask);
            userPointTaskMapper.create(new UserPointTaskModel(loginName, pointTaskModel.getId(), pointTaskModel.getPoint(), maxTaskLevel + 1));
            pointBillService.createPointBill(loginName, pointTaskModel.getId(), PointBusinessType.TASK, pointTaskModel.getPoint());
        }

        logger.debug(MessageFormat.format("{0} has completed task {1}", loginName, pointTask.name()));
    }

    @Override
    public void completeAdvancedTask(PointTask pointTask, String loginName) {
        if (this.isCompletedAdvancedTaskConditions(pointTask, loginName)) {
            PointTaskModel pointTaskModel = pointTaskMapper.findByName(pointTask);
            long maxTaskLevel = userPointTaskMapper.findMaxTaskLevelByLoginName(loginName, pointTask);
            switch (pointTask) {
                case EACH_SUM_INVEST:
                    long eachSumInvestTaskLevel = this.getEachSumInvestTaskLevel(investMapper.findLatestSuccessInvest(loginName).getAmount());
                    for (long level = maxTaskLevel + 1; level <= eachSumInvestTaskLevel; level++) {
                        userPointTaskMapper.create(new UserPointTaskModel(loginName, pointTaskModel.getId(), this.getEachSumInvestTaskPointByLevel(level), level));
                        pointBillService.createPointBill(loginName, pointTaskModel.getId(), PointBusinessType.TASK, this.getEachSumInvestTaskPointByLevel(level));
                    }
                    break;
                case FIRST_SINGLE_INVEST:
                    int eachSingleInvestTaskLevel = this.getFirstSingleInvestTaskLevel(investMapper.findLatestSuccessInvest(loginName).getAmount());
                    for (long level = maxTaskLevel + 1; level <= eachSingleInvestTaskLevel; level++) {
                        userPointTaskMapper.create(new UserPointTaskModel(loginName, pointTaskModel.getId(), this.getFirstSingleInvestTaskPointByLevel(level), level));
                        pointBillService.createPointBill(loginName, pointTaskModel.getId(), PointBusinessType.TASK, this.getFirstSingleInvestTaskPointByLevel(level));
                    }
                    break;
                case EACH_RECOMMEND:
                case FIRST_REFERRER_INVEST:
                case EACH_REFERRER_INVEST:
                    String referrer = userMapper.findByLoginName(loginName).getReferrer();
                    long referrerMaxTaskLevel = userPointTaskMapper.findMaxTaskLevelByLoginName(referrer, pointTask);
                    userPointTaskMapper.create(new UserPointTaskModel(referrer, pointTaskModel.getId(), pointTaskModel.getPoint(), referrerMaxTaskLevel + 1));
                    pointBillService.createPointBill(referrer, pointTaskModel.getId(), PointBusinessType.TASK, pointTaskModel.getPoint());
                    break;
                default:
                    userPointTaskMapper.create(new UserPointTaskModel(loginName, pointTaskModel.getId(), pointTaskModel.getPoint(), maxTaskLevel + 1));
                    pointBillService.createPointBill(loginName, pointTaskModel.getId(), PointBusinessType.TASK, pointTaskModel.getPoint());
            }
        }

        logger.debug(MessageFormat.format("{0} has completed task {1}", loginName, pointTask.name()));
    }

    @Override
    public List<PointTaskDto> displayPointTask(int index, int pageSize, final String loginName) {
        List<PointTaskModel> pointTaskModels = pointTaskMapper.findPointTaskPagination((index - 1) * pageSize, pageSize);
        if (CollectionUtils.isEmpty(pointTaskModels)) {
            return Lists.newArrayList();
        }

        return Lists.transform(pointTaskModels, new Function<PointTaskModel, PointTaskDto>() {
            @Override
            public PointTaskDto apply(PointTaskModel pointTaskModel) {
                PointTaskDto pointTaskDto = new PointTaskDto(pointTaskModel);
                UserPointTaskModel userPointTaskModel = userPointTaskMapper.findByLoginNameAndId(pointTaskModel.getId(), loginName);
                pointTaskDto.setCompleted(userPointTaskModel != null);
                return pointTaskDto;
            }
        });
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
                return !Strings.isNullOrEmpty(referrer);
            case FIRST_REFERRER_INVEST:
                return !Strings.isNullOrEmpty(referrer) && userPointTaskMapper.findMaxTaskLevelByLoginName(referrer, pointTask) == 0;
            case EACH_REFERRER_INVEST:
                return !Strings.isNullOrEmpty(referrer) && investMapper.findLatestSuccessInvest(loginName).getAmount() >= 100000L;
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
        }
        return amount / 100000000 + 5;
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

    private long getFirstSingleInvestTaskPointByLevel(long level) {
        if (level < 1) {
            return 0;
        }
        return ImmutableMap.<Long, Long>builder().put(1L, 2000L).put(2L, 5000L).put(3L, 10000L).put(4L, 20000L).put(5L, 50000L).build().get(level);
    }
}
