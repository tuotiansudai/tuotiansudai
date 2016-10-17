package com.tuotiansudai.activity.service;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.activity.dto.*;
import com.tuotiansudai.activity.dto.ActivityCategory;
import com.tuotiansudai.activity.repository.mapper.IPhone7InvestLotteryMapper;
import com.tuotiansudai.activity.repository.mapper.IPhone7LotteryConfigMapper;
import com.tuotiansudai.activity.repository.mapper.UserLotteryPrizeMapper;
import com.tuotiansudai.activity.repository.model.*;
import com.tuotiansudai.coupon.service.CouponAssignmentService;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.membership.repository.mapper.MembershipMapper;
import com.tuotiansudai.membership.repository.mapper.UserMembershipMapper;
import com.tuotiansudai.membership.repository.model.MembershipLevel;
import com.tuotiansudai.membership.repository.model.UserMembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipType;
import com.tuotiansudai.point.repository.mapper.PointBillMapper;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.RandomUtils;
import org.apache.commons.beanutils.converters.StringConverter;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class Iphone7LotteryService {

    private static Logger logger = Logger.getLogger(Iphone7LotteryService.class);

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private IPhone7LotteryConfigMapper iPhone7LotteryConfigMapper;

    @Autowired
    private IPhone7InvestLotteryMapper iPhone7InvestLotteryMapper;

    @Autowired
    private RandomUtils randomUtils;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.iphone7.startTime}\")}")
    private Date activityIphone7StartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.iphone7.endTime}\")}")
    private Date activityIphone7EndTime;

    public List<IPhone7LotteryDto> iphone7InvestLotteryWinnerViewList(){
        List<IPhone7LotteryConfigModel> iPhone7LotteryConfigModels = iPhone7LotteryConfigMapper.approvedList();
        List<IPhone7LotteryDto> dtoList = iPhone7LotteryConfigModels.stream().map(iPhone7LotteryConfigModel -> {
            return new IPhone7LotteryDto(iPhone7LotteryConfigModel, randomUtils.encryptWebMiddleMobile("13988888888"));
        }).collect(Collectors.toList());
        return dtoList;
    }

    public String nextLotteryInvestAmount(){
        long totalAmount = investMapper.sumInvestAmountRanking(activityIphone7StartTime, activityIphone7EndTime);
        int currentLotteryInvestAmount = iPhone7LotteryConfigMapper.getCurrentLotteryInvestAmount();
        long nextLotteryInvestAmount = totalAmount - currentLotteryInvestAmount * 1000000;
        return AmountConverter.convertCentToString(nextLotteryInvestAmount);
    }

    public BaseDto<BasePaginationDataDto> myInvestLotteryList(String loginName, int index, int pageSize){
        long count = iPhone7InvestLotteryMapper.findByLoginNameCount(loginName);
        List<IPhone7InvestLotteryModel> records = Lists.newArrayList();
        if (count > 0) {
            int totalPages = (int) (count % pageSize > 0 || count == 0 ? count / pageSize + 1 : count / pageSize);
            index = index > totalPages ? totalPages : index;
            records = iPhone7InvestLotteryMapper.findByAllLoginName(loginName, (index - 1) * pageSize, pageSize);
        }

        List<IPhone7InvestLotteryDto> dtoList = records.stream().map(iPhone7InvestLotteryModel -> {
            return new IPhone7InvestLotteryDto(iPhone7InvestLotteryModel);
        }).collect(Collectors.toList());

        BasePaginationDataDto<IPhone7InvestLotteryDto> paginationDataDto = new BasePaginationDataDto<>(index, pageSize, count, dtoList);
        return new BaseDto<>(paginationDataDto);
    }

}
