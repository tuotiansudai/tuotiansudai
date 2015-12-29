package com.tuotiansudai.service.impl;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.UserBillPaginationItemDataDto;
import com.tuotiansudai.repository.mapper.UserBillMapper;
import com.tuotiansudai.repository.model.UserBillBusinessType;
import com.tuotiansudai.repository.model.UserBillModel;
import com.tuotiansudai.repository.model.UserBillOperationType;
import com.tuotiansudai.service.UserBillService;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserBillServiceImpl implements UserBillService {

    static Logger logger = Logger.getLogger(UserBillServiceImpl.class);

    @Autowired
    private UserBillMapper userBillMapper;

    @Override
    public BaseDto<BasePaginationDataDto> getUserBillData(String loginName, int index, int pageSize, Date startTime, Date endTime, List<UserBillBusinessType> userBillBusinessType) {
        if (startTime == null) {
            startTime = new DateTime(0).withTimeAtStartOfDay().toDate();
        } else {
            startTime = new DateTime(startTime).withTimeAtStartOfDay().toDate();
        }

        if (endTime == null) {
            endTime = new DateTime().withDate(9999, 12, 31).withTimeAtStartOfDay().toDate();
        } else {
            endTime = new DateTime(endTime).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate();
        }
        List<UserBillModel> userBillModels = userBillMapper.findUserBills(Maps.newHashMap(ImmutableMap.<String, Object>builder()
                .put("userBillBusinessType", userBillBusinessType)
                .put("loginName", loginName)
                .put("indexPage", (index - 1) * pageSize)
                .put("startTime", startTime)
                .put("endTime", endTime)
                .put("pageSize", pageSize).build()));
        int count = userBillMapper.findUserBillsCount(Maps.newLinkedHashMap(ImmutableMap.<String, Object>builder()
                .put("userBillBusinessType", userBillBusinessType)
                .put("loginName", loginName)
                .put("startTime", startTime)
                .put("endTime", endTime).build()));

        List<UserBillPaginationItemDataDto> records = Lists.transform(userBillModels, new Function<UserBillModel, UserBillPaginationItemDataDto>() {
            @Override
            public UserBillPaginationItemDataDto apply(UserBillModel input) {
                return new UserBillPaginationItemDataDto(input);
            }
        });

        BasePaginationDataDto<UserBillPaginationItemDataDto> dataDto = new BasePaginationDataDto<>(index, pageSize, count, records);
        dataDto.setStatus(true);

        BaseDto<BasePaginationDataDto> dto = new BaseDto<>();
        dto.setData(dataDto);

        return dto;
    }

    @Override
    public long findSumRewardByLoginName(String loginName) {
        return userBillMapper.findSumRewardByLoginName(loginName);
    }

    @Override
    public List<UserBillModel> findUserFunds(UserBillBusinessType userBillBusinessType, UserBillOperationType userBillOperationType, String loginName, Date startTime, Date endTime, int index, int pageSize) {
        Date formattedStartTime;
        Date formattedEndTime;

        if (startTime == null) {
            formattedStartTime = new DateTime(0).withTimeAtStartOfDay().toDate();
        } else {
            formattedStartTime = new DateTime(startTime).toDate();
        }

        if (endTime == null) {
            formattedEndTime = new DateTime().withDate(9999, 12, 31).withTimeAtStartOfDay().toDate();
        } else {
            formattedEndTime = new DateTime(endTime).toDate();
        }

        return userBillMapper.findUserFunds(userBillBusinessType, userBillOperationType, loginName, formattedStartTime, formattedEndTime, (index - 1) * pageSize, pageSize);
    }

    @Override
    public int findUserFundsCount(UserBillBusinessType userBillBusinessType, UserBillOperationType userBillOperationType, String loginName, Date startTime, Date endTime) {
        Date formattedStartTime;
        Date formattedEndTime;

        if (startTime == null) {
            formattedStartTime = new DateTime(0).withTimeAtStartOfDay().toDate();
        } else {
            formattedStartTime = new DateTime(startTime).toDate();
        }

        if (endTime == null) {
            formattedEndTime = new DateTime().withDate(9999, 12, 31).withTimeAtStartOfDay().toDate();
        } else {
            formattedEndTime = new DateTime(endTime).toDate();
        }
        return userBillMapper.findUserFundsCount(userBillBusinessType, userBillOperationType, loginName, formattedStartTime, formattedEndTime);
    }
}
