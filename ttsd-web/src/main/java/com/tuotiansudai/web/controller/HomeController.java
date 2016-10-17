package com.tuotiansudai.web.controller;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.TransferApplicationPaginationItemDataDto;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.repository.mapper.BannerMapper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.BannerModel;
import com.tuotiansudai.repository.model.ExperienceLoanDto;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.service.AnnounceService;
import com.tuotiansudai.service.HomeService;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.transfer.service.TransferService;
import coupon.service.CouponAlertService;
import org.apache.commons.lang3.StringUtils;
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
    private InvestService investService;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private BannerMapper bannerMapper;

    @Autowired
    private TransferService transferService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("/index", "responsive", true);
        modelAndView.addObject("hello",true);
        modelAndView.addObject("loans", homeService.getLoans());
        modelAndView.addObject("announces", announceService.getAnnouncementList(1, 3).getData().getRecords());
        modelAndView.addObject("couponAlert", this.couponAlertService.getCouponAlert(LoginUserInfo.getLoginName(), Lists.newArrayList(CouponType.NEWBIE_COUPON, CouponType.RED_ENVELOPE)));
        long experienceLoanId = 1;
        Date beginTime = new DateTime(new Date()).withTimeAtStartOfDay().toDate();
        Date endTime = new DateTime(new Date()).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate();
        List<InvestModel> investModelList = investMapper.countSuccessInvestByInvestTime(experienceLoanId, beginTime, endTime);
        ExperienceLoanDto experienceLoanDto = new ExperienceLoanDto(loanMapper.findById(experienceLoanId), investModelList.size() % 100, investService.findExperienceInvestAmount(investModelList));
        modelAndView.addObject("experienceLoanDto", experienceLoanDto);
        List<BannerModel> bannerModelList = bannerMapper.findBannerIsAuthenticatedOrderByOrder(!Strings.isNullOrEmpty(LoginUserInfo.getLoginName()), Source.WEB);
        modelAndView.addObject("bannerList", bannerModelList);
        //债权转让列表显示前两项
        BasePaginationDataDto<TransferApplicationPaginationItemDataDto> transferApplicationItemList = transferService.findAllTransferApplicationPaginationList(null, 0, 0, 1, 2);
        modelAndView.addObject("transferApplications", transferApplicationItemList.getRecords());
        //企业贷款
        modelAndView.addObject("enterpriseLoans", homeService.getEnterpriseLoans());
        return modelAndView;
    }

    @RequestMapping(value = "/isLogin", method = RequestMethod.GET)
    public ModelAndView isLogin() {
        if(!StringUtils.isEmpty(LoginUserInfo.getLoginName())) {
            return null;
        } else {
            return new ModelAndView("/csrf");
        }
    }
}