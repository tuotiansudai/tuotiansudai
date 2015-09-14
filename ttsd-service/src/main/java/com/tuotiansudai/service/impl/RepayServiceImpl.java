package com.tuotiansudai.service.impl;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.RepayDto;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.LoanRepayMapper;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.LoanRepayModel;
import com.tuotiansudai.repository.model.RepayStatus;
import com.tuotiansudai.service.RepayService;
import com.tuotiansudai.utils.LoginUserInfo;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
public class RepayServiceImpl implements RepayService {

    static Logger logger = Logger.getLogger(RepayServiceImpl.class);

    @Autowired
    private LoanRepayMapper loanRepayMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Override
    public BaseDto<PayFormDataDto> repay(RepayDto repayDto) {
        long loanId = repayDto.getLoanId();
        final int period = repayDto.getPeriod();
        BaseDto<PayFormDataDto> baseDto = new BaseDto<>();
        PayFormDataDto payFormDataDto = new PayFormDataDto();
        baseDto.setData(payFormDataDto);
        LoanModel loanModel = loanMapper.findById(loanId);
        String currentLoginName = LoginUserInfo.getLoginName();
        if (!loanModel.getLoanerLoginName().equalsIgnoreCase(currentLoginName)) {
            logger.error(MessageFormat.format("Current login user {0} is not the loaner of loan {1}!", currentLoginName, String.valueOf(loanId)));
            return baseDto;
        }

        LoanRepayModel enabledLoanRepay = this.getEnabledLoanRepay(loanId);

        if (enabledLoanRepay == null || enabledLoanRepay.getPeriod() != period) {
            logger.error(MessageFormat.format("Currently, loan {0} Period {1} is not enabled ", String.valueOf(loanId), String.valueOf(period)));
            return baseDto;
        }

        return payWrapperClient.repay(repayDto);
    }

    private LoanRepayModel getEnabledLoanRepay(long loanId) {
        List<LoanRepayModel> loanRepayModels = loanRepayMapper.findByLoanId(loanId);
        final DateTime today = new DateTime().withTimeAtStartOfDay();

        Optional<LoanRepayModel> loanRepayModelOptional = Iterators.tryFind(loanRepayModels.iterator(), new Predicate<LoanRepayModel>() {
            @Override
            public boolean apply(LoanRepayModel loanRepayModel) {
                DateTime repayDate = new DateTime(loanRepayModel.getRepayDate()).withTimeAtStartOfDay();
                return repayDate.isEqual(today) || repayDate.isAfter(today);
            }
        });

        if (loanRepayModelOptional.isPresent()) {
            LoanRepayModel loanRepayModel = loanRepayModelOptional.get();
            if (loanRepayModel.getStatus() == RepayStatus.REPAYING) {
                return loanRepayModel;
            }
        }

        return null;
    }
}
