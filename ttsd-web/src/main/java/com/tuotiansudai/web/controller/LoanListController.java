package com.tuotiansudai.web.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.coupon.service.CouponAlertService;
import com.tuotiansudai.dto.LoanItemDto;
import com.tuotiansudai.repository.model.CouponType;
import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.repository.model.ProductType;
import com.tuotiansudai.service.LoanService;
import com.tuotiansudai.web.util.LoginUserInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping(path = "/loan-list")
public class LoanListController {

    static Logger logger = Logger.getLogger(LoanListController.class);

    @Autowired
    private LoanService loanService;

    @Autowired
    private CouponAlertService couponAlertService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView webLoanList(@RequestParam(value = "name", required = false) String name,
                                    @RequestParam(value = "status", required = false) LoanStatus status,
                                    @RequestParam(value = "rateStart", defaultValue = "0", required = false) double rateStart,
                                    @RequestParam(value = "rateEnd", defaultValue = "0", required = false) double rateEnd,
                                    @RequestParam(value = "durationStart", defaultValue = "0", required = false) int durationStart,
                                    @RequestParam(value = "durationEnd", defaultValue = "0", required = false) int durationEnd,
                                    @RequestParam(value = "index", defaultValue = "1", required = false) int index) {
        int count = loanService.findLoanListCountWeb(name, status, rateStart, rateEnd,durationStart,durationEnd);
        List<LoanItemDto> loanItemList = loanService.findLoanItems(name, status, rateStart, rateEnd,durationStart,durationEnd, index);

        ModelAndView modelAndView = new ModelAndView("/loan-list");
        modelAndView.addObject("index", index);
        modelAndView.addObject("count", count);
        modelAndView.addObject("loanItemList", loanItemList);
        modelAndView.addObject("rateStart", rateStart);
        modelAndView.addObject("rateEnd", rateEnd);
        modelAndView.addObject("durationStart", durationStart);
        modelAndView.addObject("durationEnd", durationEnd);
        modelAndView.addObject("name", name);
        modelAndView.addObject("status", status);
        int maxIndex = count / 10 + (count % 10 > 0 ? 1 : 0);
        modelAndView.addObject("hasPreviousPage", index > 1 && index <= maxIndex);
        modelAndView.addObject("hasNextPage", index < maxIndex);
        modelAndView.addObject("couponAlert", this.couponAlertService.getCouponAlert(LoginUserInfo.getLoginName(), Lists.newArrayList(CouponType.NEWBIE_COUPON, CouponType.RED_ENVELOPE)));
        return modelAndView;
    }

}
