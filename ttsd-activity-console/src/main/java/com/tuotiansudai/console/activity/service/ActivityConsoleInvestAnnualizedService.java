package com.tuotiansudai.console.activity.service;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.activity.repository.model.ActivityInvestAnnualized;
import com.tuotiansudai.activity.repository.model.ActivityInvestAnnualizedView;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.model.InvestProductTypeView;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ActivityConsoleInvestAnnualizedService {

    @Autowired
    private InvestMapper investMapper;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.new.year.startTime}\")}")
    private Date activityNewYearStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.new.year.endTime}\")}")
    private Date activityNewYearEndTime;

    public BasePaginationDataDto<ActivityInvestAnnualizedView> list(ActivityInvestAnnualized activityInvestAnnualized, String mobile, Date startTime, Date endTime, int index, int pageSize) {

        List<ActivityInvestAnnualizedView> activityInvestAnnualizedViews = getActivityInvestList(activityInvestAnnualized, mobile, startTime, endTime);
        int count = activityInvestAnnualizedViews.size();
        int endIndex = pageSize * index;
        int startIndex = (index - 1) * 10;
        if (count <= endIndex) {
            endIndex = count;
        }
        if (count < startIndex) {
            startIndex = count;
        }
        BasePaginationDataDto basePaginationDataDto = new BasePaginationDataDto(index, pageSize, count, activityInvestAnnualizedViews.subList(startIndex, endIndex));
        return basePaginationDataDto;
    }

    public List<ActivityInvestAnnualizedView> getActivityInvestList(ActivityInvestAnnualized activityInvestAnnualized, String mobile, Date startTime, Date endTime) {
        List<Date> searchTime = getSearchTime(activityInvestAnnualized, startTime, endTime);
        List<InvestProductTypeView> investProductTypeViews = investMapper.findAmountOrderByNameAndProductType(searchTime.get(0), searchTime.get(1), activityInvestAnnualized.getActivityDesc(), mobile);

        Map<String, ActivityInvestAnnualizedView> map = new HashMap<>();
        for (InvestProductTypeView investProductTypeView : investProductTypeViews) {
            ActivityInvestAnnualizedView activityInvestAnnualizedView = map.get(investProductTypeView.getLoginName());
            map.put(investProductTypeView.getLoginName(),
                    activityInvestAnnualizedView == null ?
                            new ActivityInvestAnnualizedView(
                                    investProductTypeView.getUserName(),
                                    investProductTypeView.getLoginName(),
                                    investProductTypeView.getMobile(),
                                    investProductTypeView.getSumAmount(),
                                    investProductTypeView.getSumAmount() * investProductTypeView.getProductType().getDuration() / 360) :
                            new ActivityInvestAnnualizedView(
                                    investProductTypeView.getUserName(),
                                    investProductTypeView.getLoginName(),
                                    investProductTypeView.getMobile(),
                                    activityInvestAnnualizedView.getInvestAmount() + investProductTypeView.getSumAmount(),
                                    activityInvestAnnualizedView.getAnnualizedAmount() + investProductTypeView.getSumAmount() * investProductTypeView.getProductType().getDuration() / 360));
        }
        return new ArrayList<>(map.values());
    }

    public List<Date> getSearchTime(ActivityInvestAnnualized activityInvestAnnualized, Date startTime, Date endTime) {
        List<Date> timeList = Maps.newHashMap(ImmutableMap.<ActivityInvestAnnualized, List<Date>>builder()
                .put(ActivityInvestAnnualized.NEW_YEAR_ACTIVITY, Lists.newArrayList(activityNewYearStartTime, activityNewYearEndTime))
                .build()).get(activityInvestAnnualized);
        if (startTime != null && startTime.after(timeList.get(0))) {
            timeList.set(0, new DateTime(startTime).withTimeAtStartOfDay().toDate());
        }
        if (endTime != null && endTime.before(timeList.get(1))) {
            timeList.set(1, new DateTime(endTime).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate());
        }
        return timeList;
    }
}
