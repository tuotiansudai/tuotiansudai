package com.tuotiansudai.transfer.service;


import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.dto.TransferApplicationDto;
import com.tuotiansudai.dto.TransferApplicationFormDto;
import com.tuotiansudai.dto.TransferApplicationPaginationItemDataDto;

import java.util.Date;
import java.util.List;

public interface InvestTransferService {

    TransferApplicationFormDto getApplicationForm(long investId);

    boolean investTransferApply(TransferApplicationDto transferApplicationDto);

    boolean cancelTransferApplication(long transferApplicationId);

    boolean cancelTransferApplicationManually(long transferApplicationId);

    Date getDeadlineFromNow();

    boolean isTransferable(long investId);

    BasePaginationDataDto<TransferApplicationPaginationItemDataDto> findWebTransferApplicationPaginationList(String transferrerLoginName, List<TransferStatus> statusList, Integer index, Integer pageSize);

    BaseDto<BaseDataDto> isInvestTransferable(long transferApplicationId);

    BasePaginationDataDto<TransferInvestDetailView> getInvestTransferList(String investorLoginName,
                                                                          int index,
                                                                          int pageSize,
                                                                          Date startTime,
                                                                          Date endTime,
                                                                          LoanStatus loanStatus);

    boolean validTransferIsCanceled(long investId);

    boolean validTransferIsDayLimit(long loanId);

    long calcultorTransferAmount(long investId);
}
