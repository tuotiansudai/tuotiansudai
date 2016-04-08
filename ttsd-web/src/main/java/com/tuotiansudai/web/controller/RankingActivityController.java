package com.tuotiansudai.web.controller;

import com.google.common.base.Strings;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.ranking.DrawLotteryDto;
import com.tuotiansudai.dto.ranking.UserScoreDto;
import com.tuotiansudai.dto.ranking.UserTianDouRecordDto;
import com.tuotiansudai.point.service.PointLotteryService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.security.MyAuthenticationManager;
import com.tuotiansudai.service.RankingActivityService;
import com.tuotiansudai.web.util.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
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

    @Autowired
    private MyAuthenticationManager myAuthenticationManager;

    @Autowired
    private RedisWrapperClient redisWrapperClient;

    @RequestMapping(value = "/rank-list", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView loadPageData(HttpServletRequest httpServletRequest) {
        String loginName = this.getLoginName(httpServletRequest);

        ModelAndView modelAndView = new ModelAndView("/activities/rank-list");

        Long myRank = rankingActivityService.getUserRank(loginName);

        List<UserScoreDto> tianDouTop15 = rankingActivityService.getTianDouTop15();

        Map<String, List<UserTianDouRecordDto>> winnerList = rankingActivityService.getTianDouWinnerList();

        List<UserTianDouRecordDto> myPrizeList = rankingActivityService.getPrizeByLoginName(loginName);

        Double myTianDou = rankingActivityService.getUserScoreByLoginName(loginName);

        AccountModel accountModel = accountMapper.findByLoginName(loginName);
        long totalInvest = rankingActivityService.getTotalInvestAmountInActivityPeriod();

        modelAndView.addObject("myRank", myRank);
        modelAndView.addObject("tianDouTop15", tianDouTop15);
        modelAndView.addObject("winnerList", winnerList);

        modelAndView.addObject("allPointLotteries", pointLotteryService.findAllDrawLottery());
        modelAndView.addObject("myPointLotteries", pointLotteryService.findMyDrawLottery(loginName));
        modelAndView.addObject("myPoint", accountModel == null ? 0 : accountModel.getPoint());

        modelAndView.addObject("myPrizeList", myPrizeList);
        modelAndView.addObject("myTianDou", myTianDou == null ? 0 : myTianDou.longValue());
        modelAndView.addObject("totalInvest", totalInvest);

        modelAndView.addObject("responsive", true);
        return modelAndView;
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

    private String getLoginName(HttpServletRequest httpServletRequest) {
        if (RequestMethod.GET.name().equalsIgnoreCase(httpServletRequest.getMethod())) {
            return LoginUserInfo.getLoginName();
        }

        String token = httpServletRequest.getParameter("token");
        String loginName = redisWrapperClient.get(token);

        if (Strings.isNullOrEmpty(loginName)) {
            myAuthenticationManager.removeAuthentication();
        } else {
            myAuthenticationManager.createAuthentication(loginName);
        }

        return loginName;
    }
}
