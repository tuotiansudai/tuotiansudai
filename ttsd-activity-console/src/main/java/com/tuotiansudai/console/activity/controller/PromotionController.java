package com.tuotiansudai.console.activity.controller;

import com.tuotiansudai.activity.repository.dto.PromotionDto;
import com.tuotiansudai.activity.repository.dto.PromotionStatus;
import com.tuotiansudai.activity.repository.model.PromotionModel;
import com.tuotiansudai.console.activity.service.ActivityConsolePromotionService;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping(value = "/activity-console/activity-manage/promotion")
public class PromotionController {

    @Autowired
    private ActivityConsolePromotionService activityConsolePromotionService;

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView promotionCreate() {
        ModelAndView modelAndView = new ModelAndView("/promotion");
        return modelAndView;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ModelAndView promotionCreate(@ModelAttribute PromotionDto promotionDto) {
        activityConsolePromotionService.create(LoginUserInfo.getLoginName(), promotionDto);
        return new ModelAndView("redirect:/activity-console/activity-manage/promotion/promotion-list");
    }

    @RequestMapping(value = "/{id:^\\d+$}/delete", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> promotionDelete(@PathVariable long id) {
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        BaseDataDto dataDto = new BaseDataDto();
        baseDto.setData(dataDto);
        String loginName = LoginUserInfo.getLoginName();
        dataDto.setStatus(activityConsolePromotionService.delPromotion(loginName, id));
        return baseDto;
    }

    @RequestMapping(value = "/{id:^\\d+$}/approved", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> ApprovedPromotion(@PathVariable Long id) {
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        BaseDataDto dataDto = new BaseDataDto();
        baseDto.setData(dataDto);
        activityConsolePromotionService.AuditPromotion(LoginUserInfo.getLoginName(), PromotionStatus.APPROVED, id);
        dataDto.setStatus(true);
        return baseDto;
    }

    @RequestMapping(value = "/{id:^\\d+$}/rejection", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> RejectionPromotion(@PathVariable Long id) {
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        BaseDataDto dataDto = new BaseDataDto();
        baseDto.setData(dataDto);
        activityConsolePromotionService.AuditPromotion(LoginUserInfo.getLoginName(), PromotionStatus.REJECTION, id);
        dataDto.setStatus(true);
        return baseDto;
    }

    @RequestMapping(value = "/{id:^\\d+$}/edit", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView editPromotion(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("/promotion-edit");
        PromotionModel promotionModel = this.activityConsolePromotionService.findById(id);
        modelAndView.addObject("promotion", promotionModel);
        return modelAndView;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public ModelAndView updatePromotion(@ModelAttribute PromotionDto promotionDto) {
        String loginName = LoginUserInfo.getLoginName();
        activityConsolePromotionService.updatePromotion(loginName, promotionDto);
        return new ModelAndView("redirect:/activity-console/activity-manage/promotion/promotion-list");
    }

    @RequestMapping(value = "/promotion-list")
    public ModelAndView promotionList() {
        ModelAndView modelAndView = new ModelAndView("/promotion-list");
        List<PromotionModel> promotionModelList = activityConsolePromotionService.promotionList();
        modelAndView.addObject("promotionList", promotionModelList);
        return modelAndView;
    }

}
