package com.tuotiansudai.console.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.jpush.dto.JPushAlertDto;
import com.tuotiansudai.jpush.repository.model.*;
import com.tuotiansudai.jpush.service.JPushAlertService;
import com.tuotiansudai.console.util.LoginUserInfo;
import com.tuotiansudai.util.DistrictUtil;
import com.tuotiansudai.util.RequestIPParser;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping(value = "/app-push-manage")
public class JPushAlertController {
    static Logger logger = Logger.getLogger(JPushAlertController.class);
    @Autowired
    private JPushAlertService jPushAlertService;

    @RequestMapping(value = "/manual-app-push/{id}/edit", method = RequestMethod.GET)
    public ModelAndView editAppPush(@PathVariable long id) {
        ModelAndView modelAndView = new ModelAndView("/manual-app-push");
        JPushAlertModel jPushAlertModel = jPushAlertService.findJPushAlertModelById(id);
        JPushAlertDto jPushAlertDto = new JPushAlertDto(jPushAlertModel);
        modelAndView.addObject("jPushAlert", jPushAlertDto);
        modelAndView.addObject("pushSources", Lists.newArrayList(PushSource.values()));
        modelAndView.addObject("pushUserTypes", Lists.newArrayList(PushUserType.values()));
        modelAndView.addObject("pushTypes", Lists.newArrayList(PushType.values()));
        modelAndView.addObject("jumpTos", Lists.newArrayList(JumpTo.values()));
        modelAndView.addObject("provinces", DistrictUtil.getProvinces());
        return modelAndView;
    }

    @RequestMapping(value = "/manual-app-push", method = RequestMethod.GET)
    public ModelAndView appPush() {
        ModelAndView modelAndView = new ModelAndView("/manual-app-push");
        modelAndView.addObject("pushSources", Lists.newArrayList(PushSource.values()));
        modelAndView.addObject("pushUserTypes", Lists.newArrayList(PushUserType.values()));
        modelAndView.addObject("pushTypes", Lists.newArrayList(PushType.values()));
        modelAndView.addObject("jumpTos", Lists.newArrayList(JumpTo.values()));
        modelAndView.addObject("provinces", DistrictUtil.getProvinces());

        return modelAndView;
    }

    @RequestMapping(value = "/manual-app-push/{id}/send", method = RequestMethod.GET)
    public String send(@PathVariable long id) {
        String loginName = LoginUserInfo.getLoginName();
        jPushAlertService.send(loginName, id);
        return "redirect:/app-push-manage/manual-app-push-list";
    }

    @RequestMapping(value = "/manual-app-push", method = RequestMethod.POST)
    public String buildAppPush(@Valid @ModelAttribute JPushAlertDto jPushAlertDto) {
        String loginName = LoginUserInfo.getLoginName();
        jPushAlertService.buildJPushAlert(loginName, jPushAlertDto);
        return "redirect:/app-push-manage/manual-app-push-list";
    }

    @RequestMapping(value = "/manual-app-push/push-type/{pushType}", method = RequestMethod.GET)
    @ResponseBody
    public int findPushTypeCount(@PathVariable PushType pushType) {
        return jPushAlertService.findPushTypeCount(pushType);
    }

    @RequestMapping(value = "/manual-app-push-list", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView appPushList(@RequestParam(value = "index", required = false, defaultValue = "1") int index,
                                    @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                    @RequestParam(value = "pushType", required = false) PushType pushType,
                                    @RequestParam(value = "pushSource", required = false) PushSource pushSource,
                                    @RequestParam(value = "pushUserType", required = false) PushUserType pushUserType,
                                    @RequestParam(value = "pushStatus", required = false) PushStatus pushStatus,
                                    @RequestParam(name = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                                    @RequestParam(name = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime) {
        ModelAndView modelAndView = new ModelAndView("/manual-app-push-list");
        modelAndView.addObject("index", index);
        modelAndView.addObject("pageSize", pageSize);
        modelAndView.addObject("pushTypeInput", pushType);
        modelAndView.addObject("pushSourceInput", pushSource);
        modelAndView.addObject("pushUserTypeInput", pushUserType);
        modelAndView.addObject("pushStatusInput", pushStatus);
        modelAndView.addObject("startTime", startTime);
        modelAndView.addObject("endTime", endTime);


        modelAndView.addObject("pushAlerts", jPushAlertService.findPushAlerts(index, pageSize, pushType, pushSource, pushUserType,pushStatus,startTime,endTime, false));
        modelAndView.addObject("provinces", DistrictUtil.getProvinces());
        modelAndView.addObject("pushSources", Lists.newArrayList(PushSource.values()));
        modelAndView.addObject("pushUserTypes", Lists.newArrayList(PushUserType.values()));
        modelAndView.addObject("pushStatuses", Lists.newArrayList(PushStatus.values()));
        modelAndView.addObject("pushTypes", Lists.newArrayList(PushType.values()));
        int jPushAlertCount = jPushAlertService.findPushAlertCount(pushType, pushSource, pushUserType,pushStatus,startTime,endTime, false);
        modelAndView.addObject("jPushAlertCount", jPushAlertCount);
        long totalPages = jPushAlertCount / pageSize + (jPushAlertCount % pageSize > 0 ? 1 : 0);
        boolean hasPreviousPage = index > 1 && index <= totalPages;
        boolean hasNextPage = index < totalPages;
        modelAndView.addObject("hasPreviousPage", hasPreviousPage);
        modelAndView.addObject("hasNextPage", hasNextPage);

        return modelAndView;
    }

    @RequestMapping(value = "/auto-app-push-list", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView autoAppPushList(@RequestParam(value = "index", required = false, defaultValue = "1") int index,
                                        @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        ModelAndView modelAndView = new ModelAndView("/auto-app-push-list");
        modelAndView.addObject("pushAlerts", jPushAlertService.findPushAlerts(index, pageSize,null,null,null,null,null,null, true));

        return modelAndView;
    }

    @RequestMapping(value = "/auto-app-push/{jPushAlertId}/disabled", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> disableJPushAlert(@PathVariable long jPushAlertId) {
        String loginName = LoginUserInfo.getLoginName();
        BaseDto<BaseDataDto> baseDto = new BaseDto<BaseDataDto>();
        BaseDataDto baseDataDto = new BaseDataDto();
        baseDto.setData(baseDataDto);
        jPushAlertService.changeJPushAlertStatus(jPushAlertId, PushStatus.DISABLED, loginName);
        baseDataDto.setStatus(true);
        return baseDto;
    }

    @RequestMapping(value = "/auto-app-push/{jPushAlertId}/enabled", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> enabledJPushAlert(@PathVariable long jPushAlertId) {
        BaseDto<BaseDataDto> baseDto = new BaseDto<BaseDataDto>();
        BaseDataDto baseDataDto = new BaseDataDto();
        baseDto.setData(baseDataDto);
        String loginName = LoginUserInfo.getLoginName();
        jPushAlertService.changeJPushAlertStatus(jPushAlertId, PushStatus.ENABLED, loginName);
        baseDataDto.setStatus(true);
        return baseDto;
    }

    @RequestMapping(value = "/auto-app-push/{jPushAlertId}/{content}", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> changContent(@PathVariable long jPushAlertId, @PathVariable String content) {
        BaseDto<BaseDataDto> baseDto = new BaseDto<BaseDataDto>();
        BaseDataDto baseDataDto = new BaseDataDto();
        baseDto.setData(baseDataDto);
        String loginName = LoginUserInfo.getLoginName();
        jPushAlertService.changeJPushAlertContent(jPushAlertId, content, loginName);
        baseDataDto.setStatus(true);
        return baseDto;
    }

    @ResponseBody
    @RequestMapping(value = "/manual-app-push/{id}/pass", method = RequestMethod.GET)
    public BaseDto<BaseDataDto> pass(@PathVariable long id, HttpServletRequest request) {
        String loginName = LoginUserInfo.getLoginName();
        String ip = RequestIPParser.parse(request);
        return jPushAlertService.pass(loginName, id, ip);
    }

    @RequestMapping(value = "/manual-app-push/{id}/reject", method = RequestMethod.GET)
    public String reject(@PathVariable long id) {
        String loginName = LoginUserInfo.getLoginName();
        jPushAlertService.reject(loginName, id);
        return "redirect:/app-push-manage/manual-app-push-list";
    }

    @RequestMapping(value = "/manual-app-push/{id}/delete", method = RequestMethod.GET)
    public String delete(@PathVariable long id) {
        String loginName = LoginUserInfo.getLoginName();
        jPushAlertService.delete(loginName, id);
        return "redirect:/app-push-manage/manual-app-push-list";
    }

    @RequestMapping(value = "/manual-app-push/{id}/clone", method = RequestMethod.GET)
    public String clone(@PathVariable long id) {
        String loginName = LoginUserInfo.getLoginName();
        JPushAlertModel jPushModel = jPushAlertService.findJPushAlertModelById(id);
        JPushAlertDto jPushDto = new JPushAlertDto(jPushModel);
        jPushDto.setId(null);
        jPushDto.setName(clonePushName(jPushDto.getPushType()));
        jPushAlertService.buildJPushAlert(loginName, jPushDto);
        return "redirect:/app-push-manage/manual-app-push-list";
    }

    private String clonePushName(PushType pushType) {
        int serialNo = jPushAlertService.findPushTypeCount(pushType) + 1;
        String today = new SimpleDateFormat("yyyyMMdd").format(new Date());
        return today + "-" + pushType.getDescription() + "-" + serialNo;
    }
}
