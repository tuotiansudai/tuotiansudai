package com.tuotiansudai.activity.service;

import com.tuotiansudai.activity.repository.mapper.ActivityInvestMapper;
import com.tuotiansudai.activity.repository.mapper.UserLotteryPrizeMapper;
import com.tuotiansudai.activity.repository.model.ActivityCategory;
import com.tuotiansudai.activity.repository.model.ActivityInvestView;
import com.tuotiansudai.dto.LoanItemDto;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.LoanModel;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SchoolSeasonService {

    @Autowired
    private UserLotteryPrizeMapper userLotteryPrizeMapper;

    @Autowired
    private ActivityInvestMapper activityInvestMapper;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.school.season.startTime}\")}")
    private Date activitySchoolSeasonStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.school.season.endTime}\")}")
    private Date activitySchoolSeasonEndTime;


    public int toDayIsDrawByMobile(String mobile, ActivityCategory activityCategory) {
        return userLotteryPrizeMapper.findUserLotteryPrizeCountViews(mobile, null, activityCategory,
                DateTime.now().withTimeAtStartOfDay().toDate(), DateTime.now().plusDays(1).withTimeAtStartOfDay().plusMillis(-1).toDate());
    }

    public List<ActivityInvestView> obtainRank(){
        return activityInvestMapper.sumAmountByNameDateAndActivity(ActivityCategory.SCHOOL_SEASON_ACTIVITY.name(), activitySchoolSeasonStartTime, activitySchoolSeasonEndTime);
    }

}
