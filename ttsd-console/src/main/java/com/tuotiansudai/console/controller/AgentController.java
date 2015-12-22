package com.tuotiansudai.console.controller;

import com.tuotiansudai.dto.AgentDto;
import com.tuotiansudai.exception.CreateAgentException;
import com.tuotiansudai.repository.mapper.AgentLevelRateMapper;
import com.tuotiansudai.repository.model.AgentLevelRateModel;
import com.tuotiansudai.service.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping(value = "/user-manage")
public class AgentController {
    @Autowired
    private AgentService agentService;

    @Autowired
    private AgentLevelRateMapper agentLevelRateMapper;

    @RequestMapping(value = "/agents",method = RequestMethod.GET)
    public ModelAndView agentManage(@RequestParam(value = "loginName",required = false) String loginName,
                                    @RequestParam(value = "index",defaultValue = "1",required = false) int index,
                                    @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize) {
        ModelAndView modelAndView = new ModelAndView("/agent-list");
        int count = agentService.findAgentLevelRateCount(loginName);
        List<AgentLevelRateModel> agentLevelRateList = agentService.findAgentLevelRate(loginName,(index-1) * pageSize,pageSize);
        modelAndView.addObject("count",count);
        modelAndView.addObject("agentLevelRateList",agentLevelRateList);
        modelAndView.addObject("index",index);
        modelAndView.addObject("pageSize",pageSize);
        modelAndView.addObject("loginName",loginName);
        long totalPages = count / pageSize + (count % pageSize > 0 ? 1 : 0);
        boolean hasPreviousPage = index > 1 && index <= totalPages;
        boolean hasNextPage = index < totalPages;
        modelAndView.addObject("hasPreviousPage",hasPreviousPage);
        modelAndView.addObject("hasNextPage",hasNextPage);

        return modelAndView;
    }

    @RequestMapping(value = "/agent/create", method = RequestMethod.GET)
    public ModelAndView addAgentLevelRate() {
        return new ModelAndView("/agent-edit");
    }

    @RequestMapping(value = "/agent/create", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView create(@Valid @ModelAttribute AgentDto agentDto,RedirectAttributes redirectAttributes){
        ModelAndView modelAndView = new ModelAndView();
        try {
            agentService.create(agentDto);
            modelAndView.setViewName("redirect:/user-manage/agents");
            return modelAndView;
        } catch (CreateAgentException e) {
            modelAndView.setViewName("redirect:/user-manage/agent/create");
            redirectAttributes.addFlashAttribute("agent", agentDto);
            redirectAttributes.addFlashAttribute("errorMessage",e.getMessage());
            return modelAndView;
        }
    }


    @RequestMapping(value = "/agent/{id}", method = RequestMethod.GET)
    public ModelAndView editAgent(@PathVariable long id) {
        AgentLevelRateModel agentLevelRateModel = agentLevelRateMapper.findAgentLevelRateById(id);
        ModelAndView mv = new ModelAndView("/agent-edit");
        mv.addObject("agent",agentLevelRateModel);
        mv.addObject("edit",true);
        return mv;
    }
    @RequestMapping(value = "/agent/delete/{id}", method = RequestMethod.GET)
    public String deleteAgent(@PathVariable long id) {
        agentLevelRateMapper.delete(id);
        return "redirect:/user-manage/agents";
    }

    @RequestMapping(value = "/agent/edit", method = RequestMethod.POST)
    public String editAgent(@Valid @ModelAttribute AgentDto agentDto) {
        agentService.update(agentDto);
        return "redirect:/user-manage/agents";
    }




}
