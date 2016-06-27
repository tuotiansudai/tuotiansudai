package com.tuotiansudai.service.impl;

import com.tuotiansudai.dto.ActivityDto;
import com.tuotiansudai.repository.mapper.ActivityMapper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.model.ActivityModel;
import com.tuotiansudai.repository.model.ActivityStatus;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.service.ActivityService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class ActivityServiceImpl implements ActivityService {

    static Logger logger = Logger.getLogger(ActivityServiceImpl.class);

    @Autowired
    ActivityMapper activityMapper;

    @Autowired
    InvestMapper investMapper;

    public List<ActivityDto> getAllOperatingActivities(String loginName, Source source) {
        final List<ActivityDto> activityDtos = new ArrayList<>();

        List<ActivityModel> activityModels;
        if (Source.MOBILE == source) {
            activityModels = activityMapper.findActivities(null, null, ActivityStatus.OPERATING, Source.IOS);
            activityModels.addAll(activityMapper.findActivities(null, null, ActivityStatus.OPERATING, Source.ANDROID));
        } else {
            activityModels = activityMapper.findActivities(null, null, ActivityStatus.OPERATING, source);
        }

        Collections.sort(activityModels, new Comparator<ActivityModel>() {
            @Override
            public int compare(ActivityModel o1, ActivityModel o2) {
                if (o1.getActivatedTime().equals(o2.getActivatedTime())) {
                    return 0;
                } else if (o1.getActivatedTime().after(o2.getActivatedTime())) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });

        if (StringUtils.isEmpty(loginName) || 0 == investMapper.findCountByLoginName(loginName)) {
            List<ActivityModel> newbieActivities = new ArrayList<>();
            List<ActivityModel> normalActivities = new ArrayList<>();
            for (ActivityModel activityModel : activityModels) {
                if (activityModel.getTitle().contains("新手")) {
                    newbieActivities.add(activityModel);
                } else {
                    normalActivities.add(activityModel);
                }
            }
            newbieActivities.addAll(normalActivities);
            activityModels.clear();
            activityModels.addAll(newbieActivities);
        }

        for (ActivityModel activityModel : activityModels) {
            activityDtos.add(new ActivityDto(activityModel));
        }

        return activityDtos;
    }
}
