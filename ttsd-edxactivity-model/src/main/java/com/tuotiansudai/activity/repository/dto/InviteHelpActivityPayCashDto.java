package com.tuotiansudai.activity.repository.dto;


import com.tuotiansudai.dto.TransferCashDto;
import com.tuotiansudai.enums.SystemBillBusinessType;
import com.tuotiansudai.enums.SystemBillDetailTemplate;
import com.tuotiansudai.enums.UserBillBusinessType;

public class InviteHelpActivityPayCashDto extends TransferCashDto{

    private String openid;

    public InviteHelpActivityPayCashDto() {
    }

    public InviteHelpActivityPayCashDto(String openid, String loginName, String orderId, String amount, UserBillBusinessType userBillBusinessType, SystemBillBusinessType systemBillBusinessType, SystemBillDetailTemplate systemBillDetailTemplate) {
        super(loginName, orderId, amount, userBillBusinessType, systemBillBusinessType, systemBillDetailTemplate);
        this.openid = openid;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }
}
