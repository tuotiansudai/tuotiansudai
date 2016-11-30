package com.tuotiansudai.api.dto.v1_0;

import com.tuotiansudai.dto.WithdrawDto;
import com.tuotiansudai.repository.model.Source;
import io.swagger.annotations.ApiModelProperty;

public class WithdrawOperateRequestDto extends BaseParamDto {

    @ApiModelProperty(value = "提现金额", example = "100")
    private double money;

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }


    public WithdrawDto convertToWithdrawDto(){
        WithdrawDto withdrawDto = new WithdrawDto();
        withdrawDto.setAmount("" + this.money);
        withdrawDto.setLoginName(this.getBaseParam().getUserId());
        withdrawDto.setSource(Source.valueOf(this.getBaseParam().getPlatform().toUpperCase()));
        return withdrawDto;
    }
}
