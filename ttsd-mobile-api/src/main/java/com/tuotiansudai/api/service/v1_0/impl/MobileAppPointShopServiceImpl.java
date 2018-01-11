package com.tuotiansudai.api.service.v1_0.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.tuotiansudai.activity.repository.dto.DrawLotteryResultDto;
import com.tuotiansudai.activity.repository.model.ActivityCategory;
import com.tuotiansudai.activity.repository.model.UserLotteryPrizeView;
import com.tuotiansudai.activity.service.LotteryDrawActivityService;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppPointShopService;
import com.tuotiansudai.api.util.PageValidUtils;
import com.tuotiansudai.coupon.service.CouponService;
import com.tuotiansudai.point.repository.mapper.*;
import com.tuotiansudai.point.repository.model.*;
import com.tuotiansudai.point.service.PointBillService;
import com.tuotiansudai.point.service.PointService;
import com.tuotiansudai.point.service.ProductService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.CouponMapper;
import com.tuotiansudai.repository.model.CouponModel;
import com.tuotiansudai.repository.model.ExchangeCouponView;
import com.tuotiansudai.spring.LoginUserInfo;
import com.tuotiansudai.util.RedisWrapperClient;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class MobileAppPointShopServiceImpl implements MobileAppPointShopService {

    static Logger logger = Logger.getLogger(MobileAppPointShopServiceImpl.class);

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

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

    @Value("${common.static.server}")
    private String bannerServer;

    @Autowired
    private ProductService productService;

    @Autowired
    private PageValidUtils pageValidUtils;

    @Autowired
    private PointService pointService;

    @Autowired
    private UserPointTaskMapper userPointTaskMapper;

    @Autowired
    private PointTaskMapper pointTaskMapper;

    @Autowired
    private LotteryDrawActivityService lotteryDrawActivityService;

    @Autowired
    private PointBillService pointBillService;

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
    public BaseResponseDto<ProductListResponseDto> findPointHome(ProductListRequestDto productListRequestDto) {

        String goodsType = productListRequestDto.getGoodsType() == null ? "" : productListRequestDto.getGoodsType();
        List<ProductDetailResponseDto> virtualProductsList = Lists.newArrayList();
        List<ProductDetailResponseDto> physicalsProductsList = Lists.newArrayList();
        switch (goodsType) {
            case "VIRTUAL":
                virtualProductsList = this.getVirtualList();
                break;
            case "PHYSICAL":
                physicalsProductsList = this.getPhysicalsProductsList();
                break;
            default:
                virtualProductsList = this.getVirtualList();
                physicalsProductsList = this.getPhysicalsProductsList();
        }

        String loginName = productListRequestDto.getBaseParam().getUserId();
        long finishedTaskCount = userPointTaskMapper.findFinishTaskByLoginName(productListRequestDto.getBaseParam().getUserId());
        long allTaskCount = pointTaskMapper.findCountAllTask();
        long unfinishedTaskCount = allTaskCount - finishedTaskCount;
        long userAvailablePoint = pointService.getAvailablePoint(loginName);
        ProductListResponseDto productListResponseDto = new ProductListResponseDto();
        productListResponseDto.setVirtuals(virtualProductsList);
        productListResponseDto.setPhysicals(physicalsProductsList);
        productListResponseDto.setMyPoints(String.valueOf(userAvailablePoint));
        productListResponseDto.setUnFinishedTaskCount(unfinishedTaskCount >= 0 ? unfinishedTaskCount : 0);
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
        String discountDesc = productService.discountShowInfo(LoginUserInfo.getLoginName());
        double discount = productService.discountRate(LoginUserInfo.getLoginName());
        long distinctPoints = Math.round(new BigDecimal(productModel.getPoints()).multiply(new BigDecimal(discount)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());

        ProductDetailResponseDto productDetailResponseDto = new ProductDetailResponseDto(productModel.getId(), bannerServer + productModel.getImageUrl(), productModel.getName(), productModel.getPoints(), productModel.getType(), productModel.getTotalCount() - productModel.getUsedCount(), productModel.getSeq(), productModel.getUpdatedTime());
        if (productModel.getType().equals(GoodsType.COUPON)) {
            CouponModel couponModel = couponService.findExchangeableCouponById(productModel.getCouponId());
            ExchangeCouponView exchangeCouponView = new ExchangeCouponView(productModel.getPoints(), distinctPoints, productModel.getSeq(), productModel.getImageUrl(), productModel.getId(), productModel.getMonthLimit(), couponModel);
            productDetailResponseDto.setLeftCount(exchangeCouponView != null ? String.valueOf(exchangeCouponView.getCouponModel() != null ? (exchangeCouponView.getCouponModel().getTotalCount() - exchangeCouponView.getCouponModel().getIssuedCount()) : "0") : String.valueOf(productModel.getTotalCount()));
        }
        if (StringUtils.isNotEmpty(productModel.getAppPictureUrl())) {
            productDetailResponseDto.setDetailImage(bannerServer + productModel.getAppPictureUrl());
        }
        List<String> description = Lists.newArrayList();
        CouponModel couponModel = couponMapper.findById(productModel.getCouponId());
        if (productModel.getType() == GoodsType.COUPON && couponModel != null) {
            description.addAll(productService.getProductDescription(couponModel.getInvestLowerLimit(), couponModel.getProductTypes(), couponModel.getDeadline()));
        } else {
            description.add(productModel.getDescription());
        }
        if (!Strings.isNullOrEmpty(discountDesc)) {
            productDetailResponseDto.setDiscountPoints(String.valueOf(distinctPoints));
        } else {
            productDetailResponseDto.setDiscountPoints("");
        }

        BaseResponseDto baseResponseDto = new BaseResponseDto();
        productDetailResponseDto.setProductDes(Lists.newArrayList(description));
        baseResponseDto.setData(productDetailResponseDto);
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        return baseResponseDto;
    }


    private List<ProductDetailResponseDto> getVirtualList() {
        List<ExchangeCouponView> exchangeCoupons = Lists.newArrayList();
        List<ProductModel> couponProducts = productMapper.findAllProductsByGoodsType(Lists.newArrayList(GoodsType.COUPON));
        for (ProductModel productModel : couponProducts) {
            CouponModel couponModel = couponMapper.findById(productModel.getCouponId());
            ExchangeCouponView exchangeCouponView = new ExchangeCouponView(productModel.getPoints(), productModel.getActualPoints(), productModel.getSeq(), productModel.getImageUrl(), productModel.getId(), productModel.getMonthLimit(), couponModel);
            exchangeCoupons.add(exchangeCouponView);
        }

        List<ProductModel> virtualProducts = productMapper.findAllProductsByGoodsType(Lists.newArrayList(GoodsType.VIRTUAL));

        String discountDesc = productService.discountShowInfo(LoginUserInfo.getLoginName());
        double discount = productService.discountRate(LoginUserInfo.getLoginName());

        Iterator<ProductDetailResponseDto> couponList = Iterators.transform(exchangeCoupons.iterator(), exchangeCouponView ->
                {
                    ProductDetailResponseDto productDetailResponseDto = new ProductDetailResponseDto(exchangeCouponView, bannerServer);
                    productDetailResponseDto.setDiscountDesc(discountDesc);
                    if (!Strings.isNullOrEmpty(discountDesc)) {
                        long discountPoints = Math.round(new BigDecimal(exchangeCouponView.getExchangePoint()).multiply(new BigDecimal(discount)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                        productDetailResponseDto.setDiscountPoints(String.valueOf(discountPoints));
                    } else {
                        productDetailResponseDto.setDiscountPoints("");
                    }
                    return productDetailResponseDto;
                }
        );

        Iterator<ProductDetailResponseDto> virtualList = Iterators.transform(virtualProducts.iterator(), input ->
                {
                    ProductDetailResponseDto productDetailResponseDto = new ProductDetailResponseDto(input.getId(), bannerServer + input.getImageUrl(), input.getName(), input.getPoints(), input.getType(), input.getTotalCount() - input.getUsedCount(), input.getSeq(), input.getUpdatedTime());
                    productDetailResponseDto.setDiscountDesc(discountDesc);

                    if (!Strings.isNullOrEmpty(discountDesc)) {
                        long discountPoints = Math.round(new BigDecimal(input.getPoints()).multiply(new BigDecimal(discount)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                        productDetailResponseDto.setDiscountPoints(String.valueOf(discountPoints));
                    } else {
                        productDetailResponseDto.setDiscountPoints("");
                    }
                    return productDetailResponseDto;
                }
        );

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
        return virtualShopList;

    }


    private List<ProductDetailResponseDto> getPhysicalsProductsList() {
        List<ProductModel> physicalsProducts = productMapper.findAllProductsByGoodsType(Lists.newArrayList(GoodsType.PHYSICAL));

        String discountDesc = productService.discountShowInfo(LoginUserInfo.getLoginName());
        double discount = productService.discountRate(LoginUserInfo.getLoginName());

        Iterator<ProductDetailResponseDto> physicals = Iterators.transform(physicalsProducts.iterator(), input ->
                {
                    ProductDetailResponseDto productDetailResponseDto = new ProductDetailResponseDto(input.getId(), bannerServer + input.getImageUrl(), input.getName(), input.getPoints(), input.getType(), input.getTotalCount() - input.getUsedCount(), input.getSeq(), input.getUpdatedTime());
                    productDetailResponseDto.setDiscountDesc(discountDesc);
                    if (!Strings.isNullOrEmpty(discountDesc)) {
                        long discountPoints = Math.round(new BigDecimal(input.getPoints()).multiply(new BigDecimal(discount)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                        productDetailResponseDto.setDiscountPoints(String.valueOf(discountPoints));
                    } else {
                        productDetailResponseDto.setDiscountPoints("");
                    }
                    return productDetailResponseDto;
                }
        );
        return Lists.newArrayList(physicals);
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

        String loginName = productDetailRequestDto.getBaseParam().getUserId();

        ProductModel productModel = productMapper.findById(Long.parseLong(productDetailRequestDto.getProductId().trim()));
        long leftCount = productDetailRequestDto.getNum() + productModel.getUsedCount();
        if (productModel.getType().equals(GoodsType.COUPON)) {
            CouponModel couponModel = couponService.findExchangeableCouponById(productModel.getCouponId());
            ExchangeCouponView exchangeCouponView = new ExchangeCouponView(productModel.getPoints(), productModel.getActualPoints(), productModel.getSeq(), productModel.getImageUrl(), productModel.getId(), productModel.getMonthLimit(), couponModel);
            leftCount = productDetailRequestDto.getNum() + (exchangeCouponView.getCouponModel() != null ? exchangeCouponView.getCouponModel().getIssuedCount() : 0l);
        }
        if (leftCount > productModel.getTotalCount() || leftCount == 0) {
            logger.info(MessageFormat.format("Insufficient product (userId = {0},totalCount = {1},usedCount = {2})", productDetailRequestDto.getBaseParam().getUserId(), productModel.getTotalCount(), productModel.getUsedCount()));
            return new BaseResponseDto(ReturnMessage.INSUFFICIENT_PRODUCT_NUM.getCode(), ReturnMessage.INSUFFICIENT_PRODUCT_NUM.getMsg());
        }

        String buyCountStr = redisWrapperClient.get(generateCountKey(productModel.getId(), loginName));
        int buyCount = buyCountStr == null ? 0 : Integer.parseInt(buyCountStr);

        if (productModel.getMonthLimit() != 0 && buyCount + productDetailRequestDto.getNum() > productModel.getMonthLimit()) {
            logger.info(MessageFormat.format("Reach the exchange limit this month, (userId = {0}, productId = {1}, monthLimit={2}, buyCount={3}, alreadyBuy={4})",
                    productDetailRequestDto.getBaseParam().getUserId(), String.valueOf(productModel.getId()), productModel.getMonthLimit(), productDetailRequestDto.getNum(), buyCount));
            return new BaseResponseDto(ReturnMessage.REACH_MONTH_LIMIT_THIS_MONTH.getCode(), MessageFormat.format(ReturnMessage.REACH_MONTH_LIMIT_THIS_MONTH.getMsg(), productModel.getMonthLimit()));
        }

        double discount = productService.discountRate(LoginUserInfo.getLoginName());
        long points = productService.discountTotalPrice(productModel.getPoints(), discount, productDetailRequestDto.getNum());
        long userAvailablePoint = pointService.getAvailablePoint(loginName);

        if (userAvailablePoint < points) {
            logger.info(MessageFormat.format("Insufficient points (userId = {0},productPoints = {2})", productDetailRequestDto.getBaseParam().getUserId(), points));
            return new BaseResponseDto(ReturnMessage.INSUFFICIENT_POINTS_BALANCE.getCode(), ReturnMessage.INSUFFICIENT_POINTS_BALANCE.getMsg());
        }

        List<UserAddressModel> userAddressModels = userAddressMapper.findByLoginName(productDetailRequestDto.getBaseParam().getUserId());
        if (CollectionUtils.isEmpty(userAddressModels) && productModel.getType().equals(GoodsType.PHYSICAL)) {
            logger.info(MessageFormat.format("Insufficient points (userId = {0},myPoints = {1},productPoints = {2})", productDetailRequestDto.getBaseParam().getUserId(), userAvailablePoint, points));
            return new BaseResponseDto(ReturnMessage.USER_ADDRESS_IS_NOT_NULL.getCode(), ReturnMessage.USER_ADDRESS_IS_NOT_NULL.getMsg());
        }

        productService.buyProduct(loginName, Long.parseLong(productDetailRequestDto.getProductId()), productModel.getType(), productDetailRequestDto.getNum(), CollectionUtils.isEmpty(userAddressModels) ? null : userAddressModels.get(0).getId(), productDetailRequestDto.getComment());
        return new BaseResponseDto(ReturnMessage.SUCCESS.getCode(), ReturnMessage.SUCCESS.getMsg());
    }

    @Transactional
    public BaseResponseDto<PointDrawResultResponseDto> lotteryDrawByPoint(BaseParamDto baseParamDto) {
        DrawLotteryResultDto drawLotteryResultDto = lotteryDrawActivityService.drawPrizeByPoint(LoginUserInfo.getMobile(), ActivityCategory.POINT_SHOP_DRAW_1000, true);
        PointDrawResultResponseDto pointDrawResultResponseDto = new PointDrawResultResponseDto(drawLotteryResultDto);
        int returnCode = drawLotteryResultDto.getReturnCode();
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        if (drawLotteryResultDto.isDrawLotterySuccess()) {
            pointBillService.createPointBill(LoginUserInfo.getLoginName(), null, PointBusinessType.POINT_LOTTERY, -1000, PointBusinessType.POINT_LOTTERY.getDescription());
            baseResponseDto.setData(pointDrawResultResponseDto);
            baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
            baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        } else {
            switch (returnCode) {
                case 1:
                    baseResponseDto.setCode(ReturnMessage.INSUFFICIENT_POINTS_BALANCE.getCode());
                    baseResponseDto.setMessage(ReturnMessage.INSUFFICIENT_POINTS_BALANCE.getMsg());
                    break;
                case 2:
                    baseResponseDto.setCode(ReturnMessage.USER_IS_NOT_EXISTS.getCode());
                    baseResponseDto.setMessage(ReturnMessage.USER_IS_NOT_EXISTS.getMsg());
                    break;
                case 3:
                    baseResponseDto.setCode(ReturnMessage.USER_ADDRESS_IS_EXPIRED.getCode());
                    baseResponseDto.setMessage(ReturnMessage.USER_ADDRESS_IS_EXPIRED.getMsg());
                    break;
                case 4:
                    baseResponseDto.setCode(ReturnMessage.USER_ADDRESS_IS_NOT_ACCOUNT.getCode());
                    baseResponseDto.setMessage(ReturnMessage.USER_ADDRESS_IS_NOT_ACCOUNT.getMsg());
                    break;
                default:
                    baseResponseDto.setCode(ReturnMessage.FAIL.getCode());
                    baseResponseDto.setMessage(ReturnMessage.FAIL.getMsg());
            }
        }

        return baseResponseDto;
    }

    @Override
    public BaseResponseDto<MyPrizeListResponseDto> findPrizeListByLoginName(BaseParamDto baseParamDto) {
        List<UserLotteryPrizeView> userLotteryPrizeViewList = lotteryDrawActivityService.findDrawLotteryPrizeRecordByMobile(LoginUserInfo.getMobile(), ActivityCategory.POINT_SHOP_DRAW_1000);
        MyPrizeListResponseDto myPrizeListResponseDto = new MyPrizeListResponseDto();
        if (CollectionUtils.isNotEmpty(userLotteryPrizeViewList)) {
            Iterator<PrizeResponseDto> transform = Iterators.transform(userLotteryPrizeViewList.iterator(), input -> new PrizeResponseDto(input));
            myPrizeListResponseDto.setMyPrizeList(Lists.newArrayList(transform));
        }

        BaseResponseDto baseResponseDto = new BaseResponseDto();
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseResponseDto.setData(myPrizeListResponseDto);
        baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        return baseResponseDto;
    }

    @Override
    public BaseResponseDto<PrizeListResponseDto> findPrizeList(BaseParamDto baseParamDto) {
        List<UserLotteryPrizeView> userLotteryPrizeViewList = lotteryDrawActivityService.findDrawLotteryPrizeRecord(null, ActivityCategory.POINT_SHOP_DRAW_1000);
        userLotteryPrizeViewList = userLotteryPrizeViewList.size() > 20 ? userLotteryPrizeViewList.subList(0, 20) : userLotteryPrizeViewList;
        PrizeListResponseDto prizeListResponseDto = new PrizeListResponseDto();
        if (CollectionUtils.isNotEmpty(userLotteryPrizeViewList)) {
            Iterator<PrizeResponseDto> transform = Iterators.transform(userLotteryPrizeViewList.iterator(), input -> new PrizeResponseDto(input));
            prizeListResponseDto.setPrizeList(Lists.newArrayList(transform));
        }

        BaseResponseDto baseResponseDto = new BaseResponseDto();
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseResponseDto.setData(prizeListResponseDto);
        baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        return baseResponseDto;
    }

    private UserAddressModel convertUserAddressModel(UserAddressRequestDto userAddressRequestDto) {
        return new UserAddressModel(userAddressRequestDto.getBaseParam().getUserId(),
                userAddressRequestDto.getContact(),
                userAddressRequestDto.getMobile(),
                userAddressRequestDto.getAddress(),
                userAddressRequestDto.getBaseParam().getUserId());
    }

    private static final String PRODUCT_USER_BUY_COUNT_KEY = "{0}:{1}:{2}";

    private static final ThreadLocal<SimpleDateFormat> SDF_LOCAL = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM"));

    private String generateCountKey(long productId, String loginName) {
        String today = SDF_LOCAL.get().format(new Date());
        return MessageFormat.format(PRODUCT_USER_BUY_COUNT_KEY, today, productId, loginName);
    }

}
