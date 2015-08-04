package com.ttsd.api.service.impl;

import com.esoft.archer.common.exception.NoMatchingObjectsException;
import com.esoft.archer.system.controller.DictUtil;
import com.esoft.core.annotations.Logger;
import com.esoft.core.util.ArithUtil;
import com.esoft.jdp2p.loan.model.Loan;
import com.esoft.jdp2p.loan.service.LoanCalculator;
import com.ttsd.api.dao.LoanListDao;
import com.ttsd.api.dto.*;
import com.ttsd.api.service.MobileLoanListAppService;
import org.apache.commons.logging.Log;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class MobileLoanListAppServiceImpl implements MobileLoanListAppService {
    @Logger
    static Log log;
    @Resource
    private LoanListDao loanListDao;

    @Resource
    private LoanCalculator loanCalculator;

    @Override
    public BaseResponseDto generateLoanList(LoanListRequestDto loanListRequestDto) {
        BaseResponseDto dto = new BaseResponseDto();
        Integer index = loanListRequestDto.getIndex();
        Integer pageSize = loanListRequestDto.getPageSize();
        String returnCode = ReturnMessage.SUCCESS.getCode();
        List<LoanDto> loanDtoList = null;
        if (index == null || pageSize == null || index.intValue() <=0 || pageSize.intValue() <=0) {
            returnCode = ReturnMessage.REQUEST_PARAM_IS_WRONG.getCode();
        }
        if (ReturnMessage.SUCCESS.getCode().equals(returnCode)) {

            List<Loan> investList = loanListDao.getInvestList(index, pageSize);

            loanDtoList = convertLoanDto(investList);
        }

        dto.setCode(returnCode);
        dto.setMessage(ReturnMessage.getErrorMsgByCode(returnCode));
        if (ReturnMessage.SUCCESS.getCode().equals(returnCode) && loanDtoList != null) {
            LoanListResponseDataDto loanListResponseDataDto = new LoanListResponseDataDto();
            loanListResponseDataDto.setLoanList(loanDtoList);
            loanListResponseDataDto.setIndex(index);
            loanListResponseDataDto.setPageSize(pageSize);
            loanListResponseDataDto.setHasNextPage(loanListDao.isHasNextPage(index,pageSize));
            dto.setData(loanListResponseDataDto);
        }

        return dto;
    }

    @Override
    public List<LoanDto> convertLoanDto(List<Loan> loanList) {
        DictUtil dictUtil = new DictUtil();
        List<LoanDto> investList = new ArrayList<LoanDto>();
        for (Loan loan : loanList) {
            LoanDto loanDto = new LoanDto();
            loanDto.setLoanId(loan.getId());
            loanDto.setLoanType(loan.getLoanActivityType());
            loanDto.setLoanName(loan.getName());
            loanDto.setRepayTypeCode(loan.getType().getRepayType());
            loanDto.setRepayTypeName(dictUtil.getValue("repay_type", loan.getType().getRepayType()));
            loanDto.setDeadline(loan.getDeadline() * loan.getType().getRepayTimePeriod());
            loanDto.setRepayUnit(dictUtil.getValue("repay_unit", loan.getType().getRepayTimeUnit()));
            loanDto.setRatePercent(loan.getRatePercent());
            loanDto.setLoanMoney(loan.getLoanMoney());
            loanDto.setLoanStatus(loan.getStatus());
            loanDto.setLoanStatusDesc(StandardStatus.getMessageByCode(loan.getStatus()));
            try {
                loanDto.setInvestedMoney(ArithUtil.sub(loan.getLoanMoney(), loanCalculator.calculateMoneyNeedRaised(loan.getId())));
            } catch (NoMatchingObjectsException e) {
                log.error(e.getLocalizedMessage(), e);
            }
            loanDto.setJkRatePercent(loan.getJkRatePercent());
            loanDto.setHdRatePercent(loan.getHdRatePercent());
            investList.add(loanDto);
        }
        return investList;
    }
}
