package com.tuotiansudai.api.service.v1_0.impl;


import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppOperationDataService;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.enums.AgeDistributionType;
import com.tuotiansudai.repository.model.InvestDataView;
import com.tuotiansudai.service.OperationDataService;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.CalculateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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
        dataDto.setTotalInterest(String.valueOf(AmountConverter.convertStringToCent(operationDataDto.getTotalInterest())));

        List<InvestDataView> investDataViewList = operationDataService.getInvestDetail(currentDate);
        List<OperationDataInvestByProductTypeResponseDataDto> operationDataInvestByProductTypeResponseDataDtoList = Lists.newArrayList();
        int totalTradeCount = 0;
        for (InvestDataView investDataView : investDataViewList) {
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
        List<Integer> loanerSexList = operationDataService.findLoanerScaleByGender(currentDate);
        dataDto.setLoanerFemaleScale(String.valueOf(CalculateUtil.calculatePercentage(sexList.get(0), loanerSexList.get(0) + loanerSexList.get(1), 1)));
        dataDto.setLoanerMaleScale(String.valueOf(100 - CalculateUtil.calculatePercentage(loanerSexList.get(0), loanerSexList.get(0) + loanerSexList.get(1), 1)));
        //近半年的交易金额
        dataDto.setLatestSixMonthDetail(convertMapToOperationDataLatestSixMonthResponseDataDto(operationDataDto));
        //各用户年龄段分布
        dataDto.setAgeDistribution(convertMapToOperationDataAgeResponseDataDto());
        //各用借款人户年龄段分布
        dataDto.setLoanerAgeDistribution(convertMapToOperationDataLoanerAgeResponseDataDto());
        //出借人数top3
        dataDto.setInvestCityScaleTop3(convertMapToOperationDataInvestCityResponseDataDto());
        //出借金额top3
        dataDto.setInvestAmountScaleTop3(convertMapToOperationDataInvestAmountResponseDataDto());

        dataDto.setLoanerCityScaleTop5(convertMapToOperationDataLoanerCityResponseDataDto());
        BaseResponseDto<OperationDataResponseDataDto> dto = new BaseResponseDto<>();
        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        dto.setData(dataDto);
        return dto;
    }

    private List<OperationDataAgeResponseDataDto> convertMapToOperationDataAgeResponseDataDto() {
        List<OperationDataAgeDataDto> operationDataAgeDataDtos = operationDataService.convertMapToOperationDataAgeDataDto();
        return operationDataAgeDataDtos.stream().map(operationDataAgeDataDto -> new OperationDataAgeResponseDataDto(operationDataAgeDataDto)).collect(Collectors.toList());
    }

    private List<OperationDataLoanerAgeResponseDataDto> convertMapToOperationDataLoanerAgeResponseDataDto() {
        List<OperationDataLoanerAgeDataDto> operationDataLoanerAgeDataDtos = operationDataService.convertMapToOperationDataLoanerAgeDataDto();
        return operationDataLoanerAgeDataDtos.stream().map(operationDataLoanerAgeDataDto -> new OperationDataLoanerAgeResponseDataDto(operationDataLoanerAgeDataDto)).collect(Collectors.toList());


    }

    private List<OperationDataLatestSixMonthResponseDataDto> convertMapToOperationDataLatestSixMonthResponseDataDto(OperationDataDto operationDataDto) {
        List<OperationDataLatestSixMonthResponseDataDto> operationDataLatestSixMonthResponseDataDtoList = Lists.newArrayList();
        //取最后6个月的数据,正序排列
        int startSeq = operationDataDto.getMonth().size() >= 6 ? operationDataDto.getMonth().size() - 6 : 0;
        for (int i = startSeq; i < operationDataDto.getMonth().size(); i++) {
            OperationDataLatestSixMonthResponseDataDto operationDataLatestSixMonthResponseDataDto = new OperationDataLatestSixMonthResponseDataDto();
            operationDataLatestSixMonthResponseDataDto.setName(operationDataDto.getMonth().get(i).substring(operationDataDto.getMonth().get(i).indexOf(".") + 1).concat("月"));
            operationDataLatestSixMonthResponseDataDto.setAmount(String.valueOf(AmountConverter.convertStringToCent(operationDataDto.getMoney().get(i))));
            operationDataLatestSixMonthResponseDataDtoList.add(operationDataLatestSixMonthResponseDataDto);
        }
        return operationDataLatestSixMonthResponseDataDtoList;
    }

    private List<OperationDataInvestCityResponseDataDto> convertMapToOperationDataInvestCityResponseDataDto() {
        List<OperationDataInvestCityDataDto> operationDataInvestCityDataDtos = operationDataService.convertMapToOperationDataInvestCityDataDto();
        return operationDataInvestCityDataDtos.
                stream()
                .map(operationDataInvestCityDataDto -> new OperationDataInvestCityResponseDataDto(operationDataInvestCityDataDto))
                .collect(Collectors.toList()).subList(0, 3);
    }

    private List<OperationDataLoanerCityResponseDataDto> convertMapToOperationDataLoanerCityResponseDataDto() {
        List<OperationDataLoanerCityDataDto> operationDataLoanerCityDataDtos = operationDataService.convertMapToOperationDataLoanerCityDataDto();
        return operationDataLoanerCityDataDtos.
                stream()
                .map(operationDataLoanerCityDataDto -> new OperationDataLoanerCityResponseDataDto(operationDataLoanerCityDataDto))
                .collect(Collectors.toList());
    }


    private List<OperationDataInvestAmountResponseDataDto> convertMapToOperationDataInvestAmountResponseDataDto() {
        List<OperationDataInvestAmountDataDto> operationDataInvestAmountDataDtos = operationDataService.convertMapToOperationDataInvestAmountDataDto();

        return operationDataInvestAmountDataDtos
                .stream()
                .map(operationDataInvestAmountDataDto -> new OperationDataInvestAmountResponseDataDto(operationDataInvestAmountDataDto))
                .collect(Collectors.toList());
    }
}
