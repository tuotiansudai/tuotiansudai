package com.tuotiansudai.service.impl;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.LoanRepayDataItemDto;
import com.tuotiansudai.repository.mapper.LoanRepayMapper;
import com.tuotiansudai.repository.model.LoanRepayModel;
import com.tuotiansudai.repository.model.RepayStatus;
import com.tuotiansudai.service.LoanRepayService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class LoanRepayServiceImpl implements LoanRepayService {

    @Autowired
    private LoanRepayMapper loanRepayMapper;

    @Override
    public BaseDto<BasePaginationDataDto> findLoanRepayPagination(int index,int pageSize,String loanId,
                                                     String loginName,String repayStartDate,String repayEndDate,RepayStatus repayStatus) {
        if( index < 1){
            index = 1;
        }
        if(pageSize < 1){
            pageSize = 10;
        }
        BaseDto<BasePaginationDataDto> baseDto = new BaseDto<>();
        Long loanIdLong = null;
        if(StringUtils.isNotEmpty(loanId)){
            loanIdLong = Long.parseLong(loanId);
        }
        int count = loanRepayMapper.findLoanRepayCount(loanIdLong, loginName, repayStatus, repayStartDate, repayEndDate);
        List<LoanRepayModel> loanRepayModels = loanRepayMapper.findLoanRepayPagination((index - 1) * pageSize, pageSize,
                loanIdLong, loginName, repayStatus, repayStartDate, repayEndDate);
        List<LoanRepayDataItemDto> loanRepayDataItemDtos = Lists.newArrayList();
        for (LoanRepayModel loanRepayModel : loanRepayModels) {
            LoanRepayDataItemDto loanRepayDataItemDto = new LoanRepayDataItemDto(loanRepayModel,false);
            loanRepayDataItemDtos.add(loanRepayDataItemDto);
        }
        BasePaginationDataDto basePaginationDataDto = new BasePaginationDataDto(index, pageSize, count,loanRepayDataItemDtos);
        basePaginationDataDto.setStatus(true);
        baseDto.setData(basePaginationDataDto);
        return baseDto;

    }


}
