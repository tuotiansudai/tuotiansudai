package com.tuotiansudai.transfer.service;


import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.TransferApplicationPaginationItemDataDto;
import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.TransferStatus;
import com.tuotiansudai.transfer.dto.TransferApplicationDto;
import com.tuotiansudai.transfer.dto.TransferApplicationFormDto;
import com.tuotiansudai.transfer.repository.model.TransferInvestDetailDto;

import java.util.Date;
import java.util.List;

public interface InvestTransferService {

    TransferApplicationFormDto getApplicationForm(long investId);

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
                                                                                                          Source source,
                                                                                                          Integer index,
                                                                                                          Integer pageSize);

    BasePaginationDataDto<TransferApplicationPaginationItemDataDto> findWebTransferApplicationPaginationList(String transferrerLoginName, List<TransferStatus> statusList, Integer index, Integer pageSize);

    BaseDto<BaseDataDto> isInvestTransferable(long transferApplicationId);

    BasePaginationDataDto<TransferInvestDetailDto> getInvestTransferList(String investorLoginName,
                                                                         int index,
                                                                         int pageSize,
                                                                         Date startTime,
                                                                         Date endTime,
                                                                         LoanStatus loanStatus);
}
