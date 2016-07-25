package com.tuotiansudai.web.controller;


import com.tuotiansudai.repository.model.ProductType;
import com.tuotiansudai.service.BookingLoanService;
import com.tuotiansudai.web.util.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/booking-loan")
public class BookingLoanController {

    @Autowired
    private BookingLoanService bookingLoanService;

    @RequestMapping(value = "/invest", method = RequestMethod.GET)
    public void invest(@RequestParam(value = "productType", required = false) ProductType productType,
                               @RequestParam(value = "pageSize", required = false) String bookingAmount) {
        bookingLoanService.create(LoginUserInfo.getLoginName(),productType,bookingAmount);
    }
}
