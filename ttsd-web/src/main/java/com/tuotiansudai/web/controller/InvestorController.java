package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.InvestRepayDataDto;
import com.tuotiansudai.dto.InvestorInvestPaginationItemDataDto;
import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.service.RepayService;
import com.tuotiansudai.repository.model.TransferInvestDetailView;
import com.tuotiansudai.transfer.service.InvestTransferService;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.web.config.interceptors.MobileAccessDecision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.constraints.Min;
import java.util.Date;

@Controller
@RequestMapping(path = "/investor")
public class InvestorController {

    @Autowired
    private InvestService investService;

    @Autowired
    private InvestTransferService investTransferService;

    @Autowired
    private RepayService repayService;

    @RequestMapping(value = "/invest-list", method = RequestMethod.GET)
    public ModelAndView investList() {
        return new ModelAndView("/investor-invest-list");
    }

    @RequestMapping(value = "/invest-list-data", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto<BasePaginationDataDto> investListData(@Min(value = 1) @RequestParam(name = "index", defaultValue = "1", required = false) int index,
                                                         @RequestParam(name = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                                                         @RequestParam(name = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
                                                         @RequestParam(name = "status", required = false, defaultValue = "REPAYING") LoanStatus status) {

        String loginName = LoginUserInfo.getLoginName();
        BaseDto<BasePaginationDataDto> dto = new BaseDto<>();
        if (MobileAccessDecision.isMobileAccess()) {
            dto.setData(investService.generateUserInvestList(loginName, index, 10, status));
        } else {
            dto.setData(investService.getInvestPagination(loginName, index, 10, startTime, endTime, status));

        }
        return dto;
    }


    @RequestMapping(value = "/invest-transfer-list", method = RequestMethod.GET)
    public ModelAndView investTransferList() {
        return new ModelAndView("/investor-invest-transfer-list");
    }

    @ResponseBody
    @RequestMapping(value = "/invest-transfer-list-data", method = RequestMethod.GET)
    public BaseDto<BasePaginationDataDto> investTransferListData(@Min(value = 1) @RequestParam(name = "index", defaultValue = "1", required = false) int index,
                                                                 @RequestParam(name = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                                                                 @RequestParam(name = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
                                                                 @RequestParam(name = "status", required = false) LoanStatus status) {
        String loginName = LoginUserInfo.getLoginName();
        BasePaginationDataDto<TransferInvestDetailView> dataDto = investTransferService.getInvestTransferList(loginName, index, 10, startTime, endTime, status);
        dataDto.setStatus(true);
        BaseDto<BasePaginationDataDto> dto = new BaseDto<>();
        dto.setData(dataDto);

        return dto;
    }

    @RequestMapping(path = "/invest/{investId:^\\d+$}/repay-data", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto<InvestRepayDataDto> getInvestRepayData(@PathVariable long investId) {
        return repayService.findInvestorInvestRepay(LoginUserInfo.getLoginName(), investId);
    }
}
