package com.tuotiansudai.service;


import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.MysteriousPrizeDto;
import com.tuotiansudai.repository.model.GivenMembership;
import com.tuotiansudai.repository.model.HeroRankingView;

import java.util.Date;
import java.util.List;

public interface HeroRankingService {

    List<HeroRankingView> obtainHeroRankingReferrer(Date tradingTime);

    void saveMysteriousPrize(MysteriousPrizeDto MysteriousPrizeDto);

    GivenMembership receiveMembership(String loginName);
}
