package com.ttsd.api.service.impl;


import com.esoft.archer.common.exception.NoMatchingObjectsException;
import com.esoft.archer.system.controller.DictUtil;
import com.esoft.core.annotations.Logger;
import com.esoft.core.util.ArithUtil;
import com.esoft.jdp2p.invest.model.Invest;
import com.esoft.jdp2p.loan.LoanConstants;
import com.esoft.jdp2p.loan.model.Loan;
import com.esoft.jdp2p.loan.service.LoanCalculator;
import com.ttsd.aliyun.PropertiesUtils;
import com.ttsd.api.dao.MobileAppInvestListDao;
import com.ttsd.api.dao.MobileAppLoanDetailDao;
import com.ttsd.api.dao.MobileAppLoanListDao;
import com.ttsd.api.dto.*;
import com.ttsd.api.service.MobileAppLoanDetailService;
import com.ttsd.util.CommonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MobileAppLoanDetailServiceImpl implements MobileAppLoanDetailService {
    @Logger
    private Log log;
    @Resource
    private MobileAppLoanDetailDao mobileAppLoanDetailDao;
    @Resource
    private MobileAppInvestListDao mobileAppInvestListDao;

    @Autowired
    private MobileAppLoanListDao mobileAppLoanListDao;


    @Resource
    private LoanCalculator loanCalculator;
    @Value("${mobile.app.imageUrl.pattern}")
    private String urlPattern;
    @Value("${domain}")
    private String domainName;




    @Override
    public BaseResponseDto generateLoanDetail(LoanDetailRequestDto loanDetailRequestDto) {

        String resultCode = ReturnMessage.SUCCESS.getCode();
        BaseResponseDto<LoanDetailResponseDataDto> dto = new BaseResponseDto<LoanDetailResponseDataDto>();
        String loanId = loanDetailRequestDto.getLoanId();
        Loan loan = mobileAppLoanDetailDao.getLoanById(loanId);

        if (loan == null) {
            resultCode = ReturnMessage.LOAN_ID_IS_NOT_EXIST.getCode();
            log.info("标的详情" + ReturnMessage.LOAN_ID_IS_NOT_EXIST.getCode() + ":" + ReturnMessage.LOAN_ID_IS_NOT_EXIST.getMsg());
        }
        if (ReturnMessage.SUCCESS.getCode().equals(resultCode)) {
            List<EvidenceResponseDataDto> evidences = new ArrayList<EvidenceResponseDataDto>();
            EvidenceResponseDataDto evidenceResponseDataDto = new EvidenceResponseDataDto();
            if (StringUtils.isNotEmpty(loan.getGuaranteeCompanyDescription())) {
                evidenceResponseDataDto.setImageUrl(getImageUrl(loan.getGuaranteeCompanyDescription()));

            }
            evidences.add(evidenceResponseDataDto);
            LoanDetailResponseDataDto loanDetailResponseDataDto = convertLoanDetailFromLoan(loan, evidences);
            dto.setData(loanDetailResponseDataDto);
        }
        dto.setCode(resultCode);
        dto.setMessage(ReturnMessage.getErrorMsgByCode(resultCode));
        return dto;
    }

    @Override
    public LoanDetailResponseDataDto convertLoanDetailFromLoan(Loan loan, List<EvidenceResponseDataDto> evidences) {
        DictUtil dictUtil = new DictUtil();
        LoanDetailResponseDataDto loanDetailResponseDataDto = new LoanDetailResponseDataDto();
        loanDetailResponseDataDto.setLoanId(loan.getId());
        loanDetailResponseDataDto.setLoanType(loan.getLoanActivityType());
        loanDetailResponseDataDto.setLoanName(loan.getName());
        loanDetailResponseDataDto.setRepayTypeCode(loan.getType().getRepayType());
        loanDetailResponseDataDto.setRepayTypeName(dictUtil.getValue("repay_type", loan.getType().getRepayType()));
        loanDetailResponseDataDto.setDeadline(loan.getDeadline() * loan.getType().getRepayTimePeriod());
        loanDetailResponseDataDto.setRepayUnit(dictUtil.getValue("repay_unit", loan.getType().getRepayTimeUnit()));
        loanDetailResponseDataDto.setInterestPointName(dictUtil.getValue("interest_point",loan.getType().getInterestPoint()));
        loanDetailResponseDataDto.setRatePercent("" + loan.getRatePercent());
        loanDetailResponseDataDto.setLoanMoney("" + loan.getLoanMoney());
        loanDetailResponseDataDto.setLoanStatus(loan.getStatus());
        loanDetailResponseDataDto.setLoanStatusDesc(StandardStatus.getMessageByCode(loan.getStatus()));
        loanDetailResponseDataDto.setInvestedCount(loanCalculator.countSuccessInvest(loan.getId()));
        loanDetailResponseDataDto.setAgent(loan.getUser().getId());
        loanDetailResponseDataDto.setLoaner(loan.getAgent());
        loanDetailResponseDataDto.setVerifyTime(new SimpleDateFormat("yyyy-MM-dd").format(loan.getVerifyTime()));
        loanDetailResponseDataDto.setRemainTime(loanCalculator.calculateRemainTimeSeconds(loan.getId()));
        if(loan.getInvestBeginTime() != null){
            loanDetailResponseDataDto.setInvestBeginTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(loan.getInvestBeginTime()));
        }
        loanDetailResponseDataDto.setInvestBeginSeconds(com.ttsd.api.util.CommonUtils.calculatorInvestBeginSeconds(loan.getInvestBeginTime()));

        try {
            loanDetailResponseDataDto.setInvestedMoney("" + ArithUtil.sub(loan.getLoanMoney(), loanCalculator.calculateMoneyNeedRaised(loan.getId())));
        } catch (NoMatchingObjectsException e) {
            log.error(e.getLocalizedMessage(), e);
        }
        loanDetailResponseDataDto.setBaseRatePercent("" + loan.getJkRatePercent());
        loanDetailResponseDataDto.setActivityRatePercent("" + loan.getHdRatePercent());
        loanDetailResponseDataDto.setLoanDetail(loan.getDescription());
        loanDetailResponseDataDto.setEvidence(evidences);
        loanDetailResponseDataDto.setInvestCount(loan.getInvests().size());
        if (CollectionUtils.isNotEmpty(loan.getInvests())) {
            loanDetailResponseDataDto.setInvestRecord(convertInvestRecordDtoFromInvest(mobileAppInvestListDao.getInvestList(1,5,loan.getId())));
        }
        loanDetailResponseDataDto.setMinInvestMoney("" + loan.getMinInvestMoney());
        loanDetailResponseDataDto.setMaxInvestMoney("" + loan.getMaxInvestMoney());
        loanDetailResponseDataDto.setCardinalNumber("" + loan.getCardinalNumber());
        if(!LoanConstants.LoanStatus.RAISING.equals(loanDetailResponseDataDto.getLoanStatus())){
            Date raiseCompletedTime = mobileAppLoanListDao.getRaiseCompletedTime(loan.getId());
            if(raiseCompletedTime != null){
                loanDetailResponseDataDto.setRaiseCompletedTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(raiseCompletedTime));
            }

        }
        return loanDetailResponseDataDto;
    }

    @Override
    public List<InvestRecordResponseDataDto> convertInvestRecordDtoFromInvest(List<Invest> invests) {
        List<InvestRecordResponseDataDto> investRecordResponseDataDtos = new ArrayList<InvestRecordResponseDataDto>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Invest invest:invests) {
            InvestRecordResponseDataDto investRecordResponseDataDto = new InvestRecordResponseDataDto();
            investRecordResponseDataDto.setUserName(com.ttsd.api.util.CommonUtils.encryptUserName(invest.getUser().getUsername()));
            investRecordResponseDataDto.setInvestMoney("" + invest.getInvestMoney());
            investRecordResponseDataDto.setInvestTime(simpleDateFormat.format(invest.getTime()));
            investRecordResponseDataDtos.add(investRecordResponseDataDto);
        }
        return investRecordResponseDataDtos;
    }

    @Override
    public List<String> getImageUrl(String guaranteeCompanyDescription) {

        Pattern pattern = Pattern.compile(urlPattern, Pattern.CASE_INSENSITIVE);
        List<String> imageUrls = new ArrayList<String>();
        String[] imageUrlsTemp = guaranteeCompanyDescription.split("title");
        for (String str : imageUrlsTemp) {
            Matcher matcher = pattern.matcher(str);
            while (matcher.find()) {
                String imagePath = domainName + matcher.group();
                imageUrls.add(imagePath);
            }

        }
        return imageUrls;
    }
}
