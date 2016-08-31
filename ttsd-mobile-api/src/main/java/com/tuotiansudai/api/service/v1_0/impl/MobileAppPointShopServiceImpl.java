package com.tuotiansudai.api.service.v1_0.impl;


import com.google.common.base.Function;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppPointShopService;
import com.tuotiansudai.point.repository.mapper.ProductMapper;
import com.tuotiansudai.point.repository.mapper.ProductOrderMapper;
import com.tuotiansudai.point.repository.mapper.UserAddressMapper;
import com.tuotiansudai.point.repository.model.ProductOrderViewDto;
import com.tuotiansudai.point.repository.model.UserAddressModel;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

@Service
public class MobileAppPointShopServiceImpl implements MobileAppPointShopService{

    @Autowired
    private UserAddressMapper userAddressMapper;

    @Autowired
    private ProductOrderMapper productOrderMapper;

    @Autowired
    private ProductMapper productMapper;

    @Override
    public BaseResponseDto updateUserAddress(UserAddressRequestDto userAddressRequestDto) {
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        List<UserAddressModel> byLoginName = userAddressMapper.findByLoginName(userAddressRequestDto.getBaseParam().getUserId());
        if(CollectionUtils.isNotEmpty(byLoginName)){
            UserAddressModel userAddressModel = convertUserAddressModel(userAddressRequestDto);
            userAddressModel.setId(byLoginName.get(0).getId());
            userAddressMapper.update(userAddressModel);
        }else{
            userAddressMapper.create(convertUserAddressModel(userAddressRequestDto));
        }
        return baseResponseDto;
    }

    @Override
    public BaseResponseDto findUserAddressResponseDto(BaseParamDto baseParamDto){
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        List<UserAddressModel> byLoginName = userAddressMapper.findByLoginName(baseParamDto.getBaseParam().getUserId());
        if(CollectionUtils.isNotEmpty(byLoginName)){
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
        List<ProductOrderViewDto> productOrderListByLoginName = productOrderMapper.findProductOrderListByLoginName(baseParamDto.getBaseParam().getUserId(), index, 10);
        ProductListOrderResponseDto  productListOrderResponseDto = new ProductListOrderResponseDto();
        if(CollectionUtils.isNotEmpty(productOrderListByLoginName)){
            Iterator<ProductOrderResponseDto> transform = Iterators.transform(productOrderListByLoginName.iterator(), new Function<ProductOrderViewDto, ProductOrderResponseDto>() {
                @Override
                public ProductOrderResponseDto apply(ProductOrderViewDto input) {
                    return new ProductOrderResponseDto(input);
                }
            });
            productListOrderResponseDto.setProductOrderResponseDtoList(Lists.newArrayList(transform));
            productListOrderResponseDto.setIndex(baseParamDto.getIndex());
            productListOrderResponseDto.setPageSize(baseParamDto.getPageSize());
            productListOrderResponseDto.setTotalCount((int)productOrderMapper.findProductOrderListByLoginNameCount(baseParamDto.getBaseParam().getUserId()));
        }
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseResponseDto.setData(productListOrderResponseDto);
        return baseResponseDto;
    }

    @Override
    public BaseResponseDto findPointHome(BaseParamDto baseParamDto){
        productMapper.findAllProducts();

        ProductListResponseDto productListResponseDto = new ProductListResponseDto();
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        baseResponseDto.setData(productListResponseDto);
        return baseResponseDto;
    }

    private UserAddressModel convertUserAddressModel(UserAddressRequestDto userAddressRequestDto){
        return new UserAddressModel(userAddressRequestDto.getBaseParam().getUserId(),
                userAddressRequestDto.getContract(),
                userAddressRequestDto.getMobile(),
                userAddressRequestDto.getAddress(),
                userAddressRequestDto.getBaseParam().getUserId());
    }
}
