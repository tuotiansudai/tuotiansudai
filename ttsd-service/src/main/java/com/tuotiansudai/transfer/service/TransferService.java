package com.tuotiansudai.transfer.service;


import com.tuotiansudai.dto.*;
import com.tuotiansudai.exception.InvestException;
import com.tuotiansudai.repository.model.TransferStatus;
import com.tuotiansudai.transfer.repository.model.TransferApplicationModel;

import java.util.List;

public interface TransferService {

    BaseDto<PayFormDataDto> transferPurchase(InvestDto investDto) throws InvestException;

    BaseDto<PayDataDto> noPasswordTransferPurchase(InvestDto investDto) throws InvestException;

    BasePaginationDataDto<TransferApplicationPaginationItemDataDto> findAllTransferApplicationPaginationList(List<TransferStatus> transferStatus,
                                                                                                            double rateStart,
                                                                                                            double tateEnd,
                                                                                                            Integer index,
                                                                                                            Integer pageSize);
    int findCountAllTransferApplicationPaginationList(List<TransferStatus> transferStatus, double rateStart, double rateEnd);

    TransferApplicationDetailDto getTransferApplicationDetailDto(long TransferApplicationId, String loginName, int showLoginNameLength);

    TransferApplicationRecodesDto getTransferee(long TransferApplicationId, String loginName);

    BasePaginationDataDto generateTransferableInvest(String loginName,Integer index,Integer pageSize);

    List<TransferApplicationModel> getTransferApplicaationByTransferInvestId(long transferApplicationId);

}
