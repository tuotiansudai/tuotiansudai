package com.tuotiansudai.activity.controller;

import com.tuotiansudai.activity.service.SuperScholarActivityService;
import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/activity/super-scholar")
public class SuperScholarActivityController {

    @Autowired
    private SuperScholarActivityService superScholarActivityService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView activityHome() {
        ModelAndView modelAndView = new ModelAndView("/activities/2018/super-scholar", "responsive", true);
        if (LoginUserInfo.getLoginName() != null) {
            modelAndView.addObject("data", superScholarActivityService.activityHome(LoginUserInfo.getLoginName()));
            modelAndView.addObject("doQuestion", superScholarActivityService.doQuestion(LoginUserInfo.getLoginName()));
        }
        return modelAndView;
    }

    @RequestMapping(value = "/view/question", method = RequestMethod.GET)
    public ModelAndView questionPage() {
        if (!superScholarActivityService.duringActivities()) {
            return new ModelAndView("redirect:/activity/super-scholar");
        }

        ModelAndView modelAndView = new ModelAndView("/activities/2018/super-scholar-question", "responsive", true);
        modelAndView.addObject("doQuestion", LoginUserInfo.getLoginName() != null && superScholarActivityService.doQuestion(LoginUserInfo.getLoginName()));
        return modelAndView;
    }

    @RequestMapping(value = "/questions", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> getQuestions() throws IOException {
        if (superScholarActivityService.doQuestion(LoginUserInfo.getLoginName())) {
            return null;
        }
        return superScholarActivityService.getQuestions(LoginUserInfo.getLoginName());
    }

    @RequestMapping(value = "/submit/answer", method = RequestMethod.POST)
    @ResponseBody
    public boolean submitAnswer(@RequestParam(value = "answer") String answer) {
        return superScholarActivityService.submitAnswer(LoginUserInfo.getLoginName(), answer);
    }

    @RequestMapping(value = "/view/result", method = RequestMethod.GET)
    public ModelAndView sharePage(@RequestParam(value = "shareType", defaultValue = "activityHome") String shareType) {
        if (!superScholarActivityService.duringActivities()) {
            return new ModelAndView("redirect:/activity/super-scholar");
        }

        ModelAndView modelAndView = new ModelAndView("/wechat/super_scholer_question_result_2018");
        Map<String, Object> map = superScholarActivityService.viewResult(LoginUserInfo.getLoginName());
        if (CollectionUtils.isEmpty(map)) {
            return new ModelAndView("redirect:/activity/super-scholar");
        }
        modelAndView.addAllObjects(map);
        modelAndView.addObject("mobile", LoginUserInfo.getMobile());
        modelAndView.addObject("shareType", shareType);
        return modelAndView;
    }

    @RequestMapping(value = "/share/success", method = RequestMethod.GET)
    @ResponseBody
    public boolean shareSuccess() {
        superScholarActivityService.shareSuccess(LoginUserInfo.getLoginName());
        return true;
    }

    @RequestMapping(value = "/share/register", method = RequestMethod.GET)
    public ModelAndView shareRegister(@RequestParam(value = "referrerMobile", required = false) String referrerMobile) {
        if (!superScholarActivityService.duringActivities()) {
            return new ModelAndView("redirect:/activity/super-scholar");
        }

        if (referrerMobile == null || !superScholarActivityService.mobileExists(referrerMobile)) {
            ModelAndView modelAndView = new ModelAndView("/error/error-info-page");
            modelAndView.addObject("errorInfo", "无效推荐链接");
            return modelAndView;
        }

        ModelAndView modelAndView = new ModelAndView("/wechat/share-app-mobile", "responsive", true);
        modelAndView.addObject("referrerInfo", superScholarActivityService.getReferrerInfo(referrerMobile));
        modelAndView.addObject("activityReferrerMobile", referrerMobile);
        return modelAndView;
    }

}
