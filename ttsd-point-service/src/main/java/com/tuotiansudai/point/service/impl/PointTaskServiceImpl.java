package com.tuotiansudai.point.service.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.enums.AppUrl;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.point.repository.dto.PointTaskDto;
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
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.RedisWrapperClient;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
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
            PointTask.EACH_RECOMMEND_REGISTER,
            PointTask.EACH_RECOMMEND_BANK_CARD,
            PointTask.EACH_RECOMMEND_INVEST,
            PointTask.FIRST_INVEST_180,
            PointTask.FIRST_TURN_ON_NO_PASSWORD_INVEST,
            PointTask.FIRST_INVEST_360);

    private final static List<PointTask> EACH_TASKS = Lists.newArrayList(PointTask.EACH_RECOMMEND_REGISTER,
            PointTask.EACH_RECOMMEND_BANK_CARD,
            PointTask.EACH_RECOMMEND_INVEST);

    private final static long SUM_INVEST_5000_AMOUNT = 500000L;
    private final static long SUM_INVEST_5000_POINT = 100;
    private final static long FIRST_INVEST_10000_AMOUNT = 1000000L;
    private final static long FIRST_INVEST_10000_POINT = 200;

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
    private BankAccountMapper bankAccountMapper;

    @Autowired
    private BankCardMapper bankCardMapper;

    @Autowired
    private BankRechargeMapper bankRechargeMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    private RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    private final String REFERRER_ACTIVITY_SUPER_SCHOLAR_REGISTER = "REFERRER_ACTIVITY_SUPER_SCHOLAR_REGISTER:{0}:{1}";

    private final String REFERRER_ACTIVITY_SUPER_SCHOLAR_ACCOUNT = "REFERRER_ACTIVITY_SUPER_SCHOLAR_ACCOUNT:{0}:{1}";

    private final int seconds = 60 * 24 * 60 * 60;

    @Override
    @Transactional
    public void completeNewbieTask(PointTask pointTask, String loginName) {
        if (this.isCompletedNewbieTaskConditions(pointTask, loginName)) {
            PointTaskModel pointTaskModel = pointTaskMapper.findByName(pointTask);
            long maxTaskLevel = userPointTaskMapper.findMaxTaskLevelByLoginName(loginName, pointTask);
            userPointTaskMapper.create(new UserPointTaskModel(loginName, pointTaskModel.getId(), pointTaskModel.getPoint(), maxTaskLevel + 1));
            pointBillService.createPointBill(loginName, pointTaskModel.getId(), PointBusinessType.TASK, pointTaskModel.getPoint(), pointTask.getDescription());
            this.sendReferrerReward(loginName, pointTask);
        }

        logger.info(MessageFormat.format("[Point Task] {0} has completed newbie task {1}", loginName, pointTask.name()));
    }

    @Override
    public void completeAdvancedTask(PointTask pointTask, String loginName) {
        final int FIRST_TASK_LEVEL = 1;
        if (this.isCompletedAdvancedTaskConditions(pointTask, loginName)) {
            PointTaskModel pointTaskModel = pointTaskMapper.findByName(pointTask);
            long maxTaskLevel = userPointTaskMapper.findMaxTaskLevelByLoginName(loginName, pointTask);
            String pointBillNote;
            String referrer;
            long referrerMaxTaskLevel;
            long firstInvestAmount;
            switch (pointTask) {
                case EACH_SUM_INVEST:
                    //累计投资满5000元返100积分，只能完成一次
                    userPointTaskMapper.create(new UserPointTaskModel(loginName, pointTaskModel.getId(), SUM_INVEST_5000_POINT, FIRST_TASK_LEVEL));
                    pointBillNote = MessageFormat.format("累计投资满{0}元奖励{1}积分", AmountConverter.convertCentToString(SUM_INVEST_5000_AMOUNT), String.valueOf(SUM_INVEST_5000_POINT));
                    pointBillService.createPointBill(loginName, pointTaskModel.getId(), PointBusinessType.TASK, SUM_INVEST_5000_POINT, pointBillNote);
                    break;
                case FIRST_SINGLE_INVEST:
                    //首次投资满10000元返200积分，只能完成一次
                    firstInvestAmount = investMapper.findLatestSuccessInvest(loginName).getAmount();
                    if (firstInvestAmount >= FIRST_INVEST_10000_AMOUNT) {
                        userPointTaskMapper.create(new UserPointTaskModel(loginName, pointTaskModel.getId(), FIRST_INVEST_10000_POINT, FIRST_TASK_LEVEL));
                        pointBillNote = MessageFormat.format("单笔投资满{0}元奖励{1}积分", AmountConverter.convertCentToString(FIRST_INVEST_10000_AMOUNT), String.valueOf(FIRST_INVEST_10000_POINT));
                        pointBillService.createPointBill(loginName, pointTaskModel.getId(), PointBusinessType.TASK, FIRST_INVEST_10000_POINT, pointBillNote);
                    }
                    break;
                case EACH_RECOMMEND_INVEST:
                    if (investMapper.sumSuccessInvestCountByLoginName(loginName) != 1) {
                        break;
                    }
                case EACH_RECOMMEND_REGISTER:
                    this.sendReferrerReward(loginName, pointTask);
                case FIRST_REFERRER_INVEST:
                    referrer = userMapper.findByLoginName(loginName).getReferrer();
                    if (Strings.isNullOrEmpty(referrer)) {
                        break;
                    }
                    referrerMaxTaskLevel = userPointTaskMapper.findMaxTaskLevelByLoginName(referrer, pointTask);
                    userPointTaskMapper.create(new UserPointTaskModel(referrer, pointTaskModel.getId(), pointTaskModel.getPoint(), referrerMaxTaskLevel + 1));
                    pointBillNote = MessageFormat.format("{0}奖励{1}积分", pointTask.getTitle(), String.valueOf(pointTaskModel.getPoint()));
                    pointBillService.createPointBill(referrer, pointTaskModel.getId(), PointBusinessType.TASK, pointTaskModel.getPoint(), pointBillNote);
                    break;
                default:
                    userPointTaskMapper.create(new UserPointTaskModel(loginName, pointTaskModel.getId(), pointTaskModel.getPoint(), maxTaskLevel + 1));
                    pointBillNote = MessageFormat.format("{0}奖励{1}积分", pointTask.getTitle(), String.valueOf(pointTaskModel.getPoint()));
                    pointBillService.createPointBill(loginName, pointTaskModel.getId(), PointBusinessType.TASK, pointTaskModel.getPoint(), pointBillNote);
            }
        }

        logger.info(MessageFormat.format("[Point Task]{0} has completed task {1}", loginName, pointTask.name()));
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
                        long sumSuccessInvestAmount = investMapper.sumSuccessInvestAmountByLoginName(null, loginName, true);
                        pointTaskDto.setTitle(MessageFormat.format(pointTask.getTitle(), AmountConverter.convertCentToString(SUM_INVEST_5000_AMOUNT)));
                        pointTaskDto.setPoint(SUM_INVEST_5000_POINT);
                        pointTaskDto.setDescription(MessageFormat.format("还差<span class='color-key'>{0}元</span>即可获得奖励", AmountConverter.convertCentToString(SUM_INVEST_5000_AMOUNT - sumSuccessInvestAmount)));
                        break;
                    case FIRST_SINGLE_INVEST:
                        pointTaskDto.setTitle(MessageFormat.format(pointTask.getTitle(),
                                AmountConverter.convertCentToString(FIRST_INVEST_10000_AMOUNT)));
                        pointTaskDto.setPoint(FIRST_INVEST_10000_POINT);
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
            case EACH_RECOMMEND_REGISTER:
            case EACH_REFERRER_INVEST:
            case FIRST_REFERRER_INVEST:
            case EACH_RECOMMEND_BANK_CARD:
            case EACH_RECOMMEND_INVEST:
                return "/referrer/refer-list";
            case FIRST_TURN_ON_NO_PASSWORD_INVEST:
                return AppUrl.SETTING.getPath();

        }
        return null;
    }

    @Override
    public List<PointTaskDto> getCompletedAdvancedPointTasks(String loginName) {
        List<PointTaskDto> data = Lists.newArrayList();
        List<UserPointTaskModel> userPointTaskModels = userPointTaskMapper.findByLoginName(loginName);
        for (UserPointTaskModel userPointTaskModel : userPointTaskModels) {
            if (EACH_TASKS.contains(userPointTaskModel.getPointTask().getName())) {
                continue;
            }

            if (ADVANCED_TASKS.contains(userPointTaskModel.getPointTask().getName())) {
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
                    case EACH_RECOMMEND_REGISTER:
                        pointTaskDto.setTitle(pointTask.getTitle());
                        break;
                    default:
                        pointTaskDto.setTitle(pointTask.getTitle());
                }
                data.add(pointTaskDto);
            }
        }

        return data;
    }


    private boolean isCompletedNewbieTaskConditions(final PointTask pointTask, String loginName) {
        List<UserPointTaskModel> completedTasks = userPointTaskMapper.findByLoginName(loginName);
        boolean isCompleted = completedTasks.stream().anyMatch(completedTask -> completedTask.getPointTask().getName() == pointTask);

        if (isCompleted) {
            return false;
        }

        switch (pointTask) {
            case REGISTER:
                return true;
            case BIND_BANK_CARD:
                return bankCardMapper.findPassedBankCardByLoginName(loginName) != null;
            case FIRST_RECHARGE:
                return bankRechargeMapper.sumRechargeSuccessAmountByLoginName(loginName) > 0;
            case FIRST_INVEST:
                return investMapper.sumSuccessInvestAmountByLoginName(null, loginName, true) > 0;
        }

        return false;
    }

    private boolean isCompletedAdvancedTaskConditions(final PointTask pointTask, String loginName) {
        String referrer = userMapper.findByLoginName(loginName).getReferrer();
        PointTaskModel pointTaskModel = pointTaskMapper.findByName(pointTask);
        if (!pointTaskModel.isActive()) {
            return false;
        }
        switch (pointTask) {
            case EACH_SUM_INVEST:
                //只能完成一次
                long sumInvestAmount = investMapper.sumSuccessInvestAmountByLoginName(null, loginName, true);
                return CollectionUtils.isEmpty(userPointTaskMapper.findByLoginNameAndTask(loginName, pointTask)) && (sumInvestAmount >= SUM_INVEST_5000_AMOUNT);
            case FIRST_SINGLE_INVEST:
                //只能完成一次
                return CollectionUtils.isEmpty(userPointTaskMapper.findByLoginNameAndTask(loginName, pointTask));
            case EACH_RECOMMEND_REGISTER:
                return true;
            case EACH_RECOMMEND_INVEST:
                if (investMapper.sumSuccessInvestCountByLoginName(loginName) == 1) {
                    return true;
                }
                break;
            case FIRST_REFERRER_INVEST:
                return bankAccountMapper.findByLoginName(referrer) != null && userPointTaskMapper.findMaxTaskLevelByLoginName(referrer, pointTask) == 0;
            case FIRST_INVEST_180:
                return userPointTaskMapper.findMaxTaskLevelByLoginName(loginName, pointTask) == 0
                        && loanMapper.findById(investMapper.findLatestSuccessInvest(loginName).getLoanId()).getProductType() == ProductType._180;
            case FIRST_INVEST_360:
                return userPointTaskMapper.findMaxTaskLevelByLoginName(loginName, pointTask) == 0
                        && loanMapper.findById(investMapper.findLatestSuccessInvest(loginName).getLoanId()).getProductType() == ProductType._360;
            case FIRST_TURN_ON_NO_PASSWORD_INVEST:
                return userPointTaskMapper.findMaxTaskLevelByLoginName(loginName, pointTask) == 0;
        }

        return false;
    }

    private void sendReferrerReward(String loginName, PointTask pointTask) {
        String referrer = userMapper.findByLoginName(loginName).getReferrer();
        if (Strings.isNullOrEmpty(referrer)) {
            return;
        }

        Long couponId = null;
        switch (pointTask) {
            case BIND_BANK_CARD:
                PointTaskModel bankCardTaskModel = pointTaskMapper.findByName(PointTask.EACH_RECOMMEND_BANK_CARD);
                long referrerMaxTaskLevel = userPointTaskMapper.findMaxTaskLevelByLoginName(referrer, PointTask.EACH_RECOMMEND_BANK_CARD);
                userPointTaskMapper.create(new UserPointTaskModel(referrer, bankCardTaskModel.getId(), bankCardTaskModel.getPoint(), referrerMaxTaskLevel + 1));
                String pointBillNote = MessageFormat.format("{0}奖励{1}积分", PointTask.EACH_RECOMMEND_BANK_CARD.getTitle(), String.valueOf(bankCardTaskModel.getPoint()));
                pointBillService.createPointBill(referrer, bankCardTaskModel.getId(), PointBusinessType.TASK, bankCardTaskModel.getPoint(), pointBillNote);
                couponId = 401L;
                break;
            case EACH_RECOMMEND_REGISTER:
                couponId = 400L;
                break;
            case FIRST_INVEST:
                couponId = 402L;
                break;
            case REGISTER:
                referrerSuperScholarActivityAccount(loginName, referrer);
                break;
        }

        if (couponId != null) {
            logger.info(MessageFormat.format("[推荐奖励] pointTask:{0} login_name:{1}, referrer:{2}, couponId:{3}", pointTask, loginName, referrer, String.valueOf(couponId)));
            mqWrapperClient.sendMessage(MessageQueue.CouponAssigning, referrer + ":" + couponId);
        }
    }

    private void referrerSuperScholarActivityAccount(String loginName, String referrer){
        String currentDate = DateTimeFormat.forPattern("yyyy-MM-dd").print(DateTime.now());
        if (redisWrapperClient.exists(MessageFormat.format(REFERRER_ACTIVITY_SUPER_SCHOLAR_REGISTER, currentDate, loginName))){
            redisWrapperClient.setex(MessageFormat.format(REFERRER_ACTIVITY_SUPER_SCHOLAR_ACCOUNT, currentDate, referrer), seconds, "SUCCESS");
        }
    }

}
