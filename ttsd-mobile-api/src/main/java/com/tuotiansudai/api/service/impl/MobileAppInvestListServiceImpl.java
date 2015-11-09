package com.tuotiansudai.api.service.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.*;
import com.tuotiansudai.api.service.MobileAppInvestListService;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.repository.model.InvestStatus;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class MobileAppInvestListServiceImpl implements MobileAppInvestListService {
    @Autowired
    private InvestMapper investMapper;
    @Override
    public BaseResponseDto generateInvestList(InvestListRequestDto investListRequestDto) {
        BaseResponseDto dto = new BaseResponseDto();
        Integer index = investListRequestDto.getIndex();
        Integer pageSize = investListRequestDto.getPageSize();
        long loanId = Long.parseLong(investListRequestDto.getLoanId());

        long count = investMapper.findCountByStatus(loanId, InvestStatus.SUCCESS);

        if(index == null || index.intValue() <=0){
            index = 1;
        }

        if(pageSize == null || pageSize.intValue() <=0){
            pageSize = 10;
        }
        List<InvestModel> investModels = investMapper.findByStatus(loanId, (index - 1) * pageSize, pageSize, InvestStatus.SUCCESS);
        List<InvestRecordResponseDataDto> investRecordResponseDataDto = null;
        if(CollectionUtils.isNotEmpty(investModels)){
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
        investListResponseDataDto.setTotalCount((int)count);
        dto.setData(investListResponseDataDto);

        return dto;
    }



    @Override
    public BaseResponseDto generateUserInvestList(UserInvestListRequestDto requestDto) {
        throw new NotImplementedException(getClass().getName());
    }
}
