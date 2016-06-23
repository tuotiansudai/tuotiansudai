package com.tuotiansudai.console.controller;

import com.tuotiansudai.console.util.LoginUserInfo;
import com.tuotiansudai.dto.BannerDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping(value = "/banner-manage")
public class BannerController {

    @Autowired
    private BannerService bannerService;

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView bannerCreate() {
        return new ModelAndView("/banner");
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<PayDataDto> bannerCreate(@RequestBody BannerDto bannerDto) {
        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        PayDataDto dataDto = new PayDataDto();
        baseDto.setData(dataDto);
        String loginName = LoginUserInfo.getLoginName();
        bannerService.create(bannerDto, loginName);
        dataDto.setStatus(true);
        return baseDto;
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
