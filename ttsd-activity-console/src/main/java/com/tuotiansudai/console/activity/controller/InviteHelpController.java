package com.tuotiansudai.console.activity.controller;


import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/activity-console/activity-manage/invite-help")
public class InviteHelpController {

    @RequestMapping(value = "/invest-reward-list")
    public ModelAndView investRewardList(@RequestParam(value = "keyWord", required = false) String keyWord,
                                         @RequestParam(value = "minInvest", required = false) Long minInvest,
                                         @RequestParam(value = "maxInvest", required = false) Long maxInvest,
                                         @RequestParam(value = "index", defaultValue = "1") int index,
                                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        ModelAndView modelAndView = new ModelAndView("/invest-reward-list");




        return modelAndView;

    }

}
