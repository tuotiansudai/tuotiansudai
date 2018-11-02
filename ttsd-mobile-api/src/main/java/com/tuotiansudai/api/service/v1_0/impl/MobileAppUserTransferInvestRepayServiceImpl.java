package com.tuotiansudai.api.service.v1_0.impl;


import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppUserTransferInvestRepayService;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.InvestRepayModel;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.repository.model.TransferApplicationModel;
import com.tuotiansudai.util.AmountConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class MobileAppUserTransferInvestRepayServiceImpl implements MobileAppUserTransferInvestRepayService {

    @Autowired
    private InvestService investService;

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Autowired
    private TransferApplicationMapper transferApplicationMapper;

    @Override
    public BaseResponseDto<UserTransferInvestRepayResponseDataDto> userTransferInvestRepay(UserTransferInvestRepayRequestDto userTransferInvestRepayRequestDto) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        BaseResponseDto<UserTransferInvestRepayResponseDataDto> responseDto = new BaseResponseDto<>();

        try{
            TransferApplicationModel transferApplicationModel = transferApplicationMapper.findById(Long.parseLong(userTransferInvestRepayRequestDto.getTransferApplicationId().trim()));

            InvestModel investModel = investService.findById(transferApplicationModel.getTransferInvestId());

            List<InvestRepayModel> investRepayModels = investRepayMapper.findByInvestIdAndPeriodAsc(investModel.getId());
            int maxPeriods = investRepayModels == null ? 0 : investRepayModels.size();

            UserTransferInvestRepayResponseDataDto userTransferInvestRepayResponseDataDto = new UserTransferInvestRepayResponseDataDto();
            List<TransferInvestRepayDataDto> transferInvestRepayList = new ArrayList<>();

            for(InvestRepayModel investRepayModel: investRepayModels){
                if(investRepayModel.getPeriod() < transferApplicationModel.getPeriod()){
                    continue;
                }
                TransferInvestRepayDataDto transferInvestRepayDataDto = new TransferInvestRepayDataDto();
                transferInvestRepayDataDto.setRepayDate(sdf.format(investRepayModel.getRepayDate()));
                transferInvestRepayDataDto.setExpectedInterest(AmountConverter.convertCentToString(investRepayModel.getCorpus() + investRepayModel.getExpectedInterest() + investRepayModel.getDefaultInterest() + investRepayModel.getOverdueInterest()));
                transferInvestRepayDataDto.setStatus(investRepayModel.getStatus().name());
                transferInvestRepayList.add(transferInvestRepayDataDto);
            }
            userTransferInvestRepayResponseDataDto.setTransferInvestRepays(transferInvestRepayList);

            responseDto.setCode(ReturnMessage.SUCCESS.getCode());
            responseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
            responseDto.setData(userTransferInvestRepayResponseDataDto);
        } catch (Exception e) {
            responseDto.setCode(ReturnMessage.REQUEST_PARAM_IS_WRONG.getCode());
            responseDto.setMessage(ReturnMessage.REQUEST_PARAM_IS_WRONG.getMsg());
            e.printStackTrace();
        }

        return responseDto;
    }
}
