package com.tuotiansudai.transfer.service;


import com.tuotiansudai.transfer.dto.TransferApplicationDto;

import java.util.Date;

public interface InvestTransferService {

    void investTransferApply(TransferApplicationDto transferApplicationDto);

    boolean cancelTransferApplication(long transferApplicationId);

    Date getDeadlineFromNow();

}
