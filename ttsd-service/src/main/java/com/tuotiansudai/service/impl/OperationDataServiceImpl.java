package com.tuotiansudai.service.impl;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.OperationDataDto;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.service.OperationDataService;
import com.tuotiansudai.util.AmountConverter;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by huoxuanbo on 16/5/9.
 */
@Repository
public class OperationDataServiceImpl implements OperationDataService {
    @Autowired
    InvestMapper investMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    RedisWrapperClient redisWrapperClient;

    private final String REDIS_INFO_PUBLISH_KEY_TEMPLATE = "web:info:publish:chart:{0}";

    private final String USERS_COUNT = "userCount";
    private final String TRADE_AMOUNT = "tradeAmount";
    private final String OPERATION_DATA_MONTH = "operationDataMonth";
    private final String OPERATION_DATA_MONTH_AMOUNT = "operationDataMonthAmount";

    private final int timeout = 60 * 60 * 24;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMdd");

    class OperationDataServiceModel {
        final private String startOperationTime = "2015-07-01";
        private long userCount;
        private String investTotalAmount;
        private List<String> investMonthAmount = new LinkedList<>();
        private List<String> investMonth = new LinkedList<>();
        private int investMonthSize;

        public OperationDataServiceModel() {
            this.setInvestMonthSize();
        }

        public OperationDataServiceModel(OperationDataDto operationDataDto) {
            this.setUserCount(operationDataDto.getUsersCount());
            this.setInvestTotalAmount(operationDataDto.getTradeAmount());
            this.setInvestMonth(operationDataDto.getMonth());
            this.setInvestMonthAmount(operationDataDto.getMoney());
            this.setInvestMonthSize();
        }

        public OperationDataDto getOperationDataDto() {
            OperationDataDto operationDataDto = new OperationDataDto();
            operationDataDto.setUsersCount(this.getUserCount());
            operationDataDto.setTradeAmount(this.getInvestTotalAmount());
            operationDataDto.setOperationDays(this.getOperationTime());
            operationDataDto.setMonth(this.getInvestMonth());
            operationDataDto.setMoney(this.getInvestMonthAmount());

            return operationDataDto;
        }

        public int getOperationTime() {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date startTime = new Date();
            try {
                startTime = simpleDateFormat.parse(this.startOperationTime);
            } catch (ParseException e) {
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startTime);
            long startMillis = calendar.getTimeInMillis();
            Date endTime = new Date();
            long endMillis = endTime.getTime();
            return (int) ((endMillis - startMillis) / (1000 * 3600 * 24));
        }

        public String getStartOperationTime() {
            return startOperationTime;
        }

        public long getUserCount() {
            return userCount;
        }

        public void setUserCount(long userCount) {
            this.userCount = userCount;
        }

        public String getInvestTotalAmount() {
            return investTotalAmount;
        }

        public void setInvestTotalAmount(String investTotalAmount) {
            this.investTotalAmount = investTotalAmount;
        }

        public List<String> getInvestMonthAmount() {
            List<String> investMonthAmountCopy = new LinkedList<>();
            for (String number : investMonthAmount) {
                investMonthAmountCopy.add(number);
            }
            return investMonthAmountCopy;
        }

        public String getInvestMonthAmountString() {
            return Joiner.on(",").join(investMonthAmount);
        }

        public void setInvestMonthAmount(String listString) {
            this.investMonthAmount = Splitter.on(",").splitToList(listString);
        }

        private void setInvestMonthAmount(List<String> list){
            this.investMonthAmount = list;
        }

        public void addInvestMonthAmount(String number) {
            if (investMonthAmount.size() >= investMonthSize) {
                throw new IndexOutOfBoundsException();
            }
            this.investMonthAmount.add(number);
        }

        public List<String> getInvestMonth() {
            List<String> investMonthCopy = new LinkedList<>();
            for (String month : investMonth) {
                investMonthCopy.add(month);
            }
            return investMonthCopy;
        }

        public String getInvestMonthString() {
            return Joiner.on(",").join(investMonth);
        }

        public void setInvestMonth(String listString) {
            this.investMonth = Splitter.on(",").splitToList(listString);
        }

        private void setInvestMonth(List<String> list) {
            this.investMonth = list;
        }

        public void addInvestMonth(String month) {
            if (investMonth.size() >= investMonthSize) {
                throw new IndexOutOfBoundsException();
            }
            this.investMonth.add(month);
        }

        public int getInvestMonthSize() {
            return investMonthSize;
        }

        private void setInvestMonthSize() {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date startTime = new Date();
            try {
                startTime = simpleDateFormat.parse(this.startOperationTime);
            } catch (ParseException e) {
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startTime);
            int startYear = calendar.get(Calendar.YEAR);
            int startMonth = calendar.get(Calendar.MONTH);
            calendar.setTime(new Date());
            int endYear = calendar.get(Calendar.YEAR);
            int endMonth = calendar.get(Calendar.MONTH);
            investMonthSize = (endYear - startYear) * 12 + (endMonth - startMonth);
        }
    }

    private String getTotalSuccessAmount()
    {
        return AmountConverter.convertCentToString(investMapper.sumInvestAmount(null, null, null, null, null, null,
                new DateTime().withTimeAtStartOfDay().toDate(), InvestStatus.SUCCESS, null));
    }

    private void setMonthOperationData(OperationDataServiceModel operationDataServiceModel)
    {
        final int monthSize = operationDataServiceModel.getInvestMonthSize();
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) - monthSize, 1, 0, 0, 0);
        for(int i = 0; i < monthSize; i++) {
            String month = calendar.get(Calendar.YEAR) + "." + (calendar.get(Calendar.MONTH) + 1);
            operationDataServiceModel.addInvestMonth(month);

            Date startTime = calendar.getTime();
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
            Date endTime = calendar.getTime();

            String amount = AmountConverter.convertCentToString(investMapper.sumInvestAmount(null, null, null, null, null,
                    startTime, endTime, InvestStatus.SUCCESS, null));
            operationDataServiceModel.addInvestMonthAmount(amount);
        }
    }

    @Override
    public OperationDataDto getOperationDataFromDatabase()
    {
        OperationDataServiceModel operationDataServiceModel = new OperationDataServiceModel();
        operationDataServiceModel.setInvestTotalAmount(getTotalSuccessAmount());
        operationDataServiceModel.setUserCount(userMapper.findUsersCount());
        setMonthOperationData(operationDataServiceModel);

        return operationDataServiceModel.getOperationDataDto();
    }

    @Override
    public OperationDataDto getOperationDataFromRedis()
    {
        final String redisInfoPublishKey = MessageFormat.format(REDIS_INFO_PUBLISH_KEY_TEMPLATE, String.valueOf(simpleDateFormat.format(new Date())));
        OperationDataDto operationDataDto;
        if(redisWrapperClient.exists(redisInfoPublishKey)){
            OperationDataServiceModel operationDataServiceModel = new OperationDataServiceModel();
            operationDataServiceModel.setUserCount(Integer.parseInt(redisWrapperClient.hget(redisInfoPublishKey, USERS_COUNT)));
            operationDataServiceModel.setInvestTotalAmount(redisWrapperClient.hget(redisInfoPublishKey, TRADE_AMOUNT));
            operationDataServiceModel.setInvestMonth(redisWrapperClient.hget(redisInfoPublishKey, OPERATION_DATA_MONTH));
            operationDataServiceModel.setInvestMonthAmount(redisWrapperClient.hget(redisInfoPublishKey, OPERATION_DATA_MONTH_AMOUNT));
            operationDataDto = operationDataServiceModel.getOperationDataDto();
        } else {
            operationDataDto = getOperationDataFromDatabase();
            updateRedis(operationDataDto);
        }
        return operationDataDto;
    }

    @Override
    public void updateRedis(OperationDataDto operationDataDto)
    {
        final String redisInfoPublishKey = MessageFormat.format(REDIS_INFO_PUBLISH_KEY_TEMPLATE, String.valueOf(simpleDateFormat.format(new Date())));
        OperationDataServiceModel operationDataServiceModel = new OperationDataServiceModel(operationDataDto);
        redisWrapperClient.hset(redisInfoPublishKey, USERS_COUNT, Long.toString(operationDataServiceModel.getUserCount()), timeout);
        redisWrapperClient.hset(redisInfoPublishKey, TRADE_AMOUNT, operationDataServiceModel.getInvestTotalAmount(), timeout);
        redisWrapperClient.hset(redisInfoPublishKey, OPERATION_DATA_MONTH, operationDataServiceModel.getInvestMonthString(), timeout);
        redisWrapperClient.hset(redisInfoPublishKey, OPERATION_DATA_MONTH_AMOUNT, operationDataServiceModel.getInvestMonthAmountString(), timeout);
    }
}
