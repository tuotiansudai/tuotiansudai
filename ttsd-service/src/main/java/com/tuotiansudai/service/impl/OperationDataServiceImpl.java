package com.tuotiansudai.service.impl;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.OperationDataDto;
import com.tuotiansudai.enums.AgeDistributionType;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.mapper.UserBillMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.InvestDataView;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.service.OperationDataService;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.CalculateUtil;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
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
    private UserBillMapper userBillMapper;

    @Autowired
    private InvestRepayMapper investRepayMapper;

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
        List<InvestDataView> investDataViewList = new ArrayList<>();
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
            Collections.sort(investDataViewList, (o1, o2) -> Long.compare(Long.parseLong(o1.getProductName()), Long.parseLong(o2.getProductName())));
        } else {
            investDataViewList = investMapper.getInvestDetail();
            for (InvestDataView investDataView : investDataViewList) {
                redisWrapperClient.hset(getRedisKeyFromTemplateByDate(TABLE_INFO_PUBLISH_KEY_TEMPLATE, endDate),
                        investDataView.getProductName(), investDataView.ConvertInvestDataViewToString(), timeout);
            }

            investDataViewList = Lists.transform(investDataViewList, input -> {
                input.setTotalInvestAmount(AmountConverter.convertCentToString(Long.parseLong(input.getTotalInvestAmount())));
                input.setAvgInvestAmount(AmountConverter.convertCentToString(Long.parseLong(input.getAvgInvestAmount())));
                return input;
            });
        }

        return investDataViewList;
    }

    public List<Integer> findScaleByGender(Date endDate) {
        return userMapper.findScaleByGender(endDate);
    }

    public Map<String, String> findLatestSixMonthTradeAmount(Date endDate) {
        List<Map<String, String>> latestSixMonthTradeAmountList = investMapper.findLatestSixMonthTradeAmount(endDate);
        Map<String, String> resultMap = new LinkedHashMap<>();
        for (Map<String, String> map : latestSixMonthTradeAmountList) {
            String month = "";
            String amount = "0";
            for (Map.Entry<String, String> latestSixMonthTradeAmount : map.entrySet()) {
                if ("currentMonth".equals(latestSixMonthTradeAmount.getKey())) {
                    month = latestSixMonthTradeAmount.getValue().substring(latestSixMonthTradeAmount.getValue().indexOf("-") + 1).replace("0","") + "月";
                } else if ("sumAmount".equals(latestSixMonthTradeAmount.getKey())) {
                    amount = (String.valueOf(latestSixMonthTradeAmount.getValue()));
                }
            }
            resultMap.put(month, amount);
        }
        return resultMap;
    }

    public Map<String, String> findAgeDistributionByAge(Date endDate) {
        List<Map<String, String>> AgeDistributionList = userMapper.findAgeDistributionByAge(endDate);
        Map<String, String> resultMap = new LinkedHashMap<>();
        Map<String, String> resultGroupMap = new LinkedHashMap<>();
        for (Map<String, String> AgeDistributionMap : AgeDistributionList) {
            String age = "", scale = "";
            for (Map.Entry<String, String> AgeDistributionEntry : AgeDistributionMap.entrySet()) {
                if ("age".equals(AgeDistributionEntry.getKey())) {
                    age = String.valueOf(Integer.parseInt(String.valueOf(AgeDistributionEntry.getValue()).replace(".0","")));
                } else if ("totalCount".equals(AgeDistributionEntry.getKey())) {
                    scale = (String.valueOf(AgeDistributionEntry.getValue()));
                }
            }
            resultMap.put(age, scale);
        }

        Set<Map.Entry<String, String>> ageGroupDistributionEntries = resultMap.entrySet();

        //先计算总数
        long totalUserCount = 0;
        long totalUnder20UserCount = 0;
        long totalBETWEEN_20_AND_30UserCount = 0;
        long totalBETWEEN_30_AND_40UserCount = 0;
        long totalBETWEEN_40_AND_50UserCount = 0;
        long totalMORE_THAN_50UserCount = 0;
        for (Map.Entry<String, String> ageGroupDistributionEntry : ageGroupDistributionEntries) {
            totalUserCount += Long.parseLong(ageGroupDistributionEntry.getValue());
        }

        //分别计算5个区间的人数
        for (Map.Entry<String, String> ageGroupDistributionEntry : ageGroupDistributionEntries) {
            if(Integer.parseInt(ageGroupDistributionEntry.getKey()) < 20){
                totalUnder20UserCount += Long.parseLong(ageGroupDistributionEntry.getValue());
            }else if(Integer.parseInt(ageGroupDistributionEntry.getKey()) >= 20 && Integer.parseInt(ageGroupDistributionEntry.getKey()) < 30){
                totalBETWEEN_20_AND_30UserCount += Long.parseLong(ageGroupDistributionEntry.getValue());
            }else if(Integer.parseInt(ageGroupDistributionEntry.getKey()) >= 30 && Integer.parseInt(ageGroupDistributionEntry.getKey()) < 40){
                totalBETWEEN_30_AND_40UserCount += Long.parseLong(ageGroupDistributionEntry.getValue());
            }else if(Integer.parseInt(ageGroupDistributionEntry.getKey()) >= 40 && Integer.parseInt(ageGroupDistributionEntry.getKey()) < 50){
                totalBETWEEN_40_AND_50UserCount += Long.parseLong(ageGroupDistributionEntry.getValue());
            }else if(Integer.parseInt(ageGroupDistributionEntry.getKey()) >= 50){
                totalMORE_THAN_50UserCount += Long.parseLong(ageGroupDistributionEntry.getValue());
            }
        }
        resultGroupMap.put(String.valueOf(AgeDistributionType.UNDER_20.getDescription()), String.valueOf(CalculateUtil.calculatePercentage(totalUnder20UserCount, totalUserCount, 1)));
        resultGroupMap.put(String.valueOf(AgeDistributionType.BETWEEN_20_AND_30.getDescription()), String.valueOf(CalculateUtil.calculatePercentage(totalBETWEEN_20_AND_30UserCount, totalUserCount, 1)));
        resultGroupMap.put(String.valueOf(AgeDistributionType.BETWEEN_30_AND_40.getDescription()), String.valueOf(CalculateUtil.calculatePercentage(totalBETWEEN_30_AND_40UserCount, totalUserCount, 1)));
        resultGroupMap.put(String.valueOf(AgeDistributionType.BETWEEN_40_AND_50.getDescription()), String.valueOf(CalculateUtil.calculatePercentage(totalBETWEEN_40_AND_50UserCount, totalUserCount, 1)));
        resultGroupMap.put(String.valueOf(AgeDistributionType.MORE_THAN_50.getDescription()), String.valueOf(CalculateUtil.calculatePercentage(totalMORE_THAN_50UserCount, totalUserCount, 1)));

        return resultGroupMap;
    }

    public Map<String, String> findCountInvestCityScaleTop3(Date endDate) {
        List<Map<String, String>> investCityList = userMapper.findCountInvestCityScaleTop3(endDate);
        Map<String, String> resultMap = new LinkedHashMap<>();
        long totalScaleCount = userMapper.findCountInvestCityScale(endDate);
        for(Map<String,String> investCityMap: investCityList){
            String city ="", scale = "";
            for(Map.Entry<String, String> investCityEntry : investCityMap.entrySet()){
                if("city".equals(investCityEntry.getKey())){
                    city =  investCityEntry.getValue();
                }else if("totalCount".equals(investCityEntry.getKey())){
                    scale = String.valueOf(CalculateUtil.calculatePercentage(Long.parseLong(String.valueOf(investCityEntry.getValue())), totalScaleCount, 1));
                }
            }
            resultMap.put(city, scale);
        }
        return resultMap;
    }

    public Map<String, String> findInvestAmountScaleTop3(Date endDate){
        List<Map<String, String>> investCityAmountScaleList = investMapper.findInvestAmountScaleTop3(endDate);
        Map<String, String> resultMap = new LinkedHashMap<>();
        long  totalScaleCount = investMapper.findInvestAmountScale(endDate);
        for(Map<String,String> investCityAmountMap: investCityAmountScaleList){
            String city ="", amount = "";
            for(Map.Entry<String,String> investCityAmountEntry : investCityAmountMap.entrySet()){
                if("city".equals(investCityAmountEntry.getKey())){
                    city = investCityAmountEntry.getValue();
                }else if("sumAmount".equals(investCityAmountEntry.getKey())){
                    amount = String.valueOf(CalculateUtil.calculatePercentage(Long.parseLong(String.valueOf(investCityAmountEntry.getValue())), totalScaleCount, 1));
                }
            }
            resultMap.put(city, amount);
        }
        return resultMap;
    }

    public long findUserSumInterest(Date endDate){
        return investRepayMapper.findSumActualInterest(endDate) + userBillMapper.findUserSumInterest(endDate);
    }


}
