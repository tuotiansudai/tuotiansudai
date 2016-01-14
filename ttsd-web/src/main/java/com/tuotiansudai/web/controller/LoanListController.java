package com.tuotiansudai.web.controller;

import com.tuotiansudai.coupon.service.UserCouponService;
import com.tuotiansudai.dto.LoanItemDto;
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
    private UserCouponService userCouponService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView webLoanList(@RequestParam(value = "productType", required = false) ProductType productType,
                                    @RequestParam(value = "status", required = false) LoanStatus status,
                                    @RequestParam(value = "rateStart", defaultValue = "0", required = false) double rateStart,
                                    @RequestParam(value = "rateEnd", defaultValue = "0", required = false) double rateEnd,
                                    @RequestParam(value = "index", defaultValue = "1", required = false) int index) {
        int count = loanService.findLoanListCountWeb(productType, status, rateStart, rateEnd);
        List<LoanItemDto> loanItemList = loanService.findLoanItems(productType, status, rateStart, rateEnd, index);

        ModelAndView modelAndView = new ModelAndView("/loan-list");
        modelAndView.addObject("count", count);
        modelAndView.addObject("loanItemList", loanItemList);
        modelAndView.addObject("index", index);
        modelAndView.addObject("rateStart", rateStart);
        modelAndView.addObject("rateEnd", rateEnd);
        modelAndView.addObject("productType", productType);
        modelAndView.addObject("status", status);
        int maxIndex = count / 10 + (count % 10 > 0 ? 1 : 0);
        modelAndView.addObject("hasPreviousPage", index > 1 && index <= maxIndex);
        modelAndView.addObject("hasNextPage", index < maxIndex);
        modelAndView.addObject("newbieCoupon", this.userCouponService.getUsableNewbieCoupon(LoginUserInfo.getLoginName()));
        return modelAndView;
    }

}
