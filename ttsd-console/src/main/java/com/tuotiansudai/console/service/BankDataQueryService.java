package com.tuotiansudai.console.service;

import com.tuotiansudai.client.BankWrapperClient;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.fudian.dto.QueryTradeType;
import com.tuotiansudai.fudian.message.*;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.BankAccountMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.BankAccountModel;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class BankDataQueryService {

    private final UserMapper userMapper;

    private final BankAccountMapper bankAccountMapper;

    private final LoanMapper loanMapper;

    private BankWrapperClient bankWrapperClient = new BankWrapperClient();

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    public BankDataQueryService(UserMapper userMapper, BankAccountMapper bankAccountMapper, LoanMapper loanMapper) {
        this.userMapper = userMapper;
        this.bankAccountMapper = bankAccountMapper;
        this.loanMapper = loanMapper;
    }

    public BankQueryUserMessage getUserStatus(Role role, String loginNameOrMobile) {
        UserModel userModel = userMapper.findByLoginNameOrMobile(loginNameOrMobile);
        if (userModel == null) {
            return new BankQueryUserMessage(false, "用户不存在");
        }
        BankAccountModel bankAccountModel = bankAccountMapper.findByLoginNameAndRole(userModel.getLoginName(), role);
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
        if (!loanModel.getIsBankPlatform()) {
            return new BankQueryLoanMessage(false, "该标的为联动优势标的");
        }
        return bankWrapperClient.queryLoan(loanModel.getLoanTxNo(), loanModel.getLoanAccNo());
    }

    public BankQueryTradeMessage getTradeStatus(String bankOrderNo, Date bankOrderDate, QueryTradeType queryTradeType) {
        return bankWrapperClient.queryTrade(bankOrderNo, new DateTime(bankOrderDate).toString("yyyyMMdd"), queryTradeType);
    }

    public BankQueryLogAccountMessage getAccountBill(Role role, String loginNameOrMobile, Date queryOrderDateStart, Date queryOrderDateEnd) {
        UserModel userModel = userMapper.findByLoginNameOrMobile(loginNameOrMobile);
        if (userModel == null) {
            return new BankQueryLogAccountMessage(false, "用户不存在");
        }
        BankAccountModel bankAccountModel = bankAccountMapper.findByLoginNameAndRole(userModel.getLoginName(), role);
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

    public Map<String, String> getUmpData(String type, String mobile, String loanId, String orderId, Date merDate, String businessType) {
        if (StringUtils.isEmpty(type)) {
            return null;
        }
        switch (type) {
            case "user":
                if (StringUtils.isEmpty(mobile)) {
                    return null;
                }
                UserModel userModel = userMapper.findByLoginNameOrMobile(mobile);
                if (userModel == null) {
                    return null;
                }
                AccountModel accountModel = accountMapper.findByLoginName(userModel.getLoginName());
                if (accountModel == null) {
                    return null;
                }
                return bankWrapperClient.getUmpUserStatus(accountModel.getPayUserId());
            case "platform":
                return bankWrapperClient.getUmpPlatformStatus();
            case "loan":
                if (StringUtils.isEmpty(loanId)) {
                    return null;
                }
                return bankWrapperClient.getUmpLoanStatus(Long.valueOf(loanId.trim()));
            case "transfer":
                if (StringUtils.isEmpty(orderId) || merDate == null || StringUtils.isEmpty(businessType)) {
                    return null;
                }
                return bankWrapperClient.getUmpTransferStatus(orderId, merDate, businessType);
        }
        return null;
    }

    public List<List<String>> getUmpTransferBill(String loginName, Date startDate, Date endDate) {
        if (StringUtils.isEmpty(loginName)) {
            return null;
        }
        UserModel userModel = userMapper.findByLoginNameOrMobile(loginName);
        if (userModel == null) {
            return null;
        }
        AccountModel accountModel = accountMapper.findByLoginName(userModel.getLoginName());
        if (accountModel == null) {
            return null;
        }
        return bankWrapperClient.getUmpTransferBill(accountModel.getPayAccountId(), startDate, endDate);
    }
}
