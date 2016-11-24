package com.tuotiansudai.api.service.v1_0.impl;


import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppOperationDataService;
import com.tuotiansudai.dto.OperationDataDto;
import com.tuotiansudai.repository.model.InvestDataView;
import com.tuotiansudai.service.OperationDataService;
import com.tuotiansudai.util.CalculateUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class MobileAppOperationDataServiceImpl implements MobileAppOperationDataService {

    @Autowired
    private OperationDataService operationDataService;

    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");

    @Override
    public BaseResponseDto generatorOperationData(BaseParamDto requestDto) {
        OperationDataResponseDataDto dataDto = new OperationDataResponseDataDto();

        OperationDataDto operationDataDto = operationDataService.getOperationDataFromRedis(new Date());
        dataDto.setCurrentDay(sdf.format(new Date()));
        dataDto.setOperationDays(String.valueOf(operationDataDto.getOperationDays()));
        dataDto.setTotalTradeAmount(operationDataDto.getTradeAmount());
        dataDto.setTotalInterest("");//需要计算

        List<InvestDataView> investDataViewList = operationDataService.getInvestDetail(new Date());
        List<OperationDataInvestByProductTypeResponseDataDto> operationDataInvestByProductTypeResponseDataDtoList = Lists.newArrayList();
        long totalTradeCount = 0;
        for(InvestDataView investDataView: investDataViewList){
            OperationDataInvestByProductTypeResponseDataDto operationDataInvestByProductTypeResponseDataDto = new OperationDataInvestByProductTypeResponseDataDto();
            operationDataInvestByProductTypeResponseDataDto.setName(investDataView.getProductName());
            operationDataInvestByProductTypeResponseDataDto.setAmount(investDataView.getTotalInvestAmount());
            operationDataInvestByProductTypeResponseDataDtoList.add(operationDataInvestByProductTypeResponseDataDto);
            totalTradeCount += investDataView.getCountInvest();
        }
        dataDto.setInvestListByProdyctType(operationDataInvestByProductTypeResponseDataDtoList);
        dataDto.setTotalTradeCount(String.valueOf(totalTradeCount));

        dataDto.setTotalInvestUserCount(String.valueOf(operationDataDto.getUsersCount()));

        List<Integer> sexList = operationDataService.findScaleBySex();
        dataDto.setFemaleScale(String.valueOf(CalculateUtil.calculatePercentage(sexList.get(0), sexList.get(0) + sexList.get(1), 2)));
        dataDto.setMaleScale(String.valueOf(100 - CalculateUtil.calculatePercentage(sexList.get(0), sexList.get(0) + sexList.get(1), 2)));

        //近半年的交易金额







        BaseResponseDto<OperationDataResponseDataDto> dto = new BaseResponseDto<>();
        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        dto.setData(dataDto);
        return dto;
    }
}
