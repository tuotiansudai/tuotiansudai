package com.tuotiansudai.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.enums.AgeDistributionType;
import com.tuotiansudai.enums.AgeNewDistributionType;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.service.OperationDataService;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.CalculateUtil;
import com.tuotiansudai.util.JsonConverter;
import com.tuotiansudai.util.RedisWrapperClient;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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
    @Autowired
    private LoanMapper loanMapper;
    @Autowired
    private LoanerDetailsMapper loanerDetailsMapper;

    private static final String CHART_INFO_PUBLISH_KEY_TEMPLATE = "web:info:publish:chart:{0}";
    private static final String TABLE_INFO_PUBLISH_KEY_TEMPLATE = "web:info:publish:table:{0}";
    private static final String SCALE_GENDER_INFO_PUBLISH_KEY_TEMPLATE = "app:info:publish:scale:gender:{0}";
    private static final String LOANER_SCALE_GENDER_INFO_PUBLISH_KEY_TEMPLATE = "app:info:publish:loaner:scale:gender:{0}";
    private static final String AGE_DISTRIBUTION_INFO_PUBLISH_KEY_TEMPLATE = "app:info:publish:age:Distribution:{0}";
    private static final String NEW_AGE_DISTRIBUTION_INFO_PUBLISH_KEY_TEMPLATE = "app:info:publish:new:age:Distribution:{0}";
    private static final String LOANER_AGE_DISTRIBUTION_INFO_PUBLISH_KEY_TEMPLATE = "app:info:publish:loaner:age:Distribution:{0}";
    private static final String COUNT_INVEST_CITY_SCALE_INFO_PUBLISH_KEY_TEMPLATE = "app:info:publish:count:invest:city:scale:{0}";
    private static final String AMOUNT_INVEST_CITY_SCALE_INFO_PUBLISH_KEY_TEMPLATE = "app:info:publish:amount:invest:city:scale:{0}";
    private static final String COUNT_LOANER_CITY_SCALE_INFO_PUBLISH_KEY_TEMPLATE = "app:info:publish:count:loaner:city:scale:{0}";

    private static final String REDIS_OPERATION_DATA = "operationData";
    private static final String REDIS_USER_SUM_INTEREST = "userSumInterest";

    private static final int timeout = 60 * 60 * 24;
    private static final Date startOperationDate = new DateTime().withDate(2015, 7, 1).withTimeAtStartOfDay().toDate();

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

    private OperationDataDto loadOperationDataDtoFromDatabase(Date endDate) {
        OperationDataDto operationDataDto = new OperationDataDto();
        operationDataDto.setOperationDays(calOperationTime(endDate));

        long tradeAmount = investMapper.sumInvestAmount(null, null, null,
                null, null, startOperationDate, new DateTime().withMillis(endDate.getTime()).withTimeAtStartOfDay().toDate(),
                InvestStatus.SUCCESS, null);
        operationDataDto.setTradeAmount(AmountConverter.convertCentToString(tradeAmount));

        operationDataDto.setUsersCount(userMapper.findUsersCount());
        operationDataDto.setInvestUsersCount(investMapper.findInvestorCountByLoanStatus(null));
        List<LoanModel> loanModels = loanMapper.findSuccessLoanOutLoan();

        long sumLoanAmount = loanModels.stream().mapToLong(LoanModel::getLoanAmount).sum();
        operationDataDto.setSumLoanAmount(AmountConverter.convertCentToString(sumLoanAmount));
        operationDataDto.setSumLoanCount(String.valueOf(loanModels.size()));
        operationDataDto.setSumLoanerCount(String.valueOf(loanerDetailsMapper.getSumLoanerCountByLoanStatus(Lists.newArrayList(LoanStatus.REPAYING, LoanStatus.OVERDUE, LoanStatus.COMPLETE))));
        List<LoanRepayModel> loanRepayModels = loanRepayMapper.findNotCompleteLoanRepay();
        long sumExpectedAmount = loanRepayModels.stream().mapToLong(LoanRepayModel::getCorpus).sum();
        long sumOverDueAmount = loanRepayModels.stream().filter(loanRepayModel -> loanRepayModel.getActualRepayDate() == null && loanRepayModel.getRepayDate().before(endDate)).mapToLong(LoanRepayModel::getCorpus).sum();
        long sumExpectedInterestAmount = loanRepayModels.stream().mapToLong(loanRepayModel -> loanRepayModel.getExpectedInterest() + loanRepayModel.getDefaultInterest() + loanRepayModel.getOverdueInterest()).sum();
        operationDataDto.setSumExpectedAmount(AmountConverter.convertCentToString(sumExpectedAmount));
        operationDataDto.setSumOverDueAmount(AmountConverter.convertCentToString(sumOverDueAmount));
        operationDataDto.setSumExpectedInterestAmount(AmountConverter.convertCentToString(sumExpectedInterestAmount));
        operationDataDto.setSumRepayIngInvestCount(String.valueOf(investMapper.sumInvestCountByLoanStatus(Lists.newArrayList(LoanStatus.REPAYING, LoanStatus.OVERDUE), startOperationDate, new DateTime().withMillis(endDate.getTime()).withTimeAtStartOfDay().toDate())));
        operationDataDto.setAvgInvestAmount(AmountConverter.convertCentToString( tradeAmount / investMapper.sumInvestCountByLoanStatus(null, startOperationDate, new DateTime().withMillis(endDate.getTime()).withTimeAtStartOfDay().toDate())));
        operationDataDto.setSumNotCompleteInvestorCount(String.valueOf(investMapper.findInvestorCountByLoanStatus(Lists.newArrayList(LoanStatus.REPAYING, LoanStatus.OVERDUE))));

        List<Long> sumInvestAmountGroupByLoginNameByTopTens = investMapper.sumInvestAmountGroupByLoginNameByTopTen(startOperationDate, new DateTime().withMillis(endDate.getTime()).withTimeAtStartOfDay().toDate());
        long maxSingleInvestAmount = sumInvestAmountGroupByLoginNameByTopTens.get(0);
        long maxTenInvestAmount = sumInvestAmountGroupByLoginNameByTopTens.stream().mapToLong(i -> i).sum();
        operationDataDto.setMaxSingleInvestAmountRate(String.valueOf(new BigDecimal(maxSingleInvestAmount).divide(new BigDecimal(tradeAmount), 4, BigDecimal.ROUND_DOWN)));
        operationDataDto.setMaxTenInvestAmountRate(String.valueOf(new BigDecimal(maxTenInvestAmount).divide(new BigDecimal(tradeAmount), 4, BigDecimal.ROUND_DOWN)));

        operationDataDto.setSumNotCompleteLoanerCount(String.valueOf(loanerDetailsMapper.getSumLoanerCountByLoanStatus(Lists.newArrayList(LoanStatus.REPAYING, LoanStatus.OVERDUE))));
        operationDataDto.setAvgLoanAmount(AmountConverter.convertCentToString(sumLoanAmount / loanModels.size()));

        List<Long> sumLoanAmountGroupByIdentityByTopTens = loanMapper.sumLoanAmountGroupByIdentityByTopTen();
        long maxSingleLoanAmount = sumLoanAmountGroupByIdentityByTopTens.get(0);
        long maxTenLoanAmount = sumLoanAmountGroupByIdentityByTopTens.stream().mapToLong(i -> i).sum();
        operationDataDto.setMaxSingleLoanAmountRate(String.valueOf(new BigDecimal(maxSingleLoanAmount).divide(new BigDecimal(sumLoanAmount), 4, BigDecimal.ROUND_DOWN)));
        operationDataDto.setMaxTenLoanAmountRate(String.valueOf(new BigDecimal(maxTenLoanAmount).divide(new BigDecimal(sumLoanAmount), 4, BigDecimal.ROUND_DOWN)));

        long sumRepayingLoanCount = loanRepayModels.stream().map(LoanRepayModel::getLoanId).distinct().count();
        long sumOverDueLoanCount = loanRepayModels.stream()
                .filter(loanRepayModel -> loanRepayModel.getActualRepayDate() == null
                        && loanRepayModel.getRepayDate().before(endDate))
                .map(LoanRepayModel::getLoanId).distinct().count();
        operationDataDto.setLoanOverDueRate(String.valueOf(sumRepayingLoanCount == 0 ? 0 : new BigDecimal(sumOverDueLoanCount).divide(new BigDecimal(sumRepayingLoanCount), 4, BigDecimal.ROUND_DOWN)));
        operationDataDto.setAmountOverDueRate(String.valueOf(sumOverDueAmount == 0 ? 0 : new BigDecimal(sumOverDueAmount).divide(new BigDecimal(sumExpectedAmount), 4, BigDecimal.ROUND_DOWN)));

        long amountOverDueLess90Amount = this.findAmountOverdueAmountByOverdueDay(90, endDate, loanRepayModels);
        long amountOverDue90To180Amount = this.findAmountOverdueAmountByOverdueDay(180, endDate, loanRepayModels) - amountOverDueLess90Amount;
        long amountOverDueGreater180Amount = sumExpectedAmount - amountOverDueLess90Amount - amountOverDue90To180Amount ;
        operationDataDto.setAmountOverDueLess90Rate(String.valueOf(amountOverDueLess90Amount == 0 ? 0 : new BigDecimal(amountOverDueLess90Amount).divide(new BigDecimal(sumExpectedAmount), 4, BigDecimal.ROUND_DOWN)));
        operationDataDto.setAmountOverDue90To180Rate(String.valueOf(amountOverDue90To180Amount == 0 ? 0 : new BigDecimal(amountOverDue90To180Amount).divide(new BigDecimal(sumExpectedAmount), 4, BigDecimal.ROUND_DOWN)));
        operationDataDto.setAmountOverDueGreater180Rate(String.valueOf(amountOverDueGreater180Amount == 0 ? 0 : new BigDecimal(amountOverDueGreater180Amount).divide(new BigDecimal(sumExpectedAmount), 4, BigDecimal.ROUND_DOWN)));

        long loanOverDueLess90Rate = this.findLoanOverdueAmountByOverdueDay(90, endDate, loanRepayModels);
        long loanOverDue90To180Rate = this.findLoanOverdueAmountByOverdueDay(180, endDate, loanRepayModels) - loanOverDueLess90Rate;
        long loanOverDueGreater180Rate = sumOverDueLoanCount - loanOverDueLess90Rate - loanOverDue90To180Rate;
        operationDataDto.setLoanOverDueLess90Rate(String.valueOf(loanOverDueLess90Rate == 0 ? 0 : new BigDecimal(loanOverDueLess90Rate).divide(new BigDecimal(sumRepayingLoanCount), 4, BigDecimal.ROUND_DOWN)));
        operationDataDto.setLoanOverDue90To180Rate(String.valueOf(loanOverDue90To180Rate == 0 ? 0 : new BigDecimal(loanOverDue90To180Rate).divide(new BigDecimal(sumRepayingLoanCount), 4, BigDecimal.ROUND_DOWN)));
        operationDataDto.setLoanOverDueGreater180Rate(String.valueOf(loanOverDueGreater180Rate == 0 ? 0 : new BigDecimal(loanOverDueGreater180Rate).divide(new BigDecimal(sumRepayingLoanCount), 4, BigDecimal.ROUND_DOWN)));

        operationDataDto.setLoanerOverDueAmount(AmountConverter.convertCentToString(sumOverDueAmount));
        operationDataDto.setLoanerOverDueCount(String.valueOf(loanRepayModels.stream().filter(loanRepayModel -> loanRepayModel.getActualRepayDate() == null && loanRepayModel.getRepayDate().before(endDate))
                .map(LoanRepayModel::getLoanId)
                .distinct()
                .count()));

        List<Integer> sexList = findScaleByGender(new Date());
        if (sexList.size() > 1) {
            operationDataDto.setFemaleScale(String.valueOf(CalculateUtil.calculatePercentage(sexList.get(0), sexList.get(0) + sexList.get(1), 1)));
            operationDataDto.setMaleScale(String.valueOf(100 - CalculateUtil.calculatePercentage(sexList.get(0), sexList.get(0) + sexList.get(1), 1)));
        }

        List<Integer> loanerSexList = findLoanerScaleByGender(new Date());
        if (loanerSexList.size() > 1) {
            operationDataDto.setLoanerFemaleScale(String.valueOf(CalculateUtil.calculatePercentage(loanerSexList.get(0), loanerSexList.get(0) + loanerSexList.get(1), 1)));
            operationDataDto.setLoanerMaleScale(String.valueOf(100 - CalculateUtil.calculatePercentage(loanerSexList.get(0), loanerSexList.get(0) + loanerSexList.get(1), 1)));
        }

        operationDataDto.setTotalInterest(String.valueOf(findUserSumInterest(new Date())));
        operationDataDto.setAgeDistribution(convertMapToOperationDataNewAgeDataDto());
        operationDataDto.setLoanerAgeDistribution(convertMapToOperationDataLoanerAgeDataDto());
        operationDataDto.setInvestAmountScaleTop3(convertMapToOperationDataInvestAmountDataDto());
        operationDataDto.setInvestCityScaleTop5(convertMapToOperationDataInvestCityDataDto());
        operationDataDto.setLoanerCityScaleTop5(convertMapToOperationDataLoanerCityDataDto());
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
        return operationDataDto;
    }

    private OperationDataDto loadOperationDataDtoFromRedis(Date endDate) {
        OperationDataDto operationDataDto = new OperationDataDto();
        final String redisInfoPublishKey = getRedisKeyFromTemplateByDate(CHART_INFO_PUBLISH_KEY_TEMPLATE, endDate);
        try {
            operationDataDto = JsonConverter.readValue(redisWrapperClient.hget(redisInfoPublishKey, REDIS_OPERATION_DATA), OperationDataDto.class);
        } catch (IOException e) {
            return loadOperationDataDtoFromDatabase(endDate);
        }

        operationDataDto.setOperationDays(calOperationTime(endDate));
        operationDataDto.setTotalInterest(String.valueOf(findUserSumInterest(endDate)));
        operationDataDto.setAgeDistribution(convertMapToOperationDataNewAgeDataDto());

        operationDataDto.setLoanerAgeDistribution(convertMapToOperationDataLoanerAgeDataDto());
        operationDataDto.setInvestCityScaleTop5(convertMapToOperationDataInvestCityDataDto());
        operationDataDto.setLoanerCityScaleTop5(convertMapToOperationDataLoanerCityDataDto());
        operationDataDto.setInvestAmountScaleTop3(convertMapToOperationDataInvestAmountDataDto());
        List<Integer> sexList = findScaleByGender(new Date());
        if (sexList.size() > 1) {
            operationDataDto.setFemaleScale(String.valueOf(CalculateUtil.calculatePercentage(sexList.get(0), sexList.get(0) + sexList.get(1), 1)));
            operationDataDto.setMaleScale(String.valueOf(100 - CalculateUtil.calculatePercentage(sexList.get(0), sexList.get(0) + sexList.get(1), 1)));
        }
        List<Integer> loanerSexList = findLoanerScaleByGender(new Date());
        if (loanerSexList.size() > 1) {
            operationDataDto.setLoanerFemaleScale(String.valueOf(CalculateUtil.calculatePercentage(loanerSexList.get(0), loanerSexList.get(0) + loanerSexList.get(1), 1)));
            operationDataDto.setLoanerMaleScale(String.valueOf(100 - CalculateUtil.calculatePercentage(loanerSexList.get(0), loanerSexList.get(0) + loanerSexList.get(1), 1)));
        }
        return operationDataDto;
    }

    private void updateRedis(OperationDataDto operationDataDto, Date endTime) {
        final String redisInfoPublishKey = getRedisKeyFromTemplateByDate(CHART_INFO_PUBLISH_KEY_TEMPLATE, endTime);
        try {
            redisWrapperClient.hset(redisInfoPublishKey, REDIS_OPERATION_DATA, JsonConverter.writeValueAsString(operationDataDto), timeout);
        } catch (JsonProcessingException ignored) {
        }
    }

    @Override
    public OperationDataDto getOperationDataFromRedis(Date endDate) {
        OperationDataDto operationDataDto = new OperationDataDto();
        if (redisWrapperClient.exists(getRedisKeyFromTemplateByDate(CHART_INFO_PUBLISH_KEY_TEMPLATE, endDate))) {
            operationDataDto = loadOperationDataDtoFromRedis(endDate);
        } else {
            operationDataDto = loadOperationDataDtoFromDatabase(endDate);
            updateRedis(operationDataDto, endDate);
        }
        operationDataDto.setNow(new Date());
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

    public List<Integer> findLoanerScaleByGender(Date endDate) {
        List<Integer> loanerScaleByGenderList = Lists.newArrayList();
        if (redisWrapperClient.exists(getRedisKeyFromTemplateByDate(LOANER_SCALE_GENDER_INFO_PUBLISH_KEY_TEMPLATE, endDate))) {
            Map<String, String> map = redisWrapperClient.hgetAll(getRedisKeyFromTemplateByDate(LOANER_SCALE_GENDER_INFO_PUBLISH_KEY_TEMPLATE, endDate));
            for (Map.Entry<String, String> entry : map.entrySet()) {
                loanerScaleByGenderList.add(Integer.parseInt(entry.getValue()));
            }
        } else {
            loanerScaleByGenderList = operationDataMapper.findLoanerScaleByGender();
            for (Integer value : loanerScaleByGenderList) {
                redisWrapperClient.hset(getRedisKeyFromTemplateByDate(LOANER_SCALE_GENDER_INFO_PUBLISH_KEY_TEMPLATE, endDate),
                        String.valueOf(value), String.valueOf(value), timeout);
            }
        }
        return loanerScaleByGenderList;
    }

    public Map<String, String> findCountInvestCityScaleTop5(Date endDate) {
        Map<String, String> resultMap = new LinkedHashMap<>();
        if (redisWrapperClient.exists(getRedisKeyFromTemplateByDate(COUNT_INVEST_CITY_SCALE_INFO_PUBLISH_KEY_TEMPLATE, endDate))) {
            Map<String, String> map = redisWrapperClient.hgetAll(getRedisKeyFromTemplateByDate(COUNT_INVEST_CITY_SCALE_INFO_PUBLISH_KEY_TEMPLATE, endDate));
            for (Map.Entry<String, String> entry : map.entrySet()) {
                resultMap.put(entry.getKey(), entry.getValue());
            }
        } else {
            long totalScaleCount = operationDataMapper.findCountInvestCityScale(endDate);
            List<Map<String, String>> investCityList = operationDataMapper.findCountInvestCityScaleTop5(endDate);
            for (Map<String, String> investCityMap : investCityList) {
                if (StringUtils.isNotEmpty(investCityMap.get("province"))) {
                    redisWrapperClient.hset(getRedisKeyFromTemplateByDate(COUNT_INVEST_CITY_SCALE_INFO_PUBLISH_KEY_TEMPLATE, endDate),
                            Lists.newArrayList("北京", "天津", "重庆", "上海").contains(investCityMap.get("province")) ? String.format("%s市", investCityMap.get("province")) : String.format("%s省", investCityMap.get("province")),
                            String.valueOf(CalculateUtil.calculatePercentage(Long.parseLong(String.valueOf(investCityMap.get("totalCount"))), totalScaleCount, 1)), timeout);
                    resultMap.put(Lists.newArrayList("北京", "天津", "重庆", "上海").contains(investCityMap.get("province")) ? String.format("%s市", investCityMap.get("province")) : String.format("%s省", investCityMap.get("province")), String.valueOf(CalculateUtil.calculatePercentage(Long.parseLong(String.valueOf(investCityMap.get("totalCount"))), totalScaleCount, 1)));
                }

            }
        }
        return resultMap;
    }

    public Map<String, String> findCountLoanerCityScaleTop5(Date endDate) {
        Map<String, String> resultMap = new LinkedHashMap<>();
        if (redisWrapperClient.exists(getRedisKeyFromTemplateByDate(COUNT_LOANER_CITY_SCALE_INFO_PUBLISH_KEY_TEMPLATE, endDate))) {
            Map<String, String> map = redisWrapperClient.hgetAll(getRedisKeyFromTemplateByDate(COUNT_LOANER_CITY_SCALE_INFO_PUBLISH_KEY_TEMPLATE, endDate));
            for (Map.Entry<String, String> entry : map.entrySet()) {
                resultMap.put(entry.getKey(), entry.getValue());
            }
        } else {
            long totalScaleCount = operationDataMapper.findCountLoanerCityScale();
            List<Map<String, String>> loanerCityList = operationDataMapper.findCountLoanerCityScaleTop5();
            for (Map<String, String> loanerCityMap : loanerCityList) {
                if (StringUtils.isNotEmpty(loanerCityMap.get("city"))) {
                    redisWrapperClient.hset(getRedisKeyFromTemplateByDate(COUNT_LOANER_CITY_SCALE_INFO_PUBLISH_KEY_TEMPLATE, endDate),
                            loanerCityMap.get("city"), String.valueOf(CalculateUtil.calculatePercentage(Long.parseLong(String.valueOf(loanerCityMap.get("totalCount"))), totalScaleCount, 1)), timeout);
                    resultMap.put(loanerCityMap.get("city"), String.valueOf(CalculateUtil.calculatePercentage(Long.parseLong(String.valueOf(loanerCityMap.get("totalCount"))), totalScaleCount, 1)));
                }

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
                if (StringUtils.isNotEmpty(investCityAmountMap.get("city"))) {
                    redisWrapperClient.hset(getRedisKeyFromTemplateByDate(AMOUNT_INVEST_CITY_SCALE_INFO_PUBLISH_KEY_TEMPLATE, endDate),
                            investCityAmountMap.get("city"), String.valueOf(CalculateUtil.calculatePercentage(Long.parseLong(String.valueOf(investCityAmountMap.get("sumAmount"))), totalScaleAmount, 1)), timeout);
                    resultMap.put(investCityAmountMap.get("city"), String.valueOf(CalculateUtil.calculatePercentage(Long.parseLong(String.valueOf(investCityAmountMap.get("sumAmount"))), totalScaleAmount, 1)));
                }
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
        ageDistributionMap = ageDistributionMap.entrySet().stream().sorted(Map.Entry.<String, String>comparingByValue().reversed()).collect(Collectors.toMap(c -> c.getKey(), c -> c.getValue()));
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
    public List<OperationDataAgeDataDto> convertMapToOperationDataNewAgeDataDto() {
        Map<String, String> ageDistributionMap = findNewAgeDistributionByAge(new Date());
        ageDistributionMap = ageDistributionMap.entrySet().stream().sorted(Map.Entry.<String, String>comparingByValue().reversed()).collect(Collectors.toMap(c -> c.getKey(), c -> c.getValue()));
        Set<Map.Entry<String, String>> ageDistributionEntries = ageDistributionMap.entrySet();
        List<OperationDataAgeDataDto> operationDataAgeDataDtoList = Lists.newArrayList();
        for (Map.Entry<String, String> ageDistributionEntry : ageDistributionEntries) {
            OperationDataAgeDataDto operationDataAgeResponseDataDto = new OperationDataAgeDataDto();
            operationDataAgeResponseDataDto.setName(AgeNewDistributionType.getNameByAgeStage(Integer.parseInt(ageDistributionEntry.getKey())));
            operationDataAgeResponseDataDto.setScale(ageDistributionEntry.getValue());
            operationDataAgeDataDtoList.add(operationDataAgeResponseDataDto);
        }
        return operationDataAgeDataDtoList;
    }

    @Override
    public List<OperationDataLoanerAgeDataDto> convertMapToOperationDataLoanerAgeDataDto() {
        Map<String, String> loanerAgeDistributionMap = findLoanerAgeDistributionByAge(new Date());
        loanerAgeDistributionMap = loanerAgeDistributionMap.entrySet().stream().sorted(Map.Entry.<String, String>comparingByValue().reversed()).collect(Collectors.toMap(c -> c.getKey(), c -> c.getValue()));
        Set<Map.Entry<String, String>> loanerAgeDistributionEntries = loanerAgeDistributionMap.entrySet();
        List<OperationDataLoanerAgeDataDto> operationDataLoanerAgeDataDtoList = Lists.newArrayList();
        for (Map.Entry<String, String> loanerAgeDistributionEntry : loanerAgeDistributionEntries) {
            OperationDataLoanerAgeDataDto operationDataLoanerAgeResponseDataDto = new OperationDataLoanerAgeDataDto();
            operationDataLoanerAgeResponseDataDto.setName(AgeNewDistributionType.getNameByAgeStage(Integer.parseInt(loanerAgeDistributionEntry.getKey())));
            operationDataLoanerAgeResponseDataDto.setScale(loanerAgeDistributionEntry.getValue());
            operationDataLoanerAgeDataDtoList.add(operationDataLoanerAgeResponseDataDto);
        }
        return operationDataLoanerAgeDataDtoList;
    }

    @Override
    public List<OperationDataInvestCityDataDto> convertMapToOperationDataInvestCityDataDto() {
        Map<String, String> investCityListMap = findCountInvestCityScaleTop5(new Date());
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
    public List<OperationDataLoanerCityDataDto> convertMapToOperationDataLoanerCityDataDto() {
        Map<String, String> loanerCityListMap = findCountLoanerCityScaleTop5(new Date());
        Set<Map.Entry<String, String>> loanerCityEntries = loanerCityListMap.entrySet();
        List<OperationDataLoanerCityDataDto> operationDataLoanerCityDataDtoList = Lists.newArrayList();
        for (Map.Entry<String, String> loanerCityEntry : loanerCityEntries) {
            OperationDataLoanerCityDataDto operationDataLoanerCityDataDto = new OperationDataLoanerCityDataDto();
            operationDataLoanerCityDataDto.setCity(loanerCityEntry.getKey());
            operationDataLoanerCityDataDto.setScale(loanerCityEntry.getValue());
            operationDataLoanerCityDataDtoList.add(operationDataLoanerCityDataDto);
        }
        Collections.sort(operationDataLoanerCityDataDtoList, (o1, o2) -> Double.compare(Double.parseDouble(o2.getScale()), Double.parseDouble(o1.getScale())));
        return operationDataLoanerCityDataDtoList;
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


    public Map<String, String> findNewAgeDistributionByAge(Date endDate) {
        Map<String, String> resultGroupMap = new LinkedHashMap<>();
        if (redisWrapperClient.exists(getRedisKeyFromTemplateByDate(NEW_AGE_DISTRIBUTION_INFO_PUBLISH_KEY_TEMPLATE, endDate))) {
            Map<String, String> map = redisWrapperClient.hgetAll(getRedisKeyFromTemplateByDate(NEW_AGE_DISTRIBUTION_INFO_PUBLISH_KEY_TEMPLATE, endDate));
            for (Map.Entry<String, String> entry : map.entrySet()) {
                resultGroupMap.put(entry.getKey(), entry.getValue());
            }
        } else {
            List<Map<String, String>> AgeDistributionList = operationDataMapper.findNewAgeDistributionByAge(endDate);
            Map<String, String> resultMap = new LinkedHashMap<>();

            for (Map<String, String> AgeDistributionMap : AgeDistributionList) {
                String age = String.valueOf(AgeDistributionMap.get("age"));
                if (StringUtils.isNotEmpty(age) && !"null".equals(age)) {
                    resultMap.put(String.valueOf(Integer.parseInt(String.valueOf(AgeDistributionMap.get("age")).replace(".0", ""))), String.valueOf(AgeDistributionMap.get("totalCount")));
                }
            }

            Set<Map.Entry<String, String>> ageGroupDistributionEntries = resultMap.entrySet();

            //先计算总数
            long totalUserCount = 0;
            long totalUnder20UserCount = 0;
            long totalBETWEEN_20_AND_35UserCount = 0;
            long totalBETWEEN_35_AND_50UserCount = 0;
            long totalMORE_THAN_50UserCount = 0;
            for (Map.Entry<String, String> ageGroupDistributionEntry : ageGroupDistributionEntries) {
                totalUserCount += Long.parseLong(ageGroupDistributionEntry.getValue());
            }
            //分别计算5个区间的人数
            for (Map.Entry<String, String> ageGroupDistributionEntry : ageGroupDistributionEntries) {
                if (Integer.parseInt(ageGroupDistributionEntry.getKey()) < 20) {
                    totalUnder20UserCount += Long.parseLong(ageGroupDistributionEntry.getValue());
                } else if (Integer.parseInt(ageGroupDistributionEntry.getKey()) >= 20 && Integer.parseInt(ageGroupDistributionEntry.getKey()) < 35) {
                    totalBETWEEN_20_AND_35UserCount += Long.parseLong(ageGroupDistributionEntry.getValue());
                } else if (Integer.parseInt(ageGroupDistributionEntry.getKey()) >= 35 && Integer.parseInt(ageGroupDistributionEntry.getKey()) < 50) {
                    totalBETWEEN_35_AND_50UserCount += Long.parseLong(ageGroupDistributionEntry.getValue());
                } else if (Integer.parseInt(ageGroupDistributionEntry.getKey()) >= 50) {
                    totalMORE_THAN_50UserCount += Long.parseLong(ageGroupDistributionEntry.getValue());
                }

            }
            resultGroupMap.put(String.valueOf(AgeNewDistributionType.UNDER_20.getAgeStage()), String.valueOf(CalculateUtil.calculatePercentage(totalUnder20UserCount, totalUserCount, 1)));
            resultGroupMap.put(String.valueOf(AgeNewDistributionType.BETWEEN_20_AND_35.getAgeStage()), String.valueOf(CalculateUtil.calculatePercentage(totalBETWEEN_20_AND_35UserCount, totalUserCount, 1)));
            resultGroupMap.put(String.valueOf(AgeNewDistributionType.BETWEEN_35_AND_50.getAgeStage()), String.valueOf(CalculateUtil.calculatePercentage(totalBETWEEN_35_AND_50UserCount, totalUserCount, 1)));
            resultGroupMap.put(String.valueOf(AgeNewDistributionType.MORE_THAN_50.getAgeStage()), String.valueOf(CalculateUtil.calculatePercentage(totalMORE_THAN_50UserCount, totalUserCount, 1)));

            redisWrapperClient.hset(getRedisKeyFromTemplateByDate(NEW_AGE_DISTRIBUTION_INFO_PUBLISH_KEY_TEMPLATE, endDate),
                    String.valueOf(AgeNewDistributionType.UNDER_20.getAgeStage()), String.valueOf(CalculateUtil.calculatePercentage(totalUnder20UserCount, totalUserCount, 1)), timeout);
            redisWrapperClient.hset(getRedisKeyFromTemplateByDate(NEW_AGE_DISTRIBUTION_INFO_PUBLISH_KEY_TEMPLATE, endDate),
                    String.valueOf(AgeNewDistributionType.BETWEEN_20_AND_35.getAgeStage()), String.valueOf(CalculateUtil.calculatePercentage(totalBETWEEN_20_AND_35UserCount, totalUserCount, 1)), timeout);
            redisWrapperClient.hset(getRedisKeyFromTemplateByDate(NEW_AGE_DISTRIBUTION_INFO_PUBLISH_KEY_TEMPLATE, endDate),
                    String.valueOf(AgeNewDistributionType.BETWEEN_35_AND_50.getAgeStage()), String.valueOf(CalculateUtil.calculatePercentage(totalBETWEEN_35_AND_50UserCount, totalUserCount, 1)), timeout);
            redisWrapperClient.hset(getRedisKeyFromTemplateByDate(NEW_AGE_DISTRIBUTION_INFO_PUBLISH_KEY_TEMPLATE, endDate),
                    String.valueOf(AgeNewDistributionType.MORE_THAN_50.getAgeStage()), String.valueOf(CalculateUtil.calculatePercentage(totalMORE_THAN_50UserCount, totalUserCount, 1)), timeout);

        }
        return resultGroupMap;
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

    public Map<String, String> findLoanerAgeDistributionByAge(Date endDate) {
        Map<String, String> resultGroupMap = new LinkedHashMap<>();
        if (redisWrapperClient.exists(getRedisKeyFromTemplateByDate(LOANER_AGE_DISTRIBUTION_INFO_PUBLISH_KEY_TEMPLATE, endDate))) {
            Map<String, String> map = redisWrapperClient.hgetAll(getRedisKeyFromTemplateByDate(LOANER_AGE_DISTRIBUTION_INFO_PUBLISH_KEY_TEMPLATE, endDate));
            for (Map.Entry<String, String> entry : map.entrySet()) {
                resultGroupMap.put(entry.getKey(), entry.getValue());
            }
        } else {
            List<Map<String, String>> loanerAgeDistributionList = operationDataMapper.findLoanerAgeDistributionByAge();
            Map<String, String> resultMap = new LinkedHashMap<>();

            for (Map<String, String> loanerAgeDistributionMap : loanerAgeDistributionList) {
                String age = String.valueOf(loanerAgeDistributionMap.get("age"));
                if (StringUtils.isNotEmpty(age) && !"null".equals(age)) {
                    resultMap.put(String.valueOf(Integer.parseInt(String.valueOf(loanerAgeDistributionMap.get("age")).replace(".0", ""))), String.valueOf(loanerAgeDistributionMap.get("totalCount")));
                }
            }

            Set<Map.Entry<String, String>> loanerAgeGroupDistributionEntries = resultMap.entrySet();

            //先计算总数
            long totalUserCount = 0;
            long totalUnder20UserCount = 0;
            long totalBETWEEN_20_AND_35UserCount = 0;
            long totalBETWEEN_35_AND_50UserCount = 0;
            long totalMORE_THAN_50UserCount = 0;
            for (Map.Entry<String, String> loanerAgeGroupDistributionEntry : loanerAgeGroupDistributionEntries) {
                totalUserCount += Long.parseLong(loanerAgeGroupDistributionEntry.getValue());
            }
            //分别计算5个区间的人数
            for (Map.Entry<String, String> loanerAgeGroupDistributionEntry : loanerAgeGroupDistributionEntries) {
                if (Integer.parseInt(loanerAgeGroupDistributionEntry.getKey()) < 20) {
                    totalUnder20UserCount += Long.parseLong(loanerAgeGroupDistributionEntry.getValue());
                } else if (Integer.parseInt(loanerAgeGroupDistributionEntry.getKey()) >= 20 && Integer.parseInt(loanerAgeGroupDistributionEntry.getKey()) < 35) {
                    totalBETWEEN_20_AND_35UserCount += Long.parseLong(loanerAgeGroupDistributionEntry.getValue());
                } else if (Integer.parseInt(loanerAgeGroupDistributionEntry.getKey()) >= 35 && Integer.parseInt(loanerAgeGroupDistributionEntry.getKey()) < 50) {
                    totalBETWEEN_35_AND_50UserCount += Long.parseLong(loanerAgeGroupDistributionEntry.getValue());
                } else if (Integer.parseInt(loanerAgeGroupDistributionEntry.getKey()) >= 50) {
                    totalMORE_THAN_50UserCount += Long.parseLong(loanerAgeGroupDistributionEntry.getValue());
                }

            }
            resultGroupMap.put(String.valueOf(AgeNewDistributionType.UNDER_20.getAgeStage()), String.valueOf(CalculateUtil.calculatePercentage(totalUnder20UserCount, totalUserCount, 1)));
            resultGroupMap.put(String.valueOf(AgeNewDistributionType.BETWEEN_20_AND_35.getAgeStage()), String.valueOf(CalculateUtil.calculatePercentage(totalBETWEEN_20_AND_35UserCount, totalUserCount, 1)));
            resultGroupMap.put(String.valueOf(AgeNewDistributionType.BETWEEN_35_AND_50.getAgeStage()), String.valueOf(CalculateUtil.calculatePercentage(totalBETWEEN_35_AND_50UserCount, totalUserCount, 1)));
            resultGroupMap.put(String.valueOf(AgeNewDistributionType.MORE_THAN_50.getAgeStage()), String.valueOf(CalculateUtil.calculatePercentage(totalMORE_THAN_50UserCount, totalUserCount, 1)));

            redisWrapperClient.hset(getRedisKeyFromTemplateByDate(LOANER_AGE_DISTRIBUTION_INFO_PUBLISH_KEY_TEMPLATE, endDate),
                    String.valueOf(AgeNewDistributionType.UNDER_20.getAgeStage()), String.valueOf(CalculateUtil.calculatePercentage(totalUnder20UserCount, totalUserCount, 1)), timeout);
            redisWrapperClient.hset(getRedisKeyFromTemplateByDate(LOANER_AGE_DISTRIBUTION_INFO_PUBLISH_KEY_TEMPLATE, endDate),
                    String.valueOf(AgeNewDistributionType.BETWEEN_20_AND_35.getAgeStage()), String.valueOf(CalculateUtil.calculatePercentage(totalBETWEEN_20_AND_35UserCount, totalUserCount, 1)), timeout);
            redisWrapperClient.hset(getRedisKeyFromTemplateByDate(LOANER_AGE_DISTRIBUTION_INFO_PUBLISH_KEY_TEMPLATE, endDate),
                    String.valueOf(AgeNewDistributionType.BETWEEN_35_AND_50.getAgeStage()), String.valueOf(CalculateUtil.calculatePercentage(totalBETWEEN_35_AND_50UserCount, totalUserCount, 1)), timeout);
            redisWrapperClient.hset(getRedisKeyFromTemplateByDate(LOANER_AGE_DISTRIBUTION_INFO_PUBLISH_KEY_TEMPLATE, endDate),
                    String.valueOf(AgeNewDistributionType.MORE_THAN_50.getAgeStage()), String.valueOf(CalculateUtil.calculatePercentage(totalMORE_THAN_50UserCount, totalUserCount, 1)), timeout);

        }
        return resultGroupMap;
    }

    private long findAmountOverdueAmountByOverdueDay(int overdueDays, Date endDate, List<LoanRepayModel> loanRepayModels){
        return loanRepayModels.stream()
                .filter(loanRepayModel -> loanRepayModel.getActualRepayDate() == null
                        && loanRepayModel.getRepayDate().after(new DateTime(endDate).minusDays(overdueDays).toDate()))
                .mapToLong(LoanRepayModel::getCorpus).sum();
    }

    private long findLoanOverdueAmountByOverdueDay(int overdueDays, Date endDate, List<LoanRepayModel> loanRepayModels){
        return loanRepayModels.stream()
                .filter(loanRepayModel -> loanRepayModel.getActualRepayDate() == null
                        && loanRepayModel.getRepayDate().after(new DateTime(endDate).minusDays(overdueDays).toDate()))
                .map(LoanRepayModel::getLoanId).distinct().count();
    }
}
