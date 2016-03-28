package com.tuotiansudai.point.service.impl;

import com.google.common.collect.Lists;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.coupon.repository.model.UserGroup;
import com.tuotiansudai.coupon.service.CouponActivationService;
import com.tuotiansudai.dto.TransferCashDto;
import com.tuotiansudai.job.JobType;
import com.tuotiansudai.job.LotteryTransferCashJob;
import com.tuotiansudai.point.repository.mapper.PointPrizeMapper;
import com.tuotiansudai.point.repository.mapper.UserPointPrizeMapper;
import com.tuotiansudai.point.repository.model.PointBusinessType;
import com.tuotiansudai.point.repository.model.PointPrizeModel;
import com.tuotiansudai.point.repository.model.UserPointPrizeModel;
import com.tuotiansudai.point.service.PointBillService;
import com.tuotiansudai.point.service.PointLotteryService;
import com.tuotiansudai.util.DateUtil;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.JobManager;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class PointLotteryServiceImpl implements PointLotteryService{

    static Logger logger = Logger.getLogger(PointLotteryServiceImpl.class);

    private final static long LOTTERY_POINT = -1000;

    private final static String ALREADY_LOTTERY = "抽奖次数已满";

    private final static String LAST_EXPIRY_TIME = " 23:59:59";

    @Autowired
    private PointPrizeMapper pointPrizeMapper;

    @Autowired
    private UserPointPrizeMapper userPointPrizeMapper;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Autowired
    private PointBillService pointBillService;

    @Autowired
    private CouponActivationService couponActivationService;

    @Autowired
    private JobManager jobManager;

    @Autowired
    private IdGenerator idGenerator;

    public static String redisShareTemple = "web:{0}{1}:share";

    @Override
    @Transactional
    public String pointLottery(String loginName) {
        DateTime dateTime = new DateTime();
        List<UserPointPrizeModel> userPointPrizeModelToday = userPointPrizeMapper.findByLoginNameAndCreateTime(loginName, dateTime.toString("yyyy-MM-dd"));
        if (CollectionUtils.isEmpty(userPointPrizeModelToday) ||
                (redisWrapperClient.exists(MessageFormat.format(redisShareTemple, loginName, dateTime.toString("yyyy-MM-dd"))) && userPointPrizeModelToday.size() < 2)) {
            PointPrizeModel winPointPrize = this.winLottery();
            UserPointPrizeModel userPointPrizeModel = new UserPointPrizeModel(winPointPrize.getId(), loginName);
            userPointPrizeMapper.create(userPointPrizeModel);

            pointBillService.createPointBill(loginName, winPointPrize.getId(), PointBusinessType.LOTTERY, LOTTERY_POINT);

            if (winPointPrize.getCouponId() != null) {
                couponActivationService.assignUserCoupon(loginName, Lists.newArrayList(UserGroup.WINNER), winPointPrize.getCouponId());
            }

            if (winPointPrize.getCash() != null) {
                long orderId = idGenerator.generate();
                TransferCashDto transferCashDto = new TransferCashDto(loginName, String.valueOf(orderId), String.valueOf(winPointPrize.getCash()));
                this.createTransferCashJob(transferCashDto);
            }

            return winPointPrize.getName();
        } else {
            return ALREADY_LOTTERY;
        }
    }

    private void createTransferCashJob(TransferCashDto transferCashDto) {
        Date triggerTime = new DateTime().plusMinutes(LotteryTransferCashJob.TRANSFER_CASH_DELAY_MINUTES).toDate();
        try {
            jobManager.newJob(JobType.LotteryTransferCash, LotteryTransferCashJob.class)
                    .addJobData(LotteryTransferCashJob.TRANSFER_CASH_KEY, transferCashDto)
                    .withIdentity(JobType.LotteryTransferCash.name(), "LoginName-" + transferCashDto.getLoginName())
                    .replaceExistingJob(true)
                    .runOnceAt(triggerTime)
                    .submit();
        } catch (SchedulerException e) {
            logger.error("create transfer cash job for login name [" + transferCashDto.getLoginName() + "] fail", e);
        }
    }

    private PointPrizeModel winLottery() {
        List<PointPrizeModel> pointPrizeModels = pointPrizeMapper.findAllPossibleWin();
        int[] probabilities = new int[pointPrizeModels.size()];
        for (int i = 0; i < probabilities.length; i++) {
            if (i > 0) {
                probabilities[i] = pointPrizeModels.get(i).getProbability() + pointPrizeModels.get(i - 1).getProbability();
            } else {
                probabilities[i] = pointPrizeModels.get(i).getProbability();
            }
        }
        int num = new Random().nextInt(100)+1;
        int choosePointPrize = 0;
        for (int i = 0; i < probabilities.length; i++) {
            if (num <= probabilities[i]) {
                choosePointPrize = i;
                break;
            }
        }
        return pointPrizeModels.get(choosePointPrize);
    }

    @Override
    public void getLotteryOnceChance(String loginName){
        SimpleDateFormat formatShortTime = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String begin = formatter.format(new Date());
        String end = formatShortTime.format(new Date()) + LAST_EXPIRY_TIME;
        int expiryTime = DateUtil.differenceSeconds(begin, end);
        redisWrapperClient.setex(MessageFormat.format(redisShareTemple, loginName, formatShortTime.format(new Date()).replace("-","")), expiryTime, "1");
    }
}
