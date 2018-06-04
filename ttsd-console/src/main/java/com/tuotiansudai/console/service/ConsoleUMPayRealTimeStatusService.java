package com.tuotiansudai.console.service;

import com.tuotiansudai.client.BankWrapperClient;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.fudian.message.BankQueryLoanMessage;
import com.tuotiansudai.fudian.message.BankQueryUserMessage;
import com.tuotiansudai.repository.mapper.BankAccountMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.BankAccountModel;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class ConsoleUMPayRealTimeStatusService {

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BankAccountMapper bankAccountMapper;

    @Autowired
    private LoanMapper loanMapper;

    private BankWrapperClient bankWrapperClient = new BankWrapperClient();

    public BankQueryUserMessage getUserStatus(String loginNameOrMobile) {
        UserModel userModel = userMapper.findByLoginNameOrMobile(loginNameOrMobile);
        if (userModel == null) {
            return new BankQueryUserMessage(false, "用户不存在");
        }
        BankAccountModel bankAccountModel = bankAccountMapper.findByLoginName(userModel.getLoginName());
        if (bankAccountModel == null) {
            return new BankQueryUserMessage(false, "存管账户不存在");
        }
        return bankWrapperClient.queryUser(bankAccountModel.getBankUserName(), bankAccountModel.getBankAccountNo());
    }

    public BankQueryLoanMessage getLoanStatus(long loanId) {
        LoanModel loanModel = loanMapper.findById(loanId);
        if (loanModel == null) {
            return new BankQueryLoanMessage(false, "标的不存在");
        }
        return bankWrapperClient.queryLoan(loanModel.getLoanTxNo(), loanModel.getLoanAccNo());
    }

    public Map<String, String> getTransferStatus(String orderId, Date merDate, String businessType) {
        return payWrapperClient.getTransferStatus(orderId, merDate, businessType);
    }
}
