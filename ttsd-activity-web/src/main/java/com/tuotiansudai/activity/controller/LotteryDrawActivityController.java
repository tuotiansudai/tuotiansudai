package com.tuotiansudai.activity.controller;


import com.google.common.base.Strings;
import com.tuotiansudai.activity.repository.dto.DrawLotteryResultDto;
import com.tuotiansudai.activity.repository.model.ActivityCategory;
import com.tuotiansudai.activity.repository.model.UserLotteryPrizeView;
import com.tuotiansudai.activity.service.CelebrationSingleActivityService;
import com.tuotiansudai.activity.service.LotteryDrawActivityService;
import com.tuotiansudai.point.repository.model.PointBusinessType;
import com.tuotiansudai.point.service.PointBillService;
import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.text.MessageFormat;
import java.util.List;

@Controller
@RequestMapping(value = "/activity/point-draw")
public class LotteryDrawActivityController {

    @Autowired
    private LotteryDrawActivityService lotteryDrawActivityService;

    @Autowired
    private PointBillService pointBillService;

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView loadPageData() {
        return new ModelAndView("redirect:/error/404");
    }

    @ResponseBody
    @RequestMapping(value = "/draw", method = RequestMethod.POST)
    public DrawLotteryResultDto travelDrawPrize(@RequestParam(value = "activityCategory", defaultValue = "POINT_DRAW_1000", required = false) ActivityCategory activityCategory) {
        DrawLotteryResultDto drawLotteryResultDto = lotteryDrawActivityService.drawPrizeByPoint(LoginUserInfo.getMobile(), activityCategory, activityCategory.equals(ActivityCategory.POINT_SHOP_DRAW_1000) ? true : false);
        if (drawLotteryResultDto.isDrawLotterySuccess()) {
            pointBillService.createPointBill(LoginUserInfo.getLoginName(), null, activityCategory.equals(ActivityCategory.POINT_SHOP_DRAW_1000) ? PointBusinessType.POINT_LOTTERY : PointBusinessType.ACTIVITY, (-activityCategory.getConsumeCategory().getPoint()), MessageFormat.format("抽中{0}", drawLotteryResultDto.getPrizeValue()));
        }
        return drawLotteryResultDto;
    }

    @ResponseBody
    @RequestMapping(value = "/task-draw", method = RequestMethod.POST)
    public DrawLotteryResultDto taskDrawPrize(@RequestParam(value = "activityCategory", defaultValue = "CARNIVAL_ACTIVITY", required = false) ActivityCategory activityCategory) {
        return new DrawLotteryResultDto(3);
    }

    @ResponseBody
    @RequestMapping(value = "/user-list", method = RequestMethod.GET)
    public List<UserLotteryPrizeView> getPrizeRecordByLoginName(@RequestParam(value = "mobile", required = false) String mobile,
                                                                @RequestParam(value = "activityCategory", required = false) ActivityCategory activityCategory) {
        return lotteryDrawActivityService.findDrawLotteryPrizeRecordByMobile(Strings.isNullOrEmpty(LoginUserInfo.getMobile()) ? mobile : LoginUserInfo.getMobile(), activityCategory);
    }

    @ResponseBody
    @RequestMapping(value = "/all-list", method = RequestMethod.GET)
    public List<UserLotteryPrizeView> getPrizeRecordByAll(@RequestParam(value = "activityCategory", required = false) ActivityCategory activityCategory) {
        return lotteryDrawActivityService.findDrawLotteryPrizeRecord(null, activityCategory);
    }

}
