package com.tuotiansudai.api.service;


import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppPointShopService;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.coupon.repository.model.UserGroup;
import com.tuotiansudai.point.repository.mapper.ProductMapper;
import com.tuotiansudai.point.repository.mapper.ProductOrderMapper;
import com.tuotiansudai.point.repository.mapper.UserAddressMapper;
import com.tuotiansudai.point.repository.model.GoodsType;
import com.tuotiansudai.point.repository.model.ProductModel;
import com.tuotiansudai.point.repository.model.ProductOrderModel;
import com.tuotiansudai.point.repository.model.UserAddressModel;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;
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

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private UserAddressMapper userAddressMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

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
        userAddressRequestDto.setContact("test");
        mobileAppPointShopService.updateUserAddress(userAddressRequestDto);
        BaseResponseDto<UserAddressResponseDto> baseResponseDto = mobileAppPointShopService.findUserAddressResponseDto(baseParamDto);
        assertEquals(baseResponseDto.getData().getAddress(),"test");
        assertEquals(baseResponseDto.getData().getContact(),"test");
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
        userAddressRequestDto.setContact("test");
        mobileAppPointShopService.updateUserAddress(userAddressRequestDto);
        BaseResponseDto<UserAddressResponseDto> baseResponseDto = mobileAppPointShopService.findUserAddressResponseDto(baseParamDto);
        assertEquals(baseResponseDto.getData().getAddress(), "test");
        assertEquals(baseResponseDto.getData().getContact(), "test");
        userAddressRequestDto.setAddress("newTest");
        userAddressRequestDto.setContact("newContract");
        mobileAppPointShopService.updateUserAddress(userAddressRequestDto);
        baseResponseDto = mobileAppPointShopService.findUserAddressResponseDto(baseParamDto);
        assertEquals(baseResponseDto.getData().getAddress(),"newTest");
        assertEquals(baseResponseDto.getData().getContact(), "newContract");
    }

    @Test
    public void shouldFindUserPointsOrdersIsOk(){
        UserModel userModel = getUserModelTest("testUserAddress");
        ProductModel productModel = getProductModel(userModel.getLoginName(),GoodsType.VIRTUAL,100,0l);
        ProductOrderModel productOrderModel = getProductOrderModel(productModel.getId(), userModel.getLoginName());
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId(userModel.getLoginName());
        BaseParamDto baseParamDto = new BaseParamDto();
        baseParamDto.setBaseParam(baseParam);
        baseParamDto.setIndex(1);
        baseParamDto.setPageSize(10);
        BaseResponseDto<ProductListOrderResponseDto> pointsOrders = mobileAppPointShopService.findUserPointsOrders(baseParamDto);
        List<ProductOrderResponseDto> productOrderResponseDtoList = pointsOrders.getData().getOrders();
        assertTrue(CollectionUtils.isNotEmpty(productOrderResponseDtoList));
        assertEquals(productOrderResponseDtoList.get(0).getProductNum(),String.valueOf(productOrderModel.getNum()));
    }

    @Test
    public void shouldFindPointHomeIsOk(){
        String loginName = "findPointHomeUser";
        UserModel userModel = getUserModelTest(loginName);
        AccountModel accountModel = new AccountModel(loginName, loginName, "identityNumber", "payUserId", "payAccountId", new Date());
        accountModel.setPoint(1000l);
        accountMapper.create(accountModel);
        BaseParamDto baseParamDto = new BaseParamDto();
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId(userModel.getLoginName());
        baseParamDto.setBaseParam(baseParam);
        getProductModel(loginName, GoodsType.COUPON, 100,0l);
        getProductModel(loginName, GoodsType.PHYSICAL, 100,0l);
        getProductModel(loginName, GoodsType.VIRTUAL, 100,0l);
        BaseResponseDto<ProductListResponseDto> pointHome = mobileAppPointShopService.findPointHome(baseParamDto);
        assertEquals(pointHome.getData().getMyPoints(),String.valueOf(accountModel.getPoint()));
        assertTrue(pointHome.getData().getVirtuals().size() >= 2);
        assertTrue(pointHome.getData().getPhysicals().size() >= 1);
    }

    @Test
    public void shouldFindProductDetailIsOk(){
        String loginName = "findPointHomeUser";
        getUserModelTest(loginName);
        ProductModel productModel = getProductModel(loginName, GoodsType.COUPON,100,0l);
        ProductDetailRequestDto productDetailRequestDto = new ProductDetailRequestDto();
        productDetailRequestDto.setProductId(String.valueOf(productModel.getId()));
        BaseResponseDto<ProductDetailResponseDto> baseResponseDto = mobileAppPointShopService.findProductDetail(productDetailRequestDto);
        assertEquals(baseResponseDto.getData().getProductId(),String.valueOf(productModel.getId()));
    }

    @Test
    public void shouldProductIdIsNullIsFault(){
        String loginName = "findPointHomeUser";
        getUserModelTest(loginName);
        ProductDetailRequestDto productDetailRequestDto = new ProductDetailRequestDto();
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId(loginName);
        productDetailRequestDto.setBaseParam(baseParam);
        BaseResponseDto baseResponseDto = mobileAppPointShopService.productExchange(productDetailRequestDto);
        assertEquals(baseResponseDto.getCode(),ReturnMessage.POINTS_PRODUCT_IS_NOT_NULL.getCode());
    }

    @Test
    public void shouldProductNumIsNullIsFault(){
        String loginName = "findPointHomeUser";
        getUserModelTest(loginName);
        ProductDetailRequestDto productDetailRequestDto = new ProductDetailRequestDto();
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId(loginName);
        productDetailRequestDto.setBaseParam(baseParam);
        productDetailRequestDto.setProductId("11");
        BaseResponseDto baseResponseDto = mobileAppPointShopService.productExchange(productDetailRequestDto);
        assertEquals(baseResponseDto.getCode(),ReturnMessage.POINTS_PRODUCT_NUM_IS_NOT_NULL.getCode());
    }

    @Test
    public void shouldProductCountIsZeroIsFault(){
        String loginName = "findPointHomeUser";
        getUserModelTest(loginName);
        ProductModel productModel = getProductModel(loginName, GoodsType.COUPON,0l,0l);
        ProductDetailRequestDto productDetailRequestDto = new ProductDetailRequestDto();
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId(loginName);
        productDetailRequestDto.setBaseParam(baseParam);
        productDetailRequestDto.setProductId(String.valueOf(productModel.getId()));
        productDetailRequestDto.setNum(0);
        BaseResponseDto baseResponseDto = mobileAppPointShopService.productExchange(productDetailRequestDto);
        assertEquals(baseResponseDto.getCode(),ReturnMessage.INSUFFICIENT_PRODUCT_NUM.getCode());
    }

    @Test
    public void shouldMyPointsIsZeroIsFault(){
        String loginName = "findPointHomeUser";
        getUserModelTest(loginName);
        ProductModel productModel = getProductModel(loginName, GoodsType.COUPON,100,0l);
        ProductDetailRequestDto productDetailRequestDto = new ProductDetailRequestDto();
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId(loginName);
        productDetailRequestDto.setBaseParam(baseParam);
        productDetailRequestDto.setProductId(String.valueOf(productModel.getId()));
        productDetailRequestDto.setNum(2);
        AccountModel accountModel = new AccountModel(loginName, loginName, "identityNumber", "payUserId", "payAccountId", new Date());
        accountModel.setPoint(10l);
        accountMapper.create(accountModel);
        BaseResponseDto baseResponseDto = mobileAppPointShopService.productExchange(productDetailRequestDto);
        assertEquals(baseResponseDto.getCode(),ReturnMessage.INSUFFICIENT_POINTS_BALANCE.getCode());
    }

    @Test
    public void shouldMyAddressIsNullIsFault(){
        String loginName = "findPointHomeUser";
        getUserModelTest(loginName);
        ProductModel productModel = getProductModel(loginName, GoodsType.COUPON,100,0l);
        ProductDetailRequestDto productDetailRequestDto = new ProductDetailRequestDto();
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId(loginName);
        productDetailRequestDto.setBaseParam(baseParam);
        productDetailRequestDto.setProductId(String.valueOf(productModel.getId()));
        productDetailRequestDto.setNum(2);
        AccountModel accountModel = new AccountModel(loginName, loginName, "identityNumber", "payUserId", "payAccountId", new Date());
        accountModel.setPoint(1000l);
        accountMapper.create(accountModel);
        BaseResponseDto baseResponseDto = mobileAppPointShopService.productExchange(productDetailRequestDto);
        assertEquals(baseResponseDto.getCode(),ReturnMessage.USER_ADDRESS_IS_NOT_NULL.getCode());
    }

    @Test
    public void shouldProductExchangeIsOk(){
        String loginName = "findPointHomeUser";
        UserModel userModel = getUserModelTest(loginName);
        CouponModel couponModel = fakeCouponModel(loginName);
        couponMapper.create(couponModel);
        ProductModel productModel = getProductModel(loginName, GoodsType.COUPON,100,couponModel.getId());
        ProductDetailRequestDto productDetailRequestDto = new ProductDetailRequestDto();
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId(loginName);
        productDetailRequestDto.setBaseParam(baseParam);
        productDetailRequestDto.setProductId(String.valueOf(productModel.getId()));
        productDetailRequestDto.setNum(2);
        AccountModel accountModel = new AccountModel(loginName, loginName, "identityNumber", "payUserId", "payAccountId", new Date());
        accountModel.setPoint(1000l);
        accountMapper.create(accountModel);
        UserAddressModel userAddressModel = new UserAddressModel(loginName,loginName,userModel.getMobile(),"",loginName);
        userAddressMapper.create(userAddressModel);
        BaseResponseDto baseResponseDto = mobileAppPointShopService.productExchange(productDetailRequestDto);
        List<ProductOrderModel> productOrderModels = productOrderMapper.findProductOrderList(productModel.getId(), loginName, 0, 10);
        List<UserCouponModel>  userCouponModels = userCouponMapper.findByCouponId(couponModel.getId());
        assertEquals(baseResponseDto.getCode(), ReturnMessage.SUCCESS.getCode());
        assertEquals(productOrderModels.get(0).getProductId(),productModel.getId());
        assertEquals(userCouponModels.get(0).getCouponId(),couponModel.getId());
    }


    private CouponModel fakeCouponModel(String loginName){
        CouponModel couponModel = new CouponModel();
        couponModel.setAmount(1000L);
        couponModel.setActivatedBy(loginName);
        couponModel.setActive(true);
        couponModel.setCreatedTime(new Date());
        couponModel.setEndTime(DateUtils.addMinutes(new Date(), +1));
        couponModel.setDeadline(10);
        couponModel.setStartTime(new Date());
        couponModel.setCreatedBy(loginName);
        couponModel.setTotalCount(1000L);
        couponModel.setUsedCount(500L);
        couponModel.setInvestLowerLimit(10000L);
        couponModel.setCouponType(CouponType.INVEST_COUPON);
        couponModel.setProductTypes(Lists.newArrayList(ProductType._30, ProductType._90, ProductType._180));
        couponModel.setCouponSource(loginName);
        couponModel.setMultiple(true);
        couponModel.setUserGroup(UserGroup.ALL_USER);
        return couponModel;
    }

    private ProductModel getProductModel(String loginName,GoodsType goodsType,long totalCount,long couponId){
        ProductModel productModel = new ProductModel(goodsType, "50元充值卡", 1, "upload/images/11.png", "50yuan", totalCount, 0, 200, new Date(), new DateTime().plusDays(7).toDate(), false, loginName, new Date());
        productModel.setCouponId(couponId);
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
