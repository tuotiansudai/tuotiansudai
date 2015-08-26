package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.paywrapper.exception.AmountTransferException;
import com.tuotiansudai.repository.model.UserBillBusinessType;
import org.springframework.transaction.annotation.Transactional;

public interface UserBillService {

    void freeze(String loginName, long orderId, long amount, UserBillBusinessType businessType) throws AmountTransferException;

    void unfreeze(String loginName, long orderId, long amount, UserBillBusinessType businessType) throws AmountTransferException;

    void transferInBalance(String loginName, long orderId, long amount, UserBillBusinessType businessType);

    void transferOutBalance(String loginName, long orderId, long amount, UserBillBusinessType businessType) throws AmountTransferException;

    void transferOutFreeze(String loginName, long orderId, long amount, UserBillBusinessType businessType) throws AmountTransferException;
}
