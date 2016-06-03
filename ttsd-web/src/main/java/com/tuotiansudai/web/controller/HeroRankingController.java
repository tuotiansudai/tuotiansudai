package com.tuotiansudai.web.controller;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BaseListDataDto;
import com.tuotiansudai.repository.model.HeroRankingView;
import com.tuotiansudai.service.HeroRankingService;
import com.tuotiansudai.util.RandomUtils;
import com.tuotiansudai.web.util.LoginUserInfo;
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
@RequestMapping(path = "/hero-ranking")
public class HeroRankingController {
    @Autowired
    private HeroRankingService heroRankingService;
    @Autowired
    private RandomUtils randomUtils;

    @RequestMapping(path = "/invest/{tradingTime}", method = RequestMethod.GET)
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
        Integer ranking = heroRankingService.obtainHeroRankingByLoginName(loginName);
        mv.addObject("ranking",ranking);
        return mv;
    }

}
