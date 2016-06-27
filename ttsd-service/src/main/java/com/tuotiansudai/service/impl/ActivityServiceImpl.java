package com.tuotiansudai.service.impl;

import com.tuotiansudai.dto.ActivityDto;
import com.tuotiansudai.repository.mapper.ActivityMapper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.model.ActivityModel;
import com.tuotiansudai.repository.model.ActivityStatus;
import com.tuotiansudai.service.ActivityService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class ActivityServiceImpl implements ActivityService {

    static Logger logger = Logger.getLogger(ActivityServiceImpl.class);

    @Autowired
    ActivityMapper activityMapper;

    @Autowired
    InvestMapper investMapper;

    public List<ActivityDto> getAllOperatingActivities(String loginName) {
        final List<ActivityDto> activityDtos = new ArrayList<>();
        List<ActivityModel> activityModels = activityMapper.findActivities(null, null, ActivityStatus.OPERATING, null);

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

    @Override
    public boolean createEditRecheckActivity(ActivityDto ActivityDto, ActivityStatus activityStatus, String loginName) {
        ActivityModel activityModel = new ActivityModel(ActivityDto);
        long activityId = activityModel.getId();
        switch(activityStatus){
            case TO_APPROVE:
                activityModel.setCreatedBy(loginName);
                activityModel.setCreatedTime(new Date());
                activityModel.setUpdatedBy(loginName);
                activityModel.setUpdatedTime(new Date());
                activityModel.setStatus(ActivityStatus.TO_APPROVE);
                activityMapper.create(activityModel);
                return true;
            case REJECTION:
                ActivityModel activityModelRejection = activityMapper.findById(activityId);
                if(activityModelRejection != null){
                    activityModelRejection.setStatus(ActivityStatus.REJECTION);
                    activityModelRejection.setUpdatedTime(new Date());
                    activityModelRejection.setUpdatedBy(loginName);
                    activityMapper.update(activityModelRejection);
                }
                return true;
            case TO_APPROVED:
                ActivityModel activityModelApproved = activityMapper.findById(activityId);
                if(activityModelApproved != null){
                    if(activityModelApproved.getActivatedTime() == null){
                        activityModelApproved.setActivatedTime(new Date());
                        activityModelApproved.setActivatedBy(loginName);
                    }
                    activityModelApproved.setUpdatedTime(new Date());
                    activityModelApproved.setUpdatedBy(loginName);
                    activityModelApproved.setStatus(ActivityStatus.TO_APPROVED);
                    activityMapper.update(activityModelApproved);
                }
                return true;

        }
        return false;
    }
}
