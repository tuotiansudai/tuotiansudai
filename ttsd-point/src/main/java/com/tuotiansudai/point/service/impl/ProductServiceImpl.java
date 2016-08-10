package com.tuotiansudai.point.service.impl;


import com.tuotiansudai.point.dto.ProductDto;
import com.tuotiansudai.point.repository.mapper.ProductMapper;
import com.tuotiansudai.point.repository.model.GoodsType;
import com.tuotiansudai.point.repository.model.ProductModel;
import com.tuotiansudai.point.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Override
    @Transactional
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
}
