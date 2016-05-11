package com.tuotiansudai.service;

import com.tuotiansudai.dto.OperationDataDto;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class OperationDataServiceTest {
    @Autowired
    OperationDataService operationDataService;

    @Test
    @Ignore
    public void testGetOperationDataModelFromDatabase()
    {
        OperationDataDto operationDataDto = operationDataService.getOperationDataFromDatabase();
        assertEquals(314, operationDataDto.getOperationDays());
        assertEquals(10, operationDataDto.getMonth().size());
    }

    @Test
    public void testUpdateRedisAndGetOperationDataFromRedis()
    {
        OperationDataDto operationDataDtoFromDB = operationDataService.getOperationDataFromDatabase();
        operationDataService.updateRedis(operationDataDtoFromDB);
        OperationDataDto operationDataDtoFromRedis = operationDataService.getOperationDataFromRedis();
        assertEquals(operationDataDtoFromDB.getOperationDays(), operationDataDtoFromRedis.getOperationDays());
        assertEquals(operationDataDtoFromDB.getUsersCount(), operationDataDtoFromRedis.getUsersCount());
        assertTrue(operationDataDtoFromRedis.getTradeAmount().equals(operationDataDtoFromDB.getTradeAmount()));
        List<String> investMonthFromDB = operationDataDtoFromDB.getMonth();
        List<String> investMonthFromRedis = operationDataDtoFromRedis.getMonth();
        List<String> investMonthAmountFromDB = operationDataDtoFromDB.getMoney();
        List<String> investMonthAMountFromRedis = operationDataDtoFromRedis.getMoney();
        assertEquals(investMonthFromDB.size(), investMonthFromRedis.size());
        for(int i = 0; i < investMonthFromDB.size(); ++i)
        {
            assertEquals(investMonthFromDB.get(i), investMonthFromRedis.get(i));
            assertEquals(investMonthAmountFromDB.get(i), investMonthAMountFromRedis.get(i));
        }
    }
}
