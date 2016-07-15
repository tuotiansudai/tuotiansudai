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
import org.joda.time.DateTime;
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

    public List<ActivityDto> getAllActiveActivities(String loginName, Source source) {
        //Web不分页
        final int fixedIndex = 1;
        final int fixedPageSize = 1000;

        List<ActivityModel> activityModels = activityMapper.findActiveActivities(source, new Date(), (fixedIndex - 1) * fixedPageSize, fixedPageSize);

        final List<ActivityDto> activityDtos = new ArrayList<>();
        for (ActivityModel activityModel : activityModels) {
            activityDtos.add(new ActivityDto(activityModel));
        }

        return activityDtos;
    }

    @Override
    public boolean saveOrUpdate(ActivityDto activityDto, ActivityStatus activityStatus, String loginName, String ip) {
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
                    activityModelExist.setExpiredTime(activityDto.getExpiredTime());
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
    public ActivityDto findActivityDtoById(long activityId) {
        ActivityModel activityModel = activityMapper.findById(activityId);

        return new ActivityDto(activityModel);
    }


    @Override
    public List<ActivityDto> findAllActivities(Date startTime, Date endTime, ActivityStatus activityStatus, Source source) {
        if(endTime != null){
            endTime = new DateTime(endTime).plusDays(1).minusSeconds(1).toDate();
        }
        List<ActivityModel> activityModels = activityMapper.findAllActivities(startTime, endTime, activityStatus, source);
        List<ActivityDto> activityDtos = Lists.newArrayList();
        for (ActivityModel activityModel : activityModels) {
            ActivityDto activityDto = new ActivityDto(activityModel);

            activityDto.setActivityId(activityModel.getId());
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
