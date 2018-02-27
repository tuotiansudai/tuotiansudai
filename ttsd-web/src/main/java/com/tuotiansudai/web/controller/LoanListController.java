package com.tuotiansudai.web.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.coupon.service.CouponAlertService;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.LoanItemDto;
import com.tuotiansudai.dto.TransferApplicationPaginationItemDataDto;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.repository.model.TransferStatus;
import com.tuotiansudai.service.LoanService;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.transfer.service.TransferService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.OptionalDouble;

@Controller
public class LoanListController {

    static Logger logger = Logger.getLogger(LoanListController.class);

    @Autowired
    private LoanService loanService;

    @Autowired
    private CouponAlertService couponAlertService;

    @Autowired
    private TransferService transferService;

    @RequestMapping(path = "/loan-list", method = RequestMethod.GET)
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
        if (index == 1) {
            BasePaginationDataDto<TransferApplicationPaginationItemDataDto> allTransferApplicationPaginationList = this.transferService.findAllTransferApplicationPaginationList(Lists.newArrayList(TransferStatus.TRANSFERRING), 0D, 0D, 1, 10);
            OptionalDouble maxTransferringRate = allTransferApplicationPaginationList.getRecords().stream().mapToDouble(item -> item.getBaseRate() + item.getActivityRate()).max();
            modelAndView.addObject("transferringCount", allTransferApplicationPaginationList.getRecords().size());
            modelAndView.addObject("maxTransferringRate", maxTransferringRate.isPresent() ? maxTransferringRate.getAsDouble() : 0.0);
        }
        return modelAndView;
    }

}
