package com.tuotiansudai.service.impl;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BasePaginationDto;
import com.tuotiansudai.dto.LoanRepayPaginationDataDto;
import com.tuotiansudai.repository.mapper.LoanRepayMapper;
import com.tuotiansudai.repository.model.LoanRepayModel;
import com.tuotiansudai.repository.model.LoanType;
import com.tuotiansudai.repository.model.RepayStatus;
import com.tuotiansudai.service.LoanRepayService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;
@Service
public class LoanRepayServiceImpl implements LoanRepayService {

    @Autowired
    private LoanRepayMapper loanRepayMapper;

    @Override
    public BasePaginationDto findLoanRepayPagination(int index,int pageSize,String loanId,
                                                     String loginName,String repayStartDate,String repayEndDate,RepayStatus repayStatus) {
        if( index < 1){
            index = 1;
        }
        if(pageSize < 1){
            pageSize = 10;
        }
        Long loanIdLong = null;
        if(StringUtils.isNotEmpty(loanId)){
            loanIdLong = Long.parseLong(loanId);
        }
        int count = loanRepayMapper.findLoanRepayCount(loanIdLong, loginName, repayStatus, repayStartDate, repayEndDate);
        BasePaginationDto baseDto = new BasePaginationDto(index, pageSize, count);
        List<LoanRepayModel> loanRepayModels = loanRepayMapper.findLoanRepayPagination((index - 1) * pageSize, pageSize,
                                                    loanIdLong, loginName, repayStatus, repayStartDate, repayEndDate);
        List<LoanRepayPaginationDataDto> loanRepayPaginationDataDtos = Lists.newArrayList();
        for (LoanRepayModel loanRepayModel : loanRepayModels) {
            LoanRepayPaginationDataDto loanRepayPaginationDataDto = new LoanRepayPaginationDataDto(loanRepayModel);
            loanRepayPaginationDataDtos.add(loanRepayPaginationDataDto);
        }

        baseDto.setRecordDtoList(loanRepayPaginationDataDtos);
        baseDto.setStatus(true);
        return baseDto;

    }


}
