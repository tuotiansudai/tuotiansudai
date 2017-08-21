package com.tuotiansudai.console.activity.service;

import com.tuotiansudai.activity.repository.mapper.ActivityInvestMapper;
import com.tuotiansudai.activity.repository.mapper.SchoolExclusiveMapper;
import com.tuotiansudai.activity.repository.model.ActivityCategory;
import com.tuotiansudai.activity.repository.model.ActivityInvestRewardView;
import com.tuotiansudai.activity.repository.model.ActivityInvestView;
import com.tuotiansudai.activity.repository.model.SchoolSeasonView;
import com.tuotiansudai.dto.BasePaginationDataDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ActivityConsoleSchoolSeasonService {

    @Autowired
    private ActivityInvestMapper activityInvestMapper;

    @Autowired
    private SchoolExclusiveMapper schoolExclusiveMapper;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.school.season.startTime}\")}")
    private Date activitySchoolSeasonStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.school.season.endTime}\")}")
    private Date activitySchoolSeasonEndTime;

    public BasePaginationDataDto<ActivityInvestRewardView> list(int index, int pageSize) {
        List<SchoolSeasonView> schoolSeasonViews = getSchoolSeasonViews();
        int count = schoolSeasonViews.size();
        int endIndex = pageSize * index;
        int startIndex = (index - 1) * 10;
        if (count <= endIndex) {
            endIndex = count;
        }
        if (count < startIndex) {
            startIndex = count;
        }
        BasePaginationDataDto basePaginationDataDto = new BasePaginationDataDto(index, pageSize, count, schoolSeasonViews.subList(startIndex, endIndex));
        return basePaginationDataDto;
    }

    public List<SchoolSeasonView> getSchoolSeasonViews(){
        List<SchoolSeasonView> list= new ArrayList<>();
        List<ActivityInvestView> activityInvestViews = activityInvestMapper.sumAmountByNameDateAndActivity(ActivityCategory.SCHOOL_SEASON_ACTIVITY.name(), activitySchoolSeasonStartTime, activitySchoolSeasonEndTime);
        for (ActivityInvestView activityInvestView: activityInvestViews) {
            list.add(new SchoolSeasonView(activityInvestView,
                    schoolExclusiveMapper.sumJDECardByName(activityInvestView.getLoginName(),activitySchoolSeasonStartTime, activitySchoolSeasonEndTime)));
        }
        return list;
    }
}
