package com.tuotiansudai.service.impl;

import com.google.common.collect.Lists;
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

import java.util.*;

@Service
public class ActivityServiceImpl implements ActivityService {

    static Logger logger = Logger.getLogger(ActivityServiceImpl.class);

    @Autowired
    ActivityMapper activityMapper;

    @Autowired
    InvestMapper investMapper;

    public List<ActivityDto> getAllOperatingActivities(String loginName, Source source) {
        final List<ActivityDto> activityDtos = new ArrayList<>();

        List<ActivityModel> activityModels = activityMapper.findActivities(null, null, ActivityStatus.OPERATING, source);

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
    public boolean createEditRecheckActivity(ActivityDto activityDto, ActivityStatus activityStatus, String loginName) {
        ActivityModel activityModelExist = activityMapper.findById(activityDto.getId());

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
                    activityModel.setStatus(ActivityStatus.TO_APPROVE);
                    activityMapper.create(activityModel);
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


    @Override
    public List<ActivityDto> findAllActivities(Date startTime, Date endTime, ActivityStatus activityStatus, Source source) {
        List<ActivityModel> activityModels = activityMapper.findAllActivities(startTime, endTime, activityStatus, source);
        List<ActivityDto> activityDtos = Lists.newArrayList();
        for (int i = 0; i < activityModels.size(); i++) {
            ActivityModel activityModel = activityModels.get(i);
            ActivityDto activityDto = new ActivityDto();
            activityDto.setId(activityModel.getId());
            activityDto.setTitle(activityModel.getTitle());
            activityDto.setWebPictureUrl(activityModel.getWebPictureUrl());
            activityDto.setAppPictureUrl(activityModel.getAppPictureUrl());
            activityDto.setActivatedTime(activityModel.getActivatedTime());
            activityDto.setExpiredTime(activityModel.getExpiredTime());
            activityDto.setWebActivityUrl(activityModel.getWebActivityUrl());
            activityDto.setAppActivityUrl(activityModel.getAppActivityUrl());
            activityDto.setDescription(activityModel.getDescription());
            activityDto.setSource(activityModel.getSource());
            activityDto.setStatus(activityModel.getStatus());
            activityDtos.add(activityDto);
        }
        return activityDtos;
    }
}
