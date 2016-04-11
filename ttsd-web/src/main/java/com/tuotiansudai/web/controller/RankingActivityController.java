package com.tuotiansudai.web.controller;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.ranking.DrawLotteryDto;
import com.tuotiansudai.dto.ranking.UserScoreDto;
import com.tuotiansudai.dto.ranking.UserTianDouRecordDto;
import com.tuotiansudai.point.dto.UserPointPrizeDto;
import com.tuotiansudai.point.service.PointLotteryService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.AccountModel;
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

    @Autowired
    private PointLotteryService pointLotteryService;

    @Autowired
    private AccountMapper accountMapper;

    @RequestMapping(value = "/rank-list", method = RequestMethod.GET)
    public ModelAndView loadPageData() {

        ModelAndView modelAndView = new ModelAndView("/activities/rank-list");
        String loginName = LoginUserInfo.getLoginName();

        Long myRank = rankingActivityService.getUserRank(loginName);
        Double myTianDou = rankingActivityService.getUserScoreByLoginName(loginName);

        AccountModel accountModel = accountMapper.findByLoginName(loginName);
        long totalInvest = rankingActivityService.getTotalInvestAmountInActivityPeriod();

        modelAndView.addObject("myRank", myRank);
        modelAndView.addObject("myPoint", accountModel == null ? 0 : accountModel.getPoint());
        modelAndView.addObject("myTianDou", myTianDou == null ? 0 : myTianDou.longValue());
        modelAndView.addObject("totalInvest", totalInvest/100);
        modelAndView.addObject("responsive", true);
        return modelAndView;
    }

    @RequestMapping(value = "/rank-list-app", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView loadAppPageData() {
        return new ModelAndView("/activities/rank-list-app", "responsive", true);
    }

    @ResponseBody
    @RequestMapping(value = "/draw-tiandou", method = RequestMethod.POST)
    public BaseDto<DrawLotteryDto> drawTianDouPrize() {
        String loginName = LoginUserInfo.getLoginName();
        String mobile = LoginUserInfo.getLoginName();

        return rankingActivityService.drawTianDouPrize(loginName, mobile);
    }

    @ResponseBody
    @RequestMapping(value = "/point-lottery", method = RequestMethod.POST)
    public String pointLottery() {
        String loginName = LoginUserInfo.getLoginName();
        return pointLotteryService.pointLottery(loginName);
    }

    @ResponseBody
    @RequestMapping(value = "/get-lottery-chance", method = RequestMethod.POST)
    public boolean getLotteryChance() {
        String loginName = LoginUserInfo.getLoginName();
        pointLotteryService.getLotteryOnceChance(loginName);
        return true;
    }

    @ResponseBody
    @RequestMapping(value = "/getTianDouPrizeList", method = RequestMethod.POST)
    public Map<String, List<UserTianDouRecordDto>> getTianDouPrizeList() {
        Map<String, List<UserTianDouRecordDto>> winnerList = rankingActivityService.getTianDouWinnerList();
        return winnerList;
    }

    @ResponseBody
    @RequestMapping(value = "/getMyTianDouPrize", method = RequestMethod.POST)
    public List<UserTianDouRecordDto> getMyTianDouPrize() {
        String loginName = LoginUserInfo.getLoginName();
        List<UserTianDouRecordDto> myPrizeList = rankingActivityService.getPrizeByLoginName(loginName);
        return myPrizeList;
    }

    @ResponseBody
    @RequestMapping(value = "/getPointPrizeList", method = RequestMethod.POST)
    public List<UserPointPrizeDto> getPointPrizeList() {
        List<UserPointPrizeDto> allPointLotteries = pointLotteryService.findAllDrawLottery();
        return allPointLotteries;
    }

    @ResponseBody
    @RequestMapping(value = "/getMyPointPrize", method = RequestMethod.POST)
    public List<UserPointPrizeDto> getMyPointPrize() {
        String loginName = LoginUserInfo.getLoginName();
        List<UserPointPrizeDto> myPrizeList = pointLotteryService.findMyDrawLottery(loginName);
        return myPrizeList;
    }

    @ResponseBody
    @RequestMapping(value = "/getTianDouTop15", method = RequestMethod.POST)
    public List<UserScoreDto> getTianDouTop15() {
        List<UserScoreDto> tianDouTop15 = rankingActivityService.getTianDouTop15();
        return tianDouTop15;
    }
}
