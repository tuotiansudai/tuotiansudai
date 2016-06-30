package com.tuotiansudai.console.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.console.util.LoginUserInfo;
import com.tuotiansudai.dto.BannerDto;
import com.tuotiansudai.repository.model.BannerModel;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.service.BannerService;
import com.tuotiansudai.util.RequestIPParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/banner-manage")
public class BannerController {

    @Autowired
    private BannerService bannerService;

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView bannerCreate() {
        ModelAndView modelAndView = new ModelAndView("/banner");
        modelAndView.addObject("sources", Lists.newArrayList(Source.WEB, Source.ANDROID, Source.IOS));
        return modelAndView;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String bannerCreate(@ModelAttribute BannerDto bannerDto, HttpServletRequest request) {
        String loginName = LoginUserInfo.getLoginName();
        String ip = RequestIPParser.parse(request);
        bannerService.create(bannerDto, loginName, ip);
        return "redirect:/banner-manage/list";
    }

    @RequestMapping(value = "/banner/{id}/delete", method = RequestMethod.DELETE)
    public String delBanner(@PathVariable Long id, HttpServletRequest request) {
        String loginName = LoginUserInfo.getLoginName();
        String ip = RequestIPParser.parse(request);
        BannerModel bannerModel = this.bannerService.findById(id);
        bannerModel.setDeleted(true);
        bannerService.updateBanner(bannerModel, loginName, ip);
        return "redirect:/banner-manage/list";
    }

    @RequestMapping(value = "/banner/{id}/deactivated", method = RequestMethod.POST)
    public String deactivatedBanner(@PathVariable Long id, HttpServletRequest request) {
        String loginName = LoginUserInfo.getLoginName();
        String ip = RequestIPParser.parse(request);
        BannerModel bannerModel = this.bannerService.findById(id);
        bannerModel.setDeactivatedTime(new Date());
        bannerModel.setActive(false);
        bannerModel.setDeactivatedBy(LoginUserInfo.getLoginName());
        bannerService.updateBanner(bannerModel, loginName, ip);
        return "redirect:/banner-manage/list";
    }

    @RequestMapping(value = "/banner/{id}/edit", method = RequestMethod.GET)
    public ModelAndView editBanner(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("/banner");
        BannerModel bannerModel = this.bannerService.findById(id);
        modelAndView.addObject("sources", Lists.newArrayList(Source.WEB, Source.ANDROID, Source.IOS));
        modelAndView.addObject("banner", bannerModel);
        return modelAndView;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String editBanner(@ModelAttribute BannerDto bannerDto, HttpServletRequest request) {
        String loginName = LoginUserInfo.getLoginName();
        String ip = RequestIPParser.parse(request);
        BannerModel bannerModel = new BannerModel(bannerDto);
        bannerModel.setActive(true);
        bannerModel.setActivatedBy(LoginUserInfo.getLoginName());
        bannerModel.setActivatedTime(new Date());
        bannerModel.setDeleted(false);
        bannerService.updateBanner(bannerModel, loginName, ip);
        return "redirect:/banner-manage/list";
    }

    @RequestMapping(value = "/list")
    public ModelAndView bannerList() {
        ModelAndView modelAndView = new ModelAndView("/banner-list");
        List<BannerModel> bannerDtoList = bannerService.findAllBannerList();
        modelAndView.addObject("bannerList", bannerDtoList);
        return modelAndView;
    }
}
