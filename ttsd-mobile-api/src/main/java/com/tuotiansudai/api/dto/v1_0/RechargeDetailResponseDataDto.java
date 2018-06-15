package com.tuotiansudai.api.dto.v1_0;

import com.tuotiansudai.repository.model.BankRechargeModel;
import com.tuotiansudai.util.AmountConverter;
import io.swagger.annotations.ApiModelProperty;

import java.text.SimpleDateFormat;

public class RechargeDetailResponseDataDto extends BaseResponseDataDto {

    /*** 充值ID（定单号）*/
    @ApiModelProperty(value = "定单号", example = "1")
    private String rechargeId;

    /*** 用户ID */
    @ApiModelProperty(value = "用户ID", example = "wangtuotian")
    private String userId;

    /*** 充值金额 */
    @ApiModelProperty(value = "充值金额", example = "100")
    private String actualMoney;

    /*** 充值类型（暂无数据 此功能暂未实现 */
    @ApiModelProperty(value = "充值类型", example = "")
    private String rechargeType;

    /*** 充值状态 */
    @ApiModelProperty(value = "充值状态", example = "SUCCESS")
    private String status;

    /*** 充值状态描述 */
    @ApiModelProperty(value = "充值状态描述", example = "成功")
    private String statusDesc;

    /*** 充值时间 */
    @ApiModelProperty(value = "充值时间", example = "2016-11-25 14:27:11")
    private String time;

    /*** 到账时间 */
    @ApiModelProperty(value = "到账之间", example = "2016-11-25 14:27:11")
    private String successTime;

    public RechargeDetailResponseDataDto() {

    }

    public RechargeDetailResponseDataDto(BankRechargeModel recharge) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.rechargeId = "" + recharge.getId();
        this.userId = recharge.getLoginName();
        this.actualMoney = AmountConverter.convertCentToString(recharge.getAmount());
        this.rechargeType = "";
        this.status = recharge.getStatus().name();
        this.statusDesc = recharge.getStatus().getDescription();
        this.time = sdf.format(recharge.getCreatedTime());
        this.successTime = sdf.format(recharge.getCreatedTime());
    }

    public String getRechargeId() {
        return rechargeId;
    }

    public void setRechargeId(String rechargeId) {
        this.rechargeId = rechargeId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getActualMoney() {
        return actualMoney;
    }

    public void setActualMoney(String actualMoney) {
        this.actualMoney = actualMoney;
    }

    public String getRechargeType() {
        return rechargeType;
    }

    public void setRechargeType(String rechargeType) {
        this.rechargeType = rechargeType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSuccessTime() {
        return successTime;
    }

    public void setSuccessTime(String successTime) {
        this.successTime = successTime;
    }
}
