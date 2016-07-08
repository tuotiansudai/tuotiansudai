package com.tuotiansudai.transfer.service;


import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.TransferApplicationPaginationItemDataDto;
import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.repository.model.TransferStatus;
import com.tuotiansudai.transfer.dto.TransferApplicationDto;
import com.tuotiansudai.transfer.repository.model.TransferInvestDetailDto;

import java.util.Date;
import java.util.List;

public interface InvestTransferService {

    boolean investTransferApply(TransferApplicationDto transferApplicationDto);

    boolean cancelTransferApplication(long transferApplicationId);

    Date getDeadlineFromNow();

    boolean isTransferable(long investId);

    BasePaginationDataDto<TransferApplicationPaginationItemDataDto> findTransferApplicationPaginationList(Long transferApplicationId,
                                                                                                          Date startTime,
                                                                                                          Date endTime,
                                                                                                          TransferStatus status,
                                                                                                          String transferrerMobile,
                                                                                                          String transfereeMobile,
                                                                                                          Long loanId,
                                                                                                          Integer index,
                                                                                                          Integer pageSize);

    BasePaginationDataDto<TransferApplicationPaginationItemDataDto> findWebTransferApplicationPaginationList(String transferrerLoginName,List<TransferStatus> statusList ,Integer index, Integer pageSize);

    BaseDataDto isAllowTransfer(long transferApplicationId);

    BasePaginationDataDto<TransferInvestDetailDto> getInvestTransferList(String investorLoginName,
                                                                         int index,
                                                                         int pageSize,
                                                                         Date startTime,
                                                                         Date endTime,
                                                                         LoanStatus loanStatus);

}
