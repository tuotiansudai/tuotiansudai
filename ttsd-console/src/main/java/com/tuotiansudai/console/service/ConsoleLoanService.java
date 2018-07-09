package com.tuotiansudai.console.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.ExtraLoanRateItemDto;
import com.tuotiansudai.dto.LoanListDto;
import com.tuotiansudai.repository.mapper.ExtraLoanRateMapper;
import com.tuotiansudai.repository.mapper.LoanDetailsMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConsoleLoanService {

    static Logger logger = Logger.getLogger(ConsoleLoanService.class);

    @Autowired
    private LoanMapper loanMapper;

    @Value("#{'${web.random.investor.list}'.split('\\|')}")
    private List<String> showRandomLoginNameList;

    @Autowired
    private LoanDetailsMapper loanDetailsMapper;

    @Autowired
    private ExtraLoanRateMapper extraLoanRateMapper;

    public int findLoanListCount(FundPlatform fundPlatform,LoanStatus status, Long loanId, String loanName, Date startTime, Date endTime) {
        return loanMapper.findLoanListCount(fundPlatform,status, loanId, loanName, startTime, endTime);
    }

    public List<LoanListDto> findLoanList(FundPlatform fundPlatform,LoanStatus status, Long loanId, String loanName, Date startTime, Date endTime, int currentPageNo, int pageSize) {
        currentPageNo = (currentPageNo - 1) * 10;
        List<LoanModel> loanModels = loanMapper.findLoanList(fundPlatform,status, loanId, loanName, startTime, endTime, currentPageNo, pageSize);
        return loanModels.stream().map(loanModel->{return new LoanListDto(loanModel,fillExtraLoanRate(extraLoanRateMapper.findByLoanId(loanModel.getId())),loanDetailsMapper.getByLoanId(loanModel.getId()));}).collect(Collectors.toList());
    }

    private List<ExtraLoanRateItemDto> fillExtraLoanRate(List<ExtraLoanRateModel> extraLoanRateModels) {
        if(CollectionUtils.isEmpty(extraLoanRateModels)){
            return null;
        }
        return Lists.transform(extraLoanRateModels, ExtraLoanRateItemDto::new);
    }


}
