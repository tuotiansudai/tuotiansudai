package com.tuotiansudai.service.impl;

import com.tuotiansudai.repository.model.ActivityType;
import com.tuotiansudai.repository.model.LoanType;
import com.tuotiansudai.service.LoanService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LoanServiceImpl implements LoanService{

    @Override
    public List<LoanType> getLoanType() {
        List<LoanType> loanTypes = new ArrayList<LoanType>();
        for (LoanType loanType:LoanType.values()){
            loanTypes.add(loanType);
        }
        return loanTypes;
    }

    @Override
    public List<ActivityType> getActivityType() {
        List<ActivityType> activityTypes = new ArrayList<ActivityType>();
        for (ActivityType activityType:ActivityType.values()){
            activityTypes.add(activityType);
        }
        return activityTypes;
    }
}
