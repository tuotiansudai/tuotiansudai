package com.tuotiansudai.service;


import com.tuotiansudai.dto.InvestAchievementDto;

import java.util.List;

public interface InvestAchievementService {

    long findInvestAchievementManageCount(String loginName);

    List<InvestAchievementDto> findInvestAchievementManage(int index, int pageSize, String loginName);

}
