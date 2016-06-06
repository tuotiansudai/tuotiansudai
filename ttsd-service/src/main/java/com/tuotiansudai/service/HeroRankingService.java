package com.tuotiansudai.service;

import com.tuotiansudai.repository.model.HeroRankingView;

import java.util.Date;
import java.util.List;

public interface HeroRankingService {

    List<HeroRankingView> obtainHeroRanking(Date tradingTime);

    Integer obtainHeroRankingByLoginName(Date tradingTime, String loginName);
}
