package com.tuotiansudai.repository.mapper;

import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface OperationDataMapper {

    List<Integer> findScaleByGender(Date endDate);

    long findCountInvestCityScale(Date endDate);

    long findCountLoanerCityScale(Date endDate);

    List<Map<String, String>> findCountInvestCityScaleTop5(Date endDate);

    List<Map<String, String>> findCountLoanerCityScaleTop5(Date endDate);

    List<Map<String, String>> findAgeDistributionByAge(Date endDate);
}
