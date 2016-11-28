package com.tuotiansudai.api.service.v1_0.impl;


import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppOperationDataService;
import com.tuotiansudai.dto.OperationDataDto;
import com.tuotiansudai.repository.model.InvestDataView;
import com.tuotiansudai.service.OperationDataService;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.CalculateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class MobileAppOperationDataServiceImpl implements MobileAppOperationDataService {

    @Autowired
    private OperationDataService operationDataService;

    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
    Date currentDate = new Date();

    @Override
    public BaseResponseDto<OperationDataResponseDataDto> generatorOperationData(BaseParamDto requestDto) {
        OperationDataResponseDataDto dataDto = new OperationDataResponseDataDto();

        OperationDataDto operationDataDto = operationDataService.getOperationDataFromRedis(currentDate);
        dataDto.setCurrentDay(sdf.format(currentDate).replace("-","月").concat("日"));
        dataDto.setOperationDays(String.valueOf(operationDataDto.getOperationDays()));
        dataDto.setTotalTradeAmount(String.valueOf(AmountConverter.convertStringToCent(operationDataDto.getTradeAmount())));
        dataDto.setTotalInterest(String.valueOf(operationDataService.findUserSumInterest()));

        List<InvestDataView> investDataViewList = operationDataService.getInvestDetail(currentDate);
        List<OperationDataInvestByProductTypeResponseDataDto> operationDataInvestByProductTypeResponseDataDtoList = Lists.newArrayList();
        int totalTradeCount = 0;
        for(InvestDataView investDataView: investDataViewList){
            OperationDataInvestByProductTypeResponseDataDto operationDataInvestByProductTypeResponseDataDto = new OperationDataInvestByProductTypeResponseDataDto();
            operationDataInvestByProductTypeResponseDataDto.setName(investDataView.getProductName().concat("天"));
            operationDataInvestByProductTypeResponseDataDto.setAmount(String.valueOf(AmountConverter.convertStringToCent(investDataView.getTotalInvestAmount())));
            operationDataInvestByProductTypeResponseDataDtoList.add(operationDataInvestByProductTypeResponseDataDto);
            totalTradeCount += investDataView.getCountInvest();
        }
        dataDto.setInvestListByProductType(operationDataInvestByProductTypeResponseDataDtoList);
        dataDto.setTotalTradeCount(String.valueOf(totalTradeCount));
        dataDto.setTotalInvestUserCount(String.valueOf(operationDataDto.getUsersCount()));
        List<Integer> sexList = operationDataService.findScaleByGender();
        dataDto.setFemaleScale(String.valueOf(CalculateUtil.calculatePercentage(sexList.get(0), sexList.get(0) + sexList.get(1), 1)));
        dataDto.setMaleScale(String.valueOf(100 - CalculateUtil.calculatePercentage(sexList.get(0), sexList.get(0) + sexList.get(1), 1)));
        //近半年的交易金额
        dataDto.setLatestSixMonthDetail(convertMapToOperationDataLatestSixMonthResponseDataDto());
        //各用户年龄段分布
        dataDto.setAgeDistribution(convertMapToOperationDataAgeResponseDataDto());
        //投资人数top3
        dataDto.setInvestCityScaleTop3(convertMapToOperationDataInvestCityResponseDataDto());
        //投资金额top3
        dataDto.setInvestAmountScaleTop3(convertMapToOperationDataInvestAmountResponseDataDto());
        BaseResponseDto<OperationDataResponseDataDto> dto = new BaseResponseDto<>();
        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        dto.setData(dataDto);
        return dto;
    }

    private List<OperationDataAgeResponseDataDto> convertMapToOperationDataAgeResponseDataDto(){
        Map<String,String> ageDistributionMap = operationDataService.findAgeDistributionByAge();
        Set<Map.Entry<String, String>> ageDistributionEntries = ageDistributionMap.entrySet();
        List<OperationDataAgeResponseDataDto> operationDataAgeResponseDataDtoList = Lists.newArrayList();
        for (Map.Entry<String, String> ageDistributionEntry : ageDistributionEntries) {
            OperationDataAgeResponseDataDto operationDataAgeResponseDataDto = new OperationDataAgeResponseDataDto();
            operationDataAgeResponseDataDto.setName(ageDistributionEntry.getKey());
            operationDataAgeResponseDataDto.setScale(ageDistributionEntry.getValue());
            operationDataAgeResponseDataDtoList.add(operationDataAgeResponseDataDto);
        }
        return operationDataAgeResponseDataDtoList;
    }

    private  List<OperationDataLatestSixMonthResponseDataDto> convertMapToOperationDataLatestSixMonthResponseDataDto(){
        Map<String, String> mapLatestSixMonthAmountMap = operationDataService.findLatestSixMonthTradeAmount();
        Set<Map.Entry<String, String>> latestSixMonthEntries = mapLatestSixMonthAmountMap.entrySet();
        List<OperationDataLatestSixMonthResponseDataDto> operationDataLatestSixMonthResponseDataDtoList = Lists.newArrayList();
        for (Map.Entry<String, String> latestSixMonthEntry : latestSixMonthEntries) {
            OperationDataLatestSixMonthResponseDataDto operationDataLatestSixMonthResponseDataDto = new OperationDataLatestSixMonthResponseDataDto();
            operationDataLatestSixMonthResponseDataDto.setName(String.valueOf(latestSixMonthEntry.getKey()));
            operationDataLatestSixMonthResponseDataDto.setAmount(String.valueOf(latestSixMonthEntry.getValue()));
            operationDataLatestSixMonthResponseDataDtoList.add(operationDataLatestSixMonthResponseDataDto);
        }
        return operationDataLatestSixMonthResponseDataDtoList;
    }

    private List<OperationDataInvestCityResponseDataDto> convertMapToOperationDataInvestCityResponseDataDto(){
        Map<String,String> investCityListMap = operationDataService.findCountInvestCityScaleTop3();
        Set<Map.Entry<String, String>> investCityEntries = investCityListMap.entrySet();
        List<OperationDataInvestCityResponseDataDto> operationDataInvestCityResponseDataDtoList = Lists.newArrayList();
        for(Map.Entry<String, String> investCityEntry : investCityEntries){
            OperationDataInvestCityResponseDataDto operationDataInvestCityResponseDataDto = new OperationDataInvestCityResponseDataDto();
            operationDataInvestCityResponseDataDto.setCity(investCityEntry.getKey());
            operationDataInvestCityResponseDataDto.setScale(investCityEntry.getValue());
            operationDataInvestCityResponseDataDtoList.add(operationDataInvestCityResponseDataDto);
        }
        return operationDataInvestCityResponseDataDtoList;
    }

    private List<OperationDataInvestAmountResponseDataDto> convertMapToOperationDataInvestAmountResponseDataDto(){
        Map<String,String> investAmountMap = operationDataService.findInvestAmountScaleTop3();
        Set<Map.Entry<String, String>> investAmountEntries = investAmountMap.entrySet();
        List<OperationDataInvestAmountResponseDataDto> operationDataInvestAmountResponseDataDtoList = Lists.newArrayList();
        for(Map.Entry<String, String> investAmountEntry : investAmountEntries){
            OperationDataInvestAmountResponseDataDto operationDataInvestAmountResponseDataDto = new OperationDataInvestAmountResponseDataDto();
            operationDataInvestAmountResponseDataDto.setCity(investAmountEntry.getKey());
            operationDataInvestAmountResponseDataDto.setScale(investAmountEntry.getValue());
            operationDataInvestAmountResponseDataDtoList.add(operationDataInvestAmountResponseDataDto);
        }
        return operationDataInvestAmountResponseDataDtoList;
    }


}
