package com.tuotiansudai.activity.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.activity.repository.dto.IPhone7InvestLotteryDto;
import com.tuotiansudai.activity.repository.dto.IPhone7LotteryDto;
import com.tuotiansudai.activity.repository.mapper.IPhone7InvestLotteryMapper;
import com.tuotiansudai.activity.repository.mapper.IPhone7LotteryConfigMapper;
import com.tuotiansudai.activity.repository.model.IPhone7InvestLotteryModel;
import com.tuotiansudai.activity.repository.model.IPhone7LotteryConfigModel;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.MobileEncryptor;
import com.tuotiansudai.util.RandomUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class Iphone7LotteryService {

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private IPhone7LotteryConfigMapper iPhone7LotteryConfigMapper;

    @Autowired
    private IPhone7InvestLotteryMapper iPhone7InvestLotteryMapper;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.iphone7.startTime}\")}")
    private Date activityIphone7StartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.iphone7.endTime}\")}")
    private Date activityIphone7EndTime;

    public List<IPhone7LotteryDto> iphone7InvestLotteryWinnerViewList(){
        List<IPhone7LotteryConfigModel> iPhone7LotteryConfigModels = iPhone7LotteryConfigMapper.effectiveList();
        return iPhone7LotteryConfigModels.stream().map(iPhone7LotteryConfigModel -> new IPhone7LotteryDto(iPhone7LotteryConfigModel, MobileEncryptor.encryptWebMiddleMobile(iPhone7LotteryConfigModel.getMobile()))).collect(Collectors.toList());
    }

    public String nextLotteryInvestAmount(){
        long totalAmount = investMapper.sumInvestAmountIphone7(activityIphone7StartTime, activityIphone7EndTime);
        int currentLotteryInvestAmount = iPhone7LotteryConfigMapper.getCurrentLotteryInvestAmount();
        long nextLotteryInvestAmount = (totalAmount - currentLotteryInvestAmount * 1000000) <= 0 ? 50000000 : (50000000 - (totalAmount - currentLotteryInvestAmount * 1000000));
        return AmountConverter.convertCentToString(nextLotteryInvestAmount);
    }

    public BasePaginationDataDto<IPhone7InvestLotteryDto> myInvestLotteryList(String loginName, int index, int pageSize){
        long count = iPhone7InvestLotteryMapper.findByLoginNameCount(loginName);
        List<IPhone7InvestLotteryModel> records = Lists.newArrayList();
        if (count > 0) {
            int totalPages = (int) (count % pageSize > 0 ? count / pageSize + 1 : count / pageSize);
            index = index > totalPages ? totalPages : index;
            records = iPhone7InvestLotteryMapper.findPaginationByLoginName(loginName, (index - 1) * pageSize, pageSize);
        }

        List<IPhone7InvestLotteryDto> dtoList = records.stream().map(iPhone7InvestLotteryModel -> new IPhone7InvestLotteryDto(iPhone7InvestLotteryModel, isNotExpiryDate())).collect(Collectors.toList());

        BasePaginationDataDto<IPhone7InvestLotteryDto> dto = new BasePaginationDataDto<>(index, pageSize, count, dtoList);
        dto.setStatus(true);
        return dto;
    }

    public boolean isNotExpiryDate(){
        Date nowDate = DateTime.now().toDate();
        return activityIphone7StartTime.before(nowDate) && activityIphone7EndTime.after(nowDate);
    }

}
