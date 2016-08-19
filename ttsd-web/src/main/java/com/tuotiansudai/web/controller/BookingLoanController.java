package com.tuotiansudai.web.controller;


import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.repository.model.ProductType;
import com.tuotiansudai.service.BookingLoanService;
import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/booking-loan")
public class BookingLoanController {

    @Autowired
    private BookingLoanService bookingLoanService;

    @RequestMapping(value = "/invest", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto<BaseDataDto> invest(@RequestParam(value = "productType", required = false) ProductType productType,
                                       @RequestParam(value = "bookingAmount", required = false) String bookingAmount) {
        bookingLoanService.create(LoginUserInfo.getLoginName(), productType, bookingAmount);
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        BaseDataDto baseDataDto = new BaseDataDto();
        baseDataDto.setStatus(true);
        baseDto.setData(baseDataDto);
        return baseDto;
    }

}
