package com.tuotiansudai.api.service.v1_0.impl;

import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.MobileAppInvestRepayListService;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.InvestRepayModel;
import com.tuotiansudai.repository.model.LoanModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MobileAppInvestRepayListServiceImpl implements MobileAppInvestRepayListService {
    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Override
    public BaseResponseDto generateUserInvestRepayList(InvestRepayListRequestDto requestDto) {
        String loginName = requestDto.getBaseParam().getUserId();
        String paidStatus = requestDto.getStatus();
        Integer index = requestDto.getIndex();
        Integer pageSize = requestDto.getPageSize();

        int rowLimit = requestDto.getPageSize().intValue();
        int rowIndex = (requestDto.getIndex() - 1) * rowLimit;
        List<InvestRepayModel> investRepayModels = investRepayMapper.findByLoginNameAndStatus(loginName, paidStatus, rowIndex, rowLimit);
        int investRepayModelCount = (int) investRepayMapper.findCountByLoginNameAndStatus(loginName, paidStatus);

        InvestRepayListResponseDataDto dtoData = new InvestRepayListResponseDataDto();
        dtoData.setRecordList(convertResponseData(investRepayModels));
        dtoData.setIndex(index);
        dtoData.setPageSize(pageSize);
        dtoData.setTotalCount(investRepayModelCount);

        BaseResponseDto dto = new BaseResponseDto();
        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        dto.setData(dtoData);

        return dto;
    }

    private List<InvestRepayRecordResponseDataDto> convertResponseData(List<InvestRepayModel> investRepayModels) {
        Map<Long, InvestModel> investCacheMap = new HashMap<>();
        Map<Long, LoanModel> loanCacheMap = new HashMap<>();

        List<InvestRepayRecordResponseDataDto> responseDataDtoList = new ArrayList<>();
        for (InvestRepayModel investRepayModel : investRepayModels) {
            Long investId = investRepayModel.getInvestId();
            InvestModel investModel;
            LoanModel loanModel;
            if (investCacheMap.containsKey(investId)) {
                investModel = investCacheMap.get(investId);
            } else {
                investModel = investMapper.findById(investId);
            }
            Long loanId = investModel.getLoanId();
            if (loanCacheMap.containsKey(loanId)) {
                loanModel = loanCacheMap.get(loanId);
            } else {
                loanModel = loanMapper.findById(loanId);
            }
            InvestRepayRecordResponseDataDto dataDto = new InvestRepayRecordResponseDataDto(investRepayModel, investModel, loanModel);
            responseDataDtoList.add(dataDto);
        }
        return responseDataDtoList;
    }
}
