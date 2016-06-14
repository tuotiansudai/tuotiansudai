package com.tuotiansudai.console.service;


import com.tuotiansudai.repository.model.LoanAchievementView;

import java.util.List;

public interface InvestAchievementService {

    long findInvestAchievementCount(String loginName);

    List<LoanAchievementView> findInvestAchievement(int index, int pageSize, String loginName);

}
