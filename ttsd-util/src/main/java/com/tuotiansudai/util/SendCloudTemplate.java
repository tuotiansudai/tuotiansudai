package com.tuotiansudai.util;

import java.util.Map;

public enum SendCloudTemplate {

    //该模板已不使用
    REPAY_COMPLETE_EMAIL("标的还款成功邮件提醒","尊敬的拓天速贷客户，您参与的平台投标项目 “{loanName}”，第{periods}期于{repayDate} 还款{amount}元，请注意查收。【拓天速贷】"),

    //使用中的模板
    ACTIVE_EMAIL("邮箱激活","尊敬的用户{loginName}:<br>恭喜您，已经进入邮箱绑定流程：<br>请点击下面的链接<a href='{activeUrl}'>验证并绑定</a>该邮箱:<br><a href='{activeUrl}'>{activeUrl}</a> 【拓天速贷】"),
    LOAN_OUT_SUCCESSFUL_EMAIL("标的放款成功邮件提醒","尊敬的拓天速贷客户，您在平台的项目借款 “{loanName}” 已成功放款，您的投资金额为{amount}元。【拓天速贷】"),

    USER_BALANCE_CHECK_RESULT_HEADER("用户账户对账结果",
            "<div>开始时间：{startTime}</div></br>" +
            "<div>结束时间：{endTime}</div></br>" +
            "<div>环境：{env}</div></br>" +
            "<div>本次共检查 {totalUserCount} 个用户</div></br>" +
            "<div>其中 {mismatchUserCount} 个用户的帐户余额错误。</div></br>" +
            "<table border=\"1\"><thead><tr><th>用户名</th><th>拓天账户余额</th><th>联动优势账户余额</th></tr></thead><tbody>"),
    USER_BALANCE_CHECK_RESULT_BODY("用户账户对账结果", "<tr><td>{loginName}</td><td>{ttsdBalance}</td><td>{umpayBalance}</td></tr>"),
    USER_BALANCE_CHECK_RESULT_TAIL("用户账户对账结果", "</tbody></table>"),

    FUDIAN_CHECK_RESULT_HEADER("富滇银行对账结果", "{title}, 共计发生交易{total}笔,当前查询{count}笔</br><table border='1'><thead>查询结果</thead><tbody><tr><th>订单号</th><th>对账结果</th></tr>"),
    FUDIAN_CHECK_RESULT_BODY("富滇银行对账结果", "<tr><td>{orderNo}</td><td>{result}</td></tr>"),
    FUDIAN_CHECK_RESULT_TAIL("富滇银行对账结果", "</tbody></table></br>"),

    ;

    private String title;
    private String template;

    SendCloudTemplate(String title, String template) {
        this.title = title;
        this.template = template;
    }

    public String generateContent(Map<String, String> templateParameters) {
        String content = this.template;
        for (Map.Entry<String, String> entry : templateParameters.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            content = content.replace("{" + key + "}", value);
        }
        return content;
    }

    public String getTitle() {
        return title;
    }

    public String getTemplate() {
        return template;
    }
}
