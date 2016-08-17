package com.tuotiansudai.console.controller;


import com.tuotiansudai.console.util.LoginUserInfo;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.point.dto.GoodsActiveDto;
import com.tuotiansudai.point.dto.GoodsConsignmentDto;
import com.tuotiansudai.point.dto.ProductDto;
import com.tuotiansudai.point.repository.model.GoodsType;
import com.tuotiansudai.point.repository.model.ProductModel;
import com.tuotiansudai.point.service.ProductService;
import com.tuotiansudai.util.RequestIPParser;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
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
        return new ModelAndView("redirect:product-list?goodsType=" + productDto.getGoodsType());
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView createProduct(@RequestParam(value = "goodsType", required = false) GoodsType goodsType) {
        ModelAndView modelAndView = new ModelAndView("/product-create");
        modelAndView.addObject("goodsType", goodsType);
        return modelAndView;
    }

    @RequestMapping(value = "/{id:^\\d+$}/edit", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView editProduct(@PathVariable long id) {
        ProductModel productModel = productService.findById(id);
        ModelAndView modelAndView = new ModelAndView("/product-edit");
        modelAndView.addObject("goodsType", productModel.getGoodsType());
        modelAndView.addObject("product", productModel);
        return modelAndView;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public ModelAndView updateProduct(@ModelAttribute ProductDto productDto) {
        productDto.setLoginName(LoginUserInfo.getLoginName());
        productService.updateProduct(productDto);
        return new ModelAndView("redirect:product-list?goodsType=" + productDto.getGoodsType());
    }

    @RequestMapping(value = "/product-list", method = RequestMethod.GET)
    public ModelAndView findVirtualGoods(@RequestParam(value = "goodsType", required = false) GoodsType goodsType,
                                         @RequestParam(value = "index", required = false, defaultValue = "1") int index,
                                         @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("products", productService.findProductsList(goodsType, index, pageSize));
        modelAndView.addObject("goodsType", goodsType);
        modelAndView.addObject("index", index);
        modelAndView.addObject("pageSize", pageSize);
        modelAndView.addObject("goodsType", goodsType);
        long goodsCount = productService.findProductsCount(goodsType);
        long totalPages = goodsCount / pageSize + (goodsCount % pageSize > 0 || goodsCount == 0 ? 1 : 0);
        boolean hasPreviousPage = index > 1 && index <= totalPages;
        boolean hasNextPage = index < totalPages;
        modelAndView.addObject("hasPreviousPage", hasPreviousPage);
        modelAndView.addObject("hasNextPage", hasNextPage);
        modelAndView.addObject("goodsCount", goodsCount);
        modelAndView.setViewName("/product-list");
        return modelAndView;
    }

    @RequestMapping(value = "/{productId:^\\d+$}/detail", method = RequestMethod.GET)
    public ModelAndView find(@PathVariable long productId,
                             @RequestParam(value = "index", required = false, defaultValue = "1") int index,
                             @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("orders", productService.findProductOrderList(productId, null, index, pageSize));
        modelAndView.addObject("product", productService.findById(productId));
        long ordersCount = productService.findProductOrderCount(productId);
        modelAndView.addObject("productId", productId);
        modelAndView.addObject("index", index);
        modelAndView.addObject("pageSize", pageSize);
        long totalPages = ordersCount / pageSize + (ordersCount % pageSize > 0 || ordersCount == 0 ? 1 : 0);
        boolean hasPreviousPage = index > 1 && index <= totalPages;
        boolean hasNextPage = index < totalPages;
        modelAndView.addObject("hasPreviousPage", hasPreviousPage);
        modelAndView.addObject("hasNextPage", hasNextPage);
        modelAndView.addObject("ordersCount", ordersCount);
        modelAndView.setViewName("/orders-list");
        return modelAndView;
    }

    @RequestMapping(value = "/{productId:^\\d+$}/active", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> productActive(@PathVariable long productId) {
        BaseDataDto dataDto = new BaseDataDto();
        productService.active(productId, LoginUserInfo.getLoginName());
        dataDto.setStatus(true);
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        baseDto.setData(dataDto);
        return baseDto;
    }

    @RequestMapping(value = "/{orderId:^\\d+$}/consignment", method = RequestMethod.POST)
    @ResponseBody
    public  BaseDto<BaseDataDto> productConsignment(@PathVariable long orderId) {
        BaseDataDto dataDto = new BaseDataDto();
        productService.consignment(orderId);
        dataDto.setStatus(true);
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        baseDto.setData(dataDto);
        return baseDto;
    }

}
