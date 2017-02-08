package com.tuotiansudai.api.service.v1_0.impl;


import com.google.common.base.Strings;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppPointShopService;
import com.tuotiansudai.api.util.PageValidUtils;
import com.tuotiansudai.repository.mapper.CouponMapper;
import com.tuotiansudai.repository.model.CouponModel;
import com.tuotiansudai.repository.model.ExchangeCouponView;
import com.tuotiansudai.coupon.service.CouponService;
import com.tuotiansudai.point.repository.mapper.ProductMapper;
import com.tuotiansudai.point.repository.mapper.ProductOrderMapper;
import com.tuotiansudai.point.repository.mapper.UserAddressMapper;
import com.tuotiansudai.point.repository.model.GoodsType;
import com.tuotiansudai.point.repository.model.ProductModel;
import com.tuotiansudai.point.repository.model.ProductOrderViewDto;
import com.tuotiansudai.point.repository.model.UserAddressModel;
import com.tuotiansudai.point.service.ProductService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.AccountModel;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

@Service
public class MobileAppPointShopServiceImpl implements MobileAppPointShopService {

    static Logger logger = Logger.getLogger(MobileAppPointShopServiceImpl.class);

    @Autowired
    private UserAddressMapper userAddressMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private ProductOrderMapper productOrderMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private CouponService couponService;

    @Value("${mobile.static.server}")
    private String bannerServer;

    @Autowired
    private ProductService productService;

    @Autowired
    private PageValidUtils pageValidUtils;

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
    public BaseResponseDto<UserAddressResponseDto> findUserAddressResponseDto(BaseParamDto baseParamDto) {
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
    public BaseResponseDto<ProductListOrderResponseDto> findUserPointsOrders(BaseParamDto baseParamDto) {
        Integer index = baseParamDto.getIndex();
        Integer pageSize = baseParamDto.getPageSize();
        if (index == null || index <= 0) {
            index = 1;
        }

        pageSize = pageValidUtils.validPageSizeLimit(pageSize);

        index = (index - 1) * pageSize;
        List<ProductOrderViewDto> productOrderListByLoginName = productOrderMapper.findProductOrderListByLoginName(baseParamDto.getBaseParam().getUserId(), index, pageSize);
        ProductListOrderResponseDto productListOrderResponseDto = new ProductListOrderResponseDto();
        if (CollectionUtils.isNotEmpty(productOrderListByLoginName)) {
            Iterator<ProductOrderResponseDto> transform = Iterators.transform(productOrderListByLoginName.iterator(), input -> new ProductOrderResponseDto(input));
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
    public BaseResponseDto<ProductListResponseDto> findPointHome(BaseParamDto baseParamDto) {
        List<ExchangeCouponView> exchangeCoupons = Lists.newArrayList();

        List<ProductModel> couponProducts = productMapper.findAllProductsByGoodsType(Lists.newArrayList(GoodsType.COUPON));
        for (ProductModel productModel : couponProducts) {
            CouponModel couponModel = couponMapper.findById(productModel.getCouponId());
            ExchangeCouponView exchangeCouponView = new ExchangeCouponView(productModel.getPoints(), productModel.getSeq(), productModel.getImageUrl(), productModel.getId(), couponModel);
            exchangeCoupons.add(exchangeCouponView);
        }

        List<ProductModel> virtualProducts = productMapper.findAllProductsByGoodsType(Lists.newArrayList(GoodsType.VIRTUAL));
        List<ProductModel> physicalsProducts = productMapper.findAllProductsByGoodsType(Lists.newArrayList(GoodsType.PHYSICAL));

        Iterator<ProductDetailResponseDto> couponList = Iterators.transform(exchangeCoupons.iterator(), exchangeCouponView -> new ProductDetailResponseDto(exchangeCouponView, bannerServer));

        Iterator<ProductDetailResponseDto> virtualList = Iterators.transform(virtualProducts.iterator(), input -> new ProductDetailResponseDto(input.getId(), bannerServer + input.getImageUrl(), input.getName(), input.getPoints(), input.getType(), 1000, input.getSeq(), input.getUpdatedTime()));

        Iterator<ProductDetailResponseDto> physicals = Iterators.transform(physicalsProducts.iterator(), input -> new ProductDetailResponseDto(input.getId(), bannerServer + input.getImageUrl(), input.getName(), input.getPoints(), input.getType(), 1000, input.getSeq(), input.getUpdatedTime()));

        List virtualShopList = Lists.newArrayList(virtualList);
        if (couponList != null) {
            virtualShopList.addAll(Lists.newArrayList(couponList));
        }

        Collections.sort(virtualShopList, new Comparator<ProductDetailResponseDto>() {
            @Override
            public int compare(ProductDetailResponseDto o1, ProductDetailResponseDto o2) {
                int result = 0;
                if (o1.getSeq() > o2.getSeq()) {
                    result = 1;
                } else if (o1.getSeq() < o2.getSeq()) {
                    result = -1;
                }
                if (0 == result && null != o1.getUpdatedTime() && null != o2.getUpdatedTime()) {
                    return o2.getUpdatedTime().compareTo(o1.getUpdatedTime());
                } else {
                    return result;
                }
            }
        });

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
    public BaseResponseDto<ProductDetailResponseDto> findProductDetail(ProductDetailRequestDto productDetailRequestDto) {
        if (productDetailRequestDto.getProductId() == null) {
            logger.info(MessageFormat.format("Product id is null (userId = {0})", productDetailRequestDto.getBaseParam().getUserId()));
            return new BaseResponseDto(ReturnMessage.POINTS_PRODUCT_IS_NOT_NULL.getCode(), ReturnMessage.POINTS_PRODUCT_IS_NOT_NULL.getMsg());
        }

        ProductModel productModel = productMapper.findById(Long.parseLong(productDetailRequestDto.getProductId()));

        ProductDetailResponseDto productDetailResponseDto = new ProductDetailResponseDto(productModel.getId(), bannerServer + productModel.getImageUrl(), productModel.getName(), productModel.getPoints(), productModel.getType(), productModel.getTotalCount() - productModel.getUsedCount(), productModel.getSeq(), productModel.getUpdatedTime());
        if (productModel.getType().equals(GoodsType.COUPON)) {
            CouponModel couponModel = couponService.findExchangeableCouponById(productModel.getCouponId());
            ExchangeCouponView exchangeCouponView = new ExchangeCouponView(productModel.getPoints(), productModel.getSeq(), productModel.getImageUrl(), productModel.getId(), couponModel);
            productDetailResponseDto.setLeftCount(exchangeCouponView != null ? String.valueOf(exchangeCouponView.getCouponModel() != null ? (exchangeCouponView.getCouponModel().getTotalCount() - exchangeCouponView.getCouponModel().getIssuedCount()) : "0") : String.valueOf(productModel.getTotalCount()));
        }
        List<String> description = Lists.newArrayList();
        CouponModel couponModel = couponMapper.findById(productModel.getCouponId());
        if (productModel.getType() == GoodsType.COUPON && couponModel != null) {
            description.addAll(productService.getProductDescription(couponModel.getInvestLowerLimit(), couponModel.getProductTypes(), couponModel.getDeadline()));
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
            return new BaseResponseDto(ReturnMessage.POINTS_PRODUCT_IS_NOT_NULL.getCode(), ReturnMessage.POINTS_PRODUCT_IS_NOT_NULL.getMsg());
        }

        if (productDetailRequestDto.getNum() == null) {
            logger.info(MessageFormat.format("Product num is null (userId = {0})", productDetailRequestDto.getBaseParam().getUserId()));
            return new BaseResponseDto(ReturnMessage.POINTS_PRODUCT_NUM_IS_NOT_NULL.getCode(), ReturnMessage.POINTS_PRODUCT_NUM_IS_NOT_NULL.getMsg());
        }

        ProductModel productModel = productMapper.findById(Long.parseLong(productDetailRequestDto.getProductId().trim()));
        AccountModel accountModel = accountMapper.findByLoginName(productDetailRequestDto.getBaseParam().getUserId());
        long leftCount = productDetailRequestDto.getNum() + productModel.getUsedCount();
        if (productModel.getType().equals(GoodsType.COUPON)) {
            CouponModel couponModel = couponService.findExchangeableCouponById(productModel.getCouponId());
            ExchangeCouponView exchangeCouponView = new ExchangeCouponView(productModel.getPoints(), productModel.getSeq(), productModel.getImageUrl(), productModel.getId(), couponModel);
            leftCount = productDetailRequestDto.getNum() + (exchangeCouponView.getCouponModel() != null ? exchangeCouponView.getCouponModel().getIssuedCount() : 0l);
        }
        if (leftCount > productModel.getTotalCount()) {
            logger.info(MessageFormat.format("Insufficient product (userId = {0},totalCount = {1},usedCount = {2})", productDetailRequestDto.getBaseParam().getUserId(), productModel.getTotalCount(), productModel.getUsedCount()));
            return new BaseResponseDto(ReturnMessage.INSUFFICIENT_PRODUCT_NUM.getCode(), ReturnMessage.INSUFFICIENT_PRODUCT_NUM.getMsg());
        }

        long points = productModel.getPoints() * productDetailRequestDto.getNum();
        if (accountModel == null || accountModel.getPoint() < points) {
            logger.info(MessageFormat.format("Insufficient points (userId = {0},productPoints = {2})", productDetailRequestDto.getBaseParam().getUserId(), points));
            return new BaseResponseDto(ReturnMessage.INSUFFICIENT_POINTS_BALANCE.getCode(), ReturnMessage.INSUFFICIENT_POINTS_BALANCE.getMsg());
        }

        List<UserAddressModel> userAddressModels = userAddressMapper.findByLoginName(productDetailRequestDto.getBaseParam().getUserId());
        if (CollectionUtils.isEmpty(userAddressModels) && productModel.getType().equals(GoodsType.PHYSICAL)) {
            logger.info(MessageFormat.format("Insufficient points (userId = {0},myPoints = {1},productPoints = {2})", productDetailRequestDto.getBaseParam().getUserId(), accountModel.getPoint(), points));
            return new BaseResponseDto(ReturnMessage.USER_ADDRESS_IS_NOT_NULL.getCode(), ReturnMessage.USER_ADDRESS_IS_NOT_NULL.getMsg());
        }

        productService.buyProduct(accountModel.getLoginName(), Long.parseLong(productDetailRequestDto.getProductId()), productModel.getType(), productDetailRequestDto.getNum(), CollectionUtils.isEmpty(userAddressModels) ? null : userAddressModels.get(0).getId());
        return new BaseResponseDto(ReturnMessage.SUCCESS.getCode(), ReturnMessage.SUCCESS.getMsg());
    }

    private UserAddressModel convertUserAddressModel(UserAddressRequestDto userAddressRequestDto) {
        return new UserAddressModel(userAddressRequestDto.getBaseParam().getUserId(),
                userAddressRequestDto.getContact(),
                userAddressRequestDto.getMobile(),
                userAddressRequestDto.getAddress(),
                userAddressRequestDto.getBaseParam().getUserId());
    }
}
