package com.tuotiansudai.api.service.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.api.dto.*;
import com.tuotiansudai.api.service.MobileAppInvestListService;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.InvestRepayModel;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.util.AmountConverter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
    private InvestRepayMapper investRepayMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Override
    public BaseResponseDto generateInvestList(InvestListRequestDto investListRequestDto) {
        BaseResponseDto dto = new BaseResponseDto();
        Integer index = investListRequestDto.getIndex();
        Integer pageSize = investListRequestDto.getPageSize();
        if(StringUtils.isEmpty(investListRequestDto.getLoanId())){
            return new BaseResponseDto(ReturnMessage.REQUEST_PARAM_IS_WRONG.getCode(),ReturnMessage.REQUEST_PARAM_IS_WRONG.getMsg());
        }
        long loanId = Long.parseLong(investListRequestDto.getLoanId());

        long count = investMapper.findCountByStatus(loanId, InvestStatus.SUCCESS);

        if (index == null || index.intValue() <= 0) {
            index = 1;
        }

        if (pageSize == null || pageSize.intValue() <= 0) {
            pageSize = 10;
        }
        List<InvestModel> investModels = investMapper.findByStatus(loanId, (index - 1) * pageSize, pageSize, InvestStatus.SUCCESS);
        List<InvestRecordResponseDataDto> investRecordResponseDataDto = null;
        if (CollectionUtils.isNotEmpty(investModels)) {
            investRecordResponseDataDto = Lists.transform(investModels, new Function<InvestModel, InvestRecordResponseDataDto>() {
                @Override
                public InvestRecordResponseDataDto apply(InvestModel input) {
                    return new InvestRecordResponseDataDto(input);
                }
            });
        }

        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        InvestListResponseDataDto investListResponseDataDto = new InvestListResponseDataDto();
        investListResponseDataDto.setInvestRecord(investRecordResponseDataDto);
        investListResponseDataDto.setIndex(index);
        investListResponseDataDto.setPageSize(pageSize);
        investListResponseDataDto.setTotalCount((int) count);
        dto.setData(investListResponseDataDto);
        return dto;
    }

    @Override
    public BaseResponseDto<UserInvestListResponseDataDto> generateUserInvestList(UserInvestListRequestDto requestDto) {
        String loginName = requestDto.getBaseParam().getUserId();
        int pageSize = requestDto.getPageSize();
        int index = (requestDto.getIndex() - 1) * pageSize;

        List<InvestModel> investList = investMapper.findByLoginName(loginName, index, pageSize);
        int investListCount = (int) investMapper.findCountByLoginName(loginName);

        // build InvestList
        UserInvestListResponseDataDto dtoData = new UserInvestListResponseDataDto();
        dtoData.setInvestList(convertResponseData(investList));
        dtoData.setIndex(requestDto.getIndex());
        dtoData.setPageSize(requestDto.getPageSize());
        dtoData.setTotalCount(investListCount);

        // BaseDto
        BaseResponseDto<UserInvestListResponseDataDto> dto = new BaseResponseDto<>();
        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        dto.setData(dtoData);

        return dto;
    }

    private List<UserInvestRecordResponseDataDto> convertResponseData(List<InvestModel> investList) {
        List<UserInvestRecordResponseDataDto> list = Lists.newArrayList();
        Map<Long, LoanModel> loanMapCache = Maps.newHashMap();
        if (investList != null) {
            for (InvestModel invest : investList) {
                long loanId = invest.getLoanId();
                LoanModel loanModel;
                if (loanMapCache.containsKey(loanId)) {
                    loanModel = loanMapCache.get(loanId);
                } else {
                    loanModel = loanMapper.findById(loanId);
                    loanMapCache.put(loanId, loanModel);
                }
                UserInvestRecordResponseDataDto dto = new UserInvestRecordResponseDataDto(invest, loanModel);

                long amount = 0;
                List<InvestRepayModel> investRepayModels = investRepayMapper.findByInvestIdAndPeriodAsc(invest.getId());
                for (InvestRepayModel investRepayModel : investRepayModels) {
                    amount += investRepayModel.getExpectedInterest() - investRepayModel.getExpectedFee();
                }

                if (CollectionUtils.isEmpty(investRepayModels)) {
                    amount = investService.estimateInvestIncome(invest.getLoanId(), invest.getAmount());
                }

                dto.setInvestInterest(AmountConverter.convertCentToString(amount));
                list.add(dto);
            }
        }
        return list;
    }
}
