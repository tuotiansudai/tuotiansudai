package com.tuotiansudai.api.service;


import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppPointShopService;
import com.tuotiansudai.point.repository.mapper.ProductMapper;
import com.tuotiansudai.point.repository.mapper.ProductOrderMapper;
import com.tuotiansudai.point.repository.model.GoodsType;
import com.tuotiansudai.point.repository.model.ProductModel;
import com.tuotiansudai.point.repository.model.ProductOrderModel;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MobileAppPointShopServiceTest extends ServiceTestBase{

    @Autowired
    private MobileAppPointShopService mobileAppPointShopService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ProductOrderMapper productOrderMapper;

    @Autowired
    private ProductMapper productMapper;

    @Test
    public void shouldUpdateUserAddressIsOk(){
        UserModel userModel = getUserModelTest("testUserAddress");
        BaseParamDto baseParamDto = new BaseParamDto();
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId(userModel.getLoginName());
        baseParamDto.setBaseParam(baseParam);
        UserAddressRequestDto userAddressRequestDto = new UserAddressRequestDto();
        userAddressRequestDto.setBaseParam(baseParam);
        userAddressRequestDto.setMobile(userModel.getMobile());
        userAddressRequestDto.setAddress("test");
        userAddressRequestDto.setContract("test");
        mobileAppPointShopService.updateUserAddress(userAddressRequestDto);
        BaseResponseDto<UserAddressResponseDto> baseResponseDto = mobileAppPointShopService.findUserAddressResponseDto(baseParamDto);
        assertEquals(baseResponseDto.getData().getAddress(),"test");
        assertEquals(baseResponseDto.getData().getContract(),"test");
    }

    @Test
    public void shouldUserAddressIsAlreadyUpdateIsOk(){
        UserModel userModel = getUserModelTest("testUserAddress");
        BaseParamDto baseParamDto = new BaseParamDto();
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId(userModel.getLoginName());
        baseParamDto.setBaseParam(baseParam);

        UserAddressRequestDto userAddressRequestDto = new UserAddressRequestDto();
        userAddressRequestDto.setBaseParam(baseParam);
        userAddressRequestDto.setMobile(userModel.getMobile());
        userAddressRequestDto.setAddress("test");
        userAddressRequestDto.setContract("test");
        mobileAppPointShopService.updateUserAddress(userAddressRequestDto);
        BaseResponseDto<UserAddressResponseDto> baseResponseDto = mobileAppPointShopService.findUserAddressResponseDto(baseParamDto);
        assertEquals(baseResponseDto.getData().getAddress(), "test");
        assertEquals(baseResponseDto.getData().getContract(), "test");
        userAddressRequestDto.setAddress("newTest");
        userAddressRequestDto.setContract("newContract");
        mobileAppPointShopService.updateUserAddress(userAddressRequestDto);
        baseResponseDto = mobileAppPointShopService.findUserAddressResponseDto(baseParamDto);
        assertEquals(baseResponseDto.getData().getAddress(),"newTest");
        assertEquals(baseResponseDto.getData().getContract(), "newContract");
    }

    @Test
    public void shouldFindUserPointsOrdersIsOk(){
        UserModel userModel = getUserModelTest("testUserAddress");
        ProductModel productModel = getProductModel(userModel.getLoginName());
        ProductOrderModel productOrderModel = getProductOrderModel(productModel.getId(), userModel.getLoginName());
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId(userModel.getLoginName());
        BaseParamDto baseParamDto = new BaseParamDto();
        baseParamDto.setBaseParam(baseParam);
        baseParamDto.setIndex(1);
        baseParamDto.setPageSize(10);
        BaseResponseDto<ProductListOrderResponseDto> pointsOrders = mobileAppPointShopService.findUserPointsOrders(baseParamDto);
        List<ProductOrderResponseDto> productOrderResponseDtoList = pointsOrders.getData().getProductOrderResponseDtoList();
        assertTrue(CollectionUtils.isNotEmpty(productOrderResponseDtoList));
        assertEquals(productOrderResponseDtoList.get(0).getProductNum(),String.valueOf(productOrderModel.getNum()));
    }


    private ProductModel getProductModel(String loginName){
        ProductModel productModel = new ProductModel(GoodsType.VIRTUAL, "50元充值卡", 1, "upload/images/11.png", "50yuan", 100, 0, 200, new Date(), new DateTime().plusDays(7).toDate(), false, loginName, new Date());
        productMapper.create(productModel);
        return productModel;
    }

    private ProductOrderModel getProductOrderModel(long productId,String loginName){
        ProductOrderModel productOrderModel = new ProductOrderModel();
        productOrderModel.setId(10001);
        productOrderModel.setProductId(productId);
        productOrderModel.setPoints(2000);
        productOrderModel.setNum(2);
        productOrderModel.setTotalPoints(4000);
        productOrderModel.setConsignment(false);
        productOrderModel.setContact("张山");
        productOrderModel.setMobile("13999999999");
        productOrderModel.setAddress("北京市北京市");
        productOrderModel.setConsignment(true);
        productOrderModel.setCreatedTime(new Date());
        productOrderModel.setCreatedBy(loginName);
        productOrderMapper.create(productOrderModel);
        return productOrderModel;

    }

    private UserModel getUserModelTest(String loginName) {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName(loginName);
        userModelTest.setPassword("123abc");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile("13900000000");
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(userModelTest);
        return userModelTest;
    }
}
