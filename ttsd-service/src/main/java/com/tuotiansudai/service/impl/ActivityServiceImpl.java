package com.tuotiansudai.service.impl;

import com.google.common.base.Function;
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

import java.util.Date;
import java.util.List;

@Service
public class ActivityServiceImpl implements ActivityService {

    static Logger logger = Logger.getLogger(ActivityServiceImpl.class);

    @Autowired
    ActivityMapper activityMapper;

    @Autowired
    InvestMapper investMapper;

    @Override
    public List<ActivityDto> getAllActiveActivities(String loginName, Source source) {
        int index = 1;
        int pageSize = Integer.MAX_VALUE;
        List<ActivityModel> activityModels = activityMapper.findActiveActivities(source, new Date(), (index - 1) * pageSize, pageSize);
        return Lists.transform(activityModels, new Function<ActivityModel, ActivityDto>() {
            @Override
            public ActivityDto apply(ActivityModel model) {
                return new ActivityDto(model);
            }
        });
    }

    @Override
    public boolean saveOrUpdate(ActivityDto activityDto, ActivityStatus activityStatus, String loginName, String ip) {
        ActivityModel activityModelExist = activityMapper.findById(activityDto.getActivityId());

        switch (activityStatus) {
            case TO_APPROVE:
                if (activityModelExist != null) {
                    activityModelExist.setDescription(activityDto.getDescription());
                    activityModelExist.setTitle(activityDto.getTitle());
                    activityModelExist.setWebActivityUrl(activityDto.getWebActivityUrl());
                    activityModelExist.setWebPictureUrl(activityDto.getWebPictureUrl());
                    activityModelExist.setAppActivityUrl(activityDto.getAppActivityUrl());
                    activityModelExist.setAppPictureUrl(activityDto.getAppPictureUrl());
                    activityModelExist.setActivatedTime(activityDto.getActivatedTime());
                    activityModelExist.setSource(activityDto.getSource());
                    activityModelExist.setShareTitle(activityDto.getShareTitle());
                    activityModelExist.setShareContent(activityDto.getShareContent());
                    activityModelExist.setShareUrl(activityDto.getShareUrl());
                    activityModelExist.setUpdatedBy(loginName);
                    activityModelExist.setExpiredTime(activityDto.getExpiredTime());
                    activityModelExist.setUpdatedTime(new Date());
                    activityModelExist.setStatus(ActivityStatus.TO_APPROVE);
                    activityModelExist.setSeq(activityDto.getSeq());
                    activityModelExist.setLongTerm(activityDto.getLongTerm());
                    if(activityDto.getLongTerm()){
                        activityModelExist.setExpiredTime(null);
                        activityModelExist.setActivatedTime(null);

                    }
                    activityMapper.update(activityModelExist);
                } else {
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
                if (activityModelExist != null) {
                    activityModelExist.setStatus(ActivityStatus.REJECTION);
                    activityModelExist.setUpdatedTime(new Date());
                    activityModelExist.setUpdatedBy(loginName);
                    activityMapper.update(activityModelExist);
                }
                return true;
            case APPROVED:
                if (activityModelExist != null) {
                    if (activityModelExist.getActivatedTime() == null) {
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
        if (endTime != null) {
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
