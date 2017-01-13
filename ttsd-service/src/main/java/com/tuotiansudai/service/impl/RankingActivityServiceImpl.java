package com.tuotiansudai.service.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.coupon.service.CouponAssignmentService;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.TransferCashDto;
import com.tuotiansudai.activity.repository.dto.DrawLotteryDto;
import com.tuotiansudai.activity.repository.dto.PrizeWinnerDto;
import com.tuotiansudai.activity.repository.dto.UserScoreDto;
import com.tuotiansudai.activity.repository.dto.UserTianDouRecordDto;
import com.tuotiansudai.activity.repository.model.TianDouPrize;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.service.AccountService;
import com.tuotiansudai.service.RankingActivityService;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Tuple;

import java.math.BigDecimal;
import java.util.*;

@Service
public class RankingActivityServiceImpl implements RankingActivityService {

    private static Logger logger = Logger.getLogger(RankingActivityServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AccountService accountService;

    @Autowired
    private CouponAssignmentService couponAssignmentService;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private LoanMapper loanMapper;

    // 抽奖次数计数器。
    public static final String TIAN_DOU_DRAW_COUNTER = "web:ranking:tian_dou_draw_counter";

    // 抽奖人集合 <SET>
    public static final String TIAN_DOU_DRAW_USER_SET = "web:ranking:tian_dou_draw_user_set";

    // 用户－奖品。loginName:<List>prize+time(yyyy-MM-dd HH:mm:ss)
    public static final String TIAN_DOU_WINNER_PRIZE = "web:ranking:tian_dou_winner_prize:";

    // 奖品－用户。prize:<List>loginName+realName+mobile+identityNumber+time
    public static final String TIAN_DOU_PRIZE_WINNER = "web:ranking:tian_dou_prize_winner:";

    // 全部用户抽奖记录。 <List>prize+loginName+realName+mobile+identityNumber+time
    public static final String TIAN_DOU_ALL_WINNER = "web:ranking:tian_dou_all_winner";

    // 排行榜。有序集合<sortedSet>：score: 天豆数，member:loginName
    public static final String TIAN_DOU_USER_SCORE_RANK = "web:ranking:tian_dou_user_score_rank";

    // 用户天豆获取记录。<List>loginName:amount+score+desc+time
    public static final String TIAN_DOU_INVEST_SCORE_RECORD = "web:ranking:tian_dou_invest_score_record:";

    private static final long DRAW_SCORE = 1000;

    @Autowired
    private RandomUtils randomUtils;

    public BaseDto<DrawLotteryDto> drawTianDouPrize(String loginName) {
        logger.info(loginName + " is drawing the tiandou prize.");

        DrawLotteryDto drawLotteryDto = new DrawLotteryDto();
        BaseDto baseDto = new BaseDto();
        baseDto.setData(drawLotteryDto);

        if (StringUtils.isEmpty(loginName)) {
            logger.error("User not login. can't draw prize.");
            drawLotteryDto.setMessage("用户未登录，不能抽奖。");
            drawLotteryDto.setReturnCode(2);
            drawLotteryDto.setStatus(false);
            baseDto.setSuccess(false);
            return baseDto;
        }

        Double tianDouScore = redisWrapperClient.zscore(TIAN_DOU_USER_SCORE_RANK, loginName);
        if (tianDouScore == null || tianDouScore < DRAW_SCORE) {
            logger.info(loginName + "'s tianDou is not enough. tianDouScore:" + tianDouScore);

            drawLotteryDto.setMessage("您的天豆不足，投资赚取更多天豆再来抽奖吧！");
            drawLotteryDto.setReturnCode(1);
            drawLotteryDto.setStatus(false);
            baseDto.setSuccess(false);
            return baseDto;
        }

        TianDouPrize prize = getPrize(loginName);

        if (prize == null) {
            drawLotteryDto.setMessage("您的天豆不足，投资赚取更多天豆再来抽奖吧！");
            drawLotteryDto.setReturnCode(1);
            drawLotteryDto.setStatus(false);
            baseDto.setSuccess(false);
            return baseDto;
        }

        logger.info(loginName + " drew a prize: " + prize);

        UserModel userModel = userMapper.findByLoginName(loginName);
        AccountModel accountModel = accountService.findByLoginName(loginName);
        String userName = accountModel == null ? "" : userModel.getUserName();
        String identityNumber = accountModel == null ? "" : userModel.getIdentityNumber();

        String dateTime = DateFormatUtils.format(new Date(), "yyyy-MM-dd_HH:mm:ss");
        String winnerPrize = prize + "+" + dateTime;
        redisWrapperClient.lpush(TIAN_DOU_WINNER_PRIZE + loginName, winnerPrize);
        redisWrapperClient.lpush(TIAN_DOU_ALL_WINNER, loginName + "+" + winnerPrize);

        String prizeWinner = loginName + "+" + userName + "+" + userModel.getMobile() + "+" + identityNumber + "+" + dateTime;
        redisWrapperClient.lpush(TIAN_DOU_PRIZE_WINNER + prize, prizeWinner);

        redisWrapperClient.sadd(TIAN_DOU_DRAW_USER_SET, loginName);

        if (TianDouPrize.Cash20 == prize) {
            sendCash20(loginName);
        } else if (TianDouPrize.InterestCoupon5 == prize) {
            sendInterestCoupon5(loginName);
        }

        drawLotteryDto.setTianDouPrize(prize);
        drawLotteryDto.setReturnCode(0);
        drawLotteryDto.setStatus(true);
        baseDto.setSuccess(true);
        return baseDto;
    }

    //1: MacBook, 2:iphone6s, 3:300京东卡, 4:现金20元  5:0.5%加息券
    private TianDouPrize getPrize(String loginName) {

        redisWrapperClient.zincrby(TIAN_DOU_USER_SCORE_RANK, -DRAW_SCORE, loginName);

        if (redisWrapperClient.zscore(TIAN_DOU_USER_SCORE_RANK, loginName) < 0) {
            logger.error(loginName + " does not enough tianDou. User might be using multiple thread for drawing.");
            return null;
        }

        long drawIndex = redisWrapperClient.incr(TIAN_DOU_DRAW_COUNTER);

//        if ((drawIndex + 16) % 1000 == 0) { // 千分之一：984, 1984, 2984, 3984, 4984....
//            return TianDouPrize.Iphone6s;
//        } else if (drawIndex > 2000 && (drawIndex - 13) % 2000 == 0) { // 两千分之一：2013,4013,6013....
//            return TianDouPrize.MacBook;
//        } else {
        int random = (int) (Math.random() * 100000000);
        int mod = random % 100;
        if (mod == 0) { // 1/100 的概率
            return TianDouPrize.JingDong300;
        } else if (mod >= 1 && mod <= 20) { // 1/5 的概率
            return TianDouPrize.Cash20;
        } else {
            return TianDouPrize.InterestCoupon5;
        }
//        }
    }

    private void sendCash20(String loginName) {
        long orderId = idGenerator.generate();
        TransferCashDto transferCashDto = new TransferCashDto(loginName, String.valueOf(orderId), "2000");
        payWrapperClient.transferCash(transferCashDto);
    }

    private void sendInterestCoupon5(String loginName) {
        couponAssignmentService.assignUserCoupon(loginName, 300L);
    }

    // reutrn null if not exists
    @Override
    public Long getUserRank(String loginName) {
        if (loginName == null)
            return null;
        Long rank = redisWrapperClient.zrevrank(TIAN_DOU_USER_SCORE_RANK, loginName);
        return rank == null ? null : rank + 1;
    }

    @Override
    public List<UserScoreDto> getTianDouTop15(String loginName) {
        List<UserScoreDto> userScoreDtoTop15 = Lists.newArrayList();

        Set<Tuple> top15 = redisWrapperClient.zrevrangeWithScores(TIAN_DOU_USER_SCORE_RANK, 0, 14);
        for (Tuple tuple : top15) {
            userScoreDtoTop15.add(new UserScoreDto(randomUtils.encryptMobileForCurrentLoginName(loginName, tuple.getElement(), null, Source.WEB), (long) tuple.getScore()));
        }

        //TODO:fake
        LoanModel loanModel = loanMapper.findById(41650602422768L);
        if (loanModel.getStatus() == LoanStatus.REPAYING) {
            userScoreDtoTop15 = Lists.newArrayList(new UserScoreDto("186****9367", new BigDecimal((double) loanModel.getLoanAmount() * loanModel.getPeriods() / 1200).setScale(0, BigDecimal.ROUND_HALF_UP).longValue()));
            Set<Tuple> top14 = redisWrapperClient.zrevrangeWithScores(TIAN_DOU_USER_SCORE_RANK, 0, 13);
            for (Tuple tuple : top14) {
                userScoreDtoTop15.add(new UserScoreDto(randomUtils.encryptMobileForCurrentLoginName(loginName, tuple.getElement(), null, Source.WEB), (long) tuple.getScore()));
            }
        }

        return userScoreDtoTop15;
    }

    // 2 MacBook + 4 iPhone + others
    @Override
    public Map<String, List<UserTianDouRecordDto>> getTianDouWinnerList() {

        Map<String, List<UserTianDouRecordDto>> map = new HashMap<>();

        List<String> macBookWinner = redisWrapperClient.lrange(TIAN_DOU_PRIZE_WINNER + TianDouPrize.MacBook, 0, 1);
        List<String> iphoneWinner = redisWrapperClient.lrange(TIAN_DOU_PRIZE_WINNER + TianDouPrize.Iphone6s, 0, 3);
        List<String> otherWinner = redisWrapperClient.lrange(TIAN_DOU_ALL_WINNER, 0, 17);

        List<UserTianDouRecordDto> macBookWinnerBeans = Lists.transform(macBookWinner, new Function<String, UserTianDouRecordDto>() {
            @Override
            public UserTianDouRecordDto apply(String input) {
                String loginName = input.split("\\+")[0];
                return new UserTianDouRecordDto(randomUtils.encryptMobileForCurrentLoginName(null, loginName, null, Source.WEB), "抽奖", TianDouPrize.MacBook);
            }
        });

        List<UserTianDouRecordDto> iphoneWinnerBeans = Lists.transform(iphoneWinner, new Function<String, UserTianDouRecordDto>() {
            @Override
            public UserTianDouRecordDto apply(String input) {
                String loginName = input.split("\\+")[0];
                return new UserTianDouRecordDto(randomUtils.encryptMobileForCurrentLoginName(null, loginName, null, Source.WEB), "抽奖", TianDouPrize.Iphone6s);
            }
        });

        for (int i = otherWinner.size() - 1; i >= 0; i--) {
            String winnerPrize = otherWinner.get(i);
            String prize = winnerPrize.split("\\+")[1];
            if (TianDouPrize.MacBook.toString().equals(prize) || TianDouPrize.Iphone6s.toString().equals(prize)) {
                otherWinner.remove(i);
            }
        }

        int otherWinnerSize = 15 - macBookWinner.size() - iphoneWinner.size();
        if (otherWinner.size() > otherWinnerSize) {
            otherWinner = otherWinner.subList(0, otherWinnerSize);
        }

        List<UserTianDouRecordDto> otherWinnerBeans = Lists.transform(otherWinner, new Function<String, UserTianDouRecordDto>() {
            @Override
            public UserTianDouRecordDto apply(String input) {
                String loginName = input.split("\\+")[0];
                String prize = input.split("\\+")[1];
                return new UserTianDouRecordDto(randomUtils.encryptMobileForCurrentLoginName(null, loginName, null, Source.WEB), "抽奖", TianDouPrize.valueOf(prize));
            }
        });

        map.put("MacBook", macBookWinnerBeans);
        map.put("iPhone", iphoneWinnerBeans);
        map.put("other", otherWinnerBeans);

        return map;
    }

    @Override
    public List<UserTianDouRecordDto> getPrizeByLoginName(final String loginName) {
        List<String> userPrize = redisWrapperClient.lrange(TIAN_DOU_WINNER_PRIZE + loginName, 0, -1);

        List<UserTianDouRecordDto> userTianDouRecordDtoList = Lists.transform(userPrize, new Function<String, UserTianDouRecordDto>() {
            @Override
            public UserTianDouRecordDto apply(String input) {
                String prize = input.split("\\+")[0];
                String time = input.split("\\+")[1].replace("_", " ");
                return new UserTianDouRecordDto(loginName, "", TianDouPrize.valueOf(prize), time);
            }
        });

        return userTianDouRecordDtoList;
    }

    /**
     * Return user's tianDou score.
     * If the user does not exist in the sorted set, return null value.
     */
    @Override
    public Double getUserScoreByLoginName(String loginName) {
        if (loginName == null)
            return 0.0;
        Double score = redisWrapperClient.zscore(TIAN_DOU_USER_SCORE_RANK, loginName);
        return score;
    }

    @Override
    public long getTotalTiandouByLoginName(String loginName) {
        List<String> tianDouRecords = redisWrapperClient.lrange(TIAN_DOU_INVEST_SCORE_RECORD + loginName, 0, -1);
        long totalScore = 0;
        for (String record : tianDouRecords) {
            String score = record.split("\\+")[1];
            totalScore += Integer.parseInt(score);
        }
        return totalScore;
    }

    @Override
    public List<UserTianDouRecordDto> getTianDouRecordsByLoginName(final String loginName) {

        List<UserTianDouRecordDto> recordDtoList = new ArrayList<>();

        List<String> investRecords = redisWrapperClient.lrange(TIAN_DOU_INVEST_SCORE_RECORD + loginName, 0, -1);
        List<UserTianDouRecordDto> investRecordDtoList = Lists.transform(investRecords, new Function<String, UserTianDouRecordDto>() {
            @Override
            public UserTianDouRecordDto apply(String input) {
                // amount+score+desc+time
                String[] ss = input.split("\\+");
                return new UserTianDouRecordDto(loginName, "投资", Long.parseLong(ss[0]), Long.parseLong(ss[1]), ss[2], ss[3]);
            }
        });

        List<String> prizes = redisWrapperClient.lrange(TIAN_DOU_WINNER_PRIZE + loginName, 0, -1);
        List<UserTianDouRecordDto> prizeDtoList = Lists.transform(prizes, new Function<String, UserTianDouRecordDto>() {
            @Override
            public UserTianDouRecordDto apply(String input) {
                // prize+time(yyyy-MM-dd HH:mm:ss)
                String[] ss = input.split("\\+");
                return new UserTianDouRecordDto(loginName, "抽奖", TianDouPrize.valueOf(ss[0]), ss[1]);
            }
        });

        recordDtoList.addAll(investRecordDtoList);
        recordDtoList.addAll(prizeDtoList);
        Collections.sort(recordDtoList);

        return recordDtoList;
    }

    @Override
    public long getDrawCount() {
        String count = redisWrapperClient.get(TIAN_DOU_DRAW_COUNTER);
        return count == null ? 0 : Long.parseLong(count);
    }

    @Override
    public long getDrawUserCount() {
        Long count = redisWrapperClient.scard(TIAN_DOU_DRAW_USER_SET);
        return count;
    }

    @Override
    public long getPrizeWinnerCount(TianDouPrize prize) {
        Long count = redisWrapperClient.llen(TIAN_DOU_PRIZE_WINNER + prize.toString());
        return count;
    }

    @Override
    public List<PrizeWinnerDto> getPrizeWinnerList(String prize) {
        List<String> prizeWinnerList = redisWrapperClient.lrange(TIAN_DOU_PRIZE_WINNER + prize, 0, -1);

        List<PrizeWinnerDto> prizeWinnerDtoList = new ArrayList<>();
        for (String s : prizeWinnerList) {
            // loginName+realName+mobile+identityNumber+time
            String[] ss = s.split("\\+");
            prizeWinnerDtoList.add(new PrizeWinnerDto(ss[0], ss[1], ss[2], ss[3], ss[4]));
        }
        return prizeWinnerDtoList;
    }

    @Override
    public long getTotalInvestAmountInActivityPeriod() {
        Date startTime = new DateTime(2016, 4, 1, 0, 0, 0).toDate(); // from 2016-04-01 00:00:00
        Date endTime = new DateTime(2016, 8, 1, 0, 0, 0).toDate(); // to 2016-08-01 00:00:00
        long totalAmount = investMapper.sumInvestAmountRanking(startTime, endTime);

        //TODO:fake
        LoanModel loanModel = loanMapper.findById(41650602422768L);
        if (loanModel.getStatus() == LoanStatus.REPAYING) {
            totalAmount += loanModel.getLoanAmount();
        }
        return totalAmount;
    }

}
