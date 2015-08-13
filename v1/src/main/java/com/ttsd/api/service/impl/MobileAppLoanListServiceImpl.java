package com.ttsd.api.service.impl;

import com.esoft.archer.common.exception.NoMatchingObjectsException;
import com.esoft.archer.system.controller.DictUtil;
import com.esoft.core.annotations.Logger;
import com.esoft.core.util.ArithUtil;
import com.esoft.jdp2p.loan.model.Loan;
import com.esoft.jdp2p.loan.service.LoanCalculator;
import com.ttsd.api.dao.MobileAppLoanListDao;
import com.ttsd.api.dto.*;
import com.ttsd.api.service.MobileAppLoanListService;
import org.apache.commons.logging.Log;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class MobileAppLoanListServiceImpl implements MobileAppLoanListService {
    @Logger
    static Log log;
    @Resource
    private MobileAppLoanListDao mobileAppLoanListDao;

    @Resource
    private LoanCalculator loanCalculator;

    @Override
    public BaseResponseDto generateLoanList(LoanListRequestDto loanListRequestDto) {
        BaseResponseDto dto = new BaseResponseDto();
        Integer index = loanListRequestDto.getIndex();
        Integer pageSize = loanListRequestDto.getPageSize();
        String returnCode = ReturnMessage.SUCCESS.getCode();
        List<LoanResponseDataDto> loanDtoList = null;
        if (index == null || pageSize == null || index.intValue() <=0 || pageSize.intValue() <=0) {
            returnCode = ReturnMessage.REQUEST_PARAM_IS_WRONG.getCode();
        }
        if (ReturnMessage.SUCCESS.getCode().equals(returnCode)) {

            List<Loan> investList = mobileAppLoanListDao.getInvestList(index, pageSize);

            loanDtoList = convertLoanDto(investList);
        }

        dto.setCode(returnCode);
        dto.setMessage(ReturnMessage.getErrorMsgByCode(returnCode));
        if (ReturnMessage.SUCCESS.getCode().equals(returnCode) && loanDtoList != null) {
            LoanListResponseDataDto loanListResponseDataDto = new LoanListResponseDataDto();
            loanListResponseDataDto.setLoanList(loanDtoList);
            loanListResponseDataDto.setIndex(index);
            loanListResponseDataDto.setPageSize(pageSize);
            loanListResponseDataDto.setTotalCount(mobileAppLoanListDao.getTotalCount());
            dto.setData(loanListResponseDataDto);
        }

        return dto;
    }

    @Override
    public List<LoanResponseDataDto> convertLoanDto(List<Loan> loanList) {
        DictUtil dictUtil = new DictUtil();
        List<LoanResponseDataDto> investList = new ArrayList<LoanResponseDataDto>();
        for (Loan loan : loanList) {
            LoanResponseDataDto loanResponseDataDto = new LoanResponseDataDto();
            loanResponseDataDto.setLoanId(loan.getId());
            loanResponseDataDto.setLoanType(loan.getLoanActivityType());
            loanResponseDataDto.setLoanName(loan.getName());
            loanResponseDataDto.setRepayTypeCode(loan.getType().getRepayType());
            loanResponseDataDto.setRepayTypeName(dictUtil.getValue("repay_type", loan.getType().getRepayType()));
            loanResponseDataDto.setDeadline(loan.getDeadline() * loan.getType().getRepayTimePeriod());
            loanResponseDataDto.setRepayUnit(dictUtil.getValue("repay_unit", loan.getType().getRepayTimeUnit()));
            loanResponseDataDto.setRatePercent(loan.getRatePercent());
            loanResponseDataDto.setLoanMoney(loan.getLoanMoney());
            loanResponseDataDto.setLoanStatus(loan.getStatus());
            loanResponseDataDto.setLoanStatusDesc(StandardStatus.getMessageByCode(loan.getStatus()));
            try {
                loanResponseDataDto.setInvestedMoney(ArithUtil.sub(loan.getLoanMoney(), loanCalculator.calculateMoneyNeedRaised(loan.getId())));
            } catch (NoMatchingObjectsException e) {
                log.error(e.getLocalizedMessage(), e);
            }
            loanResponseDataDto.setBaseRatePercent(loan.getJkRatePercent());
            loanResponseDataDto.setActivityRatePercent(loan.getHdRatePercent());
            investList.add(loanResponseDataDto);
        }
        return investList;
    }
}
