package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppPointShopService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(description = "积分商城")
public class MobileAppPointShopController extends MobileAppBaseController {

    @Autowired
    private MobileAppPointShopService mobileAppPointShopService;

    @RequestMapping(value = "/get/user-address", method = RequestMethod.POST)
    @ApiOperation("获取收货地址")
    public BaseResponseDto<UserAddressResponseDto> findUserAddressResponseDto(@RequestBody BaseParamDto baseParamDto) {
        baseParamDto.getBaseParam().setUserId(getLoginName());
        return mobileAppPointShopService.findUserAddressResponseDto(baseParamDto);
    }

    @RequestMapping(value = "/update/user-address", method = RequestMethod.POST)
    @ApiOperation("更新收货地址")
    public BaseResponseDto updateUserAddress(@RequestBody UserAddressRequestDto userAddressRequestDto) {
        userAddressRequestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppPointShopService.updateUserAddress(userAddressRequestDto);
    }

    @RequestMapping(value = "/get/product-orders", method = RequestMethod.POST)
    @ApiOperation("商品兑换记录")
    public BaseResponseDto<ProductListOrderResponseDto> getProductOrders(@RequestBody BaseParamDto baseParamDto) {
        baseParamDto.getBaseParam().setUserId(getLoginName());
        return mobileAppPointShopService.findUserPointsOrders(baseParamDto);
    }

    @RequestMapping(value = "/get/point-home", method = RequestMethod.POST)
    @ApiOperation("积分商城首页")
    public BaseResponseDto<ProductListResponseDto> getPointHome(@RequestBody ProductListRequestDto productListRequestDto) {
        productListRequestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppPointShopService.findPointHome(productListRequestDto);
    }

    @RequestMapping(value = "/get/product-detail", method = RequestMethod.POST)
    @ApiOperation("商品详情")
    public BaseResponseDto<ProductDetailResponseDto> getProductDetail(@RequestBody ProductDetailRequestDto productDetailRequestDto) {
        productDetailRequestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppPointShopService.findProductDetail(productDetailRequestDto);
    }

    @RequestMapping(value = "/product-exchange", method = RequestMethod.POST)
    @ApiOperation("商品兑换")
    public BaseResponseDto productExchange(@RequestBody ProductDetailRequestDto productDetailRequestDto) {
        productDetailRequestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppPointShopService.productExchange(productDetailRequestDto);
    }

    @RequestMapping(value = "/get/prize", method = RequestMethod.POST)
    @ApiOperation("积分抽奖")
    public BaseResponseDto lotteryDrawByPoint(@RequestBody BaseParamDto baseParamDto) {
        baseParamDto.getBaseParam().setUserId(getLoginName());
        return mobileAppPointShopService.lotteryDrawByPoint(baseParamDto);
    }

    @RequestMapping(value = "/get/user-prize-list", method = RequestMethod.POST)
    @ApiOperation("我的中奖记录")
    public BaseResponseDto myPrizeList(@RequestBody BaseParamDto baseParamDto) {
        baseParamDto.getBaseParam().setUserId(getLoginName());
        return mobileAppPointShopService.findPrizeListByLoginName(baseParamDto);
    }

    @RequestMapping(value = "/get/all-prize-list", method = RequestMethod.POST)
    @ApiOperation("全部中奖记录")
    public BaseResponseDto PrizeList(@RequestBody BaseParamDto baseParamDto) {
        baseParamDto.getBaseParam().setUserId(getLoginName());
        return mobileAppPointShopService.findPrizeList(baseParamDto);
    }

}
