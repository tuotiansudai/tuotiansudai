package com.tuotiansudai.activity.controller;

import com.tuotiansudai.activity.service.DragonBoatFestivalService;
import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Component
@RequestMapping(value = "/activity/wechat/dragon")
public class DragonBoatFestivalController {

    @Autowired
    private DragonBoatFestivalService dragonBoatFestivalService;

    // 微信公众号回复打卡页面
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView wechatFirstPage() {
        String loginName = LoginUserInfo.getLoginName();
        String exchangeCode = dragonBoatFestivalService.getCouponExchangeCode(loginName);

        ModelAndView mav = new ModelAndView("/wechat-dragon");
        mav.addObject("exchangeCode", exchangeCode);
        mav.addObject("loginName", loginName);
        return mav;
    }
}
