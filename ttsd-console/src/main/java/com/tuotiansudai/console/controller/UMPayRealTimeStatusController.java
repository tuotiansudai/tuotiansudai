package com.tuotiansudai.console.controller;

import com.google.common.base.Strings;
import com.tuotiansudai.console.service.ConsoleUMPayRealTimeStatusService;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.service.UMPayRealTimeStatusService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.Map;

@Controller
@RequestMapping(path = "/finance-manage/real-time-status")
public class UMPayRealTimeStatusController {

    static Logger logger = Logger.getLogger(UMPayRealTimeStatusController.class);

    @Autowired
    private ConsoleUMPayRealTimeStatusService consoleUMPayRealTimeStatusService;

    @Autowired
    private UMPayRealTimeStatusService umPayRealTimeStatusService;

    @Autowired
    private UserMapper userMapper;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getStatus(@RequestParam(value = "type", required = false) String type,
                                  @RequestParam(value = "mobile", required = false) String mobile,
                                  @RequestParam(value = "loanId", required = false) String loanId,
                                  @RequestParam(value = "orderId", required = false) String orderId,
                                  @RequestParam(value = "merDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date merDate,
                                  @RequestParam(value = "businessType", required = false) String businessType) {
        Map<String, String> data = null;
        if (!Strings.isNullOrEmpty(type)) {
            switch (type) {
                case "user":
                    if (!Strings.isNullOrEmpty(mobile)) {
                        UserModel userModel = userMapper.findByMobile(mobile);
                        data = consoleUMPayRealTimeStatusService.getUserStatus(userModel.getLoginName());
                    }
                    break;
                case "platform":
                    data = umPayRealTimeStatusService.getPlatformStatus();
                    break;
                case "loan":
                    if (!Strings.isNullOrEmpty(loanId)) {
                        try {
                            data = consoleUMPayRealTimeStatusService.getLoanStatus(Long.parseLong(loanId));
                        } catch (NumberFormatException e) {
                            logger.error(e.getLocalizedMessage(), e);
                        }
                    }
                    break;
                case "transfer":
                    if (orderId != null && merDate != null && businessType != null) {
                        data = consoleUMPayRealTimeStatusService.getTransferStatus(orderId, merDate, businessType);
                    }
                    break;
            }
        }

        ModelAndView modelAndView = new ModelAndView("/real-time-status");
        modelAndView.addObject("data", data);
        modelAndView.addObject("type", type);
        modelAndView.addObject("mobile", mobile);
        modelAndView.addObject("loanId", loanId);
        modelAndView.addObject("orderId", orderId);
        modelAndView.addObject("merDate", merDate);
        modelAndView.addObject("businessType", businessType);
        return modelAndView;
    }
}
