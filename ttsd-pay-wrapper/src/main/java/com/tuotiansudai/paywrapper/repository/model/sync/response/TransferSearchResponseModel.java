package com.tuotiansudai.paywrapper.repository.model.sync.response;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.tuotiansudai.util.AmountConverter;
import org.joda.time.format.DateTimeFormat;

import java.util.Map;

public class TransferSearchResponseModel extends BaseSyncResponseModel {

    private static final Map<String, String> HUMAN_READABLE_BUSINESS_TYPE = Maps.newHashMap(ImmutableMap.<String, String>builder()
            .put("01", "充值")
            .put("02", "提现")
            .put("03", "标的转账")
            .put("04", "转账")
            .build());

    private static final Map<String, String> HUMAN_READABLE_COM_AMT_TYPE_TYPE = Maps.newHashMap(ImmutableMap.<String, String>builder()
            .put("1", "前向手续费：交易方承担")
            .put("2", "前向手续费：平台商户（手续费账户）承担")
            .put("-99", "按手续费账户上线时间区分交易，参照如下说明：新交易：非前向收费 存量交易：无法区分，详见商户协议里手续费相关部分。")
            .build());

    private static final Map<String, Map<String, String>> HUMAN_READABLE_TRANSACTION_STATE = Maps.newHashMap(ImmutableMap.<String, Map<String, String>>builder()
            .put("01", ImmutableMap.<String, String>builder()
                    .put("0", "初始")
                    .put("2", "成功")
                    .put("3", "失败")
                    .put("4", "不明")
                    .put("6", "其他")
                    .build())
            .put("02", ImmutableMap.<String, String>builder()
                    .put("0", "初始")
                    .put("1", "受理中")
                    .put("2", "成功")
                    .put("3", "失败")
                    .put("4", "不明（待确认）")
                    .put("5", "交易关闭（提现）")
                    .put("6", "其他（需人工查询跟进）")
                    .put("12", "已冻结")
                    .put("13", "待解冻")
                    .put("15", "财务审核失败")
                    .build())
            .put("03", ImmutableMap.<String, String>builder()
                    .put("0", "初始")
                    .put("2", "成功")
                    .put("3", "失败")
                    .put("4", "不明")
                    .put("5", "交易关闭")
                    .put("6", "其他")
                    .build())
            .put("04", ImmutableMap.<String, String>builder()
                    .put("0", "初始")
                    .put("2", "成功")
                    .put("3", "失败")
                    .put("4", "不明")
                    .put("6", "其他")
                    .build())
            .build());


    private String orderId;

    private String merCheckDate;

    private String merDate;

    private String tradeNo;

    private String busiType;

    private String amount;

    private String orgiAmt;

    private String comAmt;

    private String comAmtType;

    private String tranState;

    private String smsNum;

    @Override
    public void initializeModel(Map<String, String> resData) {
        super.initializeModel(resData);
        this.orderId = resData.get("order_id");
        this.merCheckDate = resData.get("mer_check_date");
        this.merDate = resData.get("mer_date");
        this.tradeNo = resData.get("trade_no");
        this.busiType = resData.get("busi_type");
        this.amount = resData.get("amount");
        this.orgiAmt = resData.get("orgi_amt");
        this.comAmt = resData.get("com_amt");
        this.comAmtType = resData.get("com_amt_type");
        this.tranState = resData.get("tran_state");
        this.smsNum = resData.get("sms_num");
    }

    public Map<String, String> generateHumanReadableInfo() {
        return Maps.newLinkedHashMap(ImmutableMap.<String, String>builder()
                .put("商户订单号", Strings.isNullOrEmpty(this.orderId) ? "" : this.orderId)
                .put("对账日期", Strings.isNullOrEmpty(this.merDate) ? "" : DateTimeFormat.forPattern("yyyyMMdd").parseDateTime(this.merDate).toString("yyyy-MM-dd"))
                .put("交易平台流水号", Strings.isNullOrEmpty(this.tradeNo) ? "" : this.tradeNo)
                .put("业务类型", Strings.isNullOrEmpty(HUMAN_READABLE_BUSINESS_TYPE.get(this.busiType)) ? "" : HUMAN_READABLE_BUSINESS_TYPE.get(this.busiType))
                .put("交易金额", Strings.isNullOrEmpty(this.amount) ? "" : AmountConverter.convertCentToString(Long.parseLong(this.amount)))
                .put("原交易金额", Strings.isNullOrEmpty(this.orgiAmt) ? "" : AmountConverter.convertCentToString(Long.parseLong(this.orgiAmt)))
                .put("手续费", Strings.isNullOrEmpty(this.comAmt) ? "" : AmountConverter.convertCentToString(Long.parseLong(this.comAmt)))
                .put("手续费承担方类型", Strings.isNullOrEmpty(HUMAN_READABLE_COM_AMT_TYPE_TYPE.get(this.comAmtType)) ? "" : HUMAN_READABLE_COM_AMT_TYPE_TYPE.get(this.comAmtType))
                .put("交易状态", Strings.isNullOrEmpty(this.tranState) ? "" : HUMAN_READABLE_TRANSACTION_STATE.get(this.busiType).get(this.tranState))
                .put("短信个数", Strings.isNullOrEmpty(this.smsNum) ? "" : this.smsNum)
        .build());
    }
}
