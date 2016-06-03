package com.tuotiansudai.service.impl;

import com.tuotiansudai.coupon.service.CouponService;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.ExperienceLoanDto;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.service.ExperienceLoanDetailService;
import org.apache.commons.collections4.CollectionUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ExperienceLoanDetailServiceImpl implements ExperienceLoanDetailService{

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private CouponService couponService;

    @Override
    public ExperienceLoanDto findExperienceLoanDtoDetail(long loanId,String loginName) {
        LoanModel loanModel = loanMapper.findById(loanId);
        Date beginTime = new DateTime(new Date()).withTimeAtStartOfDay().toDate();
        Date endTime = new DateTime(new Date()).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate();
        List<InvestModel> investModels = investMapper.findByLoanIdAndLoginName(loanModel.getId(),loginName);
        long experienceProgress;
        LoanStatus loanStatus = LoanStatus.RAISING;
        if(CollectionUtils.isNotEmpty(investModels)){
            InvestModel investModel = investModels.get(0);
            long day = (new Date().getTime() - investModel.getInvestTime().getTime()) / (1000 * 60 * 60 * 24);
            switch ((int)day){
                case 2:
                    loanStatus = LoanStatus.REPAYING;
                    experienceProgress = 100;
                    break;
                case 3:
                    loanStatus = LoanStatus.COMPLETE;
                    experienceProgress = 100;
                    break;
                default:
                    experienceProgress = investMapper.countSuccessInvestByInvestTime(loanId,beginTime,endTime).size();
                    break;
            }
        }else{
            experienceProgress = investMapper.countSuccessInvestByInvestTime(loanId,beginTime,endTime).size();
        }

        List<InvestModel> investModelList = investMapper.countSuccessInvestByInvestTime(loanId,beginTime,endTime);
        ExperienceLoanDto experienceLoanDto = new ExperienceLoanDto(loanMapper.findById(loanId),experienceProgress,couponService.findExperienceInvestAmount(investModelList));
        experienceLoanDto.setLoanStatus(loanStatus);
        return experienceLoanDto;
    }
}
