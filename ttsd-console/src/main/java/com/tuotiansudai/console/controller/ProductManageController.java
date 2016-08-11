package com.tuotiansudai.console.controller;


import com.tuotiansudai.console.util.LoginUserInfo;
import com.tuotiansudai.point.dto.GoodsActiveDto;
import com.tuotiansudai.point.dto.GoodsConsignmentDto;
import com.tuotiansudai.point.dto.ProductDto;
import com.tuotiansudai.point.repository.model.GoodsType;
import com.tuotiansudai.point.repository.model.ProductModel;
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
    public ModelAndView createProduct(@Valid @ModelAttribute ProductDto productDto) {
        productDto.setLoginName(LoginUserInfo.getLoginName());
        try {
            productService.createProduct(productDto);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return new ModelAndView("redirect:find-goods?goodsType=" + productDto.getGoodsType());
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView createProduct(@RequestParam(value = "goodsType", required = false) GoodsType goodsType) {
        ModelAndView modelAndView = getRedirectPage(goodsType);
        modelAndView.addObject("goodsType", goodsType);
        modelAndView.addObject("goodsTypeDesc", goodsType.getDescription());
        return modelAndView;
    }


    @RequestMapping(value = "/edit/{id:^\\d+$}", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView editProduct(@PathVariable long id) {
        ProductModel productModel = productService.findById(id);
        ModelAndView modelAndView = new ModelAndView("/edit-product");
        modelAndView.addObject("product", productModel);
        return modelAndView;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public ModelAndView updateProduct(@ModelAttribute ProductDto productDto) {
        productService.updateProduct(productDto);
        return new ModelAndView("redirect:find-goods?goodsType=" + productDto.getGoodsType());
    }


    @RequestMapping(value = "/find-goods", method = RequestMethod.GET)
    public ModelAndView findVirtualGoods(@RequestParam(value = "goodsType", required = false) GoodsType goodsType,
                                         @RequestParam(value = "index", required = false, defaultValue = "1") int index,
                                         @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("products", productService.findGoods(goodsType));
        modelAndView.addObject("goodsTypeDesc", goodsType.getDescription());
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


    @RequestMapping(value = "/find-orders", method = RequestMethod.GET)
    public ModelAndView find(@RequestParam(value = "productId", required = false) long productId,
                             @RequestParam(value = "index", required = false, defaultValue = "1") int index,
                             @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        ModelAndView modelAndView = new ModelAndView("/orders-list");
        modelAndView.addObject("orders", productService.findProductOrderList(productId));
        modelAndView.addObject("ordersCount", productService.findProductOrderCount(productId));
        modelAndView.addObject("index", index);
        modelAndView.addObject("pageSize", pageSize);
        return modelAndView;
    }

    @RequestMapping(value = "/goods-active", method = RequestMethod.POST)
    public ModelAndView goodsActive(GoodsActiveDto activeDto) {
        try {
            productService.goodsActive(activeDto);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return new ModelAndView();
    }

    @RequestMapping(value = "/consignment", method = RequestMethod.POST)
    public ModelAndView goodsConsignment(@Valid @ModelAttribute GoodsConsignmentDto consignmentDto) {
        consignmentDto.setLoginName(LoginUserInfo.getLoginName());

        try {
            productService.goodsConsignment(consignmentDto);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return new ModelAndView();
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
