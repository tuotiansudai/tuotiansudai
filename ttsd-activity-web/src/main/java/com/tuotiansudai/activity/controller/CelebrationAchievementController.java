package com.tuotiansudai.activity.controller;

import com.tuotiansudai.activity.service.CelebrationAchievementService;
import com.tuotiansudai.dto.CelebrationLoanItemDto;
import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
        List<CelebrationLoanItemDto> loanDtos = celebrationAchievementService.celebrationAchievementList();
        loanDtos = loanDtos.size() > 3 ? loanDtos.subList(0, 3) : loanDtos;
        for (CelebrationLoanItemDto itemDto : loanDtos) {
            itemDto.getAchievementViews().forEach(i -> {
                i.setLoginName(celebrationAchievementService.encryptMobileForWeb(loginName, i.getLoginName(), i.getMobile()));
                i.setMobile(null);
            });
        }

        modelAndView.addObject("loans", loanDtos);
        return modelAndView;
    }


}
