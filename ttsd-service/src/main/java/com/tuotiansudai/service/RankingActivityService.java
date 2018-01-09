package com.tuotiansudai.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.activity.repository.dto.DrawLotteryDto;
import com.tuotiansudai.activity.repository.dto.PrizeWinnerDto;
import com.tuotiansudai.activity.repository.dto.UserScoreDto;
import com.tuotiansudai.activity.repository.dto.UserTianDouRecordDto;
import com.tuotiansudai.activity.repository.model.TianDouPrize;

import java.util.List;
import java.util.Map;

public interface RankingActivityService {

    BaseDto<DrawLotteryDto> drawTianDouPrize(String loginName);

    Long getUserRank(String loginName);

    Map<String, List<UserTianDouRecordDto>> getTianDouWinnerList();

    List<UserTianDouRecordDto> getPrizeByLoginName(String loginName);

    Double getUserScoreByLoginName(String loginName);

    long getTotalTiandouByLoginName(String loginName);

    List<UserTianDouRecordDto> getTianDouRecordsByLoginName(final String loginName);

    long getDrawCount();

    long getDrawUserCount();

    long getPrizeWinnerCount(TianDouPrize prize);

    List<PrizeWinnerDto> getPrizeWinnerList(String prize);
}
