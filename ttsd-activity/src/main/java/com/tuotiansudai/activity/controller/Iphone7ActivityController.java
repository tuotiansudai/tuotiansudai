package com.tuotiansudai.activity.controller;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.ranking.DrawLotteryDto;
import com.tuotiansudai.dto.ranking.UserScoreDto;
import com.tuotiansudai.dto.ranking.UserTianDouRecordDto;
import com.tuotiansudai.point.dto.UserPointPrizeDto;
import com.tuotiansudai.point.service.PointLotteryService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.service.RankingActivityService;
import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/activity")
public class Iphone7ActivityController {


    @RequestMapping(value = "/iphone7-lottery", method = RequestMethod.GET)
    public ModelAndView loadPageData() {
        String loginName = LoginUserInfo.getLoginName();

        ModelAndView modelAndView = new ModelAndView("/activities/iphone-lottery", "responsive", true);


        return modelAndView;
    }

}
