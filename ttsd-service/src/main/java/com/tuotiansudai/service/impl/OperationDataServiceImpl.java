package com.tuotiansudai.service.impl;

import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.repository.model.OperationDataModel;
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

    private void setMonthOperationData(OperationDataModel operationDataModel)
    {
        final int monthSize = operationDataModel.getInvestMonthSize();
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) - monthSize, 1, 0, 0, 0);
        for(int i = 0; i < monthSize; i++) {
            String month = calendar.get(Calendar.YEAR) + "年" + (calendar.get(Calendar.MONTH) + 1) + "月";
            operationDataModel.addInvestMonth(month);

            Date startTime = calendar.getTime();
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
            Date endTime = calendar.getTime();

            String amount = AmountConverter.convertCentToString(investMapper.sumInvestAmount(null, null, null, null, null,
                    startTime, endTime, InvestStatus.SUCCESS, null));
            operationDataModel.addInvestMonthAmount(amount);
        }
    }

    @Override
    public OperationDataModel getOperationDataFromDatabase()
    {
        OperationDataModel operationDataModel = new OperationDataModel();
        operationDataModel.setInvestTotalAmount(getTotalSuccessAmount());
        operationDataModel.setUserCount(userMapper.findUsersCount());
        setMonthOperationData(operationDataModel);

        return operationDataModel;
    }

    @Override
    public OperationDataModel getOperationDataFromRedis()
    {
        final String redisInfoPublishKey = MessageFormat.format(REDIS_INFO_PUBLISH_KEY_TEMPLATE, String.valueOf(simpleDateFormat.format(new Date())));
        OperationDataModel operationDataModel;
        if(redisWrapperClient.exists(redisInfoPublishKey)){
            operationDataModel = new OperationDataModel();
            operationDataModel.setUserCount(Integer.parseInt(redisWrapperClient.hget(redisInfoPublishKey, USERS_COUNT)));
            operationDataModel.setInvestTotalAmount(redisWrapperClient.hget(redisInfoPublishKey, TRADE_AMOUNT));
            operationDataModel.setInvestMonth(redisWrapperClient.hget(redisInfoPublishKey, OPERATION_DATA_MONTH));
            operationDataModel.setInvestMonthAmount(redisWrapperClient.hget(redisInfoPublishKey, OPERATION_DATA_MONTH_AMOUNT));
        } else {
            operationDataModel = getOperationDataFromDatabase();
            updateRedis(operationDataModel);
        }
        return operationDataModel;
    }

    @Override
    public void updateRedis(OperationDataModel operationDataModel)
    {
        final String redisInfoPublishKey = MessageFormat.format(REDIS_INFO_PUBLISH_KEY_TEMPLATE, String.valueOf(simpleDateFormat.format(new Date())));
        redisWrapperClient.hset(redisInfoPublishKey, USERS_COUNT, Long.toString(operationDataModel.getUserCount()), timeout);
        redisWrapperClient.hset(redisInfoPublishKey, TRADE_AMOUNT, operationDataModel.getInvestTotalAmount(), timeout);
        redisWrapperClient.hset(redisInfoPublishKey, OPERATION_DATA_MONTH, operationDataModel.getInvestMonthString(), timeout);
        redisWrapperClient.hset(redisInfoPublishKey, OPERATION_DATA_MONTH_AMOUNT, operationDataModel.getInvestMonthAmountString(), timeout);
    }
}
