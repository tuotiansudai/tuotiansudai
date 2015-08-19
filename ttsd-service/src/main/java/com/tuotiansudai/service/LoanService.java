package com.tuotiansudai.service;

import com.tuotiansudai.repository.model.ActivityType;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.LoanType;

import java.util.List;

public interface LoanService {
    List<LoanType> getLoanType();
    List<ActivityType> getActivityType();
}
