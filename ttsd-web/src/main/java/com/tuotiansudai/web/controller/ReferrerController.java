package com.tuotiansudai.web.controller;


import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.model.ReferrerManageView;
import com.tuotiansudai.repository.model.ReferrerRelationView;
import com.tuotiansudai.service.ReferrerManageService;
import com.tuotiansudai.web.util.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.constraints.Min;
import java.util.Date;


@Controller
@RequestMapping(value = "/referrer")
public class ReferrerController {

    @Autowired
    private ReferrerManageService referrerService;

    @RequestMapping(value = "/refer-list", method = RequestMethod.GET)
    public ModelAndView investList() {
        String loginName = LoginUserInfo.getLoginName();
        ModelAndView modelAndView = new ModelAndView("/refer-list");
        modelAndView.addObject("referrer", loginName);
        return modelAndView;
    }

    @RequestMapping(value = "/refer-relation", method = RequestMethod.GET, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public BaseDto<BasePaginationDataDto> referListData(@Min(value = 1) @RequestParam(name = "index", defaultValue = "1", required = false) int index,
                                                        @Min(value = 1) @RequestParam(name = "pageSize", defaultValue = "10", required = false) int pageSize,
                                                        @RequestParam(name = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                                                        @RequestParam(name = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
                                                        @RequestParam(name = "loginName", required = false) String loginName) {
        String referrerLoginName = LoginUserInfo.getLoginName();
        BaseDto<BasePaginationDataDto> dto = new BaseDto<>();
        dto.setData(referrerService.findReferrerRelationList(referrerLoginName, loginName, startTime, endTime, index, pageSize));
        return dto;
    }

    @RequestMapping(value = "/refer-invest", method = RequestMethod.GET, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public BaseDto<BasePaginationDataDto> referInvest(@Min(value = 1) @RequestParam(name = "index", defaultValue = "1", required = false) int index,
                                                      @Min(value = 1) @RequestParam(name = "pageSize", defaultValue = "10", required = false) int pageSize,
                                                      @RequestParam(name = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                                                      @RequestParam(name = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
                                                      @RequestParam(name = "loginName", required = false) String loginName) {

        String referrerLoginName = LoginUserInfo.getLoginName();
        BaseDto<BasePaginationDataDto> dto = new BaseDto<>();
        dto.setData(referrerService.findReferInvestList(referrerLoginName, loginName, startTime, endTime, index, pageSize));
        return dto;
    }

    @RequestMapping(value = "/total-reward", method = RequestMethod.GET, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public String findReferInvestTotalAmount(@RequestParam(name = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                           @RequestParam(name = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
                           @RequestParam(name = "loginName", required = false) String loginName) {

        String referrerLoginName = LoginUserInfo.getLoginName();
        return referrerService.findReferInvestTotalAmount(referrerLoginName, loginName, startTime, endTime);
    }
}
