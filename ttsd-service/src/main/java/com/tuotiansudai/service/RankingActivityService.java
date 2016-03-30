package com.tuotiansudai.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.ranking.DrawLotteryDto;
import com.tuotiansudai.dto.ranking.PrizeWinnerDto;
import com.tuotiansudai.dto.ranking.UserScoreDto;
import com.tuotiansudai.dto.ranking.UserTianDouRecordDto;
import com.tuotiansudai.repository.TianDouPrize;

import java.util.List;
import java.util.Map;

public interface RankingActivityService {

    BaseDto<DrawLotteryDto> drawTianDouPrize(String loginName, String mobile);

    Long getUserRank(String loginName);

    List<UserScoreDto> getTianDouTop15();

    Map<String, List<UserTianDouRecordDto>> getTianDouWinnerList();

    List<UserTianDouRecordDto> getPrizeByLoginName(String loginName);

    Double getUserScoreByLoginName(String loginName);

    long getTotalTiandouByLoginName(String loginName);

    List<UserTianDouRecordDto> getTianDouRecordsByLoginName(final String loginName);

    long getDrawCount();

    long getDrawUserCount();

    long getPrizeWinnerCount(TianDouPrize prize);

    List<PrizeWinnerDto> getPrizeWinnerList(String prize);

    long getTotalInvestAmountInActivityPeriod();
}
