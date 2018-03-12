package com.tuotiansudai.service;

import com.tuotiansudai.dto.*;
import com.tuotiansudai.repository.model.InvestDataView;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OperationDataService {
    OperationDataDto getOperationDataFromRedis(Date endDate);

    List<InvestDataView> getInvestDetail(Date endDate);

    List<Integer> findScaleByGender(Date endDate);

    List<Integer> findLoanerScaleByGender(Date endDate);

    Map<String, String> findAgeDistributionByAge(Date endDate);

    Map<String, String> findLoanerAgeDistributionByAge(Date endDate);

    Map<String, String> findCountInvestCityScaleTop5(Date endDate);

    Map<String, String> findInvestAmountScaleTop3(Date endDate);

    long findUserSumInterest(Date endDate);

    List<OperationDataAgeDataDto> convertMapToOperationDataAgeDataDto();

    List<OperationDataLoanerAgeDataDto>  convertMapToOperationDataLoanerAgeDataDto();

    List<OperationDataInvestCityDataDto> convertMapToOperationDataInvestCityDataDto();

    List<OperationDataInvestAmountDataDto> convertMapToOperationDataInvestAmountDataDto();

    List<OperationDataLoanerCityDataDto> convertMapToOperationDataLoanerCityDataDto();

    List<OperationDataAgeDataDto> convertMapToOperationDataNewAgeDataDto() ;

}
