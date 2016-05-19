package com.tuotiansudai.console.service;


import com.tuotiansudai.repository.model.InvestAchievement;
import com.tuotiansudai.repository.model.LoanAchievementView;

import java.util.List;

public interface InvestAchievementService {

    long findInvestAchievementManageCount(String loginName, InvestAchievement investAchievement);

    List<LoanAchievementView> findInvestAchievementManage(int index, int pageSize, String loginName, InvestAchievement investAchievement);

}
