package com.ttsd.api.service.impl;


import com.esoft.archer.common.exception.NoMatchingObjectsException;
import com.esoft.archer.system.controller.DictUtil;
import com.esoft.core.annotations.Logger;
import com.esoft.core.util.ArithUtil;
import com.esoft.jdp2p.invest.model.Invest;
import com.esoft.jdp2p.loan.model.Loan;
import com.esoft.jdp2p.loan.service.LoanCalculator;
import com.ttsd.aliyun.PropertiesUtils;
import com.ttsd.api.dao.InvestListDao;
import com.ttsd.api.dao.LoanDetailDao;
import com.ttsd.api.dto.*;
import com.ttsd.api.service.MobileLoanDetailAppService;
import com.ttsd.util.CommonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MobileLoanDetailAppServiceImpl implements MobileLoanDetailAppService {
    @Logger
    static Log log;
    @Resource
    private LoanDetailDao loanDetailDao;
    @Resource
    private InvestListDao investListDao;

    @Resource
    private LoanCalculator loanCalculator;



    @Override
    public BaseResponseDto generateLoanDetail(LoanDetailRequestDto loanDetailRequestDto) {
        String resultCode = ReturnMessage.SUCCESS.getCode();
        BaseResponseDto<LoanDetailResponseDataDto> dto = new BaseResponseDto<LoanDetailResponseDataDto>();
        String loanId = loanDetailRequestDto.getLoanId();
        Loan loan = loanDetailDao.getLoanById(loanId);

        if (loan == null) {
            resultCode = ReturnMessage.LOAN_ID_IS_NOT_EXIST.getCode();
            log.info("标的详情" + ReturnMessage.LOAN_ID_IS_NOT_EXIST.getCode() + ":" + ReturnMessage.LOAN_ID_IS_NOT_EXIST.getMsg());
        }
        if (ReturnMessage.SUCCESS.getCode().equals(resultCode)) {
            List<EvidenceDto> evidences = new ArrayList<EvidenceDto>();
            EvidenceDto evidenceDto = new EvidenceDto();
            if (StringUtils.isNotEmpty(loan.getGuaranteeCompanyDescription())) {
                evidenceDto.setImageUrl(getImageUrl(loan.getGuaranteeCompanyDescription()));
            }
            evidences.add(evidenceDto);
            LoanDetailResponseDataDto loanDetailResponseDataDto = convertLoanDetailFromLoan(loan, evidences);
            dto.setData(loanDetailResponseDataDto);
        }
        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.getErrorMsgByCode(resultCode));
        return dto;
    }

    @Override
    public LoanDetailResponseDataDto convertLoanDetailFromLoan(Loan loan, List<EvidenceDto> evidences) {
        DictUtil dictUtil = new DictUtil();
        LoanDetailResponseDataDto loanDetailResponseDataDto = new LoanDetailResponseDataDto();
        loanDetailResponseDataDto.setLoanId(loan.getId());
        loanDetailResponseDataDto.setLoanType(loan.getLoanActivityType());
        loanDetailResponseDataDto.setLoanName(loan.getName());
        loanDetailResponseDataDto.setRepayTypeCode(loan.getType().getRepayType());
        loanDetailResponseDataDto.setRepayTypeName(dictUtil.getValue("repay_type", loan.getType().getRepayType()));
        loanDetailResponseDataDto.setDeadline(loan.getDeadline() * loan.getType().getRepayTimePeriod());
        loanDetailResponseDataDto.setRepayUnit(dictUtil.getValue("repay_unit", loan.getType().getRepayTimeUnit()));
        loanDetailResponseDataDto.setRatePercent(loan.getRatePercent());
        loanDetailResponseDataDto.setLoanMoney(loan.getLoanMoney());
        loanDetailResponseDataDto.setLoanStatus(loan.getStatus());
        loanDetailResponseDataDto.setLoanStatusDesc(StandardStatus.getMessageByCode(loan.getStatus()));
        loanDetailResponseDataDto.setInvestedCount(loanCalculator.countSuccessInvest(loan.getId()));
        try {
            loanDetailResponseDataDto.setInvestedMoney(ArithUtil.sub(loan.getLoanMoney(), loanCalculator.calculateMoneyNeedRaised(loan.getId())));
        } catch (NoMatchingObjectsException e) {
            log.error(e.getLocalizedMessage(), e);
        }
        loanDetailResponseDataDto.setJkRatePercent(loan.getJkRatePercent());
        loanDetailResponseDataDto.setHdRatePercent(loan.getHdRatePercent());
        loanDetailResponseDataDto.setDescription(loan.getDescription());
        loanDetailResponseDataDto.setEvidence(evidences);
        loanDetailResponseDataDto.setTotalCount(loan.getInvests().size());
        if (CollectionUtils.isNotEmpty(loan.getInvests())) {
            loanDetailResponseDataDto.setInvestRecord(convertInvestRecordDtoFromInvest(investListDao.getInvestList(1,5,loan.getId())));
        }
        return loanDetailResponseDataDto;
    }

    @Override
    public List<InvestRecordDto> convertInvestRecordDtoFromInvest(List<Invest> invests) {
        List<InvestRecordDto> investRecordDtos = new ArrayList<InvestRecordDto>();

        for (Invest invest:invests) {
            InvestRecordDto investRecordDto = new InvestRecordDto();
            String userNameTemp = invest.getUser().getUsername();
            if(userNameTemp.length() > 3){
                userNameTemp = userNameTemp.substring(0,3) + "***";
            }else{
                userNameTemp +="***";
            }
            investRecordDto.setUserName(userNameTemp);
            investRecordDto.setInvestMoney(invest.getInvestMoney());
            investRecordDto.setInvestTime(invest.getTime().toString());
            investRecordDtos.add(investRecordDto);
        }
        return investRecordDtos;
    }

    @Override
    public List<String> getImageUrl(String guaranteeCompanyDescription) {
        String urlPattern = null;
        if (!CommonUtils.isDevEnvironment("environment")) {
            urlPattern = PropertiesUtils.getPro("production.imageUrl.pattern");
        } else {
            urlPattern = PropertiesUtils.getPro("dev.imageUrl.pattern");
        }

        Pattern pattern = Pattern.compile(urlPattern, Pattern.CASE_INSENSITIVE);
        List<String> imageUrls = new ArrayList<String>();
        String[] imageUrlsTemp = guaranteeCompanyDescription.split("title");
        for (String str : imageUrlsTemp) {
            Matcher matcher = pattern.matcher(str);
            while (matcher.find()) {
                imageUrls.add(matcher.group());
            }

        }
        return imageUrls;
    }
}
