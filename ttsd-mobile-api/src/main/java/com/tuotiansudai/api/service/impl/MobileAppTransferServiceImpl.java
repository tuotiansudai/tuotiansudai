package com.tuotiansudai.api.service.impl;

import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.*;
import com.tuotiansudai.api.service.MobileAppTransferService;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.TransferStatus;
import com.tuotiansudai.transfer.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.transfer.repository.model.TransferApplicationModel;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MobileAppTransferServiceImpl implements MobileAppTransferService{

    static Logger logger = Logger.getLogger(MobileAppTransferServiceImpl.class);

    @Autowired
    private TransferApplicationMapper transferApplicationMapper;

    @Autowired
    private InvestMapper investMapper;

    @Override
    public BaseResponseDto getTransferee(TransferTransfereeRequestDto transferTransfereeRequestDto) {
        BaseResponseDto dto = new BaseResponseDto();
        Integer index = transferTransfereeRequestDto.getIndex();
        Integer pageSize = transferTransfereeRequestDto.getPageSize();
        if (index == null || pageSize == null || index.intValue() <=0 || pageSize.intValue() <=0) {
            return new BaseResponseDto(ReturnMessage.REQUEST_PARAM_IS_WRONG.getCode(),ReturnMessage.REQUEST_PARAM_IS_WRONG.getMsg());
        }
        TransferApplicationModel transferApplicationModel = transferApplicationMapper.findById(transferTransfereeRequestDto.getTransferApplicationId());
        InvestModel investModel = null;
        if (transferApplicationModel.getStatus() == TransferStatus.SUCCESS && transferApplicationModel.getInvestId() != null) {
            investModel = investMapper.findById(transferApplicationModel.getInvestId());
        }
        TransferTransfereeRecordResponseDataDto transferTransfereeRecordResponseDataDto = new TransferTransfereeRecordResponseDataDto(investModel != null ? investModel.getLoginName() : "",
                transferApplicationModel.getTransferAmount(), transferApplicationModel.getTransferTime());
        TransferTransfereeResponseDataDto transferTransfereeResponseDataDto =  new TransferTransfereeResponseDataDto(transferTransfereeRequestDto.getIndex(), transferTransfereeRequestDto.getPageSize(),
                Lists.newArrayList(transferTransfereeRecordResponseDataDto).size(), Lists.newArrayList(transferTransfereeRecordResponseDataDto));
        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        dto.setData(transferTransfereeResponseDataDto);
        return dto;
    }

}
