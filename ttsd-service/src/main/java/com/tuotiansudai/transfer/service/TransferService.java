package com.tuotiansudai.transfer.service;


import com.tuotiansudai.dto.*;
import com.tuotiansudai.exception.InvestException;
import com.tuotiansudai.repository.model.TransferStatus;

import java.util.Date;

public interface TransferService {

    BaseDto<PayFormDataDto> transferPurchase(InvestDto investDto) throws InvestException;

    BasePaginationDataDto<TransferApplicationPaginationItemDataDto> findAllTransferApplicationPaginationList(TransferStatus status,
                                                                                                            double rateStart,
                                                                                                            double tateEnd,
                                                                                                            Integer index,
                                                                                                            Integer pageSize);
    int findCountAllTransferApplicationPaginationList(TransferStatus status, double rateStart, double tateEnd);

}
