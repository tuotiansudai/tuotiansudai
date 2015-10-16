package com.tuotiansudai.dto;

import java.util.List;

/**
 * Created by Administrator on 2015/9/18.
 */
public class FundManagementDto extends BasePaginationDataDto<UserBillDto> {

    private String balance;

    private String sumRecharge;

    private String sumWithdraw;

    public FundManagementDto(int index, int pageSize, long count, List<UserBillDto> records) {
        super(index, pageSize, count, records);
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getSumRecharge() {
        return sumRecharge;
    }

    public void setSumRecharge(String sumRecharge) {
        this.sumRecharge = sumRecharge;
    }

    public String getSumWithdraw() {
        return sumWithdraw;
    }

    public void setSumWithdraw(String sumWithdraw) {
        this.sumWithdraw = sumWithdraw;
    }
}
