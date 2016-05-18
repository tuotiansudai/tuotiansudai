package com.tuotiansudai.console.controller;

import com.tuotiansudai.dto.InvestAchievementDto;
import com.tuotiansudai.dto.ranking.PrizeWinnerDto;
import com.tuotiansudai.dto.ranking.UserTianDouRecordDto;
import com.tuotiansudai.repository.TianDouPrize;
import com.tuotiansudai.service.InvestAchievementService;
import com.tuotiansudai.service.RankingActivityService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping(value = "/activity-manage")
public class RankingActivityController {

    @Autowired
    private RankingActivityService rankingActivityService;

    @Autowired
    private InvestAchievementService investAchievementService;

    static Logger logger = Logger.getLogger(RankingActivityController.class);

    @RequestMapping(value = "/user-tiandou", method = RequestMethod.GET)
    public ModelAndView intoTiandouQuery() {
        ModelAndView modelAndView = new ModelAndView("/ranking-user-tiandou");
        return modelAndView;
    }

    @RequestMapping(value = "/user-tiandou", method = RequestMethod.POST, params = "loginName")
    public ModelAndView userTiandouQuery(String loginName) {
        ModelAndView modelAndView = new ModelAndView("/ranking-user-tiandou");

        Double usableTianDou = rankingActivityService.getUserScoreByLoginName(loginName); // null if user not exists

        Long rank = rankingActivityService.getUserRank(loginName); // null if user not exists

        long totalScore = rankingActivityService.getTotalTiandouByLoginName(loginName);

        List<UserTianDouRecordDto> useRecordList = rankingActivityService.getTianDouRecordsByLoginName(loginName);

        modelAndView.addObject("loginName", loginName);
        modelAndView.addObject("usableTianDou", usableTianDou);
        modelAndView.addObject("rank", rank);
        modelAndView.addObject("totalScore", totalScore);
        modelAndView.addObject("useRecordList", useRecordList);
        return modelAndView;
    }

    @RequestMapping(value = "/tiandou-prize", method = RequestMethod.GET)
    public ModelAndView tiandouPrize() {
        ModelAndView modelAndView = new ModelAndView("/ranking-tiandou-prize");

        long drawCount = rankingActivityService.getDrawCount();

        long drawUserCount = rankingActivityService.getDrawUserCount();

        long macBookWinnerCount = rankingActivityService.getPrizeWinnerCount(TianDouPrize.MacBook);
        long iphoneWinnerCount = rankingActivityService.getPrizeWinnerCount(TianDouPrize.Iphone6s);
        long jingDongWinnerCount = rankingActivityService.getPrizeWinnerCount(TianDouPrize.JingDong300);
        long cashWinnerCount = rankingActivityService.getPrizeWinnerCount(TianDouPrize.Cash20);
        long couponWinnerCount = rankingActivityService.getPrizeWinnerCount(TianDouPrize.InterestCoupon5);

        modelAndView.addObject("drawCount", drawCount);
        modelAndView.addObject("drawUserCount", drawUserCount);
        modelAndView.addObject("macBookWinnerCount", macBookWinnerCount);
        modelAndView.addObject("iphoneWinnerCount", iphoneWinnerCount);
        modelAndView.addObject("jingDongWinnerCount", jingDongWinnerCount);
        modelAndView.addObject("cashWinnerCount", cashWinnerCount);
        modelAndView.addObject("couponWinnerCount", couponWinnerCount);
        return modelAndView;
    }

    @RequestMapping(value = "/prize-winner", method = RequestMethod.GET, params = "prize")
    public ModelAndView prizeWinner(String prize) {
        ModelAndView modelAndView = new ModelAndView("/ranking-prize-winner");

        long winnerCount = rankingActivityService.getPrizeWinnerCount(TianDouPrize.valueOf(prize));
        List<PrizeWinnerDto> prizeWinnerDtoList = rankingActivityService.getPrizeWinnerList(prize);

        modelAndView.addObject("winnerCount", winnerCount);
        modelAndView.addObject("prizeWinnerDtoList", prizeWinnerDtoList);
        return modelAndView;
    }

    @RequestMapping(value = "/invest-achievement", method = RequestMethod.GET)
    public ModelAndView investAchievement(@RequestParam(value = "index", required = false, defaultValue = "1") int index,
                                           @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                           @RequestParam(value = "loginName", required = false) String loginName) {
        List<InvestAchievementDto> investAchievementDtos = investAchievementService.findInvestAchievementManage(index, pageSize, loginName);
        ModelAndView modelAndView = new ModelAndView("/invest-achievement");
        modelAndView.addObject("investAchievementDtos", investAchievementDtos);
        modelAndView.addObject("index", index);
        modelAndView.addObject("pageSize", pageSize);
        modelAndView.addObject("loginName", loginName);
        long investAchievementCount = investAchievementService.findInvestAchievementManageCount(loginName);
        long totalPages = investAchievementCount / pageSize + (investAchievementCount % pageSize > 0 ? 1 : 0);
        boolean hasPreviousPage = index > 1 && index <= totalPages;
        boolean hasNextPage = index < totalPages;
        modelAndView.addObject("hasPreviousPage", hasPreviousPage);
        modelAndView.addObject("hasNextPage", hasNextPage);
        return modelAndView;
    }

}
