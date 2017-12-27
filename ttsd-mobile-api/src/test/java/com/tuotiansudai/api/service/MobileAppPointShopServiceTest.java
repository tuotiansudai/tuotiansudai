package com.tuotiansudai.api.service;


import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppPointShopService;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.point.repository.mapper.ProductMapper;
import com.tuotiansudai.point.repository.mapper.ProductOrderMapper;
import com.tuotiansudai.point.repository.mapper.UserAddressMapper;
import com.tuotiansudai.point.repository.mapper.UserPointMapper;
import com.tuotiansudai.point.repository.model.*;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.CouponMapper;
import com.tuotiansudai.repository.mapper.FakeUserHelper;
import com.tuotiansudai.repository.mapper.UserCouponMapper;
import com.tuotiansudai.repository.model.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.joda.time.DateTime;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MobileAppPointShopServiceTest extends ServiceTestBase {

    @Autowired
    private MobileAppPointShopService mobileAppPointShopService;

    @Autowired
    private FakeUserHelper userMapper;

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

    @Autowired
    private UserPointMapper userPointMapper;

    @Test
    public void shouldUpdateUserAddressIsOk() {
        UserModel userModel = getUserModelTest("testUserAddress");
        BaseParamDto baseParamDto = new BaseParamDto();
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId(userModel.getLoginName());
        baseParam.setPhoneNum("15544443333");
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
    }

    @Test
    public void shouldUserAddressIsAlreadyUpdateIsOk() {
        UserModel userModel = getUserModelTest("testUserAddress");
        BaseParamDto baseParamDto = new BaseParamDto();
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId(userModel.getLoginName());
        baseParam.setPhoneNum("15544443333");
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
        assertEquals(baseResponseDto.getData().getAddress(), "newTest");
        assertEquals(baseResponseDto.getData().getContact(), "newContract");
    }

    @Test
    public void shouldFindUserPointsOrdersIsOk() {
        UserModel userModel = getUserModelTest("testUserAddress");
        ProductModel productModel = getProductModel(GoodsType.VIRTUAL, 100, 0l);
        ProductOrderModel productOrderModel = getProductOrderModel(productModel.getId(), userModel.getLoginName());
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId(userModel.getLoginName());
        baseParam.setPhoneNum("13900000000");
        BaseParamDto baseParamDto = new BaseParamDto();
        baseParamDto.setBaseParam(baseParam);
        baseParamDto.setIndex(1);
        baseParamDto.setPageSize(10);
        BaseResponseDto<ProductListOrderResponseDto> pointsOrders = mobileAppPointShopService.findUserPointsOrders(baseParamDto);
        List<ProductOrderResponseDto> productOrderResponseDtoList = pointsOrders.getData().getOrders();
        assertTrue(CollectionUtils.isNotEmpty(productOrderResponseDtoList));
        assertEquals(productOrderResponseDtoList.get(0).getProductNum(), String.valueOf(productOrderModel.getNum()));
    }

    @Ignore
    public void shouldFindPointHomeIsOk() {
        String loginName = "findPointHomeUser";
        UserModel userModel = getUserModelTest(loginName);
        AccountModel accountModel = new AccountModel(loginName, "payUserId", "payAccountId", new Date());
        accountMapper.create(accountModel);

        increaseOrCreate(loginName, 1000L);
        ProductListRequestDto baseParamDto = new ProductListRequestDto();
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId(userModel.getLoginName());
        baseParam.setPhoneNum("13900000000");
        baseParamDto.setBaseParam(baseParam);
        getProductModel(GoodsType.COUPON, 100, 0L);
        getProductModel(GoodsType.PHYSICAL, 100, 0L);
        getProductModel(GoodsType.VIRTUAL, 100, 0L);
        BaseResponseDto<ProductListResponseDto> pointHome = mobileAppPointShopService.findPointHome(baseParamDto);
        assertEquals("1000", pointHome.getData().getMyPoints());
        assertTrue(pointHome.getData().getVirtuals().size() >= 2);
        assertTrue(pointHome.getData().getPhysicals().size() >= 1);
    }

    @Test
    public void shouldFindProductDetailIsOk() {
        String loginName = "findPointHomeUser1";
        getUserModelTest(loginName);
        ProductModel productModel = getProductModel(GoodsType.COUPON, 100, 0l);
        ProductDetailRequestDto productDetailRequestDto = new ProductDetailRequestDto();
        productDetailRequestDto.setProductId(String.valueOf(productModel.getId()));
        BaseResponseDto<ProductDetailResponseDto> baseResponseDto = mobileAppPointShopService.findProductDetail(productDetailRequestDto);
        assertEquals(baseResponseDto.getData().getProductId(), String.valueOf(productModel.getId()));
    }

    @Test
    public void shouldProductIdIsNullIsFault() {
        String loginName = "findPointHomeUser6";
        getUserModelTest(loginName);
        ProductDetailRequestDto productDetailRequestDto = new ProductDetailRequestDto();
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId(loginName);
        baseParam.setPhoneNum("13900000000");
        productDetailRequestDto.setBaseParam(baseParam);
        BaseResponseDto baseResponseDto = mobileAppPointShopService.productExchange(productDetailRequestDto);
        assertEquals(baseResponseDto.getCode(), ReturnMessage.POINTS_PRODUCT_IS_NOT_NULL.getCode());
    }

    @Test
    public void shouldProductNumIsNullIsFault() {
        String loginName = "findPointHomeUser7";
        getUserModelTest(loginName);
        ProductDetailRequestDto productDetailRequestDto = new ProductDetailRequestDto();
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId(loginName);
        baseParam.setPhoneNum("13900000000");
        productDetailRequestDto.setBaseParam(baseParam);
        productDetailRequestDto.setProductId("11");
        BaseResponseDto baseResponseDto = mobileAppPointShopService.productExchange(productDetailRequestDto);
        assertEquals(baseResponseDto.getCode(), ReturnMessage.POINTS_PRODUCT_NUM_IS_NOT_NULL.getCode());
    }

    @Test
    public void shouldProductCountIsZeroIsFault() {
        String loginName = "findPointHomeUser4";
        getUserModelTest(loginName);
        ProductModel productModel = getProductModel(GoodsType.COUPON, 0l, 0l);
        ProductDetailRequestDto productDetailRequestDto = new ProductDetailRequestDto();
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId(loginName);
        baseParam.setPhoneNum("13900000000");
        productDetailRequestDto.setBaseParam(baseParam);
        productDetailRequestDto.setProductId(String.valueOf(productModel.getId()));
        productDetailRequestDto.setNum(0);
        BaseResponseDto baseResponseDto = mobileAppPointShopService.productExchange(productDetailRequestDto);
        assertEquals(baseResponseDto.getCode(), ReturnMessage.INSUFFICIENT_PRODUCT_NUM.getCode());
    }

    @Test
    public void shouldMyPointsIsZeroIsFault() {
        String loginName = "findPointHomeUser3";
        getUserModelTest(loginName);
        ProductModel productModel = getProductModel(GoodsType.COUPON, 100, 0l);
        ProductDetailRequestDto productDetailRequestDto = new ProductDetailRequestDto();
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId(loginName);
        baseParam.setPhoneNum("13900000000");
        productDetailRequestDto.setBaseParam(baseParam);
        productDetailRequestDto.setProductId(String.valueOf(productModel.getId()));
        productDetailRequestDto.setNum(2);
        AccountModel accountModel = new AccountModel(loginName, "payUserId", "payAccountId", new Date());
        accountMapper.create(accountModel);
        increaseOrCreate(loginName, 10L);
        BaseResponseDto baseResponseDto = mobileAppPointShopService.productExchange(productDetailRequestDto);
        assertEquals(ReturnMessage.INSUFFICIENT_POINTS_BALANCE.getCode(), baseResponseDto.getCode());
    }

    @Test
    public void shouldMyAddressIsNullIsFault() {
        String loginName = "findPointHomeUser2";
        getUserModelTest(loginName);
        CouponModel couponModel = fakeCouponModel(loginName);
        couponMapper.create(couponModel);
        ProductModel productModel = getProductModel(GoodsType.COUPON, 100, couponModel.getId());
        ProductDetailRequestDto productDetailRequestDto = new ProductDetailRequestDto();
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId(loginName);
        baseParam.setPhoneNum("13900000000");
        productDetailRequestDto.setBaseParam(baseParam);
        productDetailRequestDto.setProductId(String.valueOf(productModel.getId()));
        productDetailRequestDto.setNum(2);
        AccountModel accountModel = new AccountModel(loginName, "payUserId", "payAccountId", new Date());
        accountMapper.create(accountModel);
        increaseOrCreate(loginName, 100000L);
        BaseResponseDto baseResponseDto = mobileAppPointShopService.productExchange(productDetailRequestDto);
        assertEquals(baseResponseDto.getCode(), ReturnMessage.SUCCESS.getCode());
    }

    @Test
    public void shouldProductExchangeIsOk() {
        String loginName = "findPointHomeUser5";
        UserModel userModel = getUserModelTest(loginName);
        CouponModel couponModel = fakeCouponModel(loginName);
        couponMapper.create(couponModel);
        ProductModel productModel = getProductModel(GoodsType.COUPON, 100, couponModel.getId());
        ProductDetailRequestDto productDetailRequestDto = new ProductDetailRequestDto();
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId(loginName);
        baseParam.setPhoneNum("13900000000");
        productDetailRequestDto.setBaseParam(baseParam);
        productDetailRequestDto.setProductId(String.valueOf(productModel.getId()));
        productDetailRequestDto.setNum(2);
        AccountModel accountModel = new AccountModel(loginName, "payUserId", "payAccountId", new Date());
        accountMapper.create(accountModel);
        increaseOrCreate(loginName, 1000L);
        UserAddressModel userAddressModel = new UserAddressModel(loginName, loginName, userModel.getMobile(), "", loginName);
        userAddressMapper.create(userAddressModel);
        BaseResponseDto baseResponseDto = mobileAppPointShopService.productExchange(productDetailRequestDto);
        List<ProductOrderModel> productOrderModels = productOrderMapper.findProductOrderList(productModel.getId(), loginName, 0, 10);
        List<UserCouponModel> userCouponModels = userCouponMapper.findByCouponId(couponModel.getId());
        assertEquals(baseResponseDto.getCode(), ReturnMessage.SUCCESS.getCode());
        assertEquals(productOrderModels.get(0).getProductId(), productModel.getId());
        assertEquals(userCouponModels.get(0).getCouponId(), couponModel.getId());
    }


    private CouponModel fakeCouponModel(String loginName) {
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

    private ProductModel getProductModel(GoodsType goodsType, long totalCount, long couponId) {
        ProductModel productModel = new ProductModel("test", goodsType, 0, "50元充值卡", 1, "upload/images/11.png", "50yuan", totalCount, 0, 200, new Date(), new DateTime().plusDays(7).toDate());

        productModel.setCouponId(couponId);
        productModel.setActive(true);
        productMapper.create(productModel);
        return productModel;
    }

    private ProductOrderModel getProductOrderModel(long productId, String loginName) {
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
        userModelTest.setUserName("userName");
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(userModelTest);
        return userModelTest;
    }

    private UserPointModel increaseOrCreate(String loginName, long sudaiPoint) {
        UserPointModel model = new UserPointModel();
        model.setLoginName(loginName);
        model.setChannel(null);
        model.setChannelPoint(0);
        model.setSudaiPoint(sudaiPoint);
        model.setPoint(sudaiPoint);
        model.setUpdatedTime(new Date());
        userPointMapper.create(model);
        return model;
    }
}
