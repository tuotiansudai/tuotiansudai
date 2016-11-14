package com.tuotiansudai.console.controller;

import com.tuotiansudai.repository.mapper.AgentLevelRateMapper;
import com.tuotiansudai.repository.model.AgentLevelRateModel;
import com.tuotiansudai.service.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping(value = "/user-manage")
public class BindCardManagerController {
    @Autowired
    private AgentService agentService;

    @Autowired
    private AgentLevelRateMapper agentLevelRateMapper;

    @RequestMapping(value = "/bind-card",method = RequestMethod.GET)
    public ModelAndView agentManage(@RequestParam(value = "loginName",required = false) String loginName,
                                    @RequestParam(value = "index",defaultValue = "1",required = false) int index,
                                    @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize) {
        ModelAndView modelAndView = new ModelAndView("/bind-card-list");
        int count = agentService.findAgentLevelRateCount(loginName);
        List<AgentLevelRateModel> agentLevelRateList = agentService.findAgentLevelRate(loginName,(index-1) * pageSize,pageSize);
        modelAndView.addObject("count",count);
        modelAndView.addObject("agentLevelRateList",agentLevelRateList);
        modelAndView.addObject("index",index);
        modelAndView.addObject("pageSize",pageSize);
        modelAndView.addObject("loginName",loginName);
        long totalPages = count / pageSize + (count % pageSize > 0 || count == 0 ? 1 : 0);
        boolean hasPreviousPage = index > 1 && index <= totalPages;
        boolean hasNextPage = index < totalPages;
        modelAndView.addObject("hasPreviousPage",hasPreviousPage);
        modelAndView.addObject("hasNextPage",hasNextPage);

        return modelAndView;
    }

}
