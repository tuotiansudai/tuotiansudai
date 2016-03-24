package com.tuotiansudai.transfer.service;


import com.tuotiansudai.transfer.dto.TransferApplicationDto;

public interface InvestTransferService {

    void investTransferApply(TransferApplicationDto transferApplicationDto) throws Exception;

    boolean investTransferApplyCancel(long id);

}
