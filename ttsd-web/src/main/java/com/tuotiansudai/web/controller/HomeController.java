package com.tuotiansudai.web.controller;

import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.service.CouponAlertService;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.LoanRepayMapper;
import com.tuotiansudai.repository.model.CouponType;
import com.tuotiansudai.repository.model.ExperienceLoanModel;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.ProductType;
import com.tuotiansudai.service.AnnounceService;
import com.tuotiansudai.service.HomeService;
import com.tuotiansudai.web.util.LoginUserInfo;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private HomeService homeService;

    @Autowired
    private AnnounceService announceService;

    @Autowired
    private CouponAlertService couponAlertService;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private CouponMapper couponMapper;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("/index", "responsive", true);
        modelAndView.addObject("loans", homeService.getLoans());
        modelAndView.addObject("announces", announceService.getAnnouncementList(1, 3).getData().getRecords());
        modelAndView.addObject("couponAlert", this.couponAlertService.getCouponAlert(LoginUserInfo.getLoginName()));
        long experienceLoanId = 1;
        Date beginTime = new DateTime(new Date()).withTimeAtStartOfDay().toDate();
        Date endTime = new DateTime(new Date()).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate();
        long experienceProgress = investMapper.countSuccessInvestByInvestTime(experienceLoanId,beginTime,endTime);
        List<CouponModel> couponModels = couponMapper.findCouponExperienceAmount(CouponType.NEWBIE_COUPON, ProductType.EXPERIENCE);
        ExperienceLoanModel experienceLoanModel = new ExperienceLoanModel(loanMapper.findById(experienceLoanId),experienceProgress,couponModels.get(0));
        modelAndView.addObject("experienceLoanModel", experienceLoanModel);
        return modelAndView;
    }
}