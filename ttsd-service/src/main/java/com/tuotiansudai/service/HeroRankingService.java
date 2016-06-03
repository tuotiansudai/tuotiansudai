package com.tuotiansudai.service;

import com.tuotiansudai.dto.BaseListDataDto;

import java.util.Date;

public interface HeroRankingService {

    BaseListDataDto findHeroRankingByReferrer(Date tradingTime, String loginName);

}
