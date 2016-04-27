package com.tuotiansudai.transfer.service;


import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.TransferApplicationPaginationItemDataDto;
import com.tuotiansudai.repository.model.TransferStatus;
import com.tuotiansudai.transfer.dto.TransferApplicationDto;

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
                                                                             String transferrerLoginName,
                                                                             String transfereeLoginName,
                                                                             Long loanId,
                                                                             Integer index,
                                                                             Integer pageSize);

    BasePaginationDataDto<TransferApplicationPaginationItemDataDto> findWebTransferApplicationPaginationList(String transferrerLoginName,List<TransferStatus> statusList ,Integer index, Integer pageSize);

    BaseDataDto isAllowTransfer(long transferApplicationId);

}
