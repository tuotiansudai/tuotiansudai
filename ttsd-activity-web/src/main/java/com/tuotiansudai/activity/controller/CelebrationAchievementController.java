package com.tuotiansudai.activity.controller;

import com.tuotiansudai.activity.service.CelebrationAchievementService;
import com.tuotiansudai.dto.LoanItemDto;
import com.tuotiansudai.repository.model.InvestAchievementView;
import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping(value = "/activity/celebration-achievement")
public class CelebrationAchievementController {
    @Autowired
    private CelebrationAchievementService celebrationAchievementService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView celebrationAchievementList() {
        ModelAndView modelAndView = new ModelAndView("/activities/loan-king", "responsive", true);
        final String loginName = LoginUserInfo.getLoginName();
        List<LoanItemDto> loanDtos = celebrationAchievementService.celebrationAchievementList();
        modelAndView.addObject("loans", loanDtos);

        loanDtos.forEach(loanItemDto -> modelAndView.addObject(String.valueOf(loanItemDto.getId()), celebrationAchievementService.obtainCelebrationAchievement(loanItemDto.getId(), loginName)));
        return modelAndView;
    }


}
