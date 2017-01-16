package com.tuotiansudai.paywrapper.service.impl;


import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.sms.SmsCancelTransferLoanNotifyDto;
import com.tuotiansudai.paywrapper.service.AdvanceTransferService;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.LoanRepayMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.TransferStatus;
import com.tuotiansudai.transfer.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.transfer.repository.model.TransferApplicationModel;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.MessageFormat;
import java.util.List;

public class AdvanceTransferServiceImpl implements AdvanceTransferService {

    private static Logger logger = Logger.getLogger(AdvanceTransferServiceImpl.class);

    @Autowired
    LoanRepayMapper loanRepayMapper;

    @Autowired
    LoanMapper loanMapper;

    @Autowired
    TransferApplicationMapper transferApplicationMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    SmsWrapperClient smsWrapperClient;

    @Override
    public boolean modifyTransfer(long loanRepayId) {
        boolean result = true;

        logger.info(MessageFormat.format("[advanced repay {0}] return Value ({1}) aspect is starting...",
                String.valueOf(loanRepayId), String.valueOf(loanRepayId)));

        long loanId = loanRepayMapper.findById(loanRepayId).getLoanId();

        List<TransferApplicationModel> transferApplicationModels = transferApplicationMapper.findAllTransferringApplicationsByLoanId(loanId);

        for (TransferApplicationModel transferApplicationModel : transferApplicationModels) {
            transferApplicationModel.setStatus(TransferStatus.CANCEL);
            transferApplicationMapper.update(transferApplicationModel);

            logger.info(MessageFormat.format("Transfer Loan id: {0} is canceled because of advanced repay.", transferApplicationModel.getId()));

            String mobile = userMapper.findByLoginName(transferApplicationModel.getLoginName()).getMobile();
            smsWrapperClient.sendCancelTransferLoanNotify(new SmsCancelTransferLoanNotifyDto(mobile, transferApplicationModel.getName()));
        }

        logger.info(MessageFormat.format("[advanced repay {0}] return Value ({1}) aspect is done",
                String.valueOf(loanRepayId), String.valueOf(loanRepayId)));

        return result;
    }
}
