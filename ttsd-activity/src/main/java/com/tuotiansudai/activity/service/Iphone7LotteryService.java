package com.tuotiansudai.activity.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.activity.dto.IPhone7InvestLotteryDto;
import com.tuotiansudai.activity.dto.IPhone7LotteryDto;
import com.tuotiansudai.activity.repository.mapper.IPhone7InvestLotteryMapper;
import com.tuotiansudai.activity.repository.mapper.IPhone7LotteryConfigMapper;
import com.tuotiansudai.activity.repository.model.IPhone7InvestLotteryModel;
import com.tuotiansudai.activity.repository.model.IPhone7LotteryConfigModel;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.RandomUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
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

    @Autowired
    private RedisWrapperClient redisWrapperClient;


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


    @Transactional
    public void getLotteryNumber(InvestModel investModel){
        String lotteryNumber = String.valueOf((int)((Math.random() * 9 + 1) * 100000));
        IPhone7LotteryConfigModel iphone7LotteryConfigModel = iPhone7LotteryConfigMapper.findByLotteryNumber(String.valueOf(lotteryNumber));
        IPhone7InvestLotteryModel iPhone7InvestLotteryModel = iPhone7InvestLotteryMapper.findByLotteryNumber(String.valueOf(lotteryNumber));
        lotteryNumber = (iphone7LotteryConfigModel == null && iPhone7InvestLotteryModel == null) ? lotteryNumber : String.valueOf((int)((Math.random() * 9 + 1) * 100000));
        IPhone7InvestLotteryModel model = new IPhone7InvestLotteryModel(investModel.getId(), investModel.getLoginName(), investModel.getAmount(), String.valueOf(lotteryNumber));
        iPhone7InvestLotteryMapper.create(model);

        long totalAmount = investMapper.sumInvestAmountRanking(activityIphone7StartTime, activityIphone7EndTime);
        //if(totalAmount > )





    }



}
