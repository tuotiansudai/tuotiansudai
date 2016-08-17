package com.tuotiansudai.pointsystem.controller;


import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.point.dto.ProductShowItemDto;
import com.tuotiansudai.point.repository.dto.PointBillPaginationItemDataDto;
import com.tuotiansudai.point.repository.model.GoodsType;
import com.tuotiansudai.point.repository.model.ItemType;
import com.tuotiansudai.point.repository.model.PointBusinessType;
import com.tuotiansudai.point.repository.model.UserAddressModel;
import com.tuotiansudai.point.service.*;
import com.tuotiansudai.pointsystem.util.LoginUserInfo;
import com.tuotiansudai.service.AccountService;
import com.tuotiansudai.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.constraints.Min;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/pointsystem")
public class PointSystemController {

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
    private AccountService accountService;

    @Autowired
    private SignInService signInService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView pointSystemHome() {
        ModelAndView modelAndView = new ModelAndView("pointsystem-index");
        String loginName = LoginUserInfo.getLoginName();

        List<ProductShowItemDto> virtualProducts = productService.findAllProductsByGoodsTypes(Lists.newArrayList(GoodsType.COUPON,
                GoodsType.VIRTUAL));
        modelAndView.addObject("virtualProducts", virtualProducts);

        List<ProductShowItemDto> physicalProducts = productService.findAllProductsByGoodsTypes(Lists.newArrayList(GoodsType.PHYSICAL));
        modelAndView.addObject("physicalProducts", physicalProducts);

        boolean isLogin = userService.loginNameIsExist(loginName);
        if (isLogin) {
            modelAndView.addObject("userPoint", accountService.getUserPointByLoginName(loginName));
            modelAndView.addObject("isSignIn", signInService.signInIsSuccess(loginName));
        }
        modelAndView.addObject("isLogin", isLogin);
        modelAndView.addObject("responsive", true);
        return modelAndView;
    }

    @RequestMapping(path = "/signIn", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> signIn() {
        String loginName = LoginUserInfo.getLoginName();
        BaseDto<BaseDataDto> baseDto = new BaseDto<>();
        BaseDataDto baseDataDto = signInService.signIn(loginName);
        baseDataDto.setStatus(true);
        baseDto.setData(baseDataDto);
        baseDto.setSuccess(true);
        return baseDto;
    }

    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    public ModelAndView pointSystemDetail(@PathVariable long id,
                                          @RequestParam(value = "itemType", required = true) ItemType itemType) {
        ModelAndView modelAndView = new ModelAndView("/pointsystem-detail");
        ProductShowItemDto productShowItemDto = productService.findProductShowItemDto(id, itemType);
        modelAndView.addObject("productShowItem", productShowItemDto);

        modelAndView.addObject("responsive", true);
        return modelAndView;
    }

    @RequestMapping(value = "/hasEnoughGoods", method = RequestMethod.POST)
    @ResponseBody
    public BaseDto<BaseDataDto> hasEnoughGoods(@RequestParam(value = "id", required = true) long id,
                                               @RequestParam(value = "itemType", required = true) ItemType itemType,
                                               @RequestParam(value = "amount", required = true) int amount) {
        ProductShowItemDto productShowItemDto = productService.findProductShowItemDto(id, itemType);
        if (productShowItemDto.getLeftCount() < amount) {
            return new BaseDto<>(new BaseDataDto(false, "所需商品数量不足"));
        } else {
            return new BaseDto<>(new BaseDataDto(true));
        }
    }

    @RequestMapping(value = "/order/{id}/{itemType}/{number}", method = RequestMethod.GET)
    public ModelAndView pointSystemOrder(@PathVariable long id, @PathVariable ItemType itemType, @PathVariable int number) {
        ModelAndView modelAndView = new ModelAndView("/pointsystem-order");

        ProductShowItemDto productShowItemDto = productService.findProductShowItemDto(id, itemType);
        modelAndView.addObject("productShowItem", productShowItemDto);
        if (number <= productShowItemDto.getLeftCount()) {
            modelAndView.addObject("number", number);
        } else {
            modelAndView.addObject("number", productShowItemDto.getLeftCount());
        }

        if (itemType.equals(ItemType.PHYSICAL)) {
            String loginName = LoginUserInfo.getLoginName();
            List<UserAddressModel> userAddressModels = productService.getUserAddressesByLoginName(loginName);
            modelAndView.addObject("addresses", userAddressModels);
        }

        modelAndView.addObject("responsive", true);
        return modelAndView;
    }

    @RequestMapping(value = "/order", method = RequestMethod.POST)
    public BaseDto<BaseDataDto> buyProduct(@RequestParam(value = "id", required = true) long id,
                                           @RequestParam(value = "itemType", required = true) ItemType itemType,
                                           @RequestParam(value = "number", required = true) int number,
                                           @RequestParam(value = "userAddress", required = false) UserAddressModel userAddressModel) {
        String loginName = LoginUserInfo.getLoginName();
        return productService.buyProduct(loginName, id, itemType, number, userAddressModel);
    }

    @RequestMapping(value = "/add-address", method = RequestMethod.POST)
    public BaseDto<BaseDataDto> addAddress(@RequestParam(value = "realName", required = true) String realName,
                                           @RequestParam(value = "mobile", required = true) String mobile,
                                           @RequestParam(value = "address", required = true) String address) {
        String loginName = LoginUserInfo.getLoginName();
        return productService.addAddress(loginName, realName, mobile, address);
    }

    @RequestMapping(value = "/update-address", method = RequestMethod.POST)
    public BaseDto<BaseDataDto> updateAddress(@RequestParam(value = "realName", required = true) String realName,
                                              @RequestParam(value = "mobile", required = true) String mobile,
                                              @RequestParam(value = "address", required = true) String address) {
        String loginName = LoginUserInfo.getLoginName();
        return productService.updateAddress(loginName, realName, mobile, address);
    }

    @RequestMapping(value = "/task", method = RequestMethod.GET)
    public ModelAndView pointSystemTask() {
        ModelAndView modelAndView = new ModelAndView("/pointsystem-task");
        String loginName = LoginUserInfo.getLoginName();
        modelAndView.addObject("newbiePointTasks", pointTaskService.getNewbiePointTasks(loginName));
        modelAndView.addObject("advancedPointTasks", pointTaskService.getAdvancedPointTasks(loginName));
        modelAndView.addObject("completedAdvancedPointTasks", pointTaskService.getCompletedAdvancedPointTasks(loginName));
        modelAndView.addObject("responsive", true);
        return modelAndView;
    }

    @RequestMapping(value = "/record", method = RequestMethod.GET)
    public ModelAndView pointSystemRecord(String loginName) {
        ModelAndView modelAndView = new ModelAndView("/pointsystem-record");
        modelAndView.addObject("recordList", pointExchangeService.findProductOrderListByLoginName(loginName));
        modelAndView.addObject("responsive", true);
        return modelAndView;
    }

    @RequestMapping(value = "/bill", method = RequestMethod.GET)
    public ModelAndView pointSystemBill() {
        ModelAndView modelAndView = new ModelAndView("/pointsystem-bill");
        BasePaginationDataDto<PointBillPaginationItemDataDto> dataDto = pointBillService.getPointBillPagination(LoginUserInfo.getLoginName(), 1, 10, null, null, null);
        BaseDto<BasePaginationDataDto> dto = new BaseDto<>();
        dto.setData(dataDto);
        modelAndView.addObject("dto", dto);
        modelAndView.addObject("responsive", true);
        return modelAndView;

    }

    @RequestMapping(value = "/bill-list", method = RequestMethod.GET, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public BaseDto<BasePaginationDataDto> pointBillListData(@Min(value = 1) @RequestParam(name = "index", defaultValue = "1", required = false) int index,
                                                            @Min(value = 1) @RequestParam(name = "pageSize", defaultValue = "10", required = false) int pageSize,
                                                            @RequestParam(name = "startTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                                                            @RequestParam(name = "endTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
                                                            @RequestParam(name = "businessType", required = false) List<PointBusinessType> businessType) {
        BasePaginationDataDto<PointBillPaginationItemDataDto> dataDto = pointBillService.getPointBillPagination(LoginUserInfo.getLoginName(), index, pageSize, startTime, endTime, businessType);
        BaseDto<BasePaginationDataDto> dto = new BaseDto<>();
        dto.setData(dataDto);

        return dto;
    }
}
