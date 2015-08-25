package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.paywrapper.exception.AmountTransferException;
import com.tuotiansudai.repository.model.SystemBillModel;

public interface SystemBillService {

    void transferOut(long money,String detail,String billType) throws AmountTransferException;

    SystemBillModel getLastestSystemBill();

}
