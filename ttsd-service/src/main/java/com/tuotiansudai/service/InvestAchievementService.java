package com.tuotiansudai.service;

import com.tuotiansudai.repository.model.InvestModel;
import org.springframework.transaction.annotation.Transactional;

public interface InvestAchievementService {
    @Transactional
    void awardAchievement(InvestModel investModel);
}
