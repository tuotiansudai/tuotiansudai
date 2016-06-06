package com.tuotiansudai.web.controller;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BaseListDataDto;
import com.tuotiansudai.repository.model.HeroRankingView;
import com.tuotiansudai.service.HeroRankingService;
import com.tuotiansudai.util.RandomUtils;
import com.tuotiansudai.web.util.LoginUserInfo;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/hero-ranking")
public class HeroRankingController {

    @Autowired
    private HeroRankingService heroRankingService;

    @Autowired
    private RandomUtils randomUtils;

    @RequestMapping(value = "/referrer-invest/{tradingTime}", method = RequestMethod.GET)
    @ResponseBody
    public BaseListDataDto<HeroRankingView> obtainHeroRankingByReferrer(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date tradingTime) {
        return heroRankingService.findHeroRankingByReferrer(tradingTime, LoginUserInfo.getLoginName(), 1, 3);
    }

    @RequestMapping(value = "/invest/{tradingTime}", method = RequestMethod.GET)
     @ResponseBody
     public BaseListDataDto<HeroRankingView> obtainHeroRanking(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date tradingTime){
        final String loginName = LoginUserInfo.getLoginName();
        BaseListDataDto baseListDataDto = new BaseListDataDto();
        List<HeroRankingView> heroRankingViews = heroRankingService.obtainHeroRanking(tradingTime);
        if(heroRankingViews != null){
            baseListDataDto.setRecords(Lists.transform(heroRankingViews, new Function<HeroRankingView,HeroRankingView>() {
                @Override
                public HeroRankingView apply(HeroRankingView heroRankingView) {
                    heroRankingView.setLoginName(randomUtils.encryptLoginName(loginName,heroRankingView.getLoginName(),6));
                    return heroRankingView;
                }
            }));
        }
        baseListDataDto.setStatus(true);
        return baseListDataDto;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView obtainHeroRankingByLoginName(){
        ModelAndView mv = new ModelAndView();
        String loginName = LoginUserInfo.getLoginName();
        Date tradingTime = new DateTime().toDate();
        Integer ranking = heroRankingService.obtainHeroRankingByLoginName(tradingTime,loginName);
        mv.addObject("ranking",ranking);
        return mv;
    }

}
