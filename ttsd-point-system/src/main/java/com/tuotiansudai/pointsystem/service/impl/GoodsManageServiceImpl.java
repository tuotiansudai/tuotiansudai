package com.tuotiansudai.pointsystem.service.impl;

import com.tuotiansudai.pointsystem.dto.GoodsRequestDto;
import com.tuotiansudai.pointsystem.repository.mapper.ProductMapper;
import com.tuotiansudai.pointsystem.repository.model.GoodsType;
import com.tuotiansudai.pointsystem.repository.model.ProductModel;
import com.tuotiansudai.pointsystem.service.GoodsManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class GoodsManageServiceImpl implements GoodsManageService {

    @Autowired
    private ProductMapper productMapper;

    @Override
    public void createGoods(GoodsRequestDto requestDto) {
        ProductModel productModel = new ProductModel();
        productModel.setGoodsType(requestDto.getGoodsType());
        productModel.setProductName(requestDto.getProductName());
        productModel.setSeq(requestDto.getSeq());
        productModel.setImageUrl(requestDto.getImageUrl());
        productModel.setDescription(requestDto.getDescription());
        productModel.setTotalCount(requestDto.getTotalCount());
        productModel.setProductPrice(requestDto.getProductPrice());
        productModel.setStartTime(requestDto.getStartTime());
        productModel.setEndTime(requestDto.getEndTime());
        productModel.setCreatedBy(requestDto.getUserId());
        productModel.setCreatedTime(new Date());
        productModel.setUpdatedBy(requestDto.getUserId());
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
}
