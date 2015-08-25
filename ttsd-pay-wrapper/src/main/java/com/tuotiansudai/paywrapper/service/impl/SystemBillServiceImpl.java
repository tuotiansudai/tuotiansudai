package com.tuotiansudai.paywrapper.service.impl;

import com.tuotiansudai.paywrapper.exception.AmountTransferException;
import com.tuotiansudai.paywrapper.service.SystemBillService;

import com.tuotiansudai.repository.mapper.SystemBillMapper;
import com.tuotiansudai.repository.model.SystemBillModel;
import com.tuotiansudai.repository.model.SystemBillType;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SystemBillServiceImpl implements SystemBillService {
    @Autowired
    private SystemBillMapper systemBillMapper;

    @Override
    @Transactional
    public void transferOut(long money,String detail,String reason) throws AmountTransferException{
        SystemBillModel lastestSystemBill = getLastestSystemBill();
        long balance = 0L;
        if(lastestSystemBill != null){
            long lastBalance = lastestSystemBill.getBalance();
            if(lastBalance < money){

                throw new AmountTransferException("Insufficient system account balance");
            }
            balance = lastestSystemBill.getBalance() - money;
        }
        SystemBillModel systemBillModel = new SystemBillModel();
        systemBillModel.setBillType(SystemBillType.OUT);
        systemBillModel.setMoney(money);
        systemBillModel.setBalance(balance);
        systemBillModel.setReason(reason);
        systemBillMapper.create(systemBillModel);

    }

    @Override
    public SystemBillModel getLastestSystemBill() {
        List<SystemBillModel> systemBillModels = systemBillMapper.getLastestSystemBill();

        if(CollectionUtils.isNotEmpty(systemBillModels)){
            return systemBillModels.get(0);
        }
        return null;
    }
}
