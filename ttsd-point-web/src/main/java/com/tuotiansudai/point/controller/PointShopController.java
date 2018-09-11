package com.tuotiansudai.point.controller;


import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.service.UserMembershipEvaluator;
import com.tuotiansudai.point.repository.dto.PointBillPaginationItemDataDto;
import com.tuotiansudai.point.repository.dto.ProductShowItemDto;
import com.tuotiansudai.point.repository.model.GoodsType;
import com.tuotiansudai.point.repository.model.PointBusinessType;
import com.tuotiansudai.point.repository.model.ProductOrderViewDto;
import com.tuotiansudai.point.repository.model.UserAddressModel;
import com.tuotiansudai.point.service.*;
import com.tuotiansudai.service.UserService;
import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.constraints.Min;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/point-shop")
public class PointShopController {
    @Autowired
    private PointService pointService;

    @Autowired
    private PointBillService pointBillService;

    @Autowired
    private PointTaskService pointTaskService;

    @Autowired
    private PointExchangeService pointExchangeService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private SignInService signInService;

    @Autowired
    private UserMembershipEvaluator userMembershipEvaluator;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView pointSystemHome() {
        ModelAndView modelAndView = new ModelAndView("point-index");
        String loginName = LoginUserInfo.getLoginName();

        MembershipModel membershipModel = userMembershipEvaluator.evaluate(loginName);

        List<ProductShowItemDto> virtualProducts = productService.findAllProductsByGoodsTypes(Lists.newArrayList(GoodsType.COUPON,
                GoodsType.VIRTUAL));
        modelAndView.addObject("virtualProducts", virtualProducts);

        List<ProductShowItemDto> physicalProducts = productService.findAllProductsByGoodsTypes(Lists.newArrayList(GoodsType.PHYSICAL));
        modelAndView.addObject("physicalProducts", physicalProducts);

        boolean isLogin = userService.loginNameIsExist(loginName);
        boolean isShowDiscount = membershipModel != null && membershipModel.getLevel() > 1;
        if (isLogin) {
            modelAndView.addObject("userPoint", pointService.getAvailablePoint(loginName));
            modelAndView.addObject("isSignIn", signInService.signInIsSuccess(loginName));
            modelAndView.addObject("discountShow", productService.discountShowInfo(loginName));
        }
        modelAndView.addObject("discount", isLogin ? productService.discountRate(loginName) : 1.0);
        modelAndView.addObject("isLogin", isLogin);
        modelAndView.addObject("isShowDiscount", isShowDiscount);
        modelAndView.addObject("responsive", true);
        modelAndView.addObject("signCount", Strings.isNullOrEmpty(loginName) ? "0" : signInService.getSignInCount(loginName));
        return modelAndView;
    }

    @RequestMapping(path = "/sign-in", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> signIn() {
        String loginName = LoginUserInfo.getLoginName();
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        BaseDataDto baseDataDto = signInService.signIn(loginName);
        if (null != baseDataDto) {
            baseDataDto.setStatus(true);
            baseDto.setData(baseDataDto);
            return baseDto;
        } else {
            return new BaseDto<>(new BaseDataDto(false, "您还未实名认证，不可以获得积分。请先去实名认证再来签到领积分吧!"));
        }
    }

    @RequestMapping(value = "/{id}/{goodsType:(?:COUPON|PHYSICAL|VIRTUAL)}/detail", method = RequestMethod.GET)
    public ModelAndView pointSystemDetail(@PathVariable long id,
                                          @PathVariable GoodsType goodsType) {
        ModelAndView modelAndView = new ModelAndView("/point-detail");
        String loginName = LoginUserInfo.getLoginName();
        ProductShowItemDto productShowItemDto = productService.findProductShowItemDto(id, goodsType);
        modelAndView.addObject("productShowItem", productShowItemDto);
        modelAndView.addObject("discount", productService.discountRate(loginName));
        modelAndView.addObject("buyCount", productService.getUserBuyCountInMonth(id, loginName));
        modelAndView.addObject("responsive", true);
        return modelAndView;
    }

    @RequestMapping(value = "/hasEnoughGoods", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> hasEnoughGoods(@RequestParam(value = "id", required = true) long id,
                                               @RequestParam(value = "goodsType", required = true) GoodsType goodsType,
                                               @RequestParam(value = "amount", required = true) int amount) {
        ProductShowItemDto productShowItemDto = productService.findProductShowItemDto(id, goodsType);
        if (productShowItemDto.getLeftCount() < amount) {
            return new BaseDto<>(new BaseDataDto(false, "所需商品数量不足"));
        } else {
            return new BaseDto<>(new BaseDataDto(true));
        }
    }

    @RequestMapping(value = "/order/{id}/{goodsType:(?:COUPON|PHYSICAL|VIRTUAL)}/{number}", method = RequestMethod.GET)
    public ModelAndView pointSystemOrder(@PathVariable long id, @PathVariable GoodsType goodsType, @PathVariable int number) {
        ModelAndView modelAndView = new ModelAndView("/point-order");
        String loginName = LoginUserInfo.getLoginName();
        ProductShowItemDto productShowItemDto = productService.findProductShowItemDto(id, goodsType);
        modelAndView.addObject("productShowItem", productShowItemDto);
        if (number <= productShowItemDto.getLeftCount()) {
            modelAndView.addObject("number", number);
        } else {
            modelAndView.addObject("number", productShowItemDto.getLeftCount());
        }

        if (goodsType.equals(GoodsType.PHYSICAL)) {
            List<UserAddressModel> userAddressModels = productService.getUserAddressesByLoginName(loginName);
            modelAndView.addObject("addresses", userAddressModels);
        }

        modelAndView.addObject("discount", productService.discountRate(loginName));
        modelAndView.addObject("buyCount", productService.getUserBuyCountInMonth(id, loginName));
        modelAndView.addObject("responsive", true);
        return modelAndView;
    }

    @RequestMapping(value = "/order", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> buyProduct(@RequestParam(value = "id", required = true) long id,
                                           @RequestParam(value = "goodsType", required = true) GoodsType goodsType,
                                           @RequestParam(value = "number", required = true) int number,
                                           @RequestParam(value = "userAddressId", required = false) Long addressId,
                                           @RequestParam(value = "comment", required = false) String comment) {
        String loginName = LoginUserInfo.getLoginName();
        return productService.buyProduct(loginName, id, goodsType, number, addressId, comment);
    }

    @RequestMapping(value = "/add-address", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> addAddress(@RequestParam(value = "realName", required = true) String realName,
                                           @RequestParam(value = "mobile", required = true) String mobile,
                                           @RequestParam(value = "address", required = true) String address) {
        String loginName = LoginUserInfo.getLoginName();
        return productService.addAddress(loginName, realName, mobile, address);
    }

    @RequestMapping(value = "/update-address", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> updateAddress(@RequestParam(value = "id", required = true) long id,
                                              @RequestParam(value = "realName", required = true) String realName,
                                              @RequestParam(value = "mobile", required = true) String mobile,
                                              @RequestParam(value = "address", required = true) String address) {
        String loginName = LoginUserInfo.getLoginName();
        return productService.updateAddress(id, loginName, realName, mobile, address);
    }

    @RequestMapping(value = "/task", method = RequestMethod.GET)
    public ModelAndView pointSystemTask() {
        ModelAndView modelAndView = new ModelAndView("/point-task");
        String loginName = LoginUserInfo.getLoginName();
        modelAndView.addObject("newbiePointTasks", pointTaskService.getNewbiePointTasks(loginName));
        modelAndView.addObject("advancedPointTasks", pointTaskService.getAdvancedPointTasks(loginName));
        modelAndView.addObject("completedAdvancedPointTasks", pointTaskService.getCompletedAdvancedPointTasks(loginName));
        modelAndView.addObject("responsive", true);
        return modelAndView;
    }

    @RequestMapping(value = "/record", method = RequestMethod.GET)
    public ModelAndView pointSystemRecord() {
        ModelAndView modelAndView = new ModelAndView("/point-record");
        modelAndView.addObject("responsive", true);
        return modelAndView;
    }

    @RequestMapping(value = "/record-list", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto<BasePaginationDataDto> pointSystemRecordDetail(@Min(value = 1) @RequestParam(name = "index", defaultValue = "1", required = false) int index) {

        BasePaginationDataDto<ProductOrderViewDto> dataDto = pointExchangeService.findProductOrderListByLoginNamePagination(LoginUserInfo.getLoginName(), index, 10);
        BaseDto<BasePaginationDataDto> dto = new BaseDto<>();
        dto.setData(dataDto);
        return dto;
    }

    @RequestMapping(value = "/bill", method = RequestMethod.GET)
    public ModelAndView pointSystemBill() {
        ModelAndView modelAndView = new ModelAndView("/point-bill");
        modelAndView.addObject("responsive", true);
        return modelAndView;
    }

    @RequestMapping(value = "/bill-list", method = RequestMethod.GET)
    @ResponseBody
    public BaseDto<BasePaginationDataDto> pointBillListData(@Min(value = 1) @RequestParam(name = "index", defaultValue = "1", required = false) int index,
                                                            @RequestParam(name = "pointType", required = false) String pointType,
                                                            @RequestParam(name = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                                                            @RequestParam(name = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
                                                            @RequestParam(name = "businessType", required = false) List<PointBusinessType> businessType) {

        BasePaginationDataDto<PointBillPaginationItemDataDto> dataDto = pointBillService.getPointBillPagination(LoginUserInfo.getLoginName(), pointType, index, 10, startTime, endTime, businessType);
        BaseDto<BasePaginationDataDto> dto = new BaseDto<>();
        dto.setData(dataDto);
        return dto;
    }
}
