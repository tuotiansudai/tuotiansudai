package com.tuotiansudai.point.service.impl;


import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BaseDataDto;
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
        return productMapper.findProductsCount(goodsType);
    }

    @Override
    public List<ProductModel> findProductsList(GoodsType goodsType, int index, int pageSize){
        return productMapper.findProductsList(goodsType,(index-1)*pageSize, pageSize);
    }

    @Override
    public List<ProductOrderDto> findProductOrderList(final long productId, String loginName, int index, int pageSize) {
        List<ProductOrderModel> productOrderList = productOrderMapper.findProductOrderList(productId, loginName, (index-1)*pageSize, pageSize);
        return Lists.transform(productOrderList, new Function<ProductOrderModel, ProductOrderDto>() {
            @Override
            public ProductOrderDto apply(ProductOrderModel model) {
                return new ProductOrderDto(model);
            }
        });
    }

    @Override
    public long findProductOrderCount(long productId) {
        return productOrderMapper.findProductOrderCount(productId, null);
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
}
