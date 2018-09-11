package com.tuotiansudai.fudian.dto.response;

public class QueryTradeContentDto extends BaseContentDto {

    private String queryOrderNo;

    private String queryOrderDate;

    private String queryType; // 01充值 02提现 03投标 04借款人还款 05投资人回款 06债权认购 07满标放款

    /*
    01：1成功、2失败、4银行处理中
    02：0申请提现、1提现成功、2提现失败、3提现失败银行退单、4提现异常
    03：0:可投资,1:投资中,2:放款成功,3:还款中,4:还款完成,5:撤标
    04: 1放款成功 其他代表失败
    05: 1回款成功 其他代表失败
    06: 0未支付  1支付成功
    07: 0:可投资,1:投资中,2:放款成功,3:还款中,4:还款完成,5:撤标
    */
    private String queryState;

    public String getQueryOrderNo() {
        return queryOrderNo;
    }

    public void setQueryOrderNo(String queryOrderNo) {
        this.queryOrderNo = queryOrderNo;
    }

    public String getQueryOrderDate() {
        return queryOrderDate;
    }

    public void setQueryOrderDate(String queryOrderDate) {
        this.queryOrderDate = queryOrderDate;
    }

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    public String getQueryState() {
        return queryState;
    }

    public void setQueryState(String queryState) {
        this.queryState = queryState;
    }
}
