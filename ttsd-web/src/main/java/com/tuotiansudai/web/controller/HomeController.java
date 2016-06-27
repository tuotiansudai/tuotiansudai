package com.tuotiansudai.web.controller;

import com.google.common.base.Strings;
import com.tuotiansudai.coupon.service.CouponAlertService;
import com.tuotiansudai.coupon.service.CouponService;
import com.tuotiansudai.repository.mapper.BannerMapper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.ExperienceLoanDto;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.service.AnnounceService;
import com.tuotiansudai.service.HomeService;
import com.tuotiansudai.web.util.LoginUserInfo;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

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
    private CouponService couponService;

    @Autowired
    private BannerMapper bannerMapper;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("/index", "responsive", true);
        modelAndView.addObject("loans", homeService.getLoans());
        modelAndView.addObject("announces", announceService.getAnnouncementList(1, 3).getData().getRecords());
        modelAndView.addObject("couponAlert", this.couponAlertService.getCouponAlert(LoginUserInfo.getLoginName()));
        long experienceLoanId = 1;
        Date beginTime = new DateTime(new Date()).withTimeAtStartOfDay().toDate();
        Date endTime = new DateTime(new Date()).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate();
        List<InvestModel> investModelList = investMapper.countSuccessInvestByInvestTime(experienceLoanId,beginTime,endTime);
        ExperienceLoanDto experienceLoanDto = new ExperienceLoanDto(loanMapper.findById(experienceLoanId),investModelList.size() % 100 ,couponService.findExperienceInvestAmount(investModelList));
        modelAndView.addObject("experienceLoanDto", experienceLoanDto);
        modelAndView.addObject("bannerList",bannerMapper.findBannerIsAuthenticatedOrderByOrder(Strings.isNullOrEmpty(LoginUserInfo.getLoginName()) ? true : false, Source.WEB.name()));
        return modelAndView;
    }
}