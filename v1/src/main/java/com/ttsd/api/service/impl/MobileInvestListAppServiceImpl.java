package com.ttsd.api.service.impl;

import com.esoft.archer.common.exception.NoMatchingObjectsException;
import com.esoft.archer.system.controller.DictUtil;
import com.esoft.core.annotations.Logger;
import com.esoft.core.util.ArithUtil;
import com.esoft.jdp2p.loan.model.Loan;
import com.esoft.jdp2p.loan.service.LoanCalculator;
import com.ttsd.api.dao.InvestListDao;
import com.ttsd.api.dto.*;
import com.ttsd.api.service.MobileInvestListAppService;
import org.apache.commons.logging.Log;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class MobileInvestListAppServiceImpl implements MobileInvestListAppService {
    @Logger
    static Log log;
    @Resource
    private InvestListDao investListDao;

    @Resource
    private LoanCalculator loanCalculator;

    @Override
    public BaseResponseDto generateInvestList(InvestListRequestDto investListRequestDto) {
        BaseResponseDto dto = new BaseResponseDto();
        Integer index = investListRequestDto.getIndex();
        Integer pageSize = investListRequestDto.getPageSize();
        String returnCode = ReturnMessage.SUCCESS.getCode();
        List<InvestDto> investDtoList = null;
        if (index == null || pageSize == null || index.intValue() <=0 || pageSize.intValue() <=0) {
            returnCode = ReturnMessage.REQUEST_PARAM_IS_WRONG.getCode();
        }
        if (ReturnMessage.SUCCESS.getCode().equals(returnCode)) {

            List<Loan> investList = investListDao.getInvestList(index, pageSize);

            investDtoList = convertLoanToInvestDto(investList);
        }

        dto.setCode(returnCode);
        dto.setMessage(ReturnMessage.getErrorMsgByCode(returnCode));
        if (ReturnMessage.SUCCESS.getCode().equals(returnCode) && investDtoList != null) {
            InvestListResponseDataDto investListResponseDataDto = new InvestListResponseDataDto();
            investListResponseDataDto.setLoanList(investDtoList);
            investListResponseDataDto.setIndex(index);
            investListResponseDataDto.setPageSize(pageSize);
            investListResponseDataDto.setHasNextPage(investListDao.isHasNextPage(index,pageSize));
            dto.setData(investListResponseDataDto);
        }

        return dto;
    }

    @Override
    public List<InvestDto> convertLoanToInvestDto(List<Loan> loanList) {
        DictUtil dictUtil = new DictUtil();
        List<InvestDto> investList = new ArrayList<InvestDto>();
        for (Loan loan : loanList) {
            InvestDto investDto = new InvestDto();
            investDto.setLoanId(loan.getId());
            investDto.setLoanType(loan.getLoanActivityType());
            investDto.setLoanName(loan.getName());
            investDto.setRepayTypeCode(loan.getType().getRepayType());
            investDto.setRepayTypeName(dictUtil.getValue("repay_type", loan.getType().getRepayType()));
            investDto.setDeadline(loan.getDeadline() * loan.getType().getRepayTimePeriod());
            investDto.setRepayUnit(dictUtil.getValue("repay_unit", loan.getType().getRepayTimeUnit()));
            investDto.setRatePercent(loan.getRatePercent());
            investDto.setLoanMoney(loan.getLoanMoney());
            investDto.setLoanStatus(loan.getStatus());
            investDto.setLoanStatusDesc(StandardStatus.getMessageByCode(loan.getStatus()));
            try {
                investDto.setInvestedMoney(ArithUtil.sub(loan.getLoanMoney(), loanCalculator.calculateMoneyNeedRaised(loan.getId())));
            } catch (NoMatchingObjectsException e) {
                log.error(e.getLocalizedMessage(), e);
            }
            investDto.setJkRatePercent(loan.getJkRatePercent());
            investDto.setHdRatePercent(loan.getHdRatePercent());
            investList.add(investDto);
        }
        return investList;
    }
}
