package com.tuotiansudai.service.impl;

import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.service.OperationDataService;
import com.tuotiansudai.util.AmountConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

    private String getTotalSuccessAmount()
    {
        return AmountConverter.convertCentToString(investMapper.sumInvestAmount(null, null, null, null, null, null, null,
                InvestStatus.SUCCESS, null));
    }

    private void setMonthOperationData(OperationDataServiceModel operationDataServiceModel)
    {
        final int monthSize = operationDataServiceModel.getInvestMonthSize();
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) - monthSize, 1, 0, 0, 0);
        for(int i = 0; i < monthSize; i++) {
            String month = calendar.get(Calendar.YEAR) + "年" + (calendar.get(Calendar.MONTH) + 1) + "月";
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
    public OperationDataServiceModel getOperationDataFromDatabase()
    {
        OperationDataServiceModel operationDataServiceModel = new OperationDataServiceModel();
        operationDataServiceModel.setInvestTotalAmount(getTotalSuccessAmount());
        operationDataServiceModel.setUserCount(userMapper.findUsersCount());
        setMonthOperationData(operationDataServiceModel);

        return operationDataServiceModel;
    }

    @Override
    public OperationDataServiceModel getOperationDataFromRedis()
    {
        final String redisInfoPublishKey = MessageFormat.format(REDIS_INFO_PUBLISH_KEY_TEMPLATE, String.valueOf(simpleDateFormat.format(new Date())));
        OperationDataServiceModel operationDataServiceModel;
        if(redisWrapperClient.exists(redisInfoPublishKey)){
            operationDataServiceModel = new OperationDataServiceModel();
            operationDataServiceModel.setUserCount(Integer.parseInt(redisWrapperClient.hget(redisInfoPublishKey, USERS_COUNT)));
            operationDataServiceModel.setInvestTotalAmount(redisWrapperClient.hget(redisInfoPublishKey, TRADE_AMOUNT));
            operationDataServiceModel.setInvestMonth(redisWrapperClient.hget(redisInfoPublishKey, OPERATION_DATA_MONTH));
            operationDataServiceModel.setInvestMonthAmount(redisWrapperClient.hget(redisInfoPublishKey, OPERATION_DATA_MONTH_AMOUNT));
        } else {
            operationDataServiceModel = getOperationDataFromDatabase();
            updateRedis(operationDataServiceModel);
        }
        return operationDataServiceModel;
    }

    @Override
    public void updateRedis(OperationDataServiceModel operationDataServiceModel)
    {
        final String redisInfoPublishKey = MessageFormat.format(REDIS_INFO_PUBLISH_KEY_TEMPLATE, String.valueOf(simpleDateFormat.format(new Date())));
        redisWrapperClient.hset(redisInfoPublishKey, USERS_COUNT, Long.toString(operationDataServiceModel.getUserCount()), timeout);
        redisWrapperClient.hset(redisInfoPublishKey, TRADE_AMOUNT, operationDataServiceModel.getInvestTotalAmount(), timeout);
        redisWrapperClient.hset(redisInfoPublishKey, OPERATION_DATA_MONTH, operationDataServiceModel.getInvestMonthString(), timeout);
        redisWrapperClient.hset(redisInfoPublishKey, OPERATION_DATA_MONTH_AMOUNT, operationDataServiceModel.getInvestMonthAmountString(), timeout);
    }
}
