package com.tuotiansudai.service;

import com.tuotiansudai.service.impl.OperationDataServiceModel;
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
        OperationDataServiceModel operationDataServiceModel = operationDataService.getOperationDataFromDatabase();
        assertEquals(314, operationDataServiceModel.getOperationTime());
        assertEquals(operationDataServiceModel.getInvestMonthSize(), operationDataServiceModel.getInvestMonth().size());
        assertEquals(operationDataServiceModel.getInvestMonthSize(), operationDataServiceModel.getInvestMonthAmount().size());
    }

    @Test
    public void testUpdateRedisAndGetOperationDataFromRedis()
    {
        OperationDataServiceModel operationDataServiceModelFromDB = operationDataService.getOperationDataFromDatabase();
        operationDataService.updateRedis(operationDataServiceModelFromDB);
        OperationDataServiceModel operationDataServiceModelFromRedis = operationDataService.getOperationDataFromRedis();
        assertEquals(operationDataServiceModelFromDB.getOperationTime(), operationDataServiceModelFromRedis.getOperationTime());
        assertEquals(operationDataServiceModelFromDB.getUserCount(), operationDataServiceModelFromRedis.getUserCount());
        assertTrue(operationDataServiceModelFromRedis.getInvestTotalAmount().equals(operationDataServiceModelFromDB.getInvestTotalAmount()));
        List<String> investMonthFromDB = operationDataServiceModelFromDB.getInvestMonth();
        List<String> investMonthFromRedis = operationDataServiceModelFromRedis.getInvestMonth();
        assertEquals(investMonthFromDB.size(), investMonthFromRedis.size());
        assertEquals(operationDataServiceModelFromDB.getInvestMonthString(), operationDataServiceModelFromRedis.getInvestMonthString());
        assertEquals(operationDataServiceModelFromDB.getInvestMonthAmountString(), operationDataServiceModelFromRedis.getInvestMonthAmountString());
    }
}
