package com.tuotiansudai.console.jpush.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.console.jpush.dto.JPushAlertDto;
import com.tuotiansudai.console.jpush.repository.model.JPushAlertModel;
import com.tuotiansudai.console.jpush.repository.model.JumpTo;
import com.tuotiansudai.console.jpush.repository.model.PushSource;
import com.tuotiansudai.console.jpush.repository.model.PushType;
import com.tuotiansudai.console.jpush.service.JPushAlertService;
import com.tuotiansudai.console.util.DistrictUtil;
import com.tuotiansudai.console.util.LoginUserInfo;
import com.tuotiansudai.util.RequestIPParser;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping(value = "/app-push-manage")
public class JPushAlertController {
    static Logger logger = Logger.getLogger(JPushAlertController.class);
    @Autowired
    private JPushAlertService jPushAlertService;

    @RequestMapping(value = "/manual-app-push/{id}/edit",method = RequestMethod.GET)
    public ModelAndView editAppPush(@PathVariable long id){
        ModelAndView modelAndView = new ModelAndView("/manual-app-push");
        JPushAlertModel jPushAlertModel = jPushAlertService.findJPushAlertModelById(id);
        JPushAlertDto jPushAlertDto = new JPushAlertDto(jPushAlertModel);
        modelAndView.addObject("jPushAlert",jPushAlertDto);
        modelAndView.addObject("pushSources", Lists.newArrayList(PushSource.values()));
        modelAndView.addObject("pushTypes", Lists.newArrayList(PushType.values()));
        modelAndView.addObject("jumpTos", Lists.newArrayList(JumpTo.values()));
        modelAndView.addObject("provinces", DistrictUtil.getProvinces());
        return modelAndView;
    }

    @RequestMapping(value = "/manual-app-push",method = RequestMethod.GET)
    public ModelAndView appPush(){
        ModelAndView modelAndView = new ModelAndView("/manual-app-push");
        modelAndView.addObject("pushSources", Lists.newArrayList(PushSource.values()));
        modelAndView.addObject("pushTypes", Lists.newArrayList(PushType.values()));
        modelAndView.addObject("jumpTos", Lists.newArrayList(JumpTo.values()));
        modelAndView.addObject("provinces", DistrictUtil.getProvinces());

        return modelAndView;
    }
    @RequestMapping(value="/manual-app-push/{id}/send",method = RequestMethod.GET)
    public String send(@PathVariable long id, HttpServletRequest request){
        String ip = RequestIPParser.parse(request);
        String loginName = LoginUserInfo.getLoginName();
        jPushAlertService.send(loginName, id, ip);
        return "redirect:/app-push-manage/manual-app-push-list";
    }
    @RequestMapping(value = "/manual-app-push",method = RequestMethod.POST)
    public String buildAppPush(@Valid @ModelAttribute JPushAlertDto jPushAlertDto){
        String loginName = LoginUserInfo.getLoginName();
        jPushAlertService.buildJPushAlert(loginName, jPushAlertDto);
        return "redirect:/app-push-manage/manual-app-push-list";
    }
    @RequestMapping(value = "/manual-app-push/push-type/{pushType}",method = RequestMethod.GET)
    @ResponseBody
    public int findPushTypeCount(@PathVariable PushType pushType){
        return jPushAlertService.findPushTypeCount(pushType);
    }

    @RequestMapping(value = "/manual-app-push-list",method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView appPushList(@RequestParam(value = "index",required = false,defaultValue = "1") int index,
                                    @RequestParam(value = "pageSize",required = false,defaultValue = "10") int pageSize,
                                    @RequestParam(value = "name",required = false) String name) {
        ModelAndView modelAndView = new ModelAndView("/manual-app-push-list");
        modelAndView.addObject("index", index);
        modelAndView.addObject("pageSize", pageSize);
        modelAndView.addObject("name", name);
        modelAndView.addObject("pushAlerts", jPushAlertService.findPushAlerts(index, pageSize, name));
        modelAndView.addObject("provinces", DistrictUtil.getProvinces());
        int jPushAlertCount = jPushAlertService.findPushAlertCount(name);
        modelAndView.addObject("jPushAlertCount", jPushAlertCount);
        long totalPages = jPushAlertCount / pageSize + (jPushAlertCount % pageSize > 0 ? 1 : 0);
        boolean hasPreviousPage = index > 1 && index <= totalPages;
        boolean hasNextPage = index < totalPages;
        modelAndView.addObject("hasPreviousPage", hasPreviousPage);
        modelAndView.addObject("hasNextPage", hasNextPage);

        return modelAndView;
    }
}
