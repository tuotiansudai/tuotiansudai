package com.tuotiansudai.paywrapper.service.impl;

import com.tuotiansudai.paywrapper.exception.AmountTransferException;
import com.tuotiansudai.paywrapper.service.SystemBillService;

import com.tuotiansudai.repository.mapper.SystemBillMapper;
import com.tuotiansudai.repository.model.SystemBillModel;
import com.tuotiansudai.repository.model.SystemBillBusinessType;
import com.tuotiansudai.repository.model.SystemBillType;
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
    public void transferOut(long amount,String detail,SystemBillBusinessType businessType,String orderId) throws AmountTransferException{
        SystemBillModel systemBillModel = new SystemBillModel();
        systemBillModel.setType(SystemBillType.OUT);
        systemBillModel.setAmount(amount);
        systemBillModel.setDetail(detail);
        systemBillModel.setOrderId(orderId);
        systemBillModel.setBusinessType(businessType);
        systemBillModel.setCreatedTime(new Date());
        systemBillMapper.create(systemBillModel);

    }


}
