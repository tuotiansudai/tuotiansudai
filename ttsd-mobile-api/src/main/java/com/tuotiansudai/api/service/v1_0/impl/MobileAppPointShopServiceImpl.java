package com.tuotiansudai.api.service.v1_0.impl;


import com.google.common.base.Function;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppPointShopService;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.service.CouponAssignmentService;
import com.tuotiansudai.point.repository.mapper.ProductMapper;
import com.tuotiansudai.point.repository.mapper.ProductOrderMapper;
import com.tuotiansudai.point.repository.mapper.UserAddressMapper;
import com.tuotiansudai.point.repository.model.*;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.AccountModel;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public BaseResponseDto updateUserAddress(UserAddressRequestDto userAddressRequestDto) {
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        List<UserAddressModel> byLoginName = userAddressMapper.findByLoginName(userAddressRequestDto.getBaseParam().getUserId());
        if (CollectionUtils.isNotEmpty(byLoginName)) {
            UserAddressModel userAddressModel = convertUserAddressModel(userAddressRequestDto);
            userAddressModel.setId(byLoginName.get(0).getId());
            userAddressMapper.update(userAddressModel);
        } else {
            userAddressMapper.create(convertUserAddressModel(userAddressRequestDto));
        }
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
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
        return baseResponseDto;
    }

    @Override
    public BaseResponseDto findUserPointsOrders(BaseParamDto baseParamDto) {
        Integer index = baseParamDto.getIndex();
        Integer pageSize = baseParamDto.getPageSize();
        if (index == null || pageSize == null || index <= 0 || pageSize <= 0) {
            return new BaseResponseDto<>(ReturnMessage.REQUEST_PARAM_IS_WRONG.getCode(), ReturnMessage.REQUEST_PARAM_IS_WRONG.getMsg());
        }
        index = (baseParamDto.getIndex() - 1) * pageSize;
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
            productListOrderResponseDto.setTotalCount((int) productOrderMapper.findProductOrderListByLoginNameCount(baseParamDto.getBaseParam().getUserId()));
        }
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseResponseDto.setData(productListOrderResponseDto);
        return baseResponseDto;
    }

    @Override
    public BaseResponseDto findPointHome(BaseParamDto baseParamDto) {
        List<ProductModel> virtualProducts = productMapper.findAllProductsByGoodsType(Lists.newArrayList(GoodsType.COUPON, GoodsType.VIRTUAL), 0, 4);
        List<ProductModel> physicalsProducts = productMapper.findAllProductsByGoodsType(Lists.newArrayList(GoodsType.PHYSICAL), 0, 4);

        Iterator<ProductDetailResponseDto> virtualList = Iterators.transform(virtualProducts.iterator(), new Function<ProductModel, ProductDetailResponseDto>() {
            @Override
            public ProductDetailResponseDto apply(ProductModel input) {
                return new ProductDetailResponseDto(String.valueOf(input), input.getImageUrl(), input.getName(), String.valueOf(input.getPoints()));
            }
        });

        Iterator<ProductDetailResponseDto> physicals = Iterators.transform(physicalsProducts.iterator(), new Function<ProductModel, ProductDetailResponseDto>() {
            @Override
            public ProductDetailResponseDto apply(ProductModel input) {
                return new ProductDetailResponseDto(String.valueOf(input), input.getImageUrl(), input.getName(), String.valueOf(input.getPoints()));
            }
        });

        AccountModel accountModel = accountMapper.findByLoginName(baseParamDto.getBaseParam().getUserId());

        ProductListResponseDto productListResponseDto = new ProductListResponseDto();
        productListResponseDto.setVirtuals(Lists.newArrayList(virtualList));
        productListResponseDto.setPhysicals(Lists.newArrayList(physicals));
        productListResponseDto.setMyPoints(accountModel != null ? String.valueOf(accountModel.getPoint()) : "0");
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        baseResponseDto.setData(productListResponseDto);
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        return baseResponseDto;
    }

    @Override
    public BaseResponseDto findProductDetail(ProductDetailRequestDto productDetailRequestDto) {
        BaseResponseDto baseResponseDto = new BaseResponseDto();

        if (productDetailRequestDto.getProductId() == null) {
            logger.error(MessageFormat.format("Product id is null (userId = {0})", productDetailRequestDto.getBaseParam().getUserId()));
            baseResponseDto.setCode(ReturnMessage.POINTS_PRODUCT_IS_NOT_NULL.getCode());
            baseResponseDto.setMessage(ReturnMessage.POINTS_PRODUCT_IS_NOT_NULL.getMsg());
            return baseResponseDto;
        }

        ProductModel productModel = productMapper.findById(Long.parseLong(productDetailRequestDto.getProductId()));

        ProductDetailResponseDto productDetailResponseDto = new ProductDetailResponseDto(productModel);
        String[] description;
        CouponModel couponModel = couponMapper.findById(productModel.getId());
        if (productModel.getType() == GoodsType.COUPON && couponModel != null) {
            description = new String[]{couponModel.getAmount() > 0 ? MessageFormat.format("投资满{0}元即可使用;", couponModel.getAmount()) : "",
                    MessageFormat.format("{0}天产品可用;", couponModel.getProductTypes().toString()),
                    MessageFormat.format("有效期限:{0}天", couponModel.getDeadline())};
        } else {
            description = new String[]{productModel.getDescription()};
        }
        productDetailResponseDto.setProductDes(Lists.newArrayList(description));
        baseResponseDto.setData(productDetailResponseDto);
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        return baseResponseDto;
    }

    @Override
    public BaseResponseDto productExchange(ProductDetailRequestDto productDetailRequestDto) {
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        if (productDetailRequestDto.getProductId() == null) {
            logger.error(MessageFormat.format("Product id is null (userId = {0})", productDetailRequestDto.getBaseParam().getUserId()));
            baseResponseDto.setCode(ReturnMessage.POINTS_PRODUCT_IS_NOT_NULL.getCode());
            baseResponseDto.setMessage(ReturnMessage.POINTS_PRODUCT_IS_NOT_NULL.getMsg());
            return baseResponseDto;
        }

        if (productDetailRequestDto.getNum() == null) {
            logger.error(MessageFormat.format("Product num is null (userId = {0})", productDetailRequestDto.getBaseParam().getUserId()));
            baseResponseDto.setCode(ReturnMessage.POINTS_PRODUCT_NUM_IS_NOT_NULL.getCode());
            baseResponseDto.setMessage(ReturnMessage.POINTS_PRODUCT_NUM_IS_NOT_NULL.getMsg());
            return baseResponseDto;
        }

        ProductModel productModel = productMapper.findById(Long.parseLong(productDetailRequestDto.getProductId().trim()));

        if ((productModel.getTotalCount() - productModel.getUsedCount()) <= 0) {
            logger.error(MessageFormat.format("Insufficient product (userId = {0},totalCount = {1},usedCount = {2})", productDetailRequestDto.getBaseParam().getUserId(), productModel.getTotalCount(), productModel.getUsedCount()));
            baseResponseDto.setCode(ReturnMessage.INSUFFICIENT_PRODUCT_NUM.getCode());
            baseResponseDto.setMessage(ReturnMessage.INSUFFICIENT_PRODUCT_NUM.getMsg());
            return baseResponseDto;
        }

        long points = productModel.getPoints() * productDetailRequestDto.getNum();
        AccountModel accountModel = accountMapper.findByLoginName(productDetailRequestDto.getBaseParam().getUserId());
        if (accountModel.getPoint() < points) {
            logger.error(MessageFormat.format("Insufficient points (userId = {0},myPoints = {1},productPoints = {2})", productDetailRequestDto.getBaseParam().getUserId(), accountModel.getPoint(), points));
            baseResponseDto.setCode(ReturnMessage.INSUFFICIENT_POINTS_BALANCE.getCode());
            baseResponseDto.setMessage(ReturnMessage.INSUFFICIENT_POINTS_BALANCE.getMsg());
            return baseResponseDto;
        }


        List<UserAddressModel> userAddressModels = userAddressMapper.findByLoginName(productDetailRequestDto.getBaseParam().getUserId());
        if (CollectionUtils.isEmpty(userAddressModels)) {
            logger.error(MessageFormat.format("Insufficient points (userId = {0},myPoints = {1},productPoints = {2})", productDetailRequestDto.getBaseParam().getUserId(), accountModel.getPoint(), points));
            baseResponseDto.setCode(ReturnMessage.USER_ADDRESS_IS_NOT_NULL.getCode());
            baseResponseDto.setMessage(ReturnMessage.USER_ADDRESS_IS_NOT_NULL.getMsg());
            return baseResponseDto;
        }

        UserAddressModel userAddressModel = userAddressModels.get(0);
        ProductOrderModel productOrderModel = new ProductOrderModel(productModel.getId(), productModel.getPoints(), productDetailRequestDto.getNum(), points, userAddressModel.getContact(), userAddressModel.getMobile(), userAddressModel.getAddress(), false, null, productDetailRequestDto.getBaseParam().getUserId());
        productOrderMapper.create(productOrderModel);

        accountModel.setPoint(accountModel.getPoint() - points);
        accountMapper.update(accountModel);

        if(productModel.getType().equals(GoodsType.COUPON)){
            couponAssignmentService.assignUserCoupon(productDetailRequestDto.getBaseParam().getUserId(),productModel.getCouponId());
        }

        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        return baseResponseDto;
    }

    private UserAddressModel convertUserAddressModel(UserAddressRequestDto userAddressRequestDto) {
        return new UserAddressModel(userAddressRequestDto.getBaseParam().getUserId(),
                userAddressRequestDto.getContract(),
                userAddressRequestDto.getMobile(),
                userAddressRequestDto.getAddress(),
                userAddressRequestDto.getBaseParam().getUserId());
    }
}
