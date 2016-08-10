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
@RequestMapping(value = "/product-manage")
public class ProductManageController {
    private static Logger logger = Logger.getLogger(ProductManageController.class);

    @Autowired
    private ProductService productService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView createProduct(@Valid @RequestBody ProductDto productDto) {
        productDto.setLoginName(LoginUserInfo.getLoginName());
        try {
            productService.createProduct(productDto);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return getRedirectPage(productDto.getGoodsType());
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView createProduct(@RequestParam(value = "goodsType", required = false) GoodsType goodsType) {
        ModelAndView modelAndView = getRedirectPage(goodsType);
        modelAndView.addObject("productType", goodsType);
        modelAndView.addObject("productTypeDesc", goodsType.getDescription());
        return modelAndView;
    }


    @RequestMapping(value = "/find-goods", method = RequestMethod.GET)
    public ModelAndView findVirtualGoods(@RequestParam(value = "goodsType", required = false) GoodsType goodsType,
                                         @RequestParam(value = "index", required = false, defaultValue = "1") int index,
                                         @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("products", productService.findGoods(goodsType));
        modelAndView.addObject("productTypeDesc", goodsType.getDescription());
        modelAndView.addObject("index", index);
        modelAndView.addObject("pageSize", pageSize);
        modelAndView.addObject("goodsType", goodsType);
        long goodsCount = productService.findGoodsCount(goodsType);
        long totalPages = goodsCount / pageSize + (goodsCount % pageSize > 0 || goodsCount == 0 ? 1 : 0);
        boolean hasPreviousPage = index > 1 && index <= totalPages;
        boolean hasNextPage = index < totalPages;
        modelAndView.addObject("hasPreviousPage", hasPreviousPage);
        modelAndView.addObject("hasNextPage", hasNextPage);
        modelAndView.addObject("goodsCount", goodsCount);
        modelAndView.setViewName("/product-list");
        return modelAndView;
    }

    private ModelAndView getRedirectPage(GoodsType goodsType) {
        ModelAndView modelAndView = new ModelAndView();
        switch (goodsType) {
            case PHYSICAL:
                modelAndView.setViewName("/create-product");
                break;
            case VIRTUAL:
                modelAndView.setViewName("/create-product");
                break;
        }
        return modelAndView;
    }
}
