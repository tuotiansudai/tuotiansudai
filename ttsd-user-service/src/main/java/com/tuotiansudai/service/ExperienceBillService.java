package com.tuotiansudai.service;

import com.tuotiansudai.enums.ExperienceBillBusinessType;
import com.tuotiansudai.enums.ExperienceBillOperationType;

public interface ExperienceBillService {

    void updateUserExperienceBalanceByLoginName(long experienceAmount, String loginName, ExperienceBillOperationType experienceBillOperationType, ExperienceBillBusinessType experienceBusinessType);
}
