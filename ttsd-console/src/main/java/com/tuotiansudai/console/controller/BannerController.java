package com.tuotiansudai.console.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.console.util.LoginUserInfo;
import com.tuotiansudai.dto.BannerDto;
import com.tuotiansudai.repository.model.BannerModel;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.service.AuditLogService;
import com.tuotiansudai.service.BannerService;
import com.tuotiansudai.task.OperationType;
import com.tuotiansudai.util.RequestIPParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/banner-manage")
public class BannerController {

    @Autowired
    private BannerService bannerService;

    @Autowired
    private AuditLogService auditLogService;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView bannerCreate() {
        ModelAndView modelAndView = new ModelAndView("/banner");
        modelAndView.addObject("sources", Lists.newArrayList(Source.WEB, Source.ANDROID, Source.IOS));
        return modelAndView;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String bannerCreate(@ModelAttribute BannerDto bannerDto, HttpServletRequest request) {
        String loginName = LoginUserInfo.getLoginName();
        bannerService.create(bannerDto, loginName);
        String ip = RequestIPParser.parse(request);
        String description = loginName + "在 " + sdf.format(new Date()) +"创建了" +"名称为:" + bannerDto.getName() + "的banner." ;
        auditLogService.createAuditLog(loginName, loginName, OperationType.BANNER, bannerDto.getName(), description, ip);
        return "redirect:/banner-manage/list";
    }

    @RequestMapping(value = "/banner/del/{id}", method = RequestMethod.GET)
    public String delBanner(@PathVariable Long id, HttpServletRequest request) {
        String loginName = LoginUserInfo.getLoginName();
        BannerModel bannerModel = this.bannerService.findById(id);
        bannerModel.setDeleted(true);
        bannerService.updateBanner(bannerModel);
        String ip = RequestIPParser.parse(request);
        String description = loginName + "在 " + sdf.format(new Date()) +"删除了" +"名称为:" + bannerModel.getName() + "的banner." ;
        auditLogService.createAuditLog(loginName, loginName, OperationType.BANNER, bannerModel.getName(), description, ip);
        return "redirect:/banner-manage/list";
    }

    @RequestMapping(value = "/banner/deactivated/{id}", method = RequestMethod.GET)
    public String deactivatedBanner(@PathVariable Long id, HttpServletRequest request) {
        String loginName = LoginUserInfo.getLoginName();
        BannerModel bannerModel = this.bannerService.findById(id);
        bannerModel.setDeactivatedTime(new Date());
        bannerModel.setActive(false);
        bannerModel.setDeactivatedBy(LoginUserInfo.getLoginName());
        bannerService.updateBanner(bannerModel);
        String ip = RequestIPParser.parse(request);
        String description = loginName + "在 " + sdf.format(new Date()) +"下线了" +"名称为:" + bannerModel.getName() + "的banner." ;
        auditLogService.createAuditLog(loginName, loginName, OperationType.BANNER, bannerModel.getName(), description, ip);
        return "redirect:/banner-manage/list";
    }

    @RequestMapping(value = "/banner/{operator}/{id}", method = RequestMethod.GET)
    public ModelAndView editBanner(@PathVariable Long id, @PathVariable String operator) {
        ModelAndView modelAndView = new ModelAndView("/banner");
        BannerModel bannerModel = this.bannerService.findById(id);
        modelAndView.addObject("sources", Lists.newArrayList(Source.WEB, Source.ANDROID, Source.IOS));
        modelAndView.addObject("banner", bannerModel);
        modelAndView.addObject("operator", operator);
        return modelAndView;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String bannerEdit(@ModelAttribute BannerDto bannerDto, HttpServletRequest request) {
        String loginName = LoginUserInfo.getLoginName();
        BannerModel bannerModel = new BannerModel(bannerDto);
        bannerModel.setActive(true);
        bannerModel.setActivatedBy(LoginUserInfo.getLoginName());
        bannerModel.setActivatedTime(new Date());
        bannerModel.setDeleted(false);
        bannerService.updateBanner(bannerModel);
        String ip = RequestIPParser.parse(request);
        String description = loginName + "在 " + sdf.format(new Date()) +"编辑了" +"名称为:" + bannerModel.getName() + "的banner." ;
        auditLogService.createAuditLog(loginName, loginName, OperationType.BANNER, bannerModel.getName(), description, ip);
        return "redirect:/banner-manage/list";
    }

    @RequestMapping(value = "/reuse", method = RequestMethod.POST)
    public String bannerReuse(@ModelAttribute BannerDto bannerDto, HttpServletRequest request) {
        String loginName = LoginUserInfo.getLoginName();
        bannerService.create(bannerDto, loginName);
        String ip = RequestIPParser.parse(request);
        String description = loginName + "在 "+ sdf.format(new Date()) +"复用了" +"名称为:" + bannerDto.getName() + "的banner." ;
        auditLogService.createAuditLog(loginName, loginName, OperationType.BANNER, bannerDto.getName(), description, ip);
        return "redirect:/banner-manage/list";
    }

    @RequestMapping(value = "/list")
    public ModelAndView usersAccountPointList(@RequestParam(value = "index", defaultValue = "1", required = false) int index,
                                              @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {

        ModelAndView modelAndView = new ModelAndView("/banner-list");
        modelAndView.addObject("index", index);
        modelAndView.addObject("pageSize", pageSize);

        List<BannerDto> bannerDtoList = bannerService.findBannerList(index, pageSize);
        modelAndView.addObject("bannerList", bannerDtoList);
        int count = bannerService.findBannerCount();
        long totalPages = count / pageSize + (count % pageSize > 0 || count == 0 ? 1 : 0);
        boolean hasPreviousPage = index > 1 && index <= totalPages;
        boolean hasNextPage = index < totalPages;
        modelAndView.addObject("hasPreviousPage", hasPreviousPage);
        modelAndView.addObject("hasNextPage", hasNextPage);
        modelAndView.addObject("count", count);
        return modelAndView;
    }
}
