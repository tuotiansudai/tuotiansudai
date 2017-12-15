package com.tuotiansudai.activity.service;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.activity.repository.dto.DrawLotteryResultDto;
import com.tuotiansudai.activity.repository.mapper.UserLotteryPrizeMapper;
import com.tuotiansudai.activity.repository.model.*;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.MobileEncryptor;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class NationalPrizeService {

    private static Logger logger = Logger.getLogger(NationalPrizeService.class);

    @Autowired
    private UserLotteryPrizeMapper userLotteryPrizeMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private LoanDetailsMapper loanDetailsMapper;

    @Autowired
    private LotteryDrawActivityService lotteryDrawActivityService;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.national.startTime}\")}")
    private Date activityNationalStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.national.endTime}\")}")
    private Date activityNationalEndTime;

    public DrawLotteryResultDto drawLotteryPrize(String mobile){
        return lotteryDrawActivityService.drawPrizeByCompleteTask(mobile, ActivityCategory.NATIONAL_PRIZE);
    }

    public List<UserLotteryPrizeView> findDrawLotteryPrizeRecordByMobile(String mobile){
        if(Strings.isNullOrEmpty(mobile)){
            return Lists.newArrayList();
        }
        return findDrawLotteryPrizeRecord(mobile);
    }

    public List<UserLotteryPrizeView> findDrawLotteryPrizeRecord(String mobile){
        List<UserLotteryPrizeView> userLotteryPrizeViews = userLotteryPrizeMapper.findLotteryPrizeByMobileAndPrize(mobile, null, ActivityCategory.NATIONAL_PRIZE, null);
        for(UserLotteryPrizeView view : userLotteryPrizeViews){
            view.setMobile(MobileEncryptor.encryptMiddleMobile(view.getMobile()));
        }
        return userLotteryPrizeViews;
    }

    public Map getNationalActivityInvestAmountAndCount(){
        List<InvestModel> investModels = investMapper.countSuccessInvestByInvestTime(null, activityNationalStartTime, activityNationalEndTime);
        Map<String,Object> param = Maps.newConcurrentMap();
        long amount = 0l;
        long count = 0l;
        Map<String,String> userMap = Maps.newConcurrentMap();
        for(InvestModel investModel : investModels){
            LoanDetailsModel loanDetailsModel = loanDetailsMapper.getByLoanId(investModel.getLoanId());
            if(loanDetailsModel != null && loanDetailsModel.isActivity()){
                amount += investModel.getAmount();
                if(userMap.get(investModel.getLoginName()) == null){
                    userMap.put(investModel.getLoginName(),investModel.getLoginName());
                    count ++;
                }
            }
        }
        param.put("investAmount",amount);
        param.put("investCount",count);
        return param;
    }

}
