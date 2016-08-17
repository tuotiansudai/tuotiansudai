package com.tuotiansudai.point.service.impl;


import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.model.ExchangeCouponView;
import com.tuotiansudai.coupon.service.CouponAssignmentService;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.point.dto.ProductDto;
import com.tuotiansudai.point.dto.ProductShowItemDto;
import com.tuotiansudai.point.repository.mapper.ProductMapper;
import com.tuotiansudai.point.repository.mapper.ProductOrderMapper;
import com.tuotiansudai.point.repository.mapper.UserAddressMapper;
import com.tuotiansudai.point.repository.model.*;
import com.tuotiansudai.point.service.ProductService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.UserModel;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.*;

@Service
public class ProductServiceImpl implements ProductService {

    private static Logger logger = Logger.getLogger(ProductServiceImpl.class);

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private ProductOrderMapper productOrderMapper;

    @Autowired
    private UserAddressMapper userAddressMapper;

    @Autowired
    private CouponAssignmentService couponAssignmentService;

    @Override
    public void createProduct(ProductDto productDto) {
        ProductModel productModel = new ProductModel();
        productModel.setGoodsType(productDto.getGoodsType());
        productModel.setProductName(productDto.getProductName());
        productModel.setSeq(productDto.getSeq());
        productModel.setImageUrl(productDto.getImageUrl());
        productModel.setDescription(productDto.getDescription());
        productModel.setTotalCount(productDto.getTotalCount());
        productModel.setProductPrice(productDto.getProductPrice());
        productModel.setStartTime(productDto.getStartTime());
        productModel.setEndTime(productDto.getEndTime());
        productModel.setCreatedBy(productDto.getLoginName());
        productModel.setCreatedTime(new Date());
        productModel.setUpdatedBy(productDto.getLoginName());
        productModel.setUpdatedTime(new Date());
        productMapper.create(productModel);
    }

    @Override
    public List<ProductModel> findGoods(GoodsType goodsType) {
        return productMapper.findProductList(goodsType);
    }

    @Override
    public long findGoodsCount(GoodsType goodsType) {
        return productMapper.findProductCount(goodsType);
    }

    private List<ProductShowItemDto> findAllProductsByGoodsType(GoodsType goodsType) {
        List<ProductModel> productModels = null;
        List<ProductShowItemDto> productShowItemDtos = new ArrayList<>();
        switch (goodsType) {
            case VIRTUAL:
                productModels = productMapper.findProductList(GoodsType.VIRTUAL);
                productShowItemDtos = Lists.transform(productModels, new Function<ProductModel, ProductShowItemDto>() {
                    @Override
                    public ProductShowItemDto apply(ProductModel productModel) {
                        return new ProductShowItemDto(productModel, ItemType.VIRTUAL, "");
                    }
                });
                break;
            case PHYSICAL:
                productModels = productMapper.findProductList(GoodsType.PHYSICAL);
                productShowItemDtos = Lists.transform(productModels, new Function<ProductModel, ProductShowItemDto>() {
                    @Override
                    public ProductShowItemDto apply(ProductModel productModel) {
                        return new ProductShowItemDto(productModel, ItemType.PHYSICAL, "");
                    }
                });
                break;
            case COUPON:
                // 根据需求，可展示的ExchangeCoupon最多在20-30个左右。
                final int index = 0;
                final int pageSize = 100;

                List<ExchangeCouponView> exchangeCouponDtos = couponMapper.findExchangeableCouponViews(index, pageSize);
                productShowItemDtos = Lists.transform(exchangeCouponDtos, new Function<ExchangeCouponView, ProductShowItemDto>() {
                    @Override
                    public ProductShowItemDto apply(ExchangeCouponView exchangeCouponView) {
                        return new ProductShowItemDto(exchangeCouponView);
                    }
                });
                break;
            default:
                break;
        }
        return productShowItemDtos;
    }

    @Override
    public List<ProductShowItemDto> findAllProductsByGoodsTypes(List<GoodsType> goodsTypes) {
        List<ProductShowItemDto> productShowItemDtos = new ArrayList<>();
        for (GoodsType goodsType : goodsTypes) {
            productShowItemDtos.addAll(findAllProductsByGoodsType(goodsType));
        }
        Collections.sort(productShowItemDtos, new Comparator<ProductShowItemDto>() {
            @Override
            public int compare(ProductShowItemDto o1, ProductShowItemDto o2) {
                int result = o1.getSeq().compareTo(o2.getSeq());
                if (0 == result) {
                    return o1.getUpdatedTime().compareTo(o2.getUpdatedTime());
                } else {
                    return result;
                }
            }
        });
        return productShowItemDtos;
    }

    @Override
    public ProductShowItemDto findProductShowItemDto(long id, ItemType itemType) {
        ProductShowItemDto productShowItemDto = null;
        switch (itemType) {
            case PHYSICAL:
            case VIRTUAL:
                ProductModel productModel = productMapper.findById(id);
                if (null != productModel) {
                    productShowItemDto = new ProductShowItemDto(productModel, itemType, "");
                }
                break;
            case INTEREST_COUPON:
            case INVEST_COUPON:
            case RED_ENVELOPE:
                ExchangeCouponView exchangeCouponView = couponMapper.findExchangeableCouponViewById(id);
                if (null != exchangeCouponView) {
                    productShowItemDto = new ProductShowItemDto(exchangeCouponView);
                }
                break;
            default:
                break;
        }
        return productShowItemDto;
    }

    private ProductOrderModel generateOrder(AccountModel accountModel, ProductShowItemDto productShowItemDto, int amount, UserAddressModel userAddressModel) {
        if (productShowItemDto.getItemType().equals(ItemType.PHYSICAL)) {
            return new ProductOrderModel(productShowItemDto.getId(), productShowItemDto.getProductPrice(), amount, productShowItemDto.getProductPrice() * amount,
                    userAddressModel.getRealName(), userAddressModel.getMobile(), userAddressModel.getAddress(), true, "", accountModel.getLoginName());
        } else {
            UserModel userModel = userMapper.findByLoginName(accountModel.getLoginName());
            return new ProductOrderModel(productShowItemDto.getId(), productShowItemDto.getProductPrice(), amount, productShowItemDto.getProductPrice() * amount,
                    accountModel.getUserName(), userModel.getMobile(), "", false, "", accountModel.getLoginName());
        }
    }

    @Override
    @Transactional
    public synchronized BaseDto<BaseDataDto> buyProduct(String loginName, long id, ItemType itemType, int amount, UserAddressModel userAddressModel) {
        AccountModel accountModel = accountMapper.findByLoginName(loginName);
        if (null == accountModel) {
            return new BaseDto<>(new BaseDataDto(false, "该账户不存在"));
        }

        ProductShowItemDto productShowItemDto = findProductShowItemDto(id, itemType);
        if (null == productShowItemDto) {
            return new BaseDto<>(new BaseDataDto(false, "该商品或该商品类型不存在"));
        }
        if (amount > productShowItemDto.getLeftCount()) {
            return new BaseDto<>(new BaseDataDto(false, "商品数量不足"));
        }
        if (accountModel.getPoint() < productShowItemDto.getProductPrice() * amount) {
            return new BaseDto<>(new BaseDataDto(false, "积分不足"));
        }

        ProductOrderModel productOrderModel = generateOrder(accountModel, productShowItemDto, amount, userAddressModel);

        long totalPrice = productShowItemDto.getProductPrice() * amount;
        accountModel.setPoint(accountModel.getPoint() - totalPrice);

        accountMapper.update(accountModel);
        logger.info(MessageFormat.format("[ProductServiceImpl][buyProduct] User:{0} buy product {1} product type {2}, use point {3}",
                accountModel.getLoginName(), productShowItemDto.getId(), productShowItemDto.getItemType().getDescription(), totalPrice));
        productOrderMapper.create(productOrderModel);
        if (itemType.equals(ItemType.RED_ENVELOPE) || itemType.equals(ItemType.INTEREST_COUPON) || itemType.equals(ItemType.INVEST_COUPON)) {
            couponAssignmentService.assignUserCoupon(loginName, id);
            logger.info(MessageFormat.format("[ProductServiceImpl][buyProduct] User:{0} buy coupon {1} product type {2}. coupon has been assigned",
                    accountModel.getLoginName(), productShowItemDto.getId(), productShowItemDto.getItemType().getDescription()));
        }

        return new BaseDto<>(new BaseDataDto(true));
    }

    @Override
    public List<UserAddressModel> getUserAddressesByLoginName(String loginName) {
        return userAddressMapper.findByLoginName(loginName);
    }

    @Override
    public BaseDto<BaseDataDto> addAddress(String loginName, String realName, String mobile, String address) {
        List<UserAddressModel> userAddressModels = userAddressMapper.findByLoginName(loginName);
        if (userAddressModels.size() > 0) {
            return new BaseDto<>(new BaseDataDto(false, "已经填写过地址"));
        } else {
            UserAddressModel userAddressModel = new UserAddressModel(loginName, realName, mobile, address, loginName);
            userAddressMapper.create(userAddressModel);
            return new BaseDto<>(new BaseDataDto(true));
        }
    }

    @Override
    public BaseDto<BaseDataDto> updateAddress(String loginName, String realName, String mobile, String address) {
        List<UserAddressModel> userAddressModels = userAddressMapper.findByLoginName(loginName);
        if (userAddressModels.size() <= 0) {
            return new BaseDto<>(new BaseDataDto(false, "没有填写过地址"));
        } else {
            UserAddressModel userAddressModel = userAddressModels.get(0);
            userAddressModel.setRealName(realName);
            userAddressModel.setMobile(mobile);
            userAddressModel.setAddress(address);
            userAddressMapper.update(userAddressModel);
            return new BaseDto<>(new BaseDataDto(true));
        }
    }
}
