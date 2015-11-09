package com.tuotiansudai.service;

import com.tuotiansudai.exception.AmountTransferException;
import com.tuotiansudai.repository.model.UserBillBusinessType;

public interface AmountTransferService {

    void freeze(String loginName, long orderId, long amount, UserBillBusinessType businessType, String operatorLoginName, String description) throws AmountTransferException;

    void unfreeze(String loginName, long orderId, long amount, UserBillBusinessType businessType, String operatorLoginName, String description) throws AmountTransferException;

    void transferInBalance(String loginName, long orderId, long amount, UserBillBusinessType businessType, String operatorLoginName, String description) throws AmountTransferException;

    void transferOutBalance(String loginName, long orderId, long amount, UserBillBusinessType businessType, String operatorLoginName, String description) throws AmountTransferException;

    void transferOutFreeze(String loginName, long orderId, long amount, UserBillBusinessType businessType, String operatorLoginName, String description) throws AmountTransferException;
}
