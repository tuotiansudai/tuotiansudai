package com.tuotiansudai.api.service.v1_0.impl;


import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppPointShopService;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.ExchangeCouponView;
import com.tuotiansudai.coupon.service.CouponAssignmentService;
import com.tuotiansudai.point.repository.mapper.PointBillMapper;
import com.tuotiansudai.point.repository.mapper.ProductMapper;
import com.tuotiansudai.point.repository.mapper.ProductOrderMapper;
import com.tuotiansudai.point.repository.mapper.UserAddressMapper;
import com.tuotiansudai.point.repository.model.*;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.AccountModel;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;

@Service
public class MobileAppPointShopServiceImpl implements MobileAppPointShopService {

    static Logger logger = Logger.getLogger(MobileAppPointShopServiceImpl.class);

    @Autowired
    private UserAddressMapper userAddressMapper;

    @Autowired
    private ProductOrderMapper productOrderMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private CouponAssignmentService couponAssignmentService;

    @Autowired
    private PointBillMapper pointBillMapper;

    @Value("${web.banner.server}")
    private String bannerServer;

    @Override
    public BaseResponseDto updateUserAddress(UserAddressRequestDto userAddressRequestDto) {
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        List<UserAddressModel> byLoginName = userAddressMapper.findByLoginName(userAddressRequestDto.getBaseParam().getUserId());
        if (CollectionUtils.isNotEmpty(byLoginName)) {
            UserAddressModel userAddressModel = convertUserAddressModel(userAddressRequestDto);
            userAddressModel.setId(byLoginName.get(0).getId());
            userAddressMapper.update(userAddressModel);
        } else {
            userAddressMapper.create(convertUserAddressModel(userAddressRequestDto));
        }
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        return baseResponseDto;
    }

    @Override
    public BaseResponseDto findUserAddressResponseDto(BaseParamDto baseParamDto) {
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        List<UserAddressModel> byLoginName = userAddressMapper.findByLoginName(baseParamDto.getBaseParam().getUserId());
        if (CollectionUtils.isNotEmpty(byLoginName)) {
            baseResponseDto.setData(new UserAddressResponseDto(byLoginName.get(0)));
        }
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        return baseResponseDto;
    }

    @Override
    public BaseResponseDto findUserPointsOrders(BaseParamDto baseParamDto) {
        Integer index = baseParamDto.getIndex();
        Integer pageSize = baseParamDto.getPageSize();
        if (index == null || pageSize == null || index <= 0 || pageSize <= 0) {
            index = 0;
            pageSize = 10;
        }
        index = (index - 1) * pageSize;
        List<ProductOrderViewDto> productOrderListByLoginName = productOrderMapper.findProductOrderListByLoginName(baseParamDto.getBaseParam().getUserId(), index, pageSize);
        ProductListOrderResponseDto productListOrderResponseDto = new ProductListOrderResponseDto();
        if (CollectionUtils.isNotEmpty(productOrderListByLoginName)) {
            Iterator<ProductOrderResponseDto> transform = Iterators.transform(productOrderListByLoginName.iterator(), new Function<ProductOrderViewDto, ProductOrderResponseDto>() {
                @Override
                public ProductOrderResponseDto apply(ProductOrderViewDto input) {
                    return new ProductOrderResponseDto(input);
                }
            });
            productListOrderResponseDto.setOrders(Lists.newArrayList(transform));
            productListOrderResponseDto.setIndex(baseParamDto.getIndex());
            productListOrderResponseDto.setPageSize(baseParamDto.getPageSize());
            productListOrderResponseDto.setTotalCount(Integer.parseInt(String.valueOf(productOrderMapper.findProductOrderListByLoginNameCount(baseParamDto.getBaseParam().getUserId()))));
        }
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseResponseDto.setData(productListOrderResponseDto);
        baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        return baseResponseDto;
    }

    @Override
    public BaseResponseDto findPointHome(BaseParamDto baseParamDto) {
        List<ExchangeCouponView> exchangeCoupons = couponMapper.findExchangeableCouponViews(0, Integer.MAX_VALUE);
        List<ProductModel> virtualProducts = productMapper.findAllProductsByGoodsType(Lists.newArrayList(GoodsType.VIRTUAL));
        List<ProductModel> physicalsProducts = productMapper.findAllProductsByGoodsType(Lists.newArrayList(GoodsType.PHYSICAL));

        Iterator<ProductDetailResponseDto> couponList = Iterators.transform(exchangeCoupons.iterator(), new Function<ExchangeCouponView, ProductDetailResponseDto>() {
            @Override
            public ProductDetailResponseDto apply(ExchangeCouponView exchangeCouponView) {
                return new ProductDetailResponseDto(exchangeCouponView, bannerServer);
            }
        });

        Iterator<ProductDetailResponseDto> virtualList = Iterators.transform(virtualProducts.iterator(), new Function<ProductModel, ProductDetailResponseDto>() {
            @Override
            public ProductDetailResponseDto apply(ProductModel input) {
                return new ProductDetailResponseDto(input.getId(), bannerServer + input.getImageUrl(), input.getName(), input.getPoints(), input.getType(), 1000);
            }
        });

        Iterator<ProductDetailResponseDto> physicals = Iterators.transform(physicalsProducts.iterator(), new Function<ProductModel, ProductDetailResponseDto>() {
            @Override
            public ProductDetailResponseDto apply(ProductModel input) {
                return new ProductDetailResponseDto(input.getId(), bannerServer + input.getImageUrl(), input.getName(), input.getPoints(), input.getType(), 1000);
            }
        });

        List virtualShopList = Lists.newArrayList(virtualList);
        if (couponList != null) {
            virtualShopList.addAll(Lists.newArrayList(couponList));
        }
        AccountModel accountModel = accountMapper.findByLoginName(baseParamDto.getBaseParam().getUserId());
        ProductListResponseDto productListResponseDto = new ProductListResponseDto();
        productListResponseDto.setVirtuals(virtualShopList);
        productListResponseDto.setPhysicals(Lists.newArrayList(physicals));
        productListResponseDto.setMyPoints(accountModel != null ? String.valueOf(accountModel.getPoint()) : "0");
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        baseResponseDto.setData(productListResponseDto);
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        return baseResponseDto;
    }

    @Override
    public BaseResponseDto findProductDetail(ProductDetailRequestDto productDetailRequestDto) {
        if (productDetailRequestDto.getProductId() == null) {
            logger.info(MessageFormat.format("Product id is null (userId = {0})", productDetailRequestDto.getBaseParam().getUserId()));
            return new BaseResponseDto<>(ReturnMessage.POINTS_PRODUCT_IS_NOT_NULL.getCode(), ReturnMessage.POINTS_PRODUCT_IS_NOT_NULL.getMsg());
        }

        ProductModel productModel = productMapper.findById(Long.parseLong(productDetailRequestDto.getProductId()));

        ProductDetailResponseDto productDetailResponseDto = new ProductDetailResponseDto(productModel.getId(), bannerServer + productModel.getImageUrl(), productModel.getName(), productModel.getPoints(), productModel.getType(), productModel.getTotalCount() - productModel.getUsedCount());
        List<String> description = Lists.newArrayList();
        CouponModel couponModel = couponMapper.findById(productModel.getCouponId());
        if (productModel.getType() == GoodsType.COUPON && couponModel != null) {
            description.add(couponModel.getAmount() > 0 ? MessageFormat.format("投资满{0}元即可使用;", couponModel.getInvestLowerLimit()/100) : "");
            description.add(MessageFormat.format("{0}天产品可用;", couponModel.getProductTypes().toString().replaceAll("_", "")));
            description.add(MessageFormat.format("有效期限:{0}天。", couponModel.getDeadline()));
        } else {
            description.add(productModel.getDescription());
        }
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        productDetailResponseDto.setProductDes(Lists.newArrayList(description));
        baseResponseDto.setData(productDetailResponseDto);
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        return baseResponseDto;
    }

    @Override
    @Transactional
    public BaseResponseDto productExchange(ProductDetailRequestDto productDetailRequestDto) {
        if (Strings.isNullOrEmpty(productDetailRequestDto.getProductId())) {
            logger.info(MessageFormat.format("Product id is null (userId = {0})", productDetailRequestDto.getBaseParam().getUserId()));
            return new BaseResponseDto<>(ReturnMessage.POINTS_PRODUCT_IS_NOT_NULL.getCode(), ReturnMessage.POINTS_PRODUCT_IS_NOT_NULL.getMsg());
        }

        if (productDetailRequestDto.getNum() == null) {
            logger.info(MessageFormat.format("Product num is null (userId = {0})", productDetailRequestDto.getBaseParam().getUserId()));
            return new BaseResponseDto<>(ReturnMessage.POINTS_PRODUCT_NUM_IS_NOT_NULL.getCode(), ReturnMessage.POINTS_PRODUCT_NUM_IS_NOT_NULL.getMsg());
        }

        ProductModel productModel = productMapper.lockById(Long.parseLong(productDetailRequestDto.getProductId().trim()));
        AccountModel accountModel = accountMapper.lockByLoginName(productDetailRequestDto.getBaseParam().getUserId());

        if ((productDetailRequestDto.getNum() + productModel.getUsedCount()) > productModel.getTotalCount()) {
            logger.info(MessageFormat.format("Insufficient product (userId = {0},totalCount = {1},usedCount = {2})", productDetailRequestDto.getBaseParam().getUserId(), productModel.getTotalCount(), productModel.getUsedCount()));
            return new BaseResponseDto<>(ReturnMessage.INSUFFICIENT_PRODUCT_NUM.getCode(), ReturnMessage.INSUFFICIENT_PRODUCT_NUM.getMsg());
        }

        long points = productModel.getPoints() * productDetailRequestDto.getNum();
        if (accountModel == null || accountModel.getPoint() < points) {
            logger.info(MessageFormat.format("Insufficient points (userId = {0},productPoints = {2})", productDetailRequestDto.getBaseParam().getUserId(), points));
            return new BaseResponseDto<>(ReturnMessage.INSUFFICIENT_POINTS_BALANCE.getCode(), ReturnMessage.INSUFFICIENT_POINTS_BALANCE.getMsg());
        }

        List<UserAddressModel> userAddressModels = userAddressMapper.findByLoginName(productDetailRequestDto.getBaseParam().getUserId());
        if (CollectionUtils.isEmpty(userAddressModels) && productModel.getType().equals(GoodsType.PHYSICAL)) {
            logger.info(MessageFormat.format("Insufficient points (userId = {0},myPoints = {1},productPoints = {2})", productDetailRequestDto.getBaseParam().getUserId(), accountModel.getPoint(), points));
            return new BaseResponseDto<>(ReturnMessage.USER_ADDRESS_IS_NOT_NULL.getCode(), ReturnMessage.USER_ADDRESS_IS_NOT_NULL.getMsg());
        }

        return buyProduct(productModel, userAddressModels, accountModel, productDetailRequestDto.getBaseParam().getPhoneNum(), productDetailRequestDto.getBaseParam().getUserId(), productDetailRequestDto.getNum(), points);
    }

    @Transactional
    private BaseResponseDto buyProduct(ProductModel productModel, List<UserAddressModel> userAddressModels, AccountModel accountModel, String mobile, String loginName, int num, long points) {
        UserAddressModel userAddressModel;
        if (productModel.getType().equals(GoodsType.PHYSICAL)) {
            userAddressModel = userAddressModels.get(0);
        } else {
            userAddressModel = new UserAddressModel(loginName, loginName, mobile, "", loginName);
        }

        ProductOrderModel productOrderModel = new ProductOrderModel(productModel.getId(), productModel.getPoints(), num, points, userAddressModel.getContact(), userAddressModel.getMobile(), userAddressModel.getAddress(), false, null, loginName);
        productOrderMapper.create(productOrderModel);

        accountModel.setPoint(accountModel.getPoint() - points);
        accountMapper.update(accountModel);

        if (productModel.getType().equals(GoodsType.COUPON)) {
            for(int i = 0; i < num; i++){
                couponAssignmentService.assignUserCoupon(loginName, productModel.getCouponId());
            }
        }

        productModel.setUsedCount(productModel.getUsedCount() + num);
        productMapper.update(productModel);

        pointBillMapper.create(new PointBillModel(loginName, productModel.getId(), (-points), PointBusinessType.EXCHANGE, ""));
        return new BaseResponseDto<>(ReturnMessage.SUCCESS.getCode(), ReturnMessage.SUCCESS.getMsg());
    }

    private UserAddressModel convertUserAddressModel(UserAddressRequestDto userAddressRequestDto) {
        return new UserAddressModel(userAddressRequestDto.getBaseParam().getUserId(),
                userAddressRequestDto.getContact(),
                userAddressRequestDto.getMobile(),
                userAddressRequestDto.getAddress(),
                userAddressRequestDto.getBaseParam().getUserId());
    }
}
