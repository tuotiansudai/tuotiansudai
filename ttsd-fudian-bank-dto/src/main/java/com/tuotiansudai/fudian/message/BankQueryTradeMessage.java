package com.tuotiansudai.fudian.message;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.tuotiansudai.fudian.dto.QueryTradeType;

import java.text.MessageFormat;
import java.util.Map;

public class BankQueryTradeMessage extends BankBaseMessage {

    /*
    01：1成功、2失败、4银行处理中
    02：0申请提现、1提现成功、2提现失败、3提现失败银行退单、4提现异常
    03：0:可投资,1:投资中,2:放款成功,3:还款中,4:还款完成,5:撤标
    04: 1还款成功 其他代表失败
    05: 1回款成功 其他代表失败
    06: 0未支付  1支付成功
    07: 0:可投资,1:投资中,2:放款成功,3:还款中,4:还款完成,5:撤标
    */
    private final static Map<QueryTradeType, Map<String, String>> HUMAN_READABLE_SHEET = Maps.newHashMap(ImmutableMap.<QueryTradeType, Map<String, String>>builder()
            .put(QueryTradeType.RECHARGE, Maps.newHashMap(ImmutableMap.<String, String>builder().put("1", "成功").put("2", "失败").put("3", "银行处理中").build()))
            .put(QueryTradeType.WITHDRAW, Maps.newHashMap(ImmutableMap.<String, String>builder().put("0", "申请提现").put("1", "提现成功").put("2", "提现失败").put("3", "提现失败银行退单").put("4", "提现异常").build()))
            .put(QueryTradeType.LOAN_INVEST, Maps.newHashMap(ImmutableMap.<String, String>builder().put("0", "可投资").put("1", "投资中").put("2", "放款成功").put("3", "还款中").put("4", "还款完成").put("5", "撤标").build()))
            .put(QueryTradeType.LOAN_REPAY, Maps.newHashMap(ImmutableMap.<String, String>builder().put("1", "还款成功").build()))
            .put(QueryTradeType.LOAN_CALLBACK, Maps.newHashMap(ImmutableMap.<String, String>builder().put("1", "回款成功").build()))
            .put(QueryTradeType.LOAN_CREDIT_INVEST, Maps.newHashMap(ImmutableMap.<String, String>builder().put("0", "未支付").put("1", "支付成功").build()))
            .put(QueryTradeType.LOAN_FULL, Maps.newHashMap(ImmutableMap.<String, String>builder().put("0", "可投资").put("1", "投资中").put("2", "放款成功").put("3", "还款中").put("4", "还款完成").put("5", "撤标").build()))
            .build());

    private String bankOrderNo;

    private String bankOrderDate;

    private QueryTradeType queryTradeType;

    private String queryStatus;

    public BankQueryTradeMessage() {
    }

    public BankQueryTradeMessage(boolean status, String message) {
        super(status, message);
    }

    public BankQueryTradeMessage(String bankOrderNo, String bankOrderDate, QueryTradeType queryTradeType, String queryStatus) {
        super(true, null);
        this.bankOrderNo = bankOrderNo;
        this.bankOrderDate = bankOrderDate;
        this.queryTradeType = queryTradeType;
        this.queryStatus = MessageFormat.format("{0}({1})", queryStatus, HUMAN_READABLE_SHEET.get(queryTradeType).getOrDefault(queryStatus, "失败"));
    }

    public String getBankOrderNo() {
        return bankOrderNo;
    }

    public String getBankOrderDate() {
        return bankOrderDate;
    }

    public QueryTradeType getQueryTradeType() {
        return queryTradeType;
    }

    public String getQueryStatus() {
        return queryStatus;
    }
}