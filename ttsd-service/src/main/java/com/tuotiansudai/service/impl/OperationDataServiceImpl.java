package com.tuotiansudai.service.impl;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.OperationDataDto;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.InvestDataView;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.service.OperationDataService;
import com.tuotiansudai.util.AmountConverter;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Repository
public class OperationDataServiceImpl implements OperationDataService {
    static Logger logger = Logger.getLogger(ReferrerRelationServiceImpl.class);

    @Autowired
    InvestMapper investMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    RedisWrapperClient redisWrapperClient;

    private static final String CHART_INFO_PUBLISH_KEY_TEMPLATE = "web:info:publish:chart:{0}";
    private static final String TABLE_INFO_PUBLISH_KEY_TEMPLATE = "web:info:publish:table:{0}";

    private static final String REDIS_USERS_COUNT = "userCount";
    private static final String REDIS_TRADE_AMOUNT = "tradeAmount";
    private static final String REDIS_OPERATION_DATA_MONTH = "operationDataMonth";
    private static final String REDIS_OPERATION_DATA_MONTH_AMOUNT = "operationDataMonthAmount";

    private static final int timeout = 60 * 60 * 24;
    private static final Date startOperationDate = new DateTime().withDate(2015, 7, 1).withTimeAtStartOfDay().toDate();

    private List<String> convertRedisListStringIntoList(String listString) {
        return Splitter.on(",").splitToList(listString);
    }

    private String convertListIntoRedisListString(List<String> list) {
        return Joiner.on(",").join(list);
    }

    private String getRedisKeyFromTemplateByDate(String template, Date timeStampDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMdd");
        return MessageFormat.format(template, String.valueOf(simpleDateFormat.format(timeStampDate)));
    }

    private int calOperationTime(Date endTime) {
        long startMillis = startOperationDate.getTime();
        long endMillis = endTime.getTime();
        return (int) ((endMillis - startMillis) / (1000 * 3600 * 24));
    }

    private int getInvestMonthCount(Date endTime) {
        DateTime startDate = new DateTime(startOperationDate);
        DateTime endDate = new DateTime(endTime);
        Period period = new Period(startDate, endDate, PeriodType.months().withDaysRemoved());
        return period.getMonths();
    }

    private void loadOperationDataDtoFromDatabase(OperationDataDto operationDataDto, Date endDate) {
        operationDataDto.setOperationDays(calOperationTime(endDate));

        operationDataDto.setTradeAmount(AmountConverter.convertCentToString(investMapper.sumInvestAmount(null, null, null,
                null, null, startOperationDate, new DateTime().withMillis(endDate.getTime()).withTimeAtStartOfDay().toDate(),
                InvestStatus.SUCCESS, null)));

        operationDataDto.setUsersCount(userMapper.findUsersCount());

        final int monthSize = getInvestMonthCount(endDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endDate);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) - monthSize, 1, 0, 0, 0);
        for (int i = 0; i < monthSize; i++) {
            String month = calendar.get(Calendar.YEAR) + "." + (calendar.get(Calendar.MONTH) + 1);
            operationDataDto.getMonth().add(month);

            Date startTime = calendar.getTime();
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
            Date endTime = calendar.getTime();

            String amount = AmountConverter.convertCentToString(investMapper.sumInvestAmount(null, null, null, null, null,
                    startTime, endTime, InvestStatus.SUCCESS, null));
            operationDataDto.getMoney().add(amount);
        }
    }

    private void loadOperationDataDtoFromRedis(OperationDataDto operationDataDto, Date endDate) {
        final String redisInfoPublishKey = getRedisKeyFromTemplateByDate(CHART_INFO_PUBLISH_KEY_TEMPLATE, endDate);
        operationDataDto.setOperationDays(calOperationTime(endDate));
        operationDataDto.setUsersCount(Integer.parseInt(redisWrapperClient.hget(redisInfoPublishKey, REDIS_USERS_COUNT)));
        operationDataDto.setTradeAmount(redisWrapperClient.hget(redisInfoPublishKey, REDIS_TRADE_AMOUNT));
        operationDataDto.setMonth(convertRedisListStringIntoList(redisWrapperClient.hget(redisInfoPublishKey,
                REDIS_OPERATION_DATA_MONTH)));
        operationDataDto.setMoney(convertRedisListStringIntoList(redisWrapperClient.hget(redisInfoPublishKey,
                REDIS_OPERATION_DATA_MONTH_AMOUNT)));
    }

    private void updateRedis(OperationDataDto operationDataDto, Date endTime) {
        final String redisInfoPublishKey = getRedisKeyFromTemplateByDate(CHART_INFO_PUBLISH_KEY_TEMPLATE, endTime);
        redisWrapperClient.hset(redisInfoPublishKey, REDIS_USERS_COUNT, Long.toString(operationDataDto.getUsersCount()), timeout);
        redisWrapperClient.hset(redisInfoPublishKey, REDIS_TRADE_AMOUNT, operationDataDto.getTradeAmount(), timeout);
        redisWrapperClient.hset(redisInfoPublishKey, REDIS_OPERATION_DATA_MONTH, convertListIntoRedisListString(
                operationDataDto.getMonth()), timeout);
        redisWrapperClient.hset(redisInfoPublishKey, REDIS_OPERATION_DATA_MONTH_AMOUNT, convertListIntoRedisListString(
                operationDataDto.getMoney()), timeout);
    }

    @Override
    public OperationDataDto getOperationDataFromRedis(Date endDate) {
        OperationDataDto operationDataDto = new OperationDataDto();
        if (redisWrapperClient.exists(getRedisKeyFromTemplateByDate(CHART_INFO_PUBLISH_KEY_TEMPLATE, endDate))) {
            loadOperationDataDtoFromRedis(operationDataDto, endDate);
        } else {
            loadOperationDataDtoFromDatabase(operationDataDto, endDate);
            updateRedis(operationDataDto, endDate);
        }
        return operationDataDto;
    }

    @Override
    public List<InvestDataView> getInvestDetail(Date endDate) {
        List<InvestDataView> investDataViewList = new ArrayList<InvestDataView>();
        if (redisWrapperClient.exists(getRedisKeyFromTemplateByDate(TABLE_INFO_PUBLISH_KEY_TEMPLATE, endDate))) {
            Map<String, String> map = redisWrapperClient.hgetAll(getRedisKeyFromTemplateByDate(TABLE_INFO_PUBLISH_KEY_TEMPLATE,
                    endDate));
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String[] investDataViewValues = entry.getValue().split("\\|");
                InvestDataView investDataView = new InvestDataView();
                investDataView.setProductName(investDataViewValues[0]);
                investDataView.setTotalInvestAmount(investDataViewValues[1]);
                investDataView.setCountInvest(Integer.parseInt(investDataViewValues[2]));
                investDataView.setAvgInvestAmount(investDataViewValues[3]);
                investDataViewList.add(investDataView);
            }
            Collections.sort(investDataViewList, new Comparator<InvestDataView>() {
                @Override
                public int compare(InvestDataView o1, InvestDataView o2) {
                    return Long.compare(Long.parseLong(o1.getProductName()), Long.parseLong(o2.getProductName()));
                }
            });
        } else {
            investDataViewList = investMapper.getInvestDetail();
            for (InvestDataView investDataView : investDataViewList) {
                redisWrapperClient.hset(getRedisKeyFromTemplateByDate(TABLE_INFO_PUBLISH_KEY_TEMPLATE, endDate),
                        investDataView.getProductName(), investDataView.ConvertInvestDataViewToString(), timeout);
            }

            investDataViewList = Lists.transform(investDataViewList, new Function<InvestDataView, InvestDataView>() {
                @Override
                public InvestDataView apply(InvestDataView input) {
                    input.setTotalInvestAmount(AmountConverter.convertCentToString(Long.parseLong(input.getTotalInvestAmount())));
                    input.setAvgInvestAmount(AmountConverter.convertCentToString(Long.parseLong(input.getAvgInvestAmount())));
                    return input;
                }
            });
        }

        return investDataViewList;
    }
}
