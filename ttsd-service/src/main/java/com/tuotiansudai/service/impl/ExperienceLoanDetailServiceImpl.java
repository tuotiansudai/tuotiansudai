package com.tuotiansudai.service.impl;

import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.ExperienceLoanDetailService;
import org.apache.commons.collections4.CollectionUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class ExperienceLoanDetailServiceImpl implements ExperienceLoanDetailService{

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private CouponMapper couponMapper;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");

    @Override
    public ExperienceLoanDto findExperienceLoanDtoDetail(long loanId,String loginName) {
        LoanModel loanModel = loanMapper.findById(loanId);
        List<CouponModel> couponModels = couponMapper.findCouponExperienceAmount(CouponType.NEWBIE_COUPON, ProductType.EXPERIENCE);

        List<InvestModel> investModels = investMapper.findByLoanIdAndLoginName(loanModel.getId(),loginName);
        long experienceProgress;
        LoanStatus loanStatus = LoanStatus.RAISING;
        if(CollectionUtils.isNotEmpty(investModels)){
            InvestModel investModel = investModels.get(0);
            int day = Integer.parseInt(simpleDateFormat.format(new Date())) - Integer.parseInt(simpleDateFormat.format(investModel.getInvestTime()));
            switch (day){
                case 2:
                    loanStatus = LoanStatus.REPAYING;
                    experienceProgress = 100;
                    break;
                case 3:
                    loanStatus = LoanStatus.COMPLETE;
                    experienceProgress = 100;
                    break;
                default:
                    experienceProgress = investMapper.countSuccessInvestByInvestTime(loanModel.getId(),new DateTime(new Date()).withTimeAtStartOfDay().toDate(),new DateTime(new Date()).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate());
                    break;
            }
        }else{
            experienceProgress = investMapper.countSuccessInvestByInvestTime(loanModel.getId(),new DateTime(new Date()).withTimeAtStartOfDay().toDate(),new DateTime(new Date()).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate()) % 100 * 100;
        }

        ExperienceLoanDto experienceLoanDto = new ExperienceLoanDto(loanModel,experienceProgress,couponModels.get(0));
        experienceLoanDto.setLoanStatus(loanStatus);

        return experienceLoanDto;
    }
}
