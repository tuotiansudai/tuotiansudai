package com.tuotiansudai.console.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.LoanRepayDataItemDto;
import com.tuotiansudai.repository.mapper.LoanRepayMapper;
import com.tuotiansudai.repository.model.LoanRepayModel;
import com.tuotiansudai.repository.model.RepayStatus;
import com.tuotiansudai.util.CalculateUtil;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ConsoleLoanRepayService {

    static Logger logger = Logger.getLogger(ConsoleLoanRepayService.class);

    @Value("${pay.overdue.fee}")
    private double overdueFee;

    @Autowired
    private LoanRepayMapper loanRepayMapper;

    @Value("#{'${repay.remind.mobileList}'.split('\\|')}")
    private List<String> repayRemindMobileList;

    public BaseDto<BasePaginationDataDto<LoanRepayDataItemDto>> findLoanRepayPagination(int index, int pageSize, Long loanId,
                                                                                        String loginName, Date startTime, Date endTime, RepayStatus repayStatus) {

        startTime = startTime == null ? new DateTime(0).toDate() : new DateTime(startTime).withTimeAtStartOfDay().toDate();
        endTime = endTime == null ? CalculateUtil.calculateMaxDate() : new DateTime(endTime).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate();

        if (index < 1) {
            index = 1;
        }
        if (pageSize < 1) {
            pageSize = 10;
        }

        BaseDto<BasePaginationDataDto<LoanRepayDataItemDto>> baseDto = new BaseDto<>();
        int count = loanRepayMapper.findLoanRepayCount(loanId, loginName, repayStatus, startTime, endTime);
        List<LoanRepayModel> loanRepayModels = loanRepayMapper.findLoanRepayPagination((index - 1) * pageSize, pageSize,
                loanId, loginName, repayStatus, startTime, endTime);
        List<LoanRepayDataItemDto> loanRepayDataItemDtos = Lists.newArrayList();
        for (LoanRepayModel loanRepayModel : loanRepayModels) {
            LoanRepayDataItemDto loanRepayDataItemDto = new LoanRepayDataItemDto(loanRepayModel);
            loanRepayDataItemDtos.add(loanRepayDataItemDto);
        }
        BasePaginationDataDto<LoanRepayDataItemDto> basePaginationDataDto = new BasePaginationDataDto<>(index, pageSize, count, loanRepayDataItemDtos);
        basePaginationDataDto.setStatus(true);
        baseDto.setData(basePaginationDataDto);
        return baseDto;

    }
}
