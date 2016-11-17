package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.LoanListRequestDto;
import com.tuotiansudai.api.service.v1_0.MobileAppLoanListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MobileAppLoanListController extends MobileAppBaseController {
    @Autowired
    private MobileAppLoanListService mobileAppLoanListService;

    @RequestMapping(value = "/get/loans", method = RequestMethod.POST)
    public BaseResponseDto queryLoanList(@RequestBody LoanListRequestDto loanListRequestDto) {
        loanListRequestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppLoanListService.generateLoanList(loanListRequestDto);
    }
}
