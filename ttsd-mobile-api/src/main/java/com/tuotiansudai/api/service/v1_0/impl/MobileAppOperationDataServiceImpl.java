package com.tuotiansudai.api.service.v1_0.impl;


import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppOperationDataService;
import com.tuotiansudai.dto.OperationDataDto;
import com.tuotiansudai.enums.AgeDistributionType;
import com.tuotiansudai.repository.model.InvestDataView;
import com.tuotiansudai.service.OperationDataService;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.CalculateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class MobileAppOperationDataServiceImpl implements MobileAppOperationDataService {

    @Autowired
    private OperationDataService operationDataService;

    SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");

    @Override
    public BaseResponseDto<OperationDataResponseDataDto> generatorOperationData(BaseParamDto requestDto) {
        OperationDataResponseDataDto dataDto = new OperationDataResponseDataDto();
        Date currentDate = new Date();

        OperationDataDto operationDataDto = operationDataService.getOperationDataFromRedis(currentDate);
        dataDto.setCurrentDay(sdf.format(currentDate));
        dataDto.setOperationDays(String.valueOf(operationDataDto.getOperationDays()));
        dataDto.setTotalTradeAmount(String.valueOf(AmountConverter.convertStringToCent(operationDataDto.getTradeAmount())));
        dataDto.setTotalInterest(String.valueOf(operationDataService.findUserSumInterest(currentDate)));

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
        List<Integer> sexList = operationDataService.findScaleByGender(currentDate);
        dataDto.setFemaleScale(String.valueOf(CalculateUtil.calculatePercentage(sexList.get(0), sexList.get(0) + sexList.get(1), 1)));
        dataDto.setMaleScale(String.valueOf(100 - CalculateUtil.calculatePercentage(sexList.get(0), sexList.get(0) + sexList.get(1), 1)));
        //近半年的交易金额
        dataDto.setLatestSixMonthDetail(convertMapToOperationDataLatestSixMonthResponseDataDto(operationDataDto));
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
        Date currentDate = new Date();
        Map<String,String> ageDistributionMap = operationDataService.findAgeDistributionByAge(currentDate);
        Set<Map.Entry<String, String>> ageDistributionEntries = ageDistributionMap.entrySet();
        List<OperationDataAgeResponseDataDto> operationDataAgeResponseDataDtoList = Lists.newArrayList();
        for (Map.Entry<String, String> ageDistributionEntry : ageDistributionEntries) {
            OperationDataAgeResponseDataDto operationDataAgeResponseDataDto = new OperationDataAgeResponseDataDto();
            operationDataAgeResponseDataDto.setName(AgeDistributionType.getNameByAgeStage(Integer.parseInt(ageDistributionEntry.getKey())));
            operationDataAgeResponseDataDto.setScale(ageDistributionEntry.getValue());
            operationDataAgeResponseDataDtoList.add(operationDataAgeResponseDataDto);
        }
        return operationDataAgeResponseDataDtoList;
    }

    private  List<OperationDataLatestSixMonthResponseDataDto> convertMapToOperationDataLatestSixMonthResponseDataDto(OperationDataDto operationDataDto){
        List<OperationDataLatestSixMonthResponseDataDto> operationDataLatestSixMonthResponseDataDtoList = Lists.newArrayList();
        int count = 1;
        for(int i = operationDataDto.getMonth().size() - 1; i >= 0; i--){
            if(count <= 6){
                OperationDataLatestSixMonthResponseDataDto operationDataLatestSixMonthResponseDataDto = new OperationDataLatestSixMonthResponseDataDto();
                operationDataLatestSixMonthResponseDataDto.setName(operationDataDto.getMonth().get(i).substring(operationDataDto.getMonth().get(i).indexOf(".") + 1).concat("月"));
                operationDataLatestSixMonthResponseDataDto.setAmount(String.valueOf(AmountConverter.convertStringToCent(operationDataDto.getMoney().get(i))));
                operationDataLatestSixMonthResponseDataDtoList.add(operationDataLatestSixMonthResponseDataDto);
            }
            count++;
        }
        return operationDataLatestSixMonthResponseDataDtoList;
    }

    private List<OperationDataInvestCityResponseDataDto> convertMapToOperationDataInvestCityResponseDataDto(){
        Date currentDate = new Date();
        Map<String,String> investCityListMap = operationDataService.findCountInvestCityScaleTop3(currentDate);
        Set<Map.Entry<String, String>> investCityEntries = investCityListMap.entrySet();
        List<OperationDataInvestCityResponseDataDto> operationDataInvestCityResponseDataDtoList = Lists.newArrayList();
        for(Map.Entry<String, String> investCityEntry : investCityEntries){
            OperationDataInvestCityResponseDataDto operationDataInvestCityResponseDataDto = new OperationDataInvestCityResponseDataDto();
            operationDataInvestCityResponseDataDto.setCity(investCityEntry.getKey());
            operationDataInvestCityResponseDataDto.setScale(investCityEntry.getValue());
            operationDataInvestCityResponseDataDtoList.add(operationDataInvestCityResponseDataDto);
        }
        Collections.sort(operationDataInvestCityResponseDataDtoList, (o1, o2) -> Double.compare(Double.parseDouble(o2.getScale()), Double.parseDouble(o1.getScale())));
        return operationDataInvestCityResponseDataDtoList;
    }

    private List<OperationDataInvestAmountResponseDataDto> convertMapToOperationDataInvestAmountResponseDataDto(){
        Date currentDate = new Date();
        Map<String,String> investAmountMap = operationDataService.findInvestAmountScaleTop3(currentDate);
        Set<Map.Entry<String, String>> investAmountEntries = investAmountMap.entrySet();
        List<OperationDataInvestAmountResponseDataDto> operationDataInvestAmountResponseDataDtoList = Lists.newArrayList();
        for(Map.Entry<String, String> investAmountEntry : investAmountEntries){
            OperationDataInvestAmountResponseDataDto operationDataInvestAmountResponseDataDto = new OperationDataInvestAmountResponseDataDto();
            operationDataInvestAmountResponseDataDto.setCity(investAmountEntry.getKey());
            operationDataInvestAmountResponseDataDto.setScale(investAmountEntry.getValue());
            operationDataInvestAmountResponseDataDtoList.add(operationDataInvestAmountResponseDataDto);
        }
        Collections.sort(operationDataInvestAmountResponseDataDtoList, (o1, o2) -> Double.compare(Double.parseDouble(o2.getScale()), Double.parseDouble(o1.getScale())));
        return operationDataInvestAmountResponseDataDtoList;
    }
}
