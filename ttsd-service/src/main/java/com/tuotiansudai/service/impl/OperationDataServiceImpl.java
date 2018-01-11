package com.tuotiansudai.service.impl;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.tuotiansudai.dto.OperationDataAgeDataDto;
import com.tuotiansudai.dto.OperationDataDto;
import com.tuotiansudai.dto.OperationDataInvestAmountDataDto;
import com.tuotiansudai.dto.OperationDataInvestCityDataDto;
import com.tuotiansudai.enums.AgeDistributionType;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanRepayMapper;
import com.tuotiansudai.repository.mapper.OperationDataMapper;
import com.tuotiansudai.repository.mapper.UserBillMapper;
import com.tuotiansudai.repository.model.InvestDataView;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.service.OperationDataService;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.CalculateUtil;
import com.tuotiansudai.util.RedisWrapperClient;
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
    static Logger logger = Logger.getLogger(OperationDataServiceImpl.class);

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OperationDataMapper operationDataMapper;

    @Autowired
    private UserBillMapper userBillMapper;

    @Autowired
    private LoanRepayMapper loanRepayMapper;

    private static final String CHART_INFO_PUBLISH_KEY_TEMPLATE = "web:info:publish:chart:{0}";
    private static final String TABLE_INFO_PUBLISH_KEY_TEMPLATE = "web:info:publish:table:{0}";
    private static final String SCALE_GENDER_INFO_PUBLISH_KEY_TEMPLATE = "app:info:publish:scale:gender:{0}";
    private static final String AGE_DISTRIBUTION_INFO_PUBLISH_KEY_TEMPLATE = "app:info:publish:age:Distribution:{0}";
    private static final String COUNT_INVEST_CITY_SCALE_INFO_PUBLISH_KEY_TEMPLATE = "app:info:publish:count:invest:city:scale:{0}";
    private static final String AMOUNT_INVEST_CITY_SCALE_INFO_PUBLISH_KEY_TEMPLATE = "app:info:publish:amount:invest:city:scale:{0}";

    private static final String REDIS_USERS_COUNT = "userCount";
    private static final String REDIS_TRADE_AMOUNT = "tradeAmount";
    private static final String REDIS_OPERATION_DATA_MONTH = "operationDataMonth";
    private static final String REDIS_OPERATION_DATA_MONTH_AMOUNT = "operationDataMonthAmount";
    private static final String REDIS_USER_SUM_INTEREST = "userSumInterest";

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

        List<Integer> sexList = findScaleByGender(new Date());
        operationDataDto.setFemaleScale(String.valueOf(CalculateUtil.calculatePercentage(sexList.get(0), sexList.get(0) + sexList.get(1), 1)));
        operationDataDto.setMaleScale(String.valueOf(100 - CalculateUtil.calculatePercentage(sexList.get(0), sexList.get(0) + sexList.get(1), 1)));

        operationDataDto.setTotalInterest(String.valueOf(findUserSumInterest(new Date())));
        operationDataDto.setAgeDistribution(convertMapToOperationDataAgeDataDto());
        operationDataDto.setInvestAmountScaleTop3(convertMapToOperationDataInvestAmountDataDto());
        operationDataDto.setInvestCityScaleTop3(convertMapToOperationDataInvestCityDataDto());
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
                    new DateTime(startTime).withTimeAtStartOfDay().toDate(), new DateTime(endTime).withTimeAtStartOfDay().plusSeconds(-1).toDate(), InvestStatus.SUCCESS, null));
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
        operationDataDto.setTotalInterest(String.valueOf(findUserSumInterest(endDate)));
        operationDataDto.setAgeDistribution(convertMapToOperationDataAgeDataDto());
        operationDataDto.setInvestCityScaleTop3(convertMapToOperationDataInvestCityDataDto());
        operationDataDto.setInvestAmountScaleTop3(convertMapToOperationDataInvestAmountDataDto());
        List<Integer> sexList = findScaleByGender(new Date());
        operationDataDto.setFemaleScale(String.valueOf(CalculateUtil.calculatePercentage(sexList.get(0), sexList.get(0) + sexList.get(1), 1)));
        operationDataDto.setMaleScale(String.valueOf(100 - CalculateUtil.calculatePercentage(sexList.get(0), sexList.get(0) + sexList.get(1), 1)));

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
        List<Integer> scaleByGenderList = Lists.newArrayList();
        if (redisWrapperClient.exists(getRedisKeyFromTemplateByDate(SCALE_GENDER_INFO_PUBLISH_KEY_TEMPLATE, endDate))) {
            Map<String, String> map = redisWrapperClient.hgetAll(getRedisKeyFromTemplateByDate(SCALE_GENDER_INFO_PUBLISH_KEY_TEMPLATE, endDate));
            for (Map.Entry<String, String> entry : map.entrySet()) {
                scaleByGenderList.add(Integer.parseInt(entry.getValue()));
            }
        } else {
            scaleByGenderList = operationDataMapper.findScaleByGender(endDate);
            for (Integer value : scaleByGenderList) {
                redisWrapperClient.hset(getRedisKeyFromTemplateByDate(SCALE_GENDER_INFO_PUBLISH_KEY_TEMPLATE, endDate),
                        String.valueOf(value), String.valueOf(value), timeout);
            }
        }
        return scaleByGenderList;
    }

    public Map<String, String> findCountInvestCityScaleTop3(Date endDate) {
        Map<String, String> resultMap = new LinkedHashMap<>();
        if (redisWrapperClient.exists(getRedisKeyFromTemplateByDate(COUNT_INVEST_CITY_SCALE_INFO_PUBLISH_KEY_TEMPLATE, endDate))) {
            Map<String, String> map = redisWrapperClient.hgetAll(getRedisKeyFromTemplateByDate(COUNT_INVEST_CITY_SCALE_INFO_PUBLISH_KEY_TEMPLATE, endDate));
            for (Map.Entry<String, String> entry : map.entrySet()) {
                resultMap.put(entry.getKey(), entry.getValue());
            }
        } else {
            long totalScaleCount = operationDataMapper.findCountInvestCityScale(endDate);
            List<Map<String, String>> investCityList = operationDataMapper.findCountInvestCityScaleTop3(endDate);
            for (Map<String, String> investCityMap : investCityList) {
                redisWrapperClient.hset(getRedisKeyFromTemplateByDate(COUNT_INVEST_CITY_SCALE_INFO_PUBLISH_KEY_TEMPLATE, endDate),
                        investCityMap.get("city"), String.valueOf(CalculateUtil.calculatePercentage(Long.parseLong(String.valueOf(investCityMap.get("totalCount"))), totalScaleCount, 1)), timeout);
                resultMap.put(investCityMap.get("city"), String.valueOf(CalculateUtil.calculatePercentage(Long.parseLong(String.valueOf(investCityMap.get("totalCount"))), totalScaleCount, 1)));
            }
        }
        return resultMap;
    }

    public Map<String, String> findInvestAmountScaleTop3(Date endDate) {
        Map<String, String> resultMap = new LinkedHashMap<>();
        if (redisWrapperClient.exists(getRedisKeyFromTemplateByDate(AMOUNT_INVEST_CITY_SCALE_INFO_PUBLISH_KEY_TEMPLATE, endDate))) {
            Map<String, String> map = redisWrapperClient.hgetAll(getRedisKeyFromTemplateByDate(AMOUNT_INVEST_CITY_SCALE_INFO_PUBLISH_KEY_TEMPLATE, endDate));
            for (Map.Entry<String, String> entry : map.entrySet()) {
                resultMap.put(entry.getKey(), entry.getValue());
            }
        } else {
            long totalScaleAmount = investMapper.findInvestAmountScale(endDate);
            List<Map<String, String>> investCityAmountScaleList = investMapper.findInvestAmountScaleTop3(endDate);
            for (Map<String, String> investCityAmountMap : investCityAmountScaleList) {
                redisWrapperClient.hset(getRedisKeyFromTemplateByDate(AMOUNT_INVEST_CITY_SCALE_INFO_PUBLISH_KEY_TEMPLATE, endDate),
                        investCityAmountMap.get("city"), String.valueOf(CalculateUtil.calculatePercentage(Long.parseLong(String.valueOf(investCityAmountMap.get("sumAmount"))), totalScaleAmount, 1)), timeout);
                resultMap.put(investCityAmountMap.get("city"), String.valueOf(CalculateUtil.calculatePercentage(Long.parseLong(String.valueOf(investCityAmountMap.get("sumAmount"))), totalScaleAmount, 1)));
            }
        }
        return resultMap;
    }

    @Override
    public long findUserSumInterest(Date endDate) {
        long userSumInterest;
        if (redisWrapperClient.exists(REDIS_USER_SUM_INTEREST)) {
            userSumInterest = Long.parseLong(redisWrapperClient.get(REDIS_USER_SUM_INTEREST));
        } else {
            userSumInterest = loanRepayMapper.findSumActualInterest(endDate) + userBillMapper.findUserSumInterest(endDate);
            redisWrapperClient.setex(REDIS_USER_SUM_INTEREST, timeout, Long.toString(userSumInterest));
        }
        return userSumInterest;
    }

    @Override
    public List<OperationDataAgeDataDto> convertMapToOperationDataAgeDataDto() {
        Map<String, String> ageDistributionMap = findAgeDistributionByAge(new Date());
        Set<Map.Entry<String, String>> ageDistributionEntries = ageDistributionMap.entrySet();
        List<OperationDataAgeDataDto> operationDataAgeDataDtoList = Lists.newArrayList();
        for (Map.Entry<String, String> ageDistributionEntry : ageDistributionEntries) {
            OperationDataAgeDataDto operationDataAgeResponseDataDto = new OperationDataAgeDataDto();
            operationDataAgeResponseDataDto.setName(AgeDistributionType.getNameByAgeStage(Integer.parseInt(ageDistributionEntry.getKey())));
            operationDataAgeResponseDataDto.setScale(ageDistributionEntry.getValue());
            operationDataAgeDataDtoList.add(operationDataAgeResponseDataDto);
        }
        return operationDataAgeDataDtoList;
    }

    @Override
    public List<OperationDataInvestCityDataDto> convertMapToOperationDataInvestCityDataDto() {
        Map<String, String> investCityListMap = findCountInvestCityScaleTop3(new Date());
        Set<Map.Entry<String, String>> investCityEntries = investCityListMap.entrySet();
        List<OperationDataInvestCityDataDto> operationDataInvestCityDataDtoList = Lists.newArrayList();
        for (Map.Entry<String, String> investCityEntry : investCityEntries) {
            OperationDataInvestCityDataDto operationDataInvestCityDataDto = new OperationDataInvestCityDataDto();
            operationDataInvestCityDataDto.setCity(investCityEntry.getKey());
            operationDataInvestCityDataDto.setScale(investCityEntry.getValue());
            operationDataInvestCityDataDtoList.add(operationDataInvestCityDataDto);
        }
        Collections.sort(operationDataInvestCityDataDtoList, (o1, o2) -> Double.compare(Double.parseDouble(o2.getScale()), Double.parseDouble(o1.getScale())));
        return operationDataInvestCityDataDtoList;
    }

    @Override
    public List<OperationDataInvestAmountDataDto> convertMapToOperationDataInvestAmountDataDto() {
        Map<String, String> investAmountMap = findInvestAmountScaleTop3(new Date());
        Set<Map.Entry<String, String>> investAmountEntries = investAmountMap.entrySet();
        List<OperationDataInvestAmountDataDto> operationDataInvestAmountDataDtoList = Lists.newArrayList();
        for (Map.Entry<String, String> investAmountEntry : investAmountEntries) {
            OperationDataInvestAmountDataDto operationDataInvestAmountResponseDataDto = new OperationDataInvestAmountDataDto();
            operationDataInvestAmountResponseDataDto.setCity(investAmountEntry.getKey());
            operationDataInvestAmountResponseDataDto.setScale(investAmountEntry.getValue());
            operationDataInvestAmountDataDtoList.add(operationDataInvestAmountResponseDataDto);
        }
        Collections.sort(operationDataInvestAmountDataDtoList, (o1, o2) -> Double.compare(Double.parseDouble(o2.getScale()), Double.parseDouble(o1.getScale())));
        return operationDataInvestAmountDataDtoList;
    }

    public Map<String, String> findAgeDistributionByAge(Date endDate) {
        Map<String, String> resultGroupMap = new LinkedHashMap<>();
        if (redisWrapperClient.exists(getRedisKeyFromTemplateByDate(AGE_DISTRIBUTION_INFO_PUBLISH_KEY_TEMPLATE, endDate))) {
            Map<String, String> map = redisWrapperClient.hgetAll(getRedisKeyFromTemplateByDate(AGE_DISTRIBUTION_INFO_PUBLISH_KEY_TEMPLATE, endDate));
            for (Map.Entry<String, String> entry : map.entrySet()) {
                resultGroupMap.put(entry.getKey(), entry.getValue());
            }
        } else {
            List<Map<String, String>> AgeDistributionList = operationDataMapper.findAgeDistributionByAge(endDate);
            Map<String, String> resultMap = new LinkedHashMap<>();

            for (Map<String, String> AgeDistributionMap : AgeDistributionList) {
                resultMap.put(String.valueOf(Integer.parseInt(String.valueOf(AgeDistributionMap.get("age")).replace(".0", ""))), String.valueOf(AgeDistributionMap.get("totalCount")));
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
                int ageStage = Integer.parseInt(ageGroupDistributionEntry.getKey()) / 10;
                switch (ageStage) {
                    case 1:
                        totalUnder20UserCount += Long.parseLong(ageGroupDistributionEntry.getValue());
                        break;
                    case 2:
                        totalBETWEEN_20_AND_30UserCount += Long.parseLong(ageGroupDistributionEntry.getValue());
                        break;
                    case 3:
                        totalBETWEEN_30_AND_40UserCount += Long.parseLong(ageGroupDistributionEntry.getValue());
                        break;
                    case 4:
                        totalBETWEEN_40_AND_50UserCount += Long.parseLong(ageGroupDistributionEntry.getValue());
                        break;
                    case 5:
                        totalMORE_THAN_50UserCount += Long.parseLong(ageGroupDistributionEntry.getValue());
                        break;
                    default:
                        totalMORE_THAN_50UserCount += Long.parseLong(ageGroupDistributionEntry.getValue());
                }
            }
            resultGroupMap.put(String.valueOf(AgeDistributionType.UNDER_20.getAgeStage()), String.valueOf(CalculateUtil.calculatePercentage(totalUnder20UserCount, totalUserCount, 1)));
            resultGroupMap.put(String.valueOf(AgeDistributionType.BETWEEN_20_AND_30.getAgeStage()), String.valueOf(CalculateUtil.calculatePercentage(totalBETWEEN_20_AND_30UserCount, totalUserCount, 1)));
            resultGroupMap.put(String.valueOf(AgeDistributionType.BETWEEN_30_AND_40.getAgeStage()), String.valueOf(CalculateUtil.calculatePercentage(totalBETWEEN_30_AND_40UserCount, totalUserCount, 1)));
            resultGroupMap.put(String.valueOf(AgeDistributionType.BETWEEN_40_AND_50.getAgeStage()), String.valueOf(CalculateUtil.calculatePercentage(totalBETWEEN_40_AND_50UserCount, totalUserCount, 1)));
            resultGroupMap.put(String.valueOf(AgeDistributionType.MORE_THAN_50.getAgeStage()), String.valueOf(CalculateUtil.calculatePercentage(totalMORE_THAN_50UserCount, totalUserCount, 1)));

            redisWrapperClient.hset(getRedisKeyFromTemplateByDate(AGE_DISTRIBUTION_INFO_PUBLISH_KEY_TEMPLATE, endDate),
                    String.valueOf(AgeDistributionType.UNDER_20.getAgeStage()), String.valueOf(CalculateUtil.calculatePercentage(totalUnder20UserCount, totalUserCount, 1)), timeout);
            redisWrapperClient.hset(getRedisKeyFromTemplateByDate(AGE_DISTRIBUTION_INFO_PUBLISH_KEY_TEMPLATE, endDate),
                    String.valueOf(AgeDistributionType.BETWEEN_20_AND_30.getAgeStage()), String.valueOf(CalculateUtil.calculatePercentage(totalBETWEEN_20_AND_30UserCount, totalUserCount, 1)), timeout);
            redisWrapperClient.hset(getRedisKeyFromTemplateByDate(AGE_DISTRIBUTION_INFO_PUBLISH_KEY_TEMPLATE, endDate),
                    String.valueOf(AgeDistributionType.BETWEEN_30_AND_40.getAgeStage()), String.valueOf(CalculateUtil.calculatePercentage(totalBETWEEN_30_AND_40UserCount, totalUserCount, 1)), timeout);
            redisWrapperClient.hset(getRedisKeyFromTemplateByDate(AGE_DISTRIBUTION_INFO_PUBLISH_KEY_TEMPLATE, endDate),
                    String.valueOf(AgeDistributionType.BETWEEN_40_AND_50.getAgeStage()), String.valueOf(CalculateUtil.calculatePercentage(totalBETWEEN_40_AND_50UserCount, totalUserCount, 1)), timeout);
            redisWrapperClient.hset(getRedisKeyFromTemplateByDate(AGE_DISTRIBUTION_INFO_PUBLISH_KEY_TEMPLATE, endDate),
                    String.valueOf(AgeDistributionType.MORE_THAN_50.getAgeStage()), String.valueOf(CalculateUtil.calculatePercentage(totalMORE_THAN_50UserCount, totalUserCount, 1)), timeout);

        }
        return resultGroupMap;
    }

}
