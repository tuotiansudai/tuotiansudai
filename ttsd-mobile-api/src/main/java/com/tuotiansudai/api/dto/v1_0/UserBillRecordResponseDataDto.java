package com.tuotiansudai.api.dto.v1_0;

import com.tuotiansudai.enums.BankUserBillBusinessType;
import io.swagger.annotations.ApiModelProperty;

public class UserBillRecordResponseDataDto extends BaseResponseDataDto {

    @ApiModelProperty(value = "时间", example = "2016-11-25 16:27:01")
    private String time;

    @ApiModelProperty(value = "账单类型", example = "RECHARGE_SUCCESS")
    private String typeInfo;

    @ApiModelProperty(value = "金额", example = "100")
    private String money;

    @ApiModelProperty(value = "余额", example = "100")
    private String balance;

    @ApiModelProperty(value = "冻结金额", example = "0")
    private String frozenMoney = "0";

    @ApiModelProperty(value = "明细", example = "可用余额转入")
    private String detail;

    @ApiModelProperty(value = "账单描述", example = "充值成功")
    private String typeInfoDesc;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTypeInfo() {
        return typeInfo;
    }

    public void setTypeInfo(BankUserBillBusinessType typeInfo) {
        if (BankUserBillBusinessType.LOAN_SUCCESS.equals(typeInfo)) {
            this.typeInfo = "give_money_to_borrower";
        } else if (BankUserBillBusinessType.CANCEL_INVEST_PAYBACK.equals(typeInfo)) {
            this.typeInfo = "cancel_loan";
        } else {
            this.typeInfo = typeInfo.name().toLowerCase();
        }
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getFrozenMoney() {
        return frozenMoney;
    }

    public void setFrozenMoney(String frozenMoney) {
        this.frozenMoney = frozenMoney;
    }

    public String getTypeInfoDesc() {
        return typeInfoDesc;
    }

    public void setTypeInfoDesc(String typeInfoDesc) {
        this.typeInfoDesc = typeInfoDesc;
    }
}
