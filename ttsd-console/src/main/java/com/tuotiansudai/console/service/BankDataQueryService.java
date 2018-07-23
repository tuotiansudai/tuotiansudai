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
import java.util.Map;

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

    public BankQueryUserMessage getUserStatus(Role role,String loginNameOrMobile) {
        UserModel userModel = userMapper.findByLoginNameOrMobile(loginNameOrMobile);
        if (userModel == null) {
            return new BankQueryUserMessage(false, "用户不存在");
        }
        if(role == Role.INVESTOR){
            //todo 查询联动优势账户类型  原来的查询不需要 account类型
            return null;
        }else{
            BankAccountModel bankAccountModel = bankAccountMapper.findByLoginNameAndRole(userModel.getLoginName(), role);
            if (bankAccountModel == null) {
                return new BankQueryUserMessage(false, "存管账户不存在");
            }
            return bankWrapperClient.queryUser(bankAccountModel.getBankUserName(), bankAccountModel.getBankAccountNo());
        }


    }

    public BankQueryLoanMessage getLoanStatus(Role role,long loanId) {
        LoanModel loanModel = loanMapper.findById(loanId);
        if (loanModel == null) {
            return new BankQueryLoanMessage(false, "标的不存在");
        }
        if(loanModel.getIsBankPlatform() && role==Role.INVESTOR){
            return new BankQueryLoanMessage(false, "该标的为联动优势标的");
        }
        if(!loanModel.getIsBankPlatform() && (role==Role.BANK_INVESTOR || role == Role.BANK_LOANER)){
            return new BankQueryLoanMessage(false, "该标的为富滇银行标的");
        }
        if(loanModel.getIsBankPlatform()){
            return bankWrapperClient.queryLoan(loanModel.getLoanTxNo(), loanModel.getLoanAccNo());
        }
        //todo 返回联动优势 标的数据
        return null;
    }

    public BankQueryTradeMessage getTradeStatus(Role role,String bankOrderNo, Date bankOrderDate, QueryTradeType queryTradeType) {
        if(role == Role.INVESTOR){
            //查询联动优势的交易状态
            return null;
        }else{
            return bankWrapperClient.queryTrade(bankOrderNo, new DateTime(bankOrderDate).toString("yyyyMMdd"), queryTradeType);
        }
    }

    public BankQueryLogAccountMessage getAccountBill(Role role,String loginNameOrMobile, Date queryOrderDateStart, Date queryOrderDateEnd) {
        UserModel userModel = userMapper.findByLoginNameOrMobile(loginNameOrMobile);
        if (userModel == null) {
            return new BankQueryLogAccountMessage(false, "用户不存在");
        }
       if(role == Role.INVESTOR){
            //查询联动优势 交易流水
           return null;
       }else{
           BankAccountModel bankAccountModel = bankAccountMapper.findByLoginNameAndRole(userModel.getLoginName(), role);
           if (bankAccountModel == null) {
               return new BankQueryLogAccountMessage(false, "存管账户不存在");
           }
           return bankWrapperClient.queryAccountBill(bankAccountModel.getBankUserName(), bankAccountModel.getBankAccountNo(), queryOrderDateStart, queryOrderDateEnd);
       }
    }

    public BankQueryLogLoanAccountMessage getLoanBill(long loanId) {
        LoanModel loanModel = loanMapper.findById(loanId);
        if (loanModel == null) {
            return new BankQueryLogLoanAccountMessage(false, "标的不存在");
        }
        return bankWrapperClient.queryLoanBill(loanModel.getLoanTxNo(), loanModel.getLoanAccNo());
    }
}
