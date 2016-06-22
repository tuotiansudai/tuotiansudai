package com.tuotiansudai.service;


import com.tuotiansudai.dto.BaseListDataDto;
import com.tuotiansudai.dto.MysteriousPrizeDto;
import com.tuotiansudai.membership.repository.model.GivenMembership;
import com.tuotiansudai.repository.model.HeroRankingView;

import java.util.Date;
import java.util.List;

public interface HeroRankingService {

    BaseListDataDto findHeroRankingByReferrer(Date tradingTime, String loginName, int index, int pageSize);

    Integer findHeroRankingByReferrerLoginName(String loginName);

    List<HeroRankingView> obtainHeroRanking(Date tradingTime);

    List<HeroRankingView> obtainHeroRankingReferrer(Date tradingTime);

    Integer obtainHeroRankingByLoginName(Date tradingTime, String loginName);

    void saveMysteriousPrize(MysteriousPrizeDto MysteriousPrizeDto);

    MysteriousPrizeDto obtainMysteriousPrizeDto(String prizeDate);

    GivenMembership receiveMembership(String loginName);

}
