package com.tuotiansudai.console.service;

import com.tuotiansudai.client.BankWrapperClient;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.fudian.dto.QueryTradeType;
import com.tuotiansudai.fudian.message.*;
import com.tuotiansudai.repository.mapper.BankAccountMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.BankAccountModel;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class BankDataQueryService {

    private final UserMapper userMapper;

    private final BankAccountMapper bankAccountMapper;

    private final LoanMapper loanMapper;

    private BankWrapperClient bankWrapperClient = new BankWrapperClient();

    @Autowired
    public BankDataQueryService(UserMapper userMapper, BankAccountMapper bankAccountMapper, LoanMapper loanMapper) {
        this.userMapper = userMapper;
        this.bankAccountMapper = bankAccountMapper;
        this.loanMapper = loanMapper;
    }

    public BankQueryUserMessage getUserStatus(String loginNameOrMobile) {
        UserModel userModel = userMapper.findByLoginNameOrMobile(loginNameOrMobile);
        if (userModel == null) {
            return new BankQueryUserMessage(false, "用户不存在");
        }
        BankAccountModel bankAccountModel = bankAccountMapper.findByLoginNameAndRole(userModel.getLoginName(), Role.INVESTOR);
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

    public BankQueryTradeMessage getTradeStatus(String bankOrderNo, Date bankOrderDate, QueryTradeType queryTradeType) {
        return bankWrapperClient.queryTrade(bankOrderNo, new DateTime(bankOrderDate).toString("yyyyMMdd"), queryTradeType);
    }

    public BankQueryLogAccountMessage getAccountBill(String loginNameOrMobile, Date queryOrderDateStart, Date queryOrderDateEnd) {
        UserModel userModel = userMapper.findByLoginNameOrMobile(loginNameOrMobile);
        if (userModel == null) {
            return new BankQueryLogAccountMessage(false, "用户不存在");
        }
        BankAccountModel bankAccountModel = bankAccountMapper.findByLoginNameAndRole(userModel.getLoginName(), Role.INVESTOR);
        if (bankAccountModel == null) {
            return new BankQueryLogAccountMessage(false, "存管账户不存在");
        }
        return bankWrapperClient.queryAccountBill(bankAccountModel.getBankUserName(), bankAccountModel.getBankAccountNo(), queryOrderDateStart, queryOrderDateEnd);
    }

    public BankQueryLogLoanAccountMessage getLoanBill(long loanId) {
        LoanModel loanModel = loanMapper.findById(loanId);
        if (loanModel == null) {
            return new BankQueryLogLoanAccountMessage(false, "标的不存在");
        }
        return bankWrapperClient.queryLoanBill(loanModel.getLoanTxNo(), loanModel.getLoanAccNo());
    }
}
