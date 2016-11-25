package com.tuotiansudai.service;

import com.tuotiansudai.dto.OperationDataDto;
import com.tuotiansudai.repository.model.InvestDataView;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OperationDataService {
    OperationDataDto getOperationDataFromRedis(Date endDate);

    List<InvestDataView> getInvestDetail(Date endDate);

    List<Integer> findScaleByGender();

    Map<String,String> findLatestSixMonthTradeAmount();

    Map<String, String> findAgeDistributionByAge();

    Map<String, String> findCountInvestCityScaleTop3();

    Map<String, String> findInvestAmountScaleTop3();

}
