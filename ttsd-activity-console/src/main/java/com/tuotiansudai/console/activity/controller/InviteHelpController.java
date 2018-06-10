package com.tuotiansudai.console.activity.controller;


import com.google.common.collect.Lists;
import com.tuotiansudai.activity.repository.model.WeChatHelpType;
import com.tuotiansudai.activity.repository.model.WeChatHelpUserStatus;
import com.tuotiansudai.console.activity.service.ActivityConsoleInviteHelpService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/activity-console/activity-manage/invite-help")
public class InviteHelpController {

    @Autowired
    private ActivityConsoleInviteHelpService activityConsoleInviteHelpService;

    @RequestMapping(value = "/invest-reward-list")
    public ModelAndView investRewardList(@RequestParam(value = "keyWord", required = false) String keyWord,
                                         @RequestParam(value = "minInvest", required = false) Long minInvest,
                                         @RequestParam(value = "maxInvest", required = false) Long maxInvest,
                                         @RequestParam(value = "type", defaultValue = "THIRD_ANNIVERSARY_HELP") WeChatHelpType type,
                                         @RequestParam(value = "index", defaultValue = "1") int index,
                                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        ModelAndView modelAndView = new ModelAndView("/help-invest-reward-list");
        modelAndView.addObject("data", activityConsoleInviteHelpService.investRewardList(index, pageSize, keyWord, minInvest, maxInvest, type));
        modelAndView.addObject("keyWord", keyWord);
        modelAndView.addObject("type", type);
        modelAndView.addObject("minInvest", minInvest == null ? null : String.valueOf(minInvest));
        modelAndView.addObject("maxInvest", maxInvest == null ? null : String.valueOf(maxInvest));
        return modelAndView;
    }

    @RequestMapping(value = "/share-reward-list")
    public ModelAndView shareRewardList(@RequestParam(value = "keyWord", required = false) String keyWord,
                                        @RequestParam(value = "index", defaultValue = "1") int index,
                                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        ModelAndView modelAndView = new ModelAndView("/help-share-reward-list");
        modelAndView.addObject("data", activityConsoleInviteHelpService.shareRewardList(index, pageSize, keyWord));
        modelAndView.addObject("keyWord", keyWord);
        return modelAndView;
    }

    @RequestMapping(value = "/help/{id:^\\d+$}/detail")
    public ModelAndView helpDetail(@PathVariable long id,
                                   @RequestParam(value = "nickName", required = false) String nickName,
                                   @RequestParam(value = "status", required = false) WeChatHelpUserStatus status,
                                   @RequestParam(value = "index", defaultValue = "1") int index,
                                   @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        ModelAndView modelAndView = new ModelAndView("/help-invest-reward-detail");
        modelAndView.addObject("data", activityConsoleInviteHelpService.investRewardDetail(index, pageSize, id, nickName, status));
        modelAndView.addObject("helpModel", activityConsoleInviteHelpService.findById(id));
        modelAndView.addObject("helpId", id);
        modelAndView.addObject("nickName", nickName);
        modelAndView.addObject("status", status == null ? "" : status);
        modelAndView.addObject("model", activityConsoleInviteHelpService.getHelpModel(id));
        return modelAndView;
    }
}
