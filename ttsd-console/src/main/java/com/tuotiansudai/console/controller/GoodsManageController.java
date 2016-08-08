package com.tuotiansudai.console.controller;


import com.tuotiansudai.console.util.LoginUserInfo;
import com.tuotiansudai.point.dto.ProductDto;
import com.tuotiansudai.point.repository.model.GoodsType;
import com.tuotiansudai.point.service.ProductService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping(value = "/activity-manage")
public class GoodsManageController {
    private static Logger logger = Logger.getLogger(GoodsManageController.class);

    @Autowired
    private ProductService goodsManageService;

    @RequestMapping(value = "/create/goods", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView createGoods(@Valid @RequestBody ProductDto productDto) {
        String loginName = LoginUserInfo.getLoginName();
        try {
            goodsManageService.createGoods(productDto, loginName);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return getRedirectPage(productDto.getGoodsType());
    }


    @RequestMapping(value = "/find-goods", method = RequestMethod.GET)
    public ModelAndView findVirtualGoods(@RequestParam(value = "goodsType", required = false) GoodsType goodsType,
                                         @RequestParam(value = "index", required = false, defaultValue = "1") int index,
                                         @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        ModelAndView modelAndView = getRedirectPage(goodsType);
        modelAndView.addObject("products", goodsManageService.findGoods(goodsType));
        modelAndView.addObject("index", index);
        modelAndView.addObject("pageSize", pageSize);
        long goodsCount = goodsManageService.findGoodsCount(goodsType);
        long totalPages = goodsCount / pageSize + (goodsCount % pageSize > 0 || goodsCount == 0 ? 1 : 0);
        boolean hasPreviousPage = index > 1 && index <= totalPages;
        boolean hasNextPage = index < totalPages;
        modelAndView.addObject("hasPreviousPage", hasPreviousPage);
        modelAndView.addObject("hasNextPage", hasNextPage);
        modelAndView.addObject("investAchievementCount", goodsCount);
        return modelAndView;
    }

    private ModelAndView getRedirectPage(GoodsType goodsType) {
        ModelAndView modelAndView = new ModelAndView();
        switch (goodsType) {
            case COUPON:
                modelAndView.setViewName("redirect:/activity-manage/coupon");
                break;
            case PHYSICAL:
                modelAndView.setViewName("redirect:/activity-manage/physical");
                break;
            case VIRTUAL:
                modelAndView.setViewName("redirect:/activity-manage/virtual");
                break;
        }
        return modelAndView;
    }
}
