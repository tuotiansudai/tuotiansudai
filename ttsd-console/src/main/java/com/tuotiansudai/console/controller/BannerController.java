package com.tuotiansudai.console.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.activity.repository.dto.BannerDto;
import com.tuotiansudai.activity.repository.model.BannerModel;
import com.tuotiansudai.activity.service.BannerService;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.enums.AppUrl;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.util.RequestIPParser;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/banner-manage")
public class BannerController {

    @Autowired
    private BannerService bannerService;

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView bannerCreate() {
        ModelAndView modelAndView = new ModelAndView("/banner");
        modelAndView.addObject("sources", Lists.newArrayList(Source.WEB, Source.ANDROID, Source.IOS));
        modelAndView.addObject("appUrls", Lists.newArrayList(AppUrl.values()).stream().filter(n -> n != AppUrl.NONE).collect(Collectors.toList()));
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
    @ResponseBody
    public BaseDto<BaseDataDto> delBanner(@PathVariable Long id, HttpServletRequest request) {
        String loginName = LoginUserInfo.getLoginName();
        String ip = RequestIPParser.parse(request);
        BannerModel bannerModel = this.bannerService.findById(id);
        bannerModel.setDeleted(true);
        bannerService.updateBanner(bannerModel, loginName, ip);
        BaseDataDto dataDto = new BaseDataDto();
        dataDto.setStatus(true);
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        baseDto.setData(dataDto);

        return baseDto;
    }

    @RequestMapping(value = "/banner/{id}/edit", method = RequestMethod.GET)
    public ModelAndView editBanner(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("/banner");
        BannerModel bannerModel = this.bannerService.findById(id);
        modelAndView.addObject("sources", Lists.newArrayList(Source.WEB, Source.ANDROID, Source.IOS));
        modelAndView.addObject("appUrls", Lists.newArrayList(AppUrl.values()).stream().filter(n -> n != AppUrl.NONE).collect(Collectors.toList()));
        modelAndView.addObject("banner", bannerModel);
        return modelAndView;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String editBanner(@ModelAttribute BannerDto bannerDto, HttpServletRequest request) {
        String loginName = LoginUserInfo.getLoginName();
        String ip = RequestIPParser.parse(request);
        BannerModel bannerModel = new BannerModel(bannerDto);
        bannerModel.setActivatedBy(LoginUserInfo.getLoginName());
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
