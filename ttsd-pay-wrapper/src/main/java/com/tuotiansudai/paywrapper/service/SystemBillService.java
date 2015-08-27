package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.paywrapper.exception.AmountTransferException;
import com.tuotiansudai.repository.model.SystemBillBusinessType;
import com.tuotiansudai.repository.model.SystemBillModel;

public interface SystemBillService {

    void transferOut(long amount,String detail,SystemBillBusinessType businessType,String orderId) throws AmountTransferException;

}
