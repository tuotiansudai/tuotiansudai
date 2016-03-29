package com.tuotiansudai.transfer.service;


import com.tuotiansudai.transfer.dto.TransferApplicationDto;

import java.util.Date;

public interface InvestTransferService {

    void investTransferApply(TransferApplicationDto transferApplicationDto) throws Exception;

    boolean investTransferApplyCancel(long id);

    Date getDeadlineFromNow();

    boolean isTransferable(long investId);

}
