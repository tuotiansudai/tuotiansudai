package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.repository.model.InvestModel;
import org.springframework.transaction.annotation.Transactional;

public interface InvestAchievementService {

    void awardAchievement(InvestModel investModel);

}
