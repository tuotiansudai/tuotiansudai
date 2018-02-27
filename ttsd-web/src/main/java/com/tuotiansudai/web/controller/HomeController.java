package com.tuotiansudai.web.controller;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.activity.repository.mapper.BannerMapper;
import com.tuotiansudai.coupon.service.CouponAlertService;
import com.tuotiansudai.dto.HomeLoanDto;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.message.service.AnnounceService;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.ExperienceLoanDto;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.service.HomeService;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.transfer.service.TransferService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

@Controller
public class HomeController {

    static Logger logger = Logger.getLogger(HomeController.class);

    @Autowired
    private HomeService homeService;

    @Autowired
    private AnnounceService announceService;

    @Autowired
    private CouponAlertService couponAlertService;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private BannerMapper bannerMapper;

    @Autowired
    private TransferService transferService;

    @RequestMapping(path = {"/", "/m"}, method = RequestMethod.GET)
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("/index");

        modelAndView.addObject("bannerList", bannerMapper.findBannerIsAuthenticatedOrderByOrder(!Strings.isNullOrEmpty(LoginUserInfo.getLoginName()), Source.WEB,new Date())); //banner

        modelAndView.addObject("announces", announceService.getAnnouncementList(1, 3).getData().getRecords()); //公告

        modelAndView.addObject("experienceLoan", new ExperienceLoanDto(loanMapper.findById(1), 0, 0)); //体验标
        modelAndView.addObject("newbieLoan", homeService.getNewbieLoan()); //新手专享
        modelAndView.addObject("normalLoans", homeService.getNormalLoans()); //优选债权

        //债权转让列表显示前两项
        modelAndView.addObject("transferApplications", transferService.findAllTransferApplicationPaginationList(null, 0, 0, 1, 2).getRecords());

        //企业贷款
        List<HomeLoanDto> enterpriseLoans = homeService.getEnterpriseLoans();
        modelAndView.addObject("enterpriseLoans", CollectionUtils.isNotEmpty(enterpriseLoans) ? enterpriseLoans : null);

        //优惠券提醒
        modelAndView.addObject("couponAlert", this.couponAlertService.getCouponAlert(LoginUserInfo.getLoginName(), Lists.newArrayList(CouponType.NEWBIE_COUPON, CouponType.RED_ENVELOPE)));

        modelAndView.addObject("siteMapList", homeService.siteMapData());

        return modelAndView;
    }

    @RequestMapping(value = "/is-login", method = RequestMethod.GET)
    public ModelAndView isLogin() {
        if (!StringUtils.isEmpty(LoginUserInfo.getLoginName())) {
            return null;
        } else {
            return new ModelAndView("/csrf");
        }
    }

    @RequestMapping(value = "/settings")
    public ModelAndView settings(){
        return new ModelAndView("/settings");
    }

}