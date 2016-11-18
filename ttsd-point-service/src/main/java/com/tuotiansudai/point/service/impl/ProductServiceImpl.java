package com.tuotiansudai.point.service.impl;


import com.google.common.collect.Lists;
import com.tuotiansudai.coupon.dto.ExchangeCouponDto;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.ExchangeCouponView;
import com.tuotiansudai.coupon.service.CouponAssignmentService;
import com.tuotiansudai.coupon.service.CouponService;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.point.repository.dto.ProductDto;
import com.tuotiansudai.point.repository.dto.ProductOrderDto;
import com.tuotiansudai.point.repository.dto.ProductShowItemDto;
import com.tuotiansudai.point.repository.mapper.ProductMapper;
import com.tuotiansudai.point.repository.mapper.ProductOrderMapper;
import com.tuotiansudai.point.repository.mapper.UserAddressMapper;
import com.tuotiansudai.point.repository.model.*;
import com.tuotiansudai.point.service.PointBillService;
import com.tuotiansudai.point.service.ProductService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.ProductType;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.util.AmountConverter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private static Logger logger = Logger.getLogger(ProductServiceImpl.class);

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CouponService couponService;

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
        if (GoodsType.COUPON.equals(productDto.getType())) {
            ProductDto convertProductDto = convertProductDtoToCouponType(productDto);
            productDto.setName(convertProductDto.getName());
            productDto.setDescription(convertProductDto.getDescription());
        }
        ProductModel productModel = new ProductModel(
                productDto.getLoginName(),
                productDto.getType(),
                productDto.getCouponId(),
                productDto.getName(),
                productDto.getSeq(),
                productDto.getImageUrl(),
                productDto.getDescription(),
                productDto.getTotalCount(),
                productDto.getPoints(),
                productDto.getStartTime(),
                productDto.getEndTime());
        productMapper.create(productModel);
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
    public List<ProductOrderDto> findProductOrderList(final long productId, String loginName, int index, int pageSize) {
        return productOrderMapper
                .findProductOrderList(productId, loginName, (index - 1) * pageSize, pageSize)
                .stream()
                .map(ProductOrderDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public long findProductOrderCount(long goodsId) {
        return productOrderMapper.findProductOrderCount(goodsId, null);
    }

    @Override
    @Transactional
    public BaseDataDto active(long productId, String loginName) {
        String errorMessage = "product model not exist product id = ({0})";
        ProductModel productModel = productMapper.findById(productId);
        if (productModel == null) {
            logger.error(MessageFormat.format(errorMessage, productId));
            return new BaseDataDto(false, MessageFormat.format(errorMessage, productId));
        }
        productModel.setActive(true);
        productModel.setActiveBy(loginName);
        productModel.setActiveTime(new Date());
        productMapper.update(productModel);
        return new BaseDataDto(true, null);
    }

    @Override
    @Transactional
    public BaseDataDto inactive(long productId, String loginName) {
        String errorMessage = "product model not exist product id = ({0})";
        ProductModel productModel = productMapper.findById(productId);
        if (productModel == null) {
            logger.error(MessageFormat.format(errorMessage, productId));
            return new BaseDataDto(false, MessageFormat.format(errorMessage, productId));
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
            return new BaseDataDto(false, MessageFormat.format(errorMessage, orderId));
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
            return new BaseDataDto(false, MessageFormat.format(errorMessage, goodsId));
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
        if (GoodsType.COUPON.equals(productDto.getType())) {
            ProductDto convertProductDto = convertProductDtoToCouponType(productDto);
            productDto.setName(convertProductDto.getName());
            productDto.setDescription(convertProductDto.getDescription());
        }
        ProductModel productModel = productMapper.findById(productDto.getId());
        productModel.setSeq(productDto.getSeq());
        productModel.setImageUrl(productDto.getImageUrl());
        productModel.setStartTime(productDto.getStartTime());
        productModel.setEndTime(productDto.getEndTime());
        productModel.setPoints(productDto.getPoints());
        productModel.setTotalCount(productDto.getTotalCount());
        productModel.setUpdatedBy(productDto.getLoginName());
        productModel.setUpdatedTime(new Date());
        productMapper.update(productModel);
        return new BaseDataDto(true, null);
    }

    private ProductDto convertProductDtoToCouponType(ProductDto productDto) {
        ProductDto convertProductDto = new ProductDto();
        CouponModel couponModel = couponService.findCouponById(productDto.getCouponId());
        switch (couponModel.getCouponType()) {
            case RED_ENVELOPE:
                convertProductDto.setName(AmountConverter.convertCentToString(couponModel.getAmount()) + "元现金红包");
                convertProductDto.setDescription(String.valueOf(couponModel.getAmount()));
                break;
            case INVEST_COUPON:
                convertProductDto.setName(AmountConverter.convertCentToString(couponModel.getAmount()) + "元投资体验券");
                convertProductDto.setDescription(String.valueOf(couponModel.getAmount()));
                break;
            case INTEREST_COUPON:
                convertProductDto.setName(couponModel.getRate() * 100 + "%加息券");
                convertProductDto.setDescription(String.valueOf(couponModel.getRate() * 100));
                break;
        }
        return convertProductDto;
    }

    private List<ProductShowItemDto> findAllProductsByGoodsType(GoodsType goodsType) {
        List<ProductShowItemDto> productShowItemDtos = new ArrayList<>();
        switch (goodsType) {
            case VIRTUAL:
                productShowItemDtos = productMapper
                        .findExchangeableProductsList(GoodsType.VIRTUAL, 0, Integer.MAX_VALUE)
                        .stream()
                        .map(p -> new ProductShowItemDto(p, GoodsType.VIRTUAL, ""))
                        .collect(Collectors.toList());
                break;
            case PHYSICAL:
                productShowItemDtos = productMapper
                        .findExchangeableProductsList(GoodsType.PHYSICAL, 0, Integer.MAX_VALUE)
                        .stream()
                        .map(p -> new ProductShowItemDto(p, GoodsType.PHYSICAL, ""))
                        .collect(Collectors.toList());
                break;
            case COUPON:
                // 根据需求，可展示的ExchangeCoupon最多在20-30个左右。
                final int index = 0;
                final int pageSize = 100;
                productShowItemDtos = productMapper.findExchangeableProductsList(goodsType, index, pageSize)
                        .stream()
                        .map(m -> {
                            CouponModel couponModel = couponService.findExchangeableCouponById(m.getCouponId());
                            return new ExchangeCouponView(m.getPoints(), m.getSeq(), m.getImageUrl(), m.getId(), couponModel);
                        })
                        .map(this::convertProductShowItemDto)
                        .collect(Collectors.toList());
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
        return productShowItemDtos.stream().sorted((o1, o2) -> {
            int result = Integer.compare(o1.getSeq(), o2.getSeq());
            if (0 == result && null != o1.getUpdatedTime() && null != o2.getUpdatedTime()) {
                return o2.getUpdatedTime().compareTo(o1.getUpdatedTime());
            } else {
                return result;
            }
        }).collect(Collectors.toList());
    }

    @Override
    public ProductShowItemDto findProductShowItemDto(long id, GoodsType goodsType) {
        ProductShowItemDto productShowItemDto = null;
        switch (goodsType) {
            case PHYSICAL:
            case VIRTUAL:
                ProductModel productModel = productMapper.findById(id);
                if (null != productModel) {
                    productShowItemDto = new ProductShowItemDto(productModel, goodsType, "");
                }
                break;
            case COUPON:
                ProductModel productModelCoupon = productMapper.findById(id);
                if (null != productModelCoupon) {
                    CouponModel couponModel = couponService.findCouponById(productModelCoupon.getCouponId());
                    if (null != couponModel) {
                        ExchangeCouponView exchangeCouponView = new ExchangeCouponView(
                                productModelCoupon.getPoints(),
                                productModelCoupon.getSeq(),
                                productModelCoupon.getImageUrl(),
                                id,
                                couponModel);
                        productShowItemDto = convertProductShowItemDto(exchangeCouponView);
                    }
                }
                break;
            default:
                break;
        }
        return productShowItemDto;
    }

    private ProductOrderModel generateOrder(AccountModel accountModel, ProductShowItemDto productShowItemDto, int amount, UserAddressModel userAddressModel) {
        if (productShowItemDto.getGoodsType().equals(GoodsType.PHYSICAL)) {
            return new ProductOrderModel(
                    productShowItemDto.getId(),
                    productShowItemDto.getPoints(),
                    amount,
                    productShowItemDto.getPoints() * amount,
                    userAddressModel.getContact(),
                    userAddressModel.getMobile(),
                    userAddressModel.getAddress(),
                    false,
                    null,
                    accountModel.getLoginName());
        } else if (productShowItemDto.getGoodsType().equals(GoodsType.VIRTUAL)) {
            UserModel userModel = userMapper.findByLoginName(accountModel.getLoginName());
            return new ProductOrderModel(
                    productShowItemDto.getId(),
                    productShowItemDto.getPoints(),
                    amount,
                    productShowItemDto.getPoints() * amount,
                    userModel.getUserName(),
                    userModel.getMobile(),
                    "",
                    false,
                    null,
                    accountModel.getLoginName());
        } else {
            UserModel userModel = userMapper.findByLoginName(accountModel.getLoginName());
            ProductModel productModel = productMapper.findById(productShowItemDto.getId());
            return new ProductOrderModel(
                    productModel.getId(),
                    productShowItemDto.getPoints(),
                    amount,
                    productShowItemDto.getPoints() * amount,
                    userModel.getUserName(),
                    userModel.getMobile(),
                    "",
                    false,
                    null,
                    accountModel.getLoginName());
        }
    }

    @Override
    @Transactional
    public BaseDto<BaseDataDto> buyProduct(String loginName, long id, GoodsType goodsType, int amount, Long addressId) {
        ProductModel productModel = productMapper.lockById(id);
        AccountModel accountModel = accountMapper.lockByLoginName(loginName);
        if (null == accountModel) {
            return new BaseDto<>(new BaseDataDto(false, "该账户未实名认证，不能购买商品"));
        }

        ProductShowItemDto productShowItemDto = findProductShowItemDto(id, goodsType);
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
        if (GoodsType.PHYSICAL == goodsType) {
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
                accountModel.getLoginName(), productShowItemDto.getId(), productShowItemDto.getGoodsType().getDescription(), amount, totalPrice));

        productOrderMapper.create(productOrderModel);
        if (goodsType.equals(GoodsType.COUPON)) {
            for (int i = 0; i < amount; ++i) {
                couponAssignmentService.assignUserCoupon(loginName, productModel.getCouponId());
            }
            logger.info(MessageFormat.format("[ProductServiceImpl][buyProduct] User:{0} buy coupon {1} product type {2}.amount{3}. coupon has been assigned",
                    accountModel.getLoginName(), productShowItemDto.getId(), productShowItemDto.getGoodsType().getDescription(), amount));

        }

        productModel.setUsedCount(productModel.getUsedCount() + amount);
        productMapper.update(productModel);

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

    @Override
    public ProductModel findProductByCouponId(long couponId) {
        return productMapper.findByCouponId(couponId);
    }

    @Override
    public List<ExchangeCouponDto> findCouponExchanges(int index, int pageSize) {
        List<ProductModel> productModelList = productMapper.findAllProducts(GoodsType.COUPON, (index - 1) * pageSize, pageSize);
        return productModelList.stream()
                .map(input -> couponService.findCouponById(input.getCouponId()))
                .map(input -> {
                    ExchangeCouponDto exchangeCouponDto = new ExchangeCouponDto(input);
                    ProductModel productModel = productMapper.findByCouponId(input.getId());
                    exchangeCouponDto.setExchangePoint(productModel.getPoints());
                    exchangeCouponDto.setSeq(productModel.getSeq());
                    exchangeCouponDto.setImageUrl(productModel.getImageUrl());
                    return exchangeCouponDto;
                }).collect(Collectors.toList());
    }

    public int findCouponExchangeCount() {
        return (int) productMapper.findAllProductsCount(GoodsType.COUPON);
    }

    @Override
    public List<String> getProductDescription(long investLowerLimit, List<ProductType> productTypes, Integer deadline) {
        List<String> description = Lists.newArrayList();
        description.add(investLowerLimit > 0 ? MessageFormat.format("投资满{0}元即可使用;", AmountConverter.convertCentToString(investLowerLimit)) : "0");
        description.add(MessageFormat.format("{0}天产品可用;", productTypes.toString().replaceAll("_", "")));
        description.add(MessageFormat.format("有效期限:{0}天。", deadline));
        return description;
    }

    private ProductShowItemDto convertProductShowItemDto(ExchangeCouponView exchangeCouponView) {

        ProductShowItemDto productShowItemDto = new ProductShowItemDto(
                exchangeCouponView.getCouponModel().getTotalCount(),
                exchangeCouponView.getCouponModel().getIssuedCount(),
                exchangeCouponView.getExchangePoint(),
                exchangeCouponView.getSeq(),
                exchangeCouponView.getImageUrl(),
                exchangeCouponView.getCouponModel().getCouponType(),
                exchangeCouponView.getCouponModel().getAmount(),
                exchangeCouponView.getCouponModel().getRate(),
                exchangeCouponView.getProductId());
        List<String> descriptions = getProductDescription(exchangeCouponView.getCouponModel().getInvestLowerLimit(),
                exchangeCouponView.getCouponModel().getProductTypes(),
                exchangeCouponView.getCouponModel().getDeadline());
        String descriptionString = "";
        for (String description : descriptions) {
            descriptionString += description + "\n";
        }
        productShowItemDto.setDescription(descriptionString.length() > 2 ? descriptionString.substring(0, descriptionString.length() - 1) : descriptionString);
        return productShowItemDto;
    }
}
