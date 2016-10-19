package com.tuotiansudai.console.activity.controller;


import com.tuotiansudai.activity.repository.mapper.IPhone7InvestLotteryMapper;
import com.tuotiansudai.activity.repository.mapper.IPhone7LotteryConfigMapper;
import com.tuotiansudai.activity.repository.model.IPhone7LotteryConfigModel;
import com.tuotiansudai.activity.repository.model.IPhone7LotteryConfigStatus;
import com.tuotiansudai.console.activity.dto.IPhone7InvestLotteryWinnerDto;
import com.tuotiansudai.console.activity.service.IPhone7LotteryService;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.util.RequestIPParser;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/activity-console/activity-manage")
public class IPhone7LotteryController {
    private static Logger logger = Logger.getLogger(IPhone7LotteryController.class);

    @Autowired
    private IPhone7InvestLotteryMapper mapper;

    @Autowired
    private IPhone7LotteryConfigMapper configMapper;

    @Autowired
    private IPhone7LotteryService service;

    @RequestMapping(value = "/iphone7-lottery", method = RequestMethod.GET)
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView("/iphone7-lottery/home");
        int userCount = mapper.statUserCount();
        int investCount = mapper.statInvestCount();
        modelAndView.addObject("userCount", userCount);
        modelAndView.addObject("investCount", investCount);
        return modelAndView;
    }

    @RequestMapping(value = "/iphone7-lottery/stat", method = RequestMethod.GET)
    public ModelAndView stat(@RequestParam(value = "mobile", required = false) String mobile,
                             @RequestParam(value = "index", defaultValue = "1", required = false) int index,
                             @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        ModelAndView modelAndView = new ModelAndView("/iphone7-lottery/stat");
        BaseDto<BasePaginationDataDto> paginationData = service.listStat(mobile, index, pageSize);
        modelAndView.addObject("data", paginationData.getData());
        modelAndView.addObject("mobile", mobile);
        modelAndView.addObject("index", index);
        modelAndView.addObject("pageSize", pageSize);
        return modelAndView;
    }


    @RequestMapping(value = "/iphone7-lottery/winners", method = RequestMethod.GET)
    public ModelAndView winners() {
        ModelAndView modelAndView = new ModelAndView("/iphone7-lottery/winner");
        List<IPhone7InvestLotteryWinnerDto> winners = service.listWinner();
        modelAndView.addObject("winners", winners);
        return modelAndView;
    }

    @RequestMapping(value = "/iphone7-lottery/config", method = RequestMethod.GET)
    public ModelAndView config() {
        ModelAndView modelAndView = new ModelAndView("/iphone7-lottery/config");
        List<IPhone7LotteryConfigModel> configList = configMapper.list();
        List<IPhone7LotteryConfigModel> approvedConfigList = configList.stream()
                .filter(c -> c.getStatus() == IPhone7LotteryConfigStatus.APPROVED || c.getStatus() == IPhone7LotteryConfigStatus.EFFECTIVE)
                .collect(Collectors.toList());
        List<IPhone7LotteryConfigModel> toApproveConfigList = configList.stream()
                .filter(c -> c.getStatus() == IPhone7LotteryConfigStatus.TO_APPROVE)
                .collect(Collectors.toList());
        modelAndView.addObject("approvedConfigList", approvedConfigList);
        modelAndView.addObject("toApproveConfigList", toApproveConfigList);
        return modelAndView;
    }

    @RequestMapping(value = "/iphone7-lottery/config", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto submitConfig(@RequestParam(value = "investAmount") int investAmount,
                                @RequestParam(value = "lotteryNumber") String lotteryNumber,
                                @RequestParam(value = "mobile") String mobile) {
        IPhone7LotteryConfigModel configModel = new IPhone7LotteryConfigModel();
        configModel.setInvestAmount(investAmount);
        configModel.setLotteryNumber(lotteryNumber);
        configModel.setMobile(mobile);
        configModel.setCreatedBy(LoginUserInfo.getLoginName());
        configModel.setCreatedTime(new Date());
        configMapper.create(configModel);
        return new BaseDto();
    }

    @RequestMapping(value = "/iphone7-lottery/audit", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto auditConfig(@RequestParam(value = "id") long id,
                                @RequestParam(value = "passed") boolean passed,
                                HttpServletRequest request
    ) {
        BaseDataDto data = new BaseDataDto();
        try {
            if (passed) {
                service.approveConfig(id, LoginUserInfo.getLoginName(), RequestIPParser.parse(request));
            } else {
                service.refuseConfig(id, LoginUserInfo.getLoginName(), RequestIPParser.parse(request));
            }
            data.setStatus(true);
        } catch (Exception e) {
            logger.error("iphone7 lottery config item refuse failed", e);
            data.setStatus(false);
            data.setMessage(e.getMessage());
        }
        return new BaseDto<>(data);
    }
}
