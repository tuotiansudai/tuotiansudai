package com.tuotiansudai.console.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.ExtraLoanRateItemDto;
import com.tuotiansudai.dto.LoanListDto;
import com.tuotiansudai.repository.mapper.ExtraLoanRateMapper;
import com.tuotiansudai.repository.mapper.LoanDetailsMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.ExtraLoanRateModel;
import com.tuotiansudai.repository.model.LoanDetailsModel;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.LoanStatus;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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

    public int findLoanListCount(LoanStatus status, Long loanId, String loanName, Date startTime, Date endTime) {
        return loanMapper.findLoanListCount(status, loanId, loanName, startTime, endTime);
    }

    public List<LoanListDto> findLoanList(LoanStatus status, Long loanId, String loanName, Date startTime, Date endTime, int currentPageNo, int pageSize) {
        currentPageNo = (currentPageNo - 1) * 10;
        List<LoanModel> loanModels = loanMapper.findLoanList(status, loanId, loanName, startTime, endTime, currentPageNo, pageSize);
        List<LoanListDto> loanListDtos = Lists.newArrayList();
        for (LoanModel loanModel : loanModels) {
            LoanListDto loanListDto = new LoanListDto();
            loanListDto.setId(loanModel.getId());
            loanListDto.setName(loanModel.getName());
            loanListDto.setType(loanModel.getType());
            loanListDto.setAgentLoginName(loanModel.getAgentLoginName());
            loanListDto.setLoanerUserName(loanModel.getLoanerUserName());
            loanListDto.setLoanAmount(loanModel.getLoanAmount());
            loanListDto.setPeriods(loanModel.getPeriods());
            loanListDto.setBasicRate(String.valueOf(new BigDecimal(loanModel.getBaseRate() * 100).setScale(2, BigDecimal.ROUND_HALF_UP)) + "%");
            loanListDto.setActivityRate(String.valueOf(new BigDecimal(loanModel.getActivityRate() * 100).setScale(2, BigDecimal.ROUND_HALF_UP)) + "%");
            loanListDto.setStatus(loanModel.getStatus());
            loanListDto.setCreatedTime(loanModel.getCreatedTime());
            loanListDto.setProductType(loanModel.getProductType());
            loanListDto.setPledgeType(loanModel.getPledgeType());
            List<ExtraLoanRateModel> extraLoanRateModels = extraLoanRateMapper.findByLoanId(loanModel.getId());
            if (CollectionUtils.isNotEmpty(extraLoanRateModels)) {
                loanListDto.setExtraLoanRateModels(fillExtraLoanRate(extraLoanRateModels));
            }
            LoanDetailsModel loanDetailsModel = loanDetailsMapper.getByLoanId(loanModel.getId());
            loanListDto.setExtraSource(loanDetailsModel != null ? loanDetailsModel.getExtraSource() : null);
            loanListDto.setNonTransferable(loanDetailsModel !=null ? loanDetailsModel.getNonTransferable():false);
            loanListDto.setDisableCoupon(loanDetailsModel !=null ? loanDetailsModel.getDisableCoupon():false);
            loanListDtos.add(loanListDto);
        }
        return loanListDtos;
    }

    private List<ExtraLoanRateItemDto> fillExtraLoanRate(List<ExtraLoanRateModel> extraLoanRateModels) {
        return Lists.transform(extraLoanRateModels, ExtraLoanRateItemDto::new);
    }
}
