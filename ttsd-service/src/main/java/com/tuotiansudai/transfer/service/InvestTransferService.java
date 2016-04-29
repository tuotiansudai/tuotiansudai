package com.tuotiansudai.transfer.service;


import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.InvestPaginationItemDataDto;
import com.tuotiansudai.dto.TransferApplicationPaginationItemDataDto;
import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.repository.model.TransferStatus;
import com.tuotiansudai.transfer.dto.TransferApplicationDto;
import com.tuotiansudai.transfer.repository.model.TransferInvestDetailDto;

import java.util.Date;

public interface InvestTransferService {

    void investTransferApply(TransferApplicationDto transferApplicationDto);

    boolean cancelTransferApplication(long transferApplicationId);

    Date getDeadlineFromNow();

    boolean isTransferable(long investId);

    BasePaginationDataDto<TransferApplicationPaginationItemDataDto> findTransferApplicationPaginationList(Long transferApplicationId,
                                                                                                          Date startTime,
                                                                                                          Date endTime,
                                                                                                          TransferStatus status,
                                                                                                          String transferrerLoginName,
                                                                                                          String transfereeLoginName,
                                                                                                          Long loanId,
                                                                                                          Integer index,
                                                                                                          Integer pageSize);

    BasePaginationDataDto<TransferInvestDetailDto> getInvestTransferList(String investorLoginName,
                                                                         int index,
                                                                         int pageSize,
                                                                         Date startTime,
                                                                         Date endTime,
                                                                         LoanStatus loanStatus);

}
