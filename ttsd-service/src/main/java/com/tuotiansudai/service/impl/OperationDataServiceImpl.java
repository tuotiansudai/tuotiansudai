package com.tuotiansudai.service.impl;

import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.repository.model.OperationDataModel;
import com.tuotiansudai.service.OperationDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
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

    final String REDIS_KEY_USERS_AMOUNT = "REDIS_USERS_AMOUNT";
    final String REDIS_KEY_TRADE_AMOUNT = "REDIS_TRADE_AMOUNT";
    final String REDIS_KEY_OPERATION_DATA_MONTH = "REDIS_OPERATION_DATA_MONTH";
    final String REDIS_KEY_OPERATION_DATA_MONTH_AMOUNT = "REDIS_OPERATION_DATA_MONTH_AMOUNT";

    private BigDecimal getTotalSuccessAmount()
    {
        return new BigDecimal(investMapper.sumInvestAmount(null, null, null, null, null, null, null, InvestStatus.SUCCESS, null));
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

            BigDecimal amount = new BigDecimal(investMapper.sumInvestAmount(null, null, null, null, null, startTime,
                    endTime, InvestStatus.SUCCESS, null));
            operationDataModel.addInvestMonthAmount(amount);
        }
    }

    @Override
    public OperationDataModel getOperationDataFromDatabase()
    {
        OperationDataModel operationDataModel = new OperationDataModel();
        operationDataModel.setInvestTotalAmount(getTotalSuccessAmount());
        operationDataModel.setUserAmount(userMapper.findUsersAmount());
        setMonthOperationData(operationDataModel);

        return operationDataModel;
    }

    @Override
    public OperationDataModel getOperationDataFromRedis()
    {
        OperationDataModel operationDataModel = new OperationDataModel();
        operationDataModel.setUserAmount(Integer.parseInt(redisWrapperClient.get(REDIS_KEY_USERS_AMOUNT)));
        operationDataModel.setInvestTotalAmount(new BigDecimal(redisWrapperClient.get(REDIS_KEY_TRADE_AMOUNT)));
        for(String month : redisWrapperClient.lrange(REDIS_KEY_OPERATION_DATA_MONTH, 0, operationDataModel.getInvestMonthSize()))
        {
            operationDataModel.addInvestMonth(month);
        }
        for(String number : redisWrapperClient.lrange(REDIS_KEY_OPERATION_DATA_MONTH_AMOUNT, 0, operationDataModel.getInvestMonthSize()))
        {
            operationDataModel.addInvestMonthAmount(new BigDecimal(number));
        }
        return operationDataModel;
    }

    @Override
    public void updateRedis()
    {
        OperationDataModel operationDataModel = getOperationDataFromDatabase();
        redisWrapperClient.set(REDIS_KEY_USERS_AMOUNT, Long.toString(operationDataModel.getUserAmount()));
        redisWrapperClient.set(REDIS_KEY_TRADE_AMOUNT, operationDataModel.getInvestTotalAmount().toString());
        redisWrapperClient.del(REDIS_KEY_OPERATION_DATA_MONTH);
        for(String month : operationDataModel.getInvestMonth())
        {
            redisWrapperClient.rpush(REDIS_KEY_OPERATION_DATA_MONTH, month);
        }
        redisWrapperClient.del(REDIS_KEY_OPERATION_DATA_MONTH_AMOUNT);
        for(BigDecimal number : operationDataModel.getInvestMonthAmount())
        {
            redisWrapperClient.rpush(REDIS_KEY_OPERATION_DATA_MONTH_AMOUNT, number.toString());
        }
    }
}
