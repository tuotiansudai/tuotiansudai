package com.tuotiansudai.api.controller.v1_0;

import com.tuotiansudai.api.dto.v1_0.BaseParamDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.ProductDetailRequestDto;
import com.tuotiansudai.api.dto.v1_0.UserAddressRequestDto;
import com.tuotiansudai.api.service.v1_0.MobileAppPointShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MobileAppPointShopController extends MobileAppBaseController{

    @Autowired
    private MobileAppPointShopService mobileAppPointShopService;

    @RequestMapping(value = "/get/user-address", method = RequestMethod.POST)
    public BaseResponseDto findUserAddressResponseDto(@RequestBody BaseParamDto baseParamDto) {
        baseParamDto.getBaseParam().setUserId(getLoginName());
        return mobileAppPointShopService.findUserAddressResponseDto(baseParamDto);
    }

    @RequestMapping(value = "/update/user-address", method = RequestMethod.POST)
    public BaseResponseDto updateUserAddress(@RequestBody UserAddressRequestDto userAddressRequestDto) {
        userAddressRequestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppPointShopService.updateUserAddress(userAddressRequestDto);
    }

    @RequestMapping(value = "/get/product-orders", method = RequestMethod.POST)
    public BaseResponseDto getProductOrders(@RequestBody BaseParamDto baseParamDto) {
        baseParamDto.getBaseParam().setUserId(getLoginName());
        return mobileAppPointShopService.findUserPointsOrders(baseParamDto);
    }

    @RequestMapping(value = "/get/point-home",method = RequestMethod.POST)
    public BaseResponseDto getPointHome(@RequestBody BaseParamDto baseParamDto){
        baseParamDto.getBaseParam().setUserId(getLoginName());
        return mobileAppPointShopService.findPointHome(baseParamDto);
    }

    @RequestMapping(value = "/get/product-detail",method = RequestMethod.POST)
    public BaseResponseDto getProductDetail(@RequestBody ProductDetailRequestDto productDetailRequestDto){
        productDetailRequestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppPointShopService.findProductDetail(productDetailRequestDto);
    }

    @RequestMapping(value = "/product-exchange",method = RequestMethod.POST)
    public BaseResponseDto productExchange(@RequestBody ProductDetailRequestDto productDetailRequestDto){
        productDetailRequestDto.getBaseParam().setUserId(getLoginName());
        return mobileAppPointShopService.productExchange(productDetailRequestDto);
    }

}
