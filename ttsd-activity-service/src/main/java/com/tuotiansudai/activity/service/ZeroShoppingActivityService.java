package com.tuotiansudai.activity.service;

import com.tuotiansudai.activity.repository.mapper.ZeroShoppingPrizeConfigMapper;
import com.tuotiansudai.activity.repository.model.ZeroShoppingPrize;
import com.tuotiansudai.activity.repository.model.ZeroShoppingPrizeConfigModel;
import com.tuotiansudai.repository.mapper.LoanDetailsMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.LoanStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ZeroShoppingActivityService {

    @Autowired
    private ZeroShoppingPrizeConfigMapper zeroShoppingMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.zero.shopping.startTime}\")}")
    private Date activityZeroShoppingStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.zero.shopping.endTime}\")}")
    private Date activityZeroShoppingEndTime;

    public List<ZeroShoppingPrizeConfigModel> getAllPrize(){
        return zeroShoppingMapper.findAll();
    }

    public LoanModel queryActivityLoan(){
        return loanMapper.findZeroShoppingActivityByTime(activityZeroShoppingStartTime, activityZeroShoppingEndTime);
    }

    public int queryPrizeSurplus(ZeroShoppingPrize zeroShoppingPrize){
        return zeroShoppingMapper.prizeSurplus(zeroShoppingPrize);
    }
}
