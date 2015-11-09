package com.tuotiansudai.api.service.impl;

import com.tuotiansudai.api.dto.*;
import com.tuotiansudai.api.service.MobileAppInvestListService;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.utils.AmountUtil;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MobileAppInvestListServiceImpl implements MobileAppInvestListService {

    @Autowired
    private InvestService investService;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Override
    public BaseResponseDto generateInvestList(InvestListRequestDto investListRequestDto) {
        throw new NotImplementedException(getClass().getName());
    }

    @Override
    public BaseResponseDto generateUserInvestList(UserInvestListRequestDto requestDto) {
        String loginName = requestDto.getBaseParam().getUserId();
        int pageSize = requestDto.getPageSize().intValue();
        int index = (requestDto.getIndex().intValue() - 1) * pageSize;

        List<InvestModel> investList = investMapper.findByLoginName(loginName, index, pageSize);
        int investListCount = (int) investMapper.findCountByLoginName(loginName);

        // build InvestList
        UserInvestListResponseDataDto dtoData = new UserInvestListResponseDataDto();
        dtoData.setInvestList(convertResponseData(investList));
        dtoData.setIndex(index);
        dtoData.setPageSize(pageSize);
        dtoData.setTotalCount(investListCount);

        // BaseDto
        BaseResponseDto dto = new BaseResponseDto();
        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        dto.setData(dtoData);

        return dto;
    }

    private List<UserInvestRecordResponseDataDto> convertResponseData(List<InvestModel> investList) {
        List<UserInvestRecordResponseDataDto> list = new ArrayList<>();
        Map<Long, LoanModel> loanMapCache = new HashMap<>();
        if (investList != null) {
            for (InvestModel invest : investList) {
                long loanId = invest.getLoanId();
                LoanModel loanModel;
                if (loanMapCache.containsKey(loanId)) {
                    loanModel = loanMapCache.get(loanId);
                } else {
                    loanModel = loanMapper.findById(invest.getLoanId());
                    loanMapCache.put(loanId, loanModel);
                }
                UserInvestRecordResponseDataDto dto = new UserInvestRecordResponseDataDto(invest, loanModel);
                long investInterest = investService.calculateExpectedInterest(loanModel, invest.getAmount());
                dto.setInvestInterest(AmountUtil.convertCentToString(investInterest));
                list.add(dto);
            }
        }
        return list;
    }
}
