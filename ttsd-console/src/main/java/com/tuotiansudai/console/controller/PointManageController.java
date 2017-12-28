package com.tuotiansudai.console.controller;


import com.google.common.collect.Lists;
import com.tuotiansudai.dto.ChannelPointDataDto;
import com.tuotiansudai.console.service.ConsoleCouponService;
import com.tuotiansudai.console.service.CouponActivationService;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.exception.CreateCouponException;
import com.tuotiansudai.point.exception.ChannelPointDataValidationException;
import com.tuotiansudai.point.repository.dto.AccountItemDataDto;
import com.tuotiansudai.point.repository.dto.ChannelPointDetailPaginationItemDataDto;
import com.tuotiansudai.point.repository.dto.PointBillPaginationItemDataDto;
import com.tuotiansudai.point.repository.dto.ProductDto;
import com.tuotiansudai.point.repository.model.GoodsType;
import com.tuotiansudai.point.repository.model.ProductModel;
import com.tuotiansudai.point.service.ChannelPointServiceImpl;
import com.tuotiansudai.point.service.PointBillService;
import com.tuotiansudai.point.service.ProductService;
import com.tuotiansudai.repository.model.CouponModel;
import com.tuotiansudai.repository.model.ProductType;
import com.tuotiansudai.repository.model.UserGroup;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.util.PaginationUtil;
import com.tuotiansudai.util.RequestIPParser;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/point-manage")
public class PointManageController {
    private static Logger logger = Logger.getLogger(PointManageController.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private ConsoleCouponService consoleCouponService;

    @Autowired
    private CouponActivationService couponActivationService;

    @Autowired
    private PointBillService pointBillService;

    @Autowired
    private ChannelPointServiceImpl channelPointServiceImpl;

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
                                         @RequestParam(value = "index", required = false, defaultValue = "1") int index) {
        int pageSize = 10;
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("products", productService.findAllProducts(type, index, pageSize));
        modelAndView.addObject("type", type);
        modelAndView.addObject("index", index);
        modelAndView.addObject("pageSize", pageSize);
        long goodsCount = productService.findAllProductsCount(type);
        long totalPages = PaginationUtil.calculateMaxPage(goodsCount, pageSize);
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
                             @RequestParam(value = "index", required = false, defaultValue = "1") int index) {
        int pageSize = 10;
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("orders", productService.findProductOrderList(productId, null, index, pageSize));
        modelAndView.addObject("product", productService.findById(productId));
        long ordersCount = productService.findProductOrderCount(productId);
        modelAndView.addObject("productId", productId);
        modelAndView.addObject("index", index);
        modelAndView.addObject("pageSize", pageSize);
        long totalPages = PaginationUtil.calculateMaxPage(ordersCount, pageSize);
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
        productService.inactive(productService.findProductByCouponId(couponId).getId(), loginName);
        BaseDataDto dataDto = new BaseDataDto();
        dataDto.setStatus(true);
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        baseDto.setData(dataDto);

        return baseDto;
    }

    @RequestMapping(value = "/exchange-coupon/{couponId:^\\d+$}/active", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> activeExchangeCoupon(@PathVariable long couponId, HttpServletRequest request) {
        String loginName = LoginUserInfo.getLoginName();
        String ip = RequestIPParser.parse(request);
        couponActivationService.active(loginName, couponId, ip);
        productService.active(productService.findProductByCouponId(couponId).getId(), loginName);
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
        ExchangeCouponDto exchangeCouponDto = new ExchangeCouponDto(consoleCouponService.findCouponById(id));
        ProductModel productModel = productService.findProductByCouponId(id);
        exchangeCouponDto.setSeq(productModel.getSeq());
        exchangeCouponDto.setExchangePoint(productModel.getPoints());
        exchangeCouponDto.setImageUrl(productModel.getImageUrl());
        exchangeCouponDto.setMonthLimit(productModel.getMonthLimit());
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
                consoleCouponService.editCoupon(loginName, exchangeCouponDto);
                ProductModel productModel = productService.findProductByCouponId(id);
                ProductDto productDto = new ProductDto
                        (productModel.getId(),
                                GoodsType.COUPON, loginName,
                                exchangeCouponDto.getId(),
                                exchangeCouponDto.getCouponType().name(),
                                exchangeCouponDto.getSeq(),
                                exchangeCouponDto.getImageUrl(),
                                exchangeCouponDto.getExchangePoint(),
                                exchangeCouponDto.getTotalCount(),
                                exchangeCouponDto.getMonthLimit(),
                                exchangeCouponDto.getStartTime(),
                                exchangeCouponDto.getEndTime());
                productService.updateProduct(productDto);
            } else {
                ExchangeCouponDto exchangeCouponDtoView = consoleCouponService.createCoupon(loginName, exchangeCouponDto);
                ProductDto productDto = new ProductDto(
                        GoodsType.COUPON,
                        exchangeCouponDtoView.getId(),
                        exchangeCouponDtoView.getCouponType().name(),
                        exchangeCouponDtoView.getSeq(),
                        exchangeCouponDtoView.getImageUrl(),
                        exchangeCouponDtoView.getExchangePoint(),
                        exchangeCouponDtoView.getTotalCount(),
                        exchangeCouponDtoView.getMonthLimit(),
                        exchangeCouponDtoView.getStartTime(),
                        exchangeCouponDtoView.getEndTime(),
                        loginName);
                productService.createProduct(productDto);
            }
            modelAndView.setViewName("redirect:/point-manage/coupon-exchange-manage");
        } catch (CreateCouponException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return modelAndView;
    }

    @RequestMapping(value = "/coupon-exchange-manage", method = RequestMethod.GET)
    public ModelAndView couponExchangeManage(@RequestParam(value = "index", required = false, defaultValue = "1") int index) {

        int pageSize = 10;
        ModelAndView modelAndView = new ModelAndView("/coupon-exchanges");
        List<ExchangeCouponDto> exchangeCouponDtos = productService.findCouponExchanges(index, pageSize);
        modelAndView.addObject("exchangeCoupons", exchangeCouponDtos);
        modelAndView.addObject("index", index);
        modelAndView.addObject("pageSize", pageSize);
        modelAndView.addObject("isExchange", "true");
        int exchangeCouponCount = productService.findCouponExchangeCount();
        modelAndView.addObject("exchangeCouponCount", exchangeCouponCount);
        long totalPages = PaginationUtil.calculateMaxPage(exchangeCouponCount, pageSize);
        boolean hasPreviousPage = index > 1 && index <= totalPages;
        boolean hasNextPage = index < totalPages;
        modelAndView.addObject("hasPreviousPage", hasPreviousPage);
        modelAndView.addObject("hasNextPage", hasNextPage);
        return modelAndView;
    }


    @RequestMapping(value = "/user-point-list")
    public ModelAndView usersAccountPointList(@RequestParam(value = "index", defaultValue = "1", required = false) int index,
                                              @RequestParam(value = "loginName", required = false) String loginName,
                                              @RequestParam(value = "mobile", required = false) String mobile) {
        int pageSize = 10;
        BasePaginationDataDto<AccountItemDataDto> accountItemDataDtoList = pointBillService.findUsersAccountPoint(loginName, mobile, index, pageSize);

        ModelAndView modelAndView = new ModelAndView("/user-point-list");
        modelAndView.addObject("index", index);
        modelAndView.addObject("pageSize", pageSize);
        modelAndView.addObject("userPointList", accountItemDataDtoList.getRecords());
        modelAndView.addObject("hasPreviousPage", accountItemDataDtoList.isHasPreviousPage());
        modelAndView.addObject("hasNextPage", accountItemDataDtoList.isHasNextPage());
        modelAndView.addObject("count", accountItemDataDtoList.getCount());
        modelAndView.addObject("loginName", loginName);
        modelAndView.addObject("mobile", mobile);
        return modelAndView;
    }

    @RequestMapping(value = "/user-point-detail-list")
    public ModelAndView usersAccountPointDetailList(@RequestParam(value = "index", defaultValue = "1", required = false) int index,
                                                    @RequestParam(value = "loginName", required = false) String loginName,
                                                    @RequestParam(value = "point", required = false) String point,
                                                    @RequestParam(value = "totalPoint", required = false) String totalPoint) {
        int pageSize = 10;
        ModelAndView modelAndView = new ModelAndView("/user-point-detail-list");
        modelAndView.addObject("index", index);
        modelAndView.addObject("pageSize", pageSize);
        modelAndView.addObject("point", point);
        modelAndView.addObject("totalPoint", totalPoint);

        List<PointBillPaginationItemDataDto> pointBillPaginationItemDataDtoList = pointBillService.getPointBillByLoginName(loginName, index, pageSize);

        modelAndView.addObject("userPointDetailList", pointBillPaginationItemDataDtoList);
        long count = pointBillService.getPointBillCountByLoginName(loginName);
        long totalPages = PaginationUtil.calculateMaxPage(count, pageSize);
        boolean hasPreviousPage = index > 1 && index <= totalPages;
        boolean hasNextPage = index < totalPages;
        modelAndView.addObject("hasPreviousPage", hasPreviousPage);
        modelAndView.addObject("hasNextPage", hasNextPage);
        modelAndView.addObject("count", count);
        modelAndView.addObject("loginName", loginName);
        return modelAndView;
    }


    @RequestMapping(value = "/order/{couponId:^\\d+$}/detail", method = RequestMethod.GET)
    public ModelAndView findProductOrderByCouponId(@PathVariable long couponId,
                                                   @RequestParam(value = "index", required = false, defaultValue = "1") int index) {
        int pageSize = 10;
        ModelAndView modelAndView = new ModelAndView();
        ProductModel productModel = productService.findProductByCouponId(couponId);

        modelAndView.addObject("orders", productService.findProductOrderList(productModel.getId(), null, index, pageSize));
        modelAndView.addObject("product", productService.findById(productModel.getId()));
        long ordersCount = productService.findProductOrderCount(productModel.getId());
        modelAndView.addObject("productId", productModel.getId());
        modelAndView.addObject("index", index);
        modelAndView.addObject("pageSize", pageSize);
        long totalPages = PaginationUtil.calculateMaxPage(ordersCount, pageSize);
        boolean hasPreviousPage = index > 1 && index <= totalPages;
        boolean hasNextPage = index < totalPages;
        modelAndView.addObject("hasPreviousPage", hasPreviousPage);
        modelAndView.addObject("hasNextPage", hasNextPage);
        modelAndView.addObject("ordersCount", ordersCount);
        modelAndView.setViewName("/orders-list");
        return modelAndView;
    }


    @RequestMapping(value = "/coupon/{couponId:^\\d+$}/{createdTime}/detail", method = RequestMethod.GET)
    public ModelAndView couponDetailByCouponIdCreatedTime(@PathVariable long couponId, @PathVariable(value = "createdTime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date createdTime, @RequestParam(value = "isUsed", required = false) Boolean isUsed,
                                                          @RequestParam(value = "usedStartTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date usedStartTime,
                                                          @RequestParam(value = "usedEndTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date usedEndTime,
                                                          @RequestParam(value = "loginName", required = false) String loginName,
                                                          @RequestParam(value = "mobile", required = false) String mobile,
                                                          @RequestParam(value = "index", required = false, defaultValue = "1") int index) {
        int pageSize = 10;
        ModelAndView modelAndView = new ModelAndView("/coupon-detail");
        List<CouponDetailsDto> userCoupons = consoleCouponService.findCouponDetail(couponId, isUsed, loginName, mobile, createdTime != null ? createdTime : null, null, null, usedStartTime, usedEndTime, index, pageSize);
        int userCouponsCount = consoleCouponService.findCouponDetailCount(couponId, isUsed, loginName, mobile, null, null, usedStartTime, usedEndTime);

        long investAmount = 0l;
        long interest = 0l;
        for (CouponDetailsDto couponDetailsDto : userCoupons) {
            investAmount += couponDetailsDto.getInvestAmount() != null ? couponDetailsDto.getInvestAmount() : 0l;
            interest += couponDetailsDto.getAnnualInterest() != null ? couponDetailsDto.getAnnualInterest() : 0l;
        }

        CouponModel couponModel = consoleCouponService.findCouponById(couponId);
        modelAndView.addObject("investAmount", investAmount);
        modelAndView.addObject("interest", interest);
        modelAndView.addObject("userCoupons", userCoupons);
        modelAndView.addObject("isUsed", isUsed);
        modelAndView.addObject("couponId", couponId);
        modelAndView.addObject("usedStartTime", usedStartTime);
        modelAndView.addObject("usedEndTime", usedEndTime);
        modelAndView.addObject("loginName", loginName);
        modelAndView.addObject("mobile", mobile);
        modelAndView.addObject("index", index);
        modelAndView.addObject("pageSize", pageSize);
        modelAndView.addObject("userCouponsCount", userCouponsCount);
        long totalPages = PaginationUtil.calculateMaxPage(userCouponsCount, pageSize);
        boolean hasPreviousPage = index > 1 && index <= totalPages;
        boolean hasNextPage = index < totalPages;
        modelAndView.addObject("hasPreviousPage", hasPreviousPage);
        modelAndView.addObject("hasNextPage", hasNextPage);
        String sideLabType;
        String headLab;
        if (couponModel.getCouponType() == CouponType.INTEREST_COUPON) {
            sideLabType = "statisticsInterestCoupon";
        } else if (couponModel.getCouponType() == CouponType.RED_ENVELOPE) {
            sideLabType = "statisticsRedEnvelope";
        } else if (couponModel.getCouponType() == CouponType.BIRTHDAY_COUPON) {
            sideLabType = "statisticsBirthdayCoupon";
        } else {
            sideLabType = "statisticsCoupon";
        }
        if (couponModel.getUserGroup() == UserGroup.EXCHANGER) {
            sideLabType = "couponExchangeManage";
            headLab = "point-manage";
        } else {
            headLab = "activity-manage";
        }
        modelAndView.addObject("sideLabType", sideLabType);
        modelAndView.addObject("headLab", headLab);
        return modelAndView;
    }

    @RequestMapping(value = "/channel-point", method = RequestMethod.GET)
    public ModelAndView getChannelPointList(@RequestParam(value = "index", required = false, defaultValue = "1") int index) {
        ModelAndView modelAndView = new ModelAndView("/channel-point", "data", channelPointServiceImpl.getChannelPointList(index, 10));
        modelAndView.addObject("sumHeadCount", channelPointServiceImpl.getSumHeadCount());
        modelAndView.addObject("sumTotalPoint", channelPointServiceImpl.getSumTotalPoint());

        return modelAndView;

    }

    @RequestMapping(value = "/channel-point-detail/{channelPointId:^\\d+$}", method = RequestMethod.GET)
    public ModelAndView getChannelPointDetailList(@PathVariable long channelPointId,
                                                  @RequestParam(value = "channel", required = false, defaultValue = "") String channel,
                                                  @RequestParam(value = "userNameOrMobile", required = false) String userNameOrMobile,
                                                  @RequestParam(value = "success", required = false) Boolean success,
                                                  @RequestParam(value = "index", required = false, defaultValue = "1") int index) {
        BasePaginationDataDto<ChannelPointDetailPaginationItemDataDto> basePaginationDataDto = channelPointServiceImpl.getChannelPointDetailList(channelPointId, channel, userNameOrMobile, success, index, 10);
        ModelAndView modelAndView = new ModelAndView("/channel-point-detail", "data", basePaginationDataDto);
        modelAndView.addObject("sumHeadCount", basePaginationDataDto.getCount());
        modelAndView.addObject("channel", channel);
        modelAndView.addObject("userNameOrMobile", userNameOrMobile);
        modelAndView.addObject("success", success);
        modelAndView.addObject("channelList", channelPointServiceImpl.findAllChannel());
        modelAndView.addObject("channelPointId", channelPointId);
        return modelAndView;
    }

    @RequestMapping(value = "/import", method = RequestMethod.POST)
    @ResponseBody
    public ChannelPointDataDto importCsv(HttpServletRequest httpServletRequest) throws Exception {
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) httpServletRequest;
        MultipartFile multipartFile = multipartHttpServletRequest.getFile("file");
        try {
            String originalFileName = channelPointServiceImpl.checkFileName(multipartFile);

            return channelPointServiceImpl.importChannelPoint(originalFileName, LoginUserInfo.getLoginName(), multipartFile.getInputStream());

        } catch (ChannelPointDataValidationException e) {
            return new ChannelPointDataDto(false, e.getMessage());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            return new ChannelPointDataDto(false, "內部程序异常");
        }
    }

    @RequestMapping(value = "/coupon/{couponId:^\\d+$}/detail", method = RequestMethod.GET)
    public ModelAndView couponDetailByCouponIdCreatedTime(@PathVariable long couponId, @RequestParam(value = "isUsed", required = false) Boolean isUsed,
                                                          @RequestParam(value = "usedStartTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date usedStartTime,
                                                          @RequestParam(value = "usedEndTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date usedEndTime,
                                                          @RequestParam(value = "loginName", required = false) String loginName,
                                                          @RequestParam(value = "mobile", required = false) String mobile,
                                                          @RequestParam(value = "index", required = false, defaultValue = "1") int index) {
        int pageSize = 10;
        ModelAndView modelAndView = new ModelAndView("/coupon-detail");
        List<CouponDetailsDto> userCoupons = consoleCouponService.findCouponDetail(couponId, isUsed, loginName, mobile, null, null, null, usedStartTime, usedEndTime, index, pageSize);
        int userCouponsCount = consoleCouponService.findCouponDetailCount(couponId, isUsed, loginName, mobile, null, null, usedStartTime, usedEndTime);

        long investAmount = 0l;
        long interest = 0l;
        for (CouponDetailsDto couponDetailsDto : userCoupons) {
            investAmount += couponDetailsDto.getInvestAmount() != null ? couponDetailsDto.getInvestAmount() : 0l;
            interest += couponDetailsDto.getAnnualInterest() != null ? couponDetailsDto.getAnnualInterest() : 0l;
        }

        CouponModel couponModel = consoleCouponService.findCouponById(couponId);
        modelAndView.addObject("investAmount", investAmount);
        modelAndView.addObject("interest", interest);
        modelAndView.addObject("userCoupons", userCoupons);
        modelAndView.addObject("isUsed", isUsed);
        modelAndView.addObject("couponId", couponId);
        modelAndView.addObject("usedStartTime", usedStartTime);
        modelAndView.addObject("usedEndTime", usedEndTime);
        modelAndView.addObject("loginName", loginName);
        modelAndView.addObject("mobile", mobile);
        modelAndView.addObject("index", index);
        modelAndView.addObject("pageSize", pageSize);
        modelAndView.addObject("userCouponsCount", userCouponsCount);
        long totalPages = PaginationUtil.calculateMaxPage(userCouponsCount, pageSize);
        boolean hasPreviousPage = index > 1 && index <= totalPages;
        boolean hasNextPage = index < totalPages;
        modelAndView.addObject("hasPreviousPage", hasPreviousPage);
        modelAndView.addObject("hasNextPage", hasNextPage);
        String sideLabType;
        String headLab;
        if (couponModel.getCouponType() == CouponType.INTEREST_COUPON) {
            sideLabType = "statisticsInterestCoupon";
        } else if (couponModel.getCouponType() == CouponType.RED_ENVELOPE) {
            sideLabType = "statisticsRedEnvelope";
        } else if (couponModel.getCouponType() == CouponType.BIRTHDAY_COUPON) {
            sideLabType = "statisticsBirthdayCoupon";
        } else {
            sideLabType = "statisticsCoupon";
        }
        if (couponModel.getUserGroup() == UserGroup.EXCHANGER) {
            sideLabType = "couponExchangeManage";
            headLab = "point-manage";
        } else {
            headLab = "activity-manage";
        }
        modelAndView.addObject("sideLabType", sideLabType);
        modelAndView.addObject("headLab", headLab);
        return modelAndView;
    }


}
