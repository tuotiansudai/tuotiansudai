<#import "../macro/global-dev.ftl" as global>
<#assign jsName = 'invest_detail' >
<#assign js = {"${jsName}":"http://localhost:3008/wapSite/js/account/${jsName}.js"} >
<#assign css = {"${jsName}":"http://localhost:3008/wapSite/js/account/${jsName}.css"}>

<@global.main pageCss="${css.invest_detail}" pageJavascript="${js.invest_detail}" title="体验金项目">

<div class="my-account-content amount-detail experience-detail" id="wrapperOut" >

    <div class="amount-detail-inner" >
        <ul class="input-list">
            <li>
                <label>投资金额</label>
                <em>50元(体验金)</em>
            </li>

            <li>
                <label>预期总收益</label>
                <em>0.05元</em>
            </li>
            <li>
                <label>已收回款</label>
                <em>0.00元</em>
            </li>
            <li>
                <label>待收回款</label>
                <em>0.05元</em>
            </li>
            <li>
                <label>起息日</label>
                <em>2016/09/19</em>
            </li>
            <li>
                <label>到期日</label>
                <em>2016/12/30</em>
            </li>
        </ul>

        <dl class="payment-plan experience">
            <dt>回款计划</dt>
            <dd>
                <b>温馨提示:</b><br/>
                投资体验项目的收益，用户需投资直投项目累计满1000元即可提现（投资债权转让项目除外）；

            </dd>

        </dl>
        <ul class="input-list">
            <li>
                <label>预期年化收益</label>
                <em>13.00%</em>
            </li>

            <li>
                <label>项目期限</label>
                <em>3天</em>
            </li>

            <li>
                <label>购买日期</label>
                <em>2017/03/12 11:50:19</em>
            </li>
        </ul>
    </div>
</div>

</@global.main>
