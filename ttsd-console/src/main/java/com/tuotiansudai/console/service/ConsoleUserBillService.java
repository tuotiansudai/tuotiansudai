package com.tuotiansudai.console.service;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.UserBillPaginationItemDataDto;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.repository.mapper.UserBillMapper;
import com.tuotiansudai.repository.model.UserBillModel;
import com.tuotiansudai.repository.model.UserBillOperationType;
import com.tuotiansudai.repository.model.UserBillPaginationView;
import com.tuotiansudai.service.UserBillService;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ConsoleUserBillService {

    static Logger logger = Logger.getLogger(ConsoleUserBillService.class);

    @Autowired
    private UserBillMapper userBillMapper;

    public List<UserBillPaginationView> findUserFunds(UserBillBusinessType userBillBusinessType, UserBillOperationType userBillOperationType, String mobile, Date startTime, Date endTime, int index, int pageSize) {
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

        return userBillMapper.findUserFunds(userBillBusinessType, userBillOperationType, mobile, formattedStartTime, formattedEndTime, (index - 1) * pageSize, pageSize);
    }

    public int findUserFundsCount(UserBillBusinessType userBillBusinessType, UserBillOperationType userBillOperationType, String mobile, Date startTime, Date endTime) {
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
        return userBillMapper.findUserFundsCount(userBillBusinessType, userBillOperationType, mobile, formattedStartTime, formattedEndTime);
    }
}
