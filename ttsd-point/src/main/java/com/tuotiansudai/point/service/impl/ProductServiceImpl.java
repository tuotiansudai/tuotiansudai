package com.tuotiansudai.point.service.impl;


import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.coupon.dto.ExchangeCouponDto;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.ExchangeCouponView;
import com.tuotiansudai.coupon.service.CouponAssignmentService;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.point.dto.ProductDto;
import com.tuotiansudai.point.dto.ProductOrderDto;
import com.tuotiansudai.point.dto.ProductShowItemDto;
import com.tuotiansudai.point.repository.mapper.ProductMapper;
import com.tuotiansudai.point.repository.mapper.ProductOrderMapper;
import com.tuotiansudai.point.repository.mapper.UserAddressMapper;
import com.tuotiansudai.point.repository.model.*;
import com.tuotiansudai.point.service.PointBillService;
import com.tuotiansudai.point.service.ProductService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.util.AmountConverter;
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

    @Autowired
    private PointBillService pointBillService;

    @Override
    @Transactional
    public void createProduct(ProductDto productDto) {
        if("COUPON" == productDto.getType().name()){
            CouponModel couponModel = couponMapper.findById(productDto.getCouponId());
            switch (couponModel.getCouponType()) {
                case RED_ENVELOPE:
                    productDto.setName(AmountConverter.convertCentToString(couponModel.getAmount()) + "元现金红包");
                    productDto.setDescription(String.valueOf(couponModel.getAmount()));
                    productDto.setImageUrl("");
                    break;
                case INVEST_COUPON:
                    productDto.setName(AmountConverter.convertCentToString(couponModel.getAmount()) + "元投资体验券");
                    productDto.setDescription(String.valueOf(couponModel.getAmount()));
                    productDto.setImageUrl("");
                    break;
                case INTEREST_COUPON:
                    productDto.setName(couponModel.getRate()*100 + "%加息券");
                    productDto.setDescription(String.valueOf(couponModel.getRate() * 100));
                    productDto.setImageUrl("");
                    break;
            }
        }
        ProductModel productModel = new ProductModel(productDto);
        productModel.setActive(false);
        productModel.setCreatedBy(productDto.getLoginName());
        productModel.setCreatedTime(new Date());
        productModel.setUpdatedBy(productDto.getLoginName());
        productModel.setUpdatedTime(new Date());
        productMapper.create(productModel);
    }

    @Override
    public long findProductsCount(GoodsType goodsType) {
        return productMapper.findExchangeableProductsCount(goodsType);
    }

    @Override
    public List<ProductModel> findProductsList(GoodsType goodsType, int index, int pageSize) {
        return productMapper.findExchangeableProductsList(goodsType, (index - 1) * pageSize, pageSize);
    }


    @Override
    public long findAllProductsCount(GoodsType goodsType) {
        return productMapper.findAllProductsCount(goodsType);
    }

    @Override
    public List<ProductModel> findAllProducts(GoodsType goodsType, int index, int pageSize) {
        return productMapper.findAllProducts(goodsType, (index - 1) * pageSize, pageSize);
    }

    @Override
    public List<ProductOrderDto> findProductOrderList(final long goodsId, String loginName, int index, int pageSize) {
        List<ProductOrderModel> productOrderList = productOrderMapper.findProductOrderList(goodsId, loginName, (index - 1) * pageSize, pageSize);
        return Lists.transform(productOrderList, new Function<ProductOrderModel, ProductOrderDto>() {
            @Override
            public ProductOrderDto apply(ProductOrderModel model) {
                return new ProductOrderDto(model);
            }
        });
    }

    @Override
    public long findProductOrderCount(long goodsId) {
        return productOrderMapper.findProductOrderCount(goodsId, null);
    }

    @Override
    @Transactional
    public BaseDataDto active(long porductId, String loginName) {
        String errorMessage = "product model not exist product id = ({0})";
        ProductModel productModel = productMapper.findById(porductId);
        if (productModel == null) {
            logger.error(MessageFormat.format(errorMessage, productModel.getId()));
            return new BaseDataDto(false, MessageFormat.format(errorMessage, productModel.getId()));
        }
        productModel.setActive(true);
        productModel.setActiveBy(loginName);
        productModel.setActiveTime(new Date());
        productMapper.update(productModel);
        return new BaseDataDto(true, null);
    }

    @Override
    @Transactional
    public BaseDataDto inactive(long porductId, String loginName) {
        String errorMessage = "product model not exist product id = ({0})";
        ProductModel productModel = productMapper.findById(porductId);
        if (productModel == null) {
            logger.error(MessageFormat.format(errorMessage, productModel.getId()));
            return new BaseDataDto(false, MessageFormat.format(errorMessage, productModel.getId()));
        }
        productModel.setActive(false);
        productModel.setActiveBy(loginName);
        productModel.setActiveTime(new Date());
        productMapper.update(productModel);
        return new BaseDataDto(true, null);
    }

    @Override
    @Transactional
    public BaseDataDto consignment(long orderId) {
        String errorMessage = "goods order not exist, product Order id = ({0})";
        ProductOrderModel productOrderModel = productOrderMapper.findById(orderId);
        if (productOrderModel == null) {
            logger.debug(MessageFormat.format(errorMessage, orderId));
        }
        productOrderModel.setConsignment(true);
        productOrderModel.setConsignmentTime(new Date());
        productOrderMapper.update(productOrderModel);
        return new BaseDataDto(true, null);
    }

    @Override
    @Transactional
    public BaseDataDto batchConsignment(long goodsId) {
        String errorMessage = "goodsId is not exist, product Order goodsId = ({0})";
        ProductModel productModel = productMapper.findById(goodsId);
        if (productModel == null) {
            logger.debug(MessageFormat.format(errorMessage, goodsId));
        }
        productOrderMapper.batchConsignment(goodsId);
        return new BaseDataDto(true, null);
    }

    @Override
    public ProductModel findById(long id) {
        return productMapper.findById(id);
    }

    @Override
    @Transactional
    public BaseDataDto updateProduct(ProductDto productDto) {
        ProductModel productModel = new ProductModel(productDto);
        productModel.setUpdatedBy(productDto.getLoginName());
        productModel.setUpdatedTime(new Date());
        productMapper.update(productModel);
        return new BaseDataDto(true, null);
    }

    private List<ProductShowItemDto> findAllProductsByGoodsType(GoodsType goodsType) {
        List<ProductModel> productModels = null;
        List<ProductShowItemDto> productShowItemDtos = new ArrayList<>();
        switch (goodsType) {
            case VIRTUAL:
                productModels = productMapper.findExchangeableProductsList(GoodsType.VIRTUAL, 0, Integer.MAX_VALUE);
                productShowItemDtos = Lists.transform(productModels, new Function<ProductModel, ProductShowItemDto>() {
                    @Override
                    public ProductShowItemDto apply(ProductModel productModel) {
                        return new ProductShowItemDto(productModel, ItemType.VIRTUAL, "");
                    }
                });
                break;
            case PHYSICAL:
                productModels = productMapper.findExchangeableProductsList(GoodsType.PHYSICAL, 0, Integer.MAX_VALUE);
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
            return new ProductOrderModel(productShowItemDto.getId(), productShowItemDto.getPoints(), amount, productShowItemDto.getPoints() * amount,
                    userAddressModel.getContact(), userAddressModel.getMobile(), userAddressModel.getAddress(), false, null, accountModel.getLoginName());
        } else {
            UserModel userModel = userMapper.findByLoginName(accountModel.getLoginName());
            return new ProductOrderModel(productShowItemDto.getId(), productShowItemDto.getPoints(), amount, productShowItemDto.getPoints() * amount,
                    accountModel.getUserName(), userModel.getMobile(), "", false, null, accountModel.getLoginName());
        }
    }

    @Override
    @Transactional
    public synchronized BaseDto<BaseDataDto> buyProduct(String loginName, long id, ItemType itemType, int amount, Long addressId) {
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
        if (accountModel.getPoint() < productShowItemDto.getPoints() * amount) {
            return new BaseDto<>(new BaseDataDto(false, "积分不足"));
        }

        UserAddressModel userAddressModel = null;
        if (ItemType.PHYSICAL == itemType) {
            if (null == addressId) {
                return new BaseDto<>(new BaseDataDto(false, "地址不存在"));
            }
            userAddressModel = userAddressMapper.findByLoginNameAndId(addressId, loginName);
            if (null == userAddressModel) {
                return new BaseDto<>(new BaseDataDto(false, "地址不存在"));
            }
        }

        ProductOrderModel productOrderModel = generateOrder(accountModel, productShowItemDto, amount, userAddressModel);

        long totalPrice = productShowItemDto.getPoints() * amount;

        pointBillService.createPointBill(loginName, productShowItemDto.getId(), PointBusinessType.EXCHANGE, (-totalPrice), productShowItemDto.getName());
        logger.info(MessageFormat.format("[ProductServiceImpl][buyProduct] User:{0} buy product {1} product type {2}, amount:{3}, use point {4}",
                accountModel.getLoginName(), productShowItemDto.getId(), productShowItemDto.getItemType().getDescription(), amount, totalPrice));

        productOrderMapper.create(productOrderModel);
        if (itemType.equals(ItemType.RED_ENVELOPE) || itemType.equals(ItemType.INTEREST_COUPON) || itemType.equals(ItemType.INVEST_COUPON)) {
            for (int i = 0; i < amount; ++i) {
                couponAssignmentService.assignUserCoupon(loginName, id);
            }
            logger.info(MessageFormat.format("[ProductServiceImpl][buyProduct] User:{0} buy coupon {1} product type {2}.amount{3}. coupon has been assigned",
                    accountModel.getLoginName(), productShowItemDto.getId(), productShowItemDto.getItemType().getDescription(), amount));
        } else if (itemType.equals(ItemType.PHYSICAL) || itemType.equals(ItemType.VIRTUAL)) {
            ProductModel productModel = productMapper.findById(id);
            productModel.setUsedCount(productModel.getUsedCount() + amount);
            productMapper.update(productModel);
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
            return new BaseDto<>(new BaseDataDto(true, String.valueOf(userAddressModel.getId())));
        }
    }

    @Override
    public BaseDto<BaseDataDto> updateAddress(long id, String loginName, String contact, String mobile, String address) {
        UserAddressModel userAddressModel = userAddressMapper.findByLoginNameAndId(id, loginName);
        if (null == userAddressModel) {
            return new BaseDto<>(new BaseDataDto(false, "地址不存在"));
        } else {
            userAddressModel.setContact(contact);
            userAddressModel.setMobile(mobile);
            userAddressModel.setAddress(address);
            userAddressMapper.update(userAddressModel);
            return new BaseDto<>(new BaseDataDto(true));
        }
    }
}
