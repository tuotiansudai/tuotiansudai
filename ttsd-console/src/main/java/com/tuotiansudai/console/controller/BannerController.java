package com.tuotiansudai.console.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.console.util.LoginUserInfo;
import com.tuotiansudai.dto.BannerDto;
import com.tuotiansudai.repository.model.BannerModel;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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
    public String bannerCreate(@ModelAttribute BannerDto bannerDto) {
        String loginName = LoginUserInfo.getLoginName();
        bannerService.create(bannerDto, loginName);
        return "redirect:/banner-manage/list";
    }

    @RequestMapping(value = "/banner/del/{id}", method = RequestMethod.GET)
    public String delBanner(@PathVariable Long id) {
        BannerModel bannerModel = this.bannerService.findById(id);
        bannerModel.setDeleted(false);
        bannerService.updateBanner(bannerModel);
        return "redirect:/banner-manage/list";
    }

    @RequestMapping(value = "/banner/deactivated/{id}", method = RequestMethod.GET)
    public String deactivatedBanner(@PathVariable Long id) {
        BannerModel bannerModel = this.bannerService.findById(id);
        bannerModel.setDeactivatedTime(new Date());
        bannerModel.setActive(false);
        bannerModel.setDeactivatedBy(LoginUserInfo.getLoginName());
        bannerService.updateBanner(bannerModel);
        return "redirect:/banner-manage/list";
    }

    @RequestMapping(value = "/banner/edit/{id}", method = RequestMethod.GET)
    public ModelAndView editBanner(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("/banner-edit");
        BannerModel bannerModel = this.bannerService.findById(id);
        modelAndView.addObject("banner",bannerModel);
        return modelAndView;
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
