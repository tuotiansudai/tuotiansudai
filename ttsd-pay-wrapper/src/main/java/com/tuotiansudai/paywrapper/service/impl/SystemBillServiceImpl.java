package com.tuotiansudai.paywrapper.service.impl;

import com.tuotiansudai.paywrapper.service.SystemBillService;

import com.tuotiansudai.repository.mapper.SystemBillMapper;
import com.tuotiansudai.repository.model.SystemBillModel;
import com.tuotiansudai.repository.model.SystemBillBusinessType;
import com.tuotiansudai.repository.model.SystemBillOperationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class SystemBillServiceImpl implements SystemBillService {

    @Autowired
    private SystemBillMapper systemBillMapper;

    @Override
    @Transactional
    public void transferOut(long amount, String orderId, SystemBillBusinessType businessType, String detail) {
        SystemBillModel systemBillModel = new SystemBillModel();
        systemBillModel.setOrderId(orderId);
        systemBillModel.setType(SystemBillOperationType.OUT);
        systemBillModel.setAmount(amount);
        systemBillModel.setBusinessType(businessType);
        systemBillModel.setDetail(detail);
        systemBillModel.setCreatedTime(new Date());
        systemBillMapper.create(systemBillModel);
    }

    @Override
    @Transactional
    public void transferIn(long amount, String orderId, SystemBillBusinessType businessType, String detail) {
        SystemBillModel systemBillModel = new SystemBillModel();
        systemBillModel.setOrderId(orderId);
        systemBillModel.setType(SystemBillOperationType.IN);
        systemBillModel.setAmount(amount);
        systemBillModel.setBusinessType(businessType);
        systemBillModel.setDetail(detail);
        systemBillModel.setCreatedTime(new Date());
        systemBillMapper.create(systemBillModel);
    }


}
