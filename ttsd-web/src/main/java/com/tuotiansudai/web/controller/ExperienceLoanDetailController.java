package com.tuotiansudai.web.controller;


import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.UserCouponView;
import com.tuotiansudai.coupon.service.CouponAlertService;
import com.tuotiansudai.coupon.service.UserCouponService;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.ProcessListMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.LoanDetailService;
import com.tuotiansudai.web.util.LoginUserInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Controller
@RequestMapping(value = "/loan")
public class ExperienceLoanDetailController {

    @Autowired
    private LoanDetailService loanDetailService;

    @Autowired
    private CouponAlertService couponAlertService;

    @Autowired
    private UserCouponService userCouponService;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");

    @RequestMapping(value = "/1", method = RequestMethod.GET)
    public ModelAndView getLoanDetail() {
        LoanModel loanModel = loanMapper.findById(1);
        List<CouponModel> couponModels = couponMapper.findCouponExperienceAmount(CouponType.NEWBIE_COUPON, ProductType.EXPERIENCE);

        List<InvestModel> investModels = investMapper.findByLoanIdAndLoginName(loanModel.getId(),LoginUserInfo.getLoginName());
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
        List<UserCouponView> userCouponViewList = userCouponMapper.findUnusedCoupons(LoginUserInfo.getLoginName());
        UserCouponView coupon = null;
        if(CollectionUtils.isNotEmpty(userCouponViewList)){
            for(UserCouponView userCouponView : userCouponViewList){
                for(ProductType productType : userCouponView.getProductTypeList()){
                    if(productType.equals(ProductType.EXPERIENCE)){
                        coupon = userCouponView;
                        break;
                    }
                }
            }
        }

        ModelAndView modelAndView = new ModelAndView("/experience-loan", "responsive", true);
        modelAndView.addObject("loan", experienceLoanDto);
        modelAndView.addObject("couponAlert", this.couponAlertService.getCouponAlert(LoginUserInfo.getLoginName()));
        modelAndView.addObject("coupons", userCouponService.getInvestUserCoupons(LoginUserInfo.getLoginName(), 1));
        return modelAndView;
    }

}
