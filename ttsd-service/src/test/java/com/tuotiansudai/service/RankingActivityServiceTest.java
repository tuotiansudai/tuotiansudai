package com.tuotiansudai.service;


import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.activity.repository.dto.DrawLotteryDto;
import com.tuotiansudai.activity.repository.dto.UserTianDouRecordDto;
import com.tuotiansudai.activity.repository.model.TianDouPrize;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import com.tuotiansudai.service.impl.RankingActivityServiceImpl;
import com.tuotiansudai.util.RandomUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class RankingActivityServiceTest {

    private static Logger logger = Logger.getLogger(RankingActivityServiceTest.class);
    @Autowired
    RankingActivityService rankingActivityService;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private RandomUtils randomUtils;

    private void createUserByUserId(String userId) {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName(userId);
        userModelTest.setPassword("123abc");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile("1" + RandomStringUtils.randomNumeric(10));
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(userModelTest);

        AccountModel accountModel = new AccountModel(userId, "payUserId", "payAccountId", new Date());
        accountMapper.create(accountModel);
    }

    @Before
    public void init() {
//        clearRankingDataInRedis();
    }

    @After
    public void cleanRedis() {
        clearRankingDataInRedis();
    }

    @Test
    public void shouldDrawPrizeNotEnoughTianDou() {
        clearRankingDataInRedis();

        String loginName = "ranking_test_1";
        createUserByUserId(loginName);
        BaseDto<DrawLotteryDto> baseDto = rankingActivityService.drawTianDouPrize(loginName);

        assert (!baseDto.isSuccess());
        assert (baseDto.getData().getReturnCode() == 1);
    }

    @Test
    public void shouldDrawPrizeSuccess() {
        clearRankingDataInRedis();

        String loginName = "rankTest";
        createUserByUserId(loginName);
        long investAmount = 30000;
        long tianDouScore = investAmount * 3 / 12; // 3月标，7500
        String loanId = "11111111";

        mockInvestTianDou(loginName, investAmount, tianDouScore, loanId);

        BaseDto<DrawLotteryDto> baseDto = rankingActivityService.drawTianDouPrize(loginName);

        assert (baseDto.isSuccess() == true);
        assert (baseDto.getData().getStatus() == true);
        assert (baseDto.getData().getReturnCode() == 0);

        Double score = rankingActivityService.getUserScoreByLoginName(loginName);
        assert (score == 6500);

        long rank = rankingActivityService.getUserRank(loginName);
        assert (rank == 1);

        long drawCount = rankingActivityService.getDrawCount();
        assert (drawCount == 1);

        long drawUserCount = rankingActivityService.getDrawUserCount();
        assert (drawUserCount == 1);

        List<UserTianDouRecordDto> prizeList = rankingActivityService.getPrizeByLoginName(loginName);
        assert (prizeList != null && prizeList.size() == 1);

//        UserTianDouRecordDto recordDto = prizeList.get(0);
//        assert (recordDto.getPrize() == TianDouPrize.InterestCoupon5);

//        long prizeWinnerCount = rankingActivityService.getPrizeWinnerCount(TianDouPrize.InterestCoupon5);
//        assert (prizeWinnerCount == 1);

//        List<PrizeWinnerDto> prizeWinnerDtoList = rankingActivityService.getPrizeWinnerList(TianDouPrize.InterestCoupon5.toString());
//        assert (prizeWinnerDtoList != null && prizeWinnerDtoList.size() == 1);

//        PrizeWinnerDto winnerDto = prizeWinnerDtoList.get(0);
//        assert (winnerDto.getLoginName().equals(loginName));
//        assert (winnerDto.getMobile().equals(mobile));

        long totalTiandou = rankingActivityService.getTotalTiandouByLoginName(loginName);
        assert (totalTiandou == tianDouScore);

        List<UserTianDouRecordDto> tianDouRecordDtoList = rankingActivityService.getTianDouRecordsByLoginName(loginName);
        assert (tianDouRecordDtoList != null && tianDouRecordDtoList.size() == 2);

        UserTianDouRecordDto userInvestRecord = tianDouRecordDtoList.get(0);
        assert (userInvestRecord.getType().equals("投资"));
        assert (userInvestRecord.getAmount() == investAmount);
        assert (userInvestRecord.getScore() == tianDouScore);
        assert (userInvestRecord.getDesc().equals(loanId));

        UserTianDouRecordDto userDrawRecord = tianDouRecordDtoList.get(1);
        assert (userDrawRecord.getType().equals("抽奖"));
//        assert (userDrawRecord.getPrize() == TianDouPrize.InterestCoupon5);

        Map<String, List<UserTianDouRecordDto>> winnerListMap = rankingActivityService.getTianDouWinnerList();
        List<UserTianDouRecordDto> macBookList = winnerListMap.get("MacBook");
        List<UserTianDouRecordDto> iPhoneList = winnerListMap.get("iPhone");
        List<UserTianDouRecordDto> otherList = winnerListMap.get("other");
        assert (macBookList == null || macBookList.size() == 0);
        assert (iPhoneList == null || iPhoneList.size() == 0);
        assert (otherList != null && otherList.size() == 1);

        UserTianDouRecordDto userTianDouRecordDto = otherList.get(0);
//        assert (userTianDouRecordDto.getPrize() == TianDouPrize.InterestCoupon5);

        assert (userTianDouRecordDto.getLoginName().equals(randomUtils.encryptMobileForCurrentLoginName("", loginName, null, Source.WEB)));
    }

    @Test
    public void shouldDrawPrizeSuccessMultiTimes() {
        clearRankingDataInRedis();

        String loginName1 = "test111";
        String mobile1 = "13900001111";

        String loginName2 = "test222";

        String loginName3 = "test333";
        createUserByUserId("test111");
        createUserByUserId("test222");
        createUserByUserId("test333");
        long investAmount = 30000;
        long tianDouScore = investAmount * 3 / 12; // 3月标，7500
        String loanId = "11111111";

        try {
            mockInvestTianDou(loginName1, investAmount, tianDouScore, loanId);
            Thread.sleep(1000L);
            mockInvestTianDou(loginName2, investAmount, tianDouScore, loanId);
            Thread.sleep(1000L);
            mockInvestTianDou(loginName3, investAmount, tianDouScore, loanId);
            Thread.sleep(1000L);
            mockInvestTianDou(loginName1, investAmount, tianDouScore, loanId);
            Thread.sleep(1000L);
            mockInvestTianDou(loginName1, investAmount, tianDouScore, loanId);
            Thread.sleep(1000L);

            BaseDto<DrawLotteryDto> baseDto1 = rankingActivityService.drawTianDouPrize(loginName1);
            Thread.sleep(1000L);
            BaseDto<DrawLotteryDto> baseDto2 = rankingActivityService.drawTianDouPrize(loginName1);
            Thread.sleep(1000L);
            BaseDto<DrawLotteryDto> baseDto3 = rankingActivityService.drawTianDouPrize(loginName1);
            Thread.sleep(1000L);
            BaseDto<DrawLotteryDto> baseDto4 = rankingActivityService.drawTianDouPrize(loginName1);

            assert (baseDto1.isSuccess() == true);
            assert (baseDto1.getData().getStatus() == true);
            assert (baseDto1.getData().getReturnCode() == 0);

            assert (baseDto2.isSuccess() == true);
            assert (baseDto2.getData().getStatus() == true);
            assert (baseDto2.getData().getReturnCode() == 0);

            assert (baseDto3.isSuccess() == true);
            assert (baseDto3.getData().getStatus() == true);
            assert (baseDto3.getData().getReturnCode() == 0);

            assert (baseDto4.isSuccess() == true);
            assert (baseDto4.getData().getStatus() == true);
            assert (baseDto4.getData().getReturnCode() == 0);

        } catch (InterruptedException e) {
            logger.error("Thread sleep exception. ", e);
            assert (false);
        }

        Double score = rankingActivityService.getUserScoreByLoginName(loginName1);
        assert (score == 7500 * 3 - 4000);

        long rank = rankingActivityService.getUserRank(loginName1);
        assert (rank == 1);

        long drawCount = rankingActivityService.getDrawCount();
        assert (drawCount == 4);

        long drawUserCount = rankingActivityService.getDrawUserCount();
        assert (drawUserCount == 1);

        List<UserTianDouRecordDto> prizeList = rankingActivityService.getPrizeByLoginName(loginName1);
        assert (prizeList != null && prizeList.size() == 4);

        long couponPrizeWinnerCount = rankingActivityService.getPrizeWinnerCount(TianDouPrize.InterestCoupon5);
        long cashPrizeWinnerCount = rankingActivityService.getPrizeWinnerCount(TianDouPrize.Cash20);
        long jingdongPrizeWinnerCount = rankingActivityService.getPrizeWinnerCount(TianDouPrize.JingDong300);

        assert (couponPrizeWinnerCount + cashPrizeWinnerCount + jingdongPrizeWinnerCount == 4);

        long totalTiandou = rankingActivityService.getTotalTiandouByLoginName(loginName1);
        assert (totalTiandou == tianDouScore * 3); // 投资3次

        List<UserTianDouRecordDto> tianDouRecordDtoList = rankingActivityService.getTianDouRecordsByLoginName(loginName1);
        assert (tianDouRecordDtoList != null && tianDouRecordDtoList.size() == 7);

        UserTianDouRecordDto userInvestRecord1_0 = tianDouRecordDtoList.get(0);
        assert (userInvestRecord1_0.getType().equals("投资"));
        assert (userInvestRecord1_0.getAmount() == investAmount);
        assert (userInvestRecord1_0.getScore() == tianDouScore);
        assert (userInvestRecord1_0.getDesc().equals(loanId));

        UserTianDouRecordDto userInvestRecord1_1 = tianDouRecordDtoList.get(1);
        assert (userInvestRecord1_1.getType().equals("投资"));
        assert (userInvestRecord1_1.getAmount() == investAmount);
        assert (userInvestRecord1_1.getScore() == tianDouScore);
        assert (userInvestRecord1_1.getDesc().equals(loanId));

        UserTianDouRecordDto userInvestRecord1_2 = tianDouRecordDtoList.get(2);
        assert (userInvestRecord1_2.getType().equals("投资"));
        assert (userInvestRecord1_2.getAmount() == investAmount);
        assert (userInvestRecord1_2.getScore() == tianDouScore);
        assert (userInvestRecord1_2.getDesc().equals(loanId));

        UserTianDouRecordDto userDrawRecord1_0 = tianDouRecordDtoList.get(3);
        assert (userDrawRecord1_0.getType().equals("抽奖"));

        UserTianDouRecordDto userDrawRecord1_1 = tianDouRecordDtoList.get(4);
        assert (userDrawRecord1_1.getType().equals("抽奖"));

        UserTianDouRecordDto userDrawRecord1_2 = tianDouRecordDtoList.get(5);
        assert (userDrawRecord1_2.getType().equals("抽奖"));

        UserTianDouRecordDto userDrawRecord1_3 = tianDouRecordDtoList.get(6);
        assert (userDrawRecord1_3.getType().equals("抽奖"));

        Map<String, List<UserTianDouRecordDto>> winnerListMap = rankingActivityService.getTianDouWinnerList();
        List<UserTianDouRecordDto> macBookList = winnerListMap.get("MacBook");
        List<UserTianDouRecordDto> iPhoneList = winnerListMap.get("iPhone");
        List<UserTianDouRecordDto> otherList = winnerListMap.get("other");
        assert (macBookList == null || macBookList.size() == 0);
        assert (iPhoneList == null || iPhoneList.size() == 0);
        assert (otherList != null && otherList.size() == 4);

        UserTianDouRecordDto userTianDouRecordDto1_0 = otherList.get(0);
        assert (userTianDouRecordDto1_0.getLoginName().equals(randomUtils.encryptMobileForCurrentLoginName("", loginName1, null, Source.WEB)));

        UserTianDouRecordDto userTianDouRecordDto1_1 = otherList.get(1);
        assert (userTianDouRecordDto1_1.getLoginName().equals(randomUtils.encryptMobileForCurrentLoginName("", loginName1, null, Source.WEB)));

        UserTianDouRecordDto userTianDouRecordDto1_2 = otherList.get(2);
        assert (userTianDouRecordDto1_2.getLoginName().equals(randomUtils.encryptMobileForCurrentLoginName("", loginName1, null, Source.WEB)));

        UserTianDouRecordDto userTianDouRecordDto1_3 = otherList.get(3);
        assert (userTianDouRecordDto1_3.getLoginName().equals(randomUtils.encryptMobileForCurrentLoginName("", loginName1, null, Source.WEB)));
    }

//    @Test
//    public void makeTestData() {
//
//        String loginName2 = "sidneygao";
//        String mobile2 = "13900002222";
//
//        String loginName3 = "baoxin";
//        String mobile3 = "13900003333";
//
//
//        long investAmount = 30000;
//        long tianDouScore = investAmount * 3 / 12; // 3月标，7500
//        String loanId = "11111111";
//
//        try {
//
//            BaseDto<DrawLotteryDto> baseDto1 = rankingActivityService.drawTianDouPrize(loginName2, mobile2);//加息券
//            Thread.sleep(1000L);
//            BaseDto<DrawLotteryDto> baseDto2 = rankingActivityService.drawTianDouPrize(loginName2, mobile2);//20元现金
//            Thread.sleep(1000L);
//            BaseDto<DrawLotteryDto> baseDto3 = rankingActivityService.drawTianDouPrize(loginName2, mobile2);//加息券
//            Thread.sleep(1000L);
//            BaseDto<DrawLotteryDto> baseDto4 = rankingActivityService.drawTianDouPrize(loginName2, mobile2);//加息券
//            Thread.sleep(1000L);
//            rankingActivityService.drawTianDouPrize(loginName2, mobile2);//加息券
//            Thread.sleep(1000L);
//            rankingActivityService.drawTianDouPrize(loginName2, mobile2);//加息券
//            Thread.sleep(1000L);
//            rankingActivityService.drawTianDouPrize(loginName2, mobile2);//加息券
//
//
//            Thread.sleep(1000L);
//            rankingActivityService.drawTianDouPrize(loginName3, mobile3);//加息券
//            Thread.sleep(1000L);
//            rankingActivityService.drawTianDouPrize(loginName3, mobile3);//加息券
//            Thread.sleep(1000L);
//            rankingActivityService.drawTianDouPrize(loginName3, mobile3);//加息券
//            Thread.sleep(1000L);
//            rankingActivityService.drawTianDouPrize(loginName3, mobile3);//加息券
//            Thread.sleep(1000L);
//            BaseDto<DrawLotteryDto> baseDto5 = rankingActivityService.drawTianDouPrize(loginName3, mobile3);//加息券
//            Thread.sleep(1000L);
//            BaseDto<DrawLotteryDto> baseDto6 = rankingActivityService.drawTianDouPrize(loginName3, mobile3);//加息券
//            Thread.sleep(1000L);
//            BaseDto<DrawLotteryDto> baseDto7 = rankingActivityService.drawTianDouPrize(loginName3, mobile3);//加息券
//            Thread.sleep(1000L);
//
//            assert (baseDto1.isSuccess() == true);
//            assert (baseDto1.getData().getStatus() == true);
//            assert (baseDto1.getData().getReturnCode() == 0);
//
//            assert (baseDto2.isSuccess() == true);
//            assert (baseDto2.getData().getStatus() == true);
//            assert (baseDto2.getData().getReturnCode() == 0);
//
//            assert (baseDto3.isSuccess() == true);
//            assert (baseDto3.getData().getStatus() == true);
//            assert (baseDto3.getData().getReturnCode() == 0);
//
//            assert (baseDto4.isSuccess() == true);
//            assert (baseDto4.getData().getStatus() == true);
//            assert (baseDto4.getData().getReturnCode() == 0);
//
//            assert (baseDto5.isSuccess() == true);
//            assert (baseDto5.getData().getStatus() == true);
//            assert (baseDto5.getData().getReturnCode() == 0);
//
//            assert (baseDto6.isSuccess() == true);
//            assert (baseDto6.getData().getStatus() == true);
//            assert (baseDto6.getData().getReturnCode() == 0);
//
//            assert (baseDto7.isSuccess() == true);
//            assert (baseDto7.getData().getStatus() == true);
//            assert (baseDto7.getData().getReturnCode() == 0);
//
//        } catch (InterruptedException e) {
//            logger.error("Thread sleep exception. ", e);
//            assert (false);
//        }
//    }

    private void mockInvestTianDou(String loginName, long amount, long tianDouScore, String loanId) {

        String time = DateFormatUtils.format(new Date(), "yyyy-MM-dd_HH:mm:ss");

        String value = amount + "+" + tianDouScore + "+" + loanId + "+" + time;
        redisWrapperClient.lpush(RankingActivityServiceImpl.TIAN_DOU_INVEST_SCORE_RECORD + loginName, value);
        redisWrapperClient.zincrby(RankingActivityServiceImpl.TIAN_DOU_USER_SCORE_RANK, tianDouScore, loginName);
    }

    private void clearRankingDataInRedis() {
        String[] keys = new String[]{RankingActivityServiceImpl.TIAN_DOU_DRAW_COUNTER,
                RankingActivityServiceImpl.TIAN_DOU_DRAW_USER_SET,
                RankingActivityServiceImpl.TIAN_DOU_ALL_WINNER,
                RankingActivityServiceImpl.TIAN_DOU_USER_SCORE_RANK};
        redisWrapperClient.del(keys);
        redisWrapperClient.delPattern(RankingActivityServiceImpl.TIAN_DOU_WINNER_PRIZE + "*");
        redisWrapperClient.delPattern(RankingActivityServiceImpl.TIAN_DOU_PRIZE_WINNER + "*");
        redisWrapperClient.delPattern(RankingActivityServiceImpl.TIAN_DOU_INVEST_SCORE_RECORD + "*");
    }
}
