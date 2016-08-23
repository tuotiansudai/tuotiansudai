package com.tuotiansudai.console.controller;


import com.google.common.collect.Lists;
import com.tuotiansudai.coupon.dto.ExchangeCouponDto;
import com.tuotiansudai.coupon.repository.model.CouponExchangeModel;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.service.CouponActivationService;
import com.tuotiansudai.coupon.service.CouponService;
import com.tuotiansudai.dto.AccountItemDataDto;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.exception.CreateCouponException;
import com.tuotiansudai.point.dto.ProductDto;
import com.tuotiansudai.point.repository.dto.PointBillPaginationItemDataDto;
import com.tuotiansudai.point.repository.model.GoodsType;
import com.tuotiansudai.point.repository.model.ProductModel;
import com.tuotiansudai.point.service.PointBillService;
import com.tuotiansudai.point.service.ProductService;
import com.tuotiansudai.repository.model.CouponType;
import com.tuotiansudai.repository.model.ProductType;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.util.RequestIPParser;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping(value = "/point-manage")
public class PointManageController {
    private static Logger logger = Logger.getLogger(PointManageController.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private CouponService couponService;

    @Autowired
    private CouponActivationService couponActivationService;

    @Autowired
    private PointBillService pointBillService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ModelAndView createProduct(@Valid @ModelAttribute ProductDto productDto) {
        productDto.setLoginName(LoginUserInfo.getLoginName());
        try {
            productService.createProduct(productDto);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return new ModelAndView("redirect:product-list?type=" + productDto.getType());
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView createProduct(@RequestParam(value = "type", required = false) GoodsType type) {
        ModelAndView modelAndView = new ModelAndView("/product-create");
        modelAndView.addObject("type", type);
        return modelAndView;
    }

    @RequestMapping(value = "/{id:^\\d+$}/edit", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView editProduct(@PathVariable long id) {
        ProductModel productModel = productService.findById(id);
        ModelAndView modelAndView = new ModelAndView("/product-edit");
        modelAndView.addObject("type", productModel.getType());
        modelAndView.addObject("product", productModel);
        return modelAndView;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public ModelAndView updateProduct(@ModelAttribute ProductDto productDto) {
        productDto.setLoginName(LoginUserInfo.getLoginName());
        productService.updateProduct(productDto);
        return new ModelAndView("redirect:product-list?type=" + productDto.getType());
    }

    @RequestMapping(value = "/product-list", method = RequestMethod.GET)
    public ModelAndView findVirtualGoods(@RequestParam(value = "type", required = false) GoodsType type,
                                         @RequestParam(value = "index", required = false, defaultValue = "1") int index,
                                         @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("products", productService.findAllProducts(type, index, pageSize));
        modelAndView.addObject("type", type);
        modelAndView.addObject("index", index);
        modelAndView.addObject("pageSize", pageSize);
        long goodsCount = productService.findAllProductsCount(type);
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

    @RequestMapping(value = "/{productId:^\\d+$}/inactive", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> productInActive(@PathVariable long productId) {
        BaseDataDto dataDto = new BaseDataDto();
        productService.inactive(productId, LoginUserInfo.getLoginName());
        dataDto.setStatus(true);
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        baseDto.setData(dataDto);
        return baseDto;
    }

    @RequestMapping(value = "/{orderId:^\\d+$}/consignment", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> productConsignment(@PathVariable long orderId) {
        BaseDataDto dataDto = new BaseDataDto();
        productService.consignment(orderId);
        dataDto.setStatus(true);
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        baseDto.setData(dataDto);
        return baseDto;
    }

    @RequestMapping(value = "/batch/{productId:^\\d+$}/consignment", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> productBatchConsignment(@PathVariable long productId) {
        BaseDataDto dataDto = new BaseDataDto();
        productService.batchConsignment(productId);
        dataDto.setStatus(true);
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        baseDto.setData(dataDto);
        return baseDto;
    }



    @RequestMapping(value = "/exchange-coupon/{couponId:^\\d+$}/inactive", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> inactiveExchangeCoupon(@PathVariable long couponId, HttpServletRequest request) {
        String loginName = LoginUserInfo.getLoginName();
        String ip = RequestIPParser.parse(request);
        couponActivationService.exchangeInactive(loginName, couponId, ip);
        BaseDataDto dataDto = new BaseDataDto();
        dataDto.setStatus(true);
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        baseDto.setData(dataDto);

        return baseDto;
    }

    @RequestMapping(value = "/coupon-exchange", method = RequestMethod.GET)
    public ModelAndView couponExchange() {
        ModelAndView modelAndView = new ModelAndView("/coupon-exchange");
        modelAndView.addObject("productTypes", Lists.newArrayList(ProductType.values()));
        modelAndView.addObject("couponTypes", Lists.newArrayList(CouponType.INVEST_COUPON, CouponType.INTEREST_COUPON, CouponType.RED_ENVELOPE));
        return modelAndView;
    }

    @RequestMapping(value = "/coupon-exchange/{id:^\\d+$}/edit", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable long id) {
        CouponModel couponModel = couponService.findCouponById(id);
        CouponExchangeModel couponExchangeModel = couponService.findCouponExchangeByCouponId(id);
        ExchangeCouponDto exchangeCouponDto = new ExchangeCouponDto(couponModel);
        exchangeCouponDto.setExchangePoint(couponExchangeModel.getExchangePoint());
        exchangeCouponDto.setSeq(couponExchangeModel.getSeq());
        ModelAndView modelAndView = new ModelAndView("/coupon-exchange-edit");
        modelAndView.addObject("exchangeCouponDto", exchangeCouponDto);
        modelAndView.addObject("productTypes", Lists.newArrayList(ProductType.values()));
        modelAndView.addObject("couponTypes", Lists.newArrayList(CouponType.INVEST_COUPON, CouponType.INTEREST_COUPON, CouponType.RED_ENVELOPE));
        return modelAndView;
    }

    @RequestMapping(value = "/coupon-exchange", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView createCouponExchange(@Valid @ModelAttribute ExchangeCouponDto exchangeCouponDto, RedirectAttributes redirectAttributes) {

        String loginName = LoginUserInfo.getLoginName();
        ModelAndView modelAndView = new ModelAndView();
        Long id = exchangeCouponDto.getId();
        try {
            if (id != null) {
                couponService.editCoupon(loginName, exchangeCouponDto);
                productService.updateProduct(new ProductDto(exchangeCouponDto, loginName));
            } else {
                ExchangeCouponDto exchangeCouponDtoView  = couponService.createCoupon(loginName, exchangeCouponDto);
                productService.createProduct(new ProductDto(exchangeCouponDtoView, loginName));

            }
            modelAndView.setViewName("redirect:/point-manage/coupon-exchange-manage");
        } catch (CreateCouponException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return modelAndView;
    }

    @RequestMapping(value = "/coupon-exchange-manage", method = RequestMethod.GET)
    public ModelAndView couponExchangeManage(@RequestParam(value = "index", required = false, defaultValue = "1") int index,
                                             @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {

        ModelAndView modelAndView = new ModelAndView("/coupon-exchanges");
        List<ExchangeCouponDto> exchangeCouponDtos = couponService.findCouponExchanges(index, pageSize);
        modelAndView.addObject("exchangeCoupons", exchangeCouponDtos);
        modelAndView.addObject("index", index);
        modelAndView.addObject("pageSize", pageSize);
        int exchangeCouponCount = couponService.findCouponExchangeCount();
        modelAndView.addObject("exchangeCouponCount", exchangeCouponCount);
        long totalPages = exchangeCouponCount / pageSize + (exchangeCouponCount % pageSize > 0 || exchangeCouponCount == 0 ? 1 : 0);
        boolean hasPreviousPage = index > 1 && index <= totalPages;
        boolean hasNextPage = index < totalPages;
        modelAndView.addObject("hasPreviousPage", hasPreviousPage);
        modelAndView.addObject("hasNextPage", hasNextPage);
        return modelAndView;
    }



    @RequestMapping(value = "/user-point-list")
    public ModelAndView usersAccountPointList(@RequestParam(value = "index", defaultValue = "1", required = false) int index,
                                              @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                              @RequestParam(value = "loginName", required = false) String loginName,
                                              @RequestParam(value = "userName", required = false) String userName,
                                              @RequestParam(value = "mobile", required = false) String mobile) {

        ModelAndView modelAndView = new ModelAndView("/user-point-list");
        modelAndView.addObject("index", index);
        modelAndView.addObject("pageSize", pageSize);
        List<AccountItemDataDto> accountItemDataDtoList = pointBillService.findUsersAccountPoint(loginName, userName, mobile, index, pageSize);
        modelAndView.addObject("userPointList", accountItemDataDtoList);
        int count = pointBillService.findUsersAccountPointCount(loginName, userName, mobile);
        long totalPages = count / pageSize + (count % pageSize > 0 || count == 0 ? 1 : 0);
        boolean hasPreviousPage = index > 1 && index <= totalPages;
        boolean hasNextPage = index < totalPages;
        modelAndView.addObject("hasPreviousPage", hasPreviousPage);
        modelAndView.addObject("hasNextPage", hasNextPage);
        modelAndView.addObject("count", count);
        modelAndView.addObject("loginName", loginName);
        modelAndView.addObject("userName", userName);
        modelAndView.addObject("mobile", mobile);
        return modelAndView;
    }

    @RequestMapping(value = "/user-point-detail-list")
    public ModelAndView usersAccountPointDetailList(@RequestParam(value = "index", defaultValue = "1", required = false) int index,
                                                    @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                                    @RequestParam(value = "loginName", required = false) String loginName,
                                                    @RequestParam(value = "point", required = false) String point,
                                                    @RequestParam(value = "totalPoint", required = false) String totalPoint) {

        ModelAndView modelAndView = new ModelAndView("/user-point-detail-list");
        modelAndView.addObject("index", index);
        modelAndView.addObject("pageSize", pageSize);
        modelAndView.addObject("point", point);
        modelAndView.addObject("totalPoint", totalPoint);

        List<PointBillPaginationItemDataDto> pointBillPaginationItemDataDtoList = pointBillService.getPointBillByLoginName(loginName, index, pageSize);

        modelAndView.addObject("userPointDetailList", pointBillPaginationItemDataDtoList);
        long count = pointBillService.getPointBillCountByLoginName(loginName);
        long totalPages = count / pageSize + (count % pageSize > 0 || count == 0 ? 1 : 0);
        boolean hasPreviousPage = index > 1 && index <= totalPages;
        boolean hasNextPage = index < totalPages;
        modelAndView.addObject("hasPreviousPage", hasPreviousPage);
        modelAndView.addObject("hasNextPage", hasNextPage);
        modelAndView.addObject("count", count);
        modelAndView.addObject("loginName", loginName);
        return modelAndView;
    }


}
