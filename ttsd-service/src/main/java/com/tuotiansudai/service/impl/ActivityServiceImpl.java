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

import java.util.ArrayList;
import java.util.Date;
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

        List<ActivityModel> activityModels = activityMapper.findOperatingActivities(source);

        for (ActivityModel activityModel : activityModels) {
            activityDtos.add(new ActivityDto(activityModel));
        }

        return activityDtos;
    }

    @Override
    public boolean createEditRecheckActivity(ActivityDto activityDto, ActivityStatus activityStatus, String loginName, String ip) {
        ActivityModel activityModelExist = activityMapper.findById(activityDto.getActivityId());

        switch(activityStatus){
            case TO_APPROVE:
                if(activityModelExist != null){
                    activityModelExist.setDescription(activityDto.getDescription());
                    activityModelExist.setTitle(activityDto.getTitle());
                    activityModelExist.setWebActivityUrl(activityDto.getWebActivityUrl());
                    activityModelExist.setWebPictureUrl(activityDto.getWebPictureUrl());
                    activityModelExist.setAppActivityUrl(activityDto.getAppActivityUrl());
                    activityModelExist.setAppPictureUrl(activityDto.getAppPictureUrl());
                    activityModelExist.setActivatedTime(activityDto.getActivatedTime());
                    activityModelExist.setSource(activityDto.getSource());
                    activityModelExist.setUpdatedBy(loginName);
                    activityModelExist.setUpdatedTime(new Date());
                    activityModelExist.setStatus(ActivityStatus.TO_APPROVE);
                    activityMapper.update(activityModelExist);
                }else{
                    ActivityModel activityModel = new ActivityModel(activityDto);
                    activityModel.setCreatedBy(loginName);
                    activityModel.setCreatedTime(new Date());
                    activityModel.setUpdatedBy(loginName);
                    activityModel.setUpdatedTime(new Date());
                    activityModel.setStatus(ActivityStatus.TO_APPROVE);
                    activityMapper.create(activityModel);
                    activityDto.setActivityId(activityModel.getId());
                }
                return true;
            case REJECTION:
                if(activityModelExist != null){
                    activityModelExist.setStatus(ActivityStatus.REJECTION);
                    activityModelExist.setUpdatedTime(new Date());
                    activityModelExist.setUpdatedBy(loginName);
                    activityMapper.update(activityModelExist);
                }
                return true;
            case APPROVED:
                if(activityModelExist != null){
                    if(activityModelExist.getActivatedTime() == null){
                        activityModelExist.setActivatedTime(new Date());
                        activityModelExist.setActivatedBy(loginName);
                    }
                    activityModelExist.setUpdatedTime(new Date());
                    activityModelExist.setUpdatedBy(loginName);
                    activityModelExist.setStatus(ActivityStatus.APPROVED);
                    activityMapper.update(activityModelExist);
                }
                return true;

        }
        return false;
    }

    @Override
    public ActivityModel findById(long activityId) {
        return activityMapper.findById(activityId);
    }
}
