package com.tuotiansudai.service;


import com.tuotiansudai.dto.MysteriousPrizeDto;
import com.tuotiansudai.repository.model.ActivityCategory;
import com.tuotiansudai.repository.model.HeroRankingView;

import java.util.Date;
import java.util.List;

public interface HeroRankingService {

    List<HeroRankingView> obtainHeroRanking(ActivityCategory activityCategory,Date tradingTime);

    List<HeroRankingView> obtainHeroRankingReferrer(ActivityCategory activityCategory,Date tradingTime);

    void saveMysteriousPrize(MysteriousPrizeDto MysteriousPrizeDto);

    MysteriousPrizeDto obtainMysteriousPrizeDto(String prizeDate);

}
