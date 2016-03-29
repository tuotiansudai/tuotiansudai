package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.ranking.DrawLotteryDto;
import com.tuotiansudai.dto.ranking.UserTianDouRecordDto;
import com.tuotiansudai.dto.ranking.UserScoreDto;
import com.tuotiansudai.service.RankingActivityService;
import com.tuotiansudai.web.util.LoginUserInfo;
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
public class RankingActivityController {

    @Autowired
    private RankingActivityService rankingActivityService;

    @RequestMapping(value = "/rank-list", method = RequestMethod.GET)
    public ModelAndView loadPageData() {

        ModelAndView modelAndView = new ModelAndView("/activities/rank-list");
        String loginName = LoginUserInfo.getLoginName();

        Long myRank = rankingActivityService.getUserRank(loginName);

        List<UserScoreDto> tianDouTop15 = rankingActivityService.getTianDouTop15();

        Map<String, List<UserTianDouRecordDto>> winnerList = rankingActivityService.getTianDouWinnerList();

        List<UserTianDouRecordDto> myPrizeList = rankingActivityService.getPrizeByLoginName(loginName);

        Double myTianDou = rankingActivityService.getUserScoreByLoginName(loginName);

        modelAndView.addObject("myRank", myRank);
        modelAndView.addObject("tianDouTop15", tianDouTop15);
        modelAndView.addObject("winnerList", winnerList);
        modelAndView.addObject("myPrizeList", myPrizeList);
        modelAndView.addObject("myTianDou", myTianDou);
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping(value = "/drawTianDou", method = RequestMethod.POST)
    public BaseDto<DrawLotteryDto> drawTianDouPrize() {
        String loginName = LoginUserInfo.getLoginName();
        String mobile = LoginUserInfo.getLoginName();

        BaseDto<DrawLotteryDto> baseDto = rankingActivityService.drawTianDouPrize(loginName, mobile);

        return baseDto;
    }

//    @ResponseBody
//    @RequestMapping(value = "/getRank", method = RequestMethod.GET)
//    public String getUserRank() {
//        String loginName = LoginUserInfo.getLoginName();
//        String rank = rankingActivityService.getUserRank(loginName);
//        return rank;
//    }
//
//    @ResponseBody
//    @RequestMapping(value = "/getTianDouTop15", method = RequestMethod.GET)
//    public List<UserScoreDto> getTianDouTop15() {
//        List<UserScoreDto> userScoreDtos = rankingActivityService.getTianDouTop15();
//        return userScoreDtos;
//    }
//
//    @ResponseBody
//    @RequestMapping(value = "/getTianDouWinnerList", method = RequestMethod.GET)
//    public Map<String, List<UserTianDouRecordDto>> getTiandouWinnerList() {
//        Map<String, List<UserTianDouRecordDto>> winnerList = rankingActivityService.getTianDouWinnerList();
//        return winnerList;
//    }
//
//    @ResponseBody
//    @RequestMapping(value = "/getMyPrize", method = RequestMethod.GET)
//    public List<UserTianDouRecordDto> getMyPrize() {
//        String loginName = LoginUserInfo.getLoginName();
//        List<UserTianDouRecordDto> myPrize = rankingActivityService.getPrizeByLoginName(loginName);
//        return myPrize;
//    }


}
