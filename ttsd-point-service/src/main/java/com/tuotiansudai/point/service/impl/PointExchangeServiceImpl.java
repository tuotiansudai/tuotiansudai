package com.tuotiansudai.point.service.impl;


import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.point.repository.mapper.ProductOrderMapper;
import com.tuotiansudai.point.repository.model.ProductOrderViewDto;
import com.tuotiansudai.point.service.PointExchangeService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PointExchangeServiceImpl implements PointExchangeService {
    static Logger logger = Logger.getLogger(PointExchangeServiceImpl.class);

    @Autowired
    private ProductOrderMapper productOrderMapper;

    @Override
    public BasePaginationDataDto<ProductOrderViewDto> findProductOrderListByLoginNamePagination(String loginName, int index, int pageSize) {
        long count = productOrderMapper.findProductOrderListByLoginNameCount(loginName);
        List<ProductOrderViewDto> records = productOrderMapper.findProductOrderListByLoginName(loginName, (index - 1) * pageSize, pageSize);
        BasePaginationDataDto<ProductOrderViewDto> dto = new BasePaginationDataDto<ProductOrderViewDto>(index, pageSize, count, records);
        dto.setStatus(true);
        return dto;
    }
}
