package com.tuotiansudai.service;

import com.tuotiansudai.repository.model.OperationDataModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by huoxuanbo on 16/5/9.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class OperationDataServiceTest {
    @Autowired
    OperationDataService operationDataService;

    @Test
    public void testGetOperationDataModelFromDatabase()
    {
        OperationDataModel operationDataModel = operationDataService.getOperationDataFromDatabase();
        assertEquals(314, operationDataModel.getOperationTime());
        assertEquals(operationDataModel.getInvestMonthSize(), operationDataModel.getInvestMonth().size());
        assertEquals(operationDataModel.getInvestMonthSize(), operationDataModel.getInvestMonthAmount().size());
    }

    @Test
    public void testUpdateRedisAndGetOperationDataFromRedis()
    {
        OperationDataModel operationDataModelFromDB = operationDataService.getOperationDataFromDatabase();
        operationDataService.updateRedis(operationDataModelFromDB);
        OperationDataModel operationDataModelFromRedis = operationDataService.getOperationDataFromRedis();
        assertEquals(operationDataModelFromDB.getOperationTime(), operationDataModelFromRedis.getOperationTime());
        assertEquals(operationDataModelFromDB.getUserCount(), operationDataModelFromRedis.getUserCount());
        assertTrue(operationDataModelFromRedis.getInvestTotalAmount().equals(operationDataModelFromDB.getInvestTotalAmount()));
        List<String> investMonthFromDB = operationDataModelFromDB.getInvestMonth();
        List<String> investMonthFromRedis = operationDataModelFromRedis.getInvestMonth();
        assertEquals(investMonthFromDB.size(), investMonthFromRedis.size());
        assertEquals(operationDataModelFromDB.getInvestMonthString(), operationDataModelFromRedis.getInvestMonthString());
        assertEquals(operationDataModelFromDB.getInvestMonthAmountString(), operationDataModelFromRedis.getInvestMonthAmountString());
    }

    @Test
    public void testGetJSONString()
    {
        final String exceptJSONString = "{\"operationDays\":314,\"usersAmount\":40039,\"TradeAmount\":\"658.00\",\"money\"" +
                ":[\"0.00\",\"0.00\",\"0.00\",\"0.00\",\"658.00\",\"0.00\",\"0.00\",\"0.00\",\"0.00\",\"0.00\"],\"month\"" +
                ":[\"2015年7月\",\"2015年8月\",\"2015年9月\",\"2015年10月\",\"2015年11月\",\"2015年12月\",\"2016年1月\",\"" +
                "2016年2月\",\"2016年3月\",\"2016年4月\"]}";
        OperationDataModel operationDataModel = operationDataService.getOperationDataFromDatabase();
        assertEquals(exceptJSONString, operationDataModel.getJSONString());
    }
}
