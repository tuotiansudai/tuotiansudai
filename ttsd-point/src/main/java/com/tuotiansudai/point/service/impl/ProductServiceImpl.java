package com.tuotiansudai.point.service.impl;


import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.point.dto.GoodsActiveDto;
import com.tuotiansudai.point.dto.GoodsConsignmentDto;
import com.tuotiansudai.point.dto.ProductDto;
import com.tuotiansudai.point.dto.ProductOrderDto;
import com.tuotiansudai.point.repository.mapper.ProductMapper;
import com.tuotiansudai.point.repository.mapper.ProductOrderMapper;
import com.tuotiansudai.point.repository.model.GoodsType;
import com.tuotiansudai.point.repository.model.ProductModel;
import com.tuotiansudai.point.repository.model.ProductOrderModel;
import com.tuotiansudai.point.service.ProductService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductOrderMapper productOrderMapper;

    private static Logger logger = Logger.getLogger(ProductServiceImpl.class);

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
        productModel.setActive(false);
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

    @Override
    public List<ProductOrderDto> findProductOrderList(final long productId) {
        List<ProductOrderModel> productOrderList = productOrderMapper.findProductOrderList(productId, null);
        return Lists.transform(productOrderList, new Function<ProductOrderModel, ProductOrderDto>() {
            @Override
            public ProductOrderDto apply(ProductOrderModel model) {
                model.getCreatedBy();
                long orderCount = productOrderMapper.findProductOrderCount(productId, model.getCreatedBy());
                return new ProductOrderDto(model, orderCount);
            }
        });
    }

    @Override
    public long findProductOrderCount(long productId) {
        return productOrderMapper.findProductOrderCount(productId, null);
    }

    @Override
    @Transactional
    public BaseDataDto goodsActive(GoodsActiveDto goodsActiveDto) {
        String errorMessage = "product model not exist product id = ({0})";
        Long[] productIds = goodsActiveDto.getProductId();
        for (Long productId : productIds) {
            ProductModel productModel = productMapper.findById(productId);
            if (productModel == null) {
                logger.error(MessageFormat.format(errorMessage, goodsActiveDto.getProductId()));
                return new BaseDataDto(false, MessageFormat.format(errorMessage, goodsActiveDto.getProductId()));
            }
            productModel.setActive(true);
            productMapper.update(productModel);
        }
        return new BaseDataDto(true, null);
    }

    @Override
    @Transactional
    public BaseDataDto goodsConsignment(GoodsConsignmentDto consignmentDto) {
        String errorMessage = "goods order not exist, product Order id = ({0})";
        Long[] orderIds = consignmentDto.getProductOrderId();
        for (Long orderId : orderIds) {
            ProductOrderModel productOrderModel = productOrderMapper.findById(orderId);
            if (productOrderModel == null) {
                logger.error(MessageFormat.format(errorMessage, orderId));
                continue;
            }
            productOrderModel.setConsignment(true);
        }
        return new BaseDataDto(true, null);
    }

    @Override
    public ProductModel findById(long id) {
        return productMapper.findById(id);
    }

    @Override
    @Transactional
    public BaseDataDto updateProduct(ProductDto productDto) {
        ProductModel productModel = productMapper.findById(productDto.getId());
        if (productModel == null) {
            return new BaseDataDto(false, null);
        }
        if (productModel.isActive()) {
            return new BaseDataDto(false, null);
        }
        productModel.setGoodsType(productDto.getGoodsType());
        productModel.setProductName(productDto.getProductName());
        productModel.setSeq(productDto.getSeq());
        productModel.setImageUrl(productDto.getImageUrl());
        productModel.setDescription(productDto.getDescription());
        productModel.setTotalCount(productDto.getTotalCount());
        productModel.setProductPrice(productDto.getProductPrice());
        productModel.setStartTime(productDto.getStartTime());
        productModel.setEndTime(productDto.getEndTime());
        productModel.setUpdatedBy(productDto.getLoginName());
        productModel.setUpdatedTime(new Date());
        productMapper.update(productModel);
        return new BaseDataDto(true, null);
    }

    @Override
    public BaseDataDto deleteProduct(long id) {
        ProductModel productModel = productMapper.findById(id);
        if (productModel == null) {
            return new BaseDataDto(false, null);
        }
        if (productModel.isActive()) {
            return new BaseDataDto(false, null);
        }
        productMapper.delete(id);
        return new BaseDataDto(true, null);
    }
}
