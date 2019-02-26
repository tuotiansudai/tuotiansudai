<#import "../macro/global-dev.ftl" as global>

<#assign jsName = 'cash_money' >
<#assign js = {"${jsName}":"http://localhost:3008/wapSite/js/account/${jsName}.js"} >
<#assign css = {"${jsName}":"http://localhost:3008/wapSite/js/account/${jsName}.css"}>

<@global.main pageCss="${css.cash_money}" pageJavascript="${js.cash_money}" title="提现成功">

<div class="my-account-content bank-card-success" >
    <div class="info">
        <i class="icon-success"></i>
        <em>申请提现成功</em>

        <ul class="input-list">
            <li>
                <label>到账卡号</label>
                <span>中国银行  6212 **** 4017</span>
            </li>
            <li>
                <label>提取金额</label>
                <span>15,000.00元</span>
            </li>
            <li>
                <label>订单号</label>
                <span>39414095743040</span>
            </li>
        </ul>
    </div>
    <div class="button-note">
        <a href="#" class="btn-wap-normal next-step" >确定</a>
        <span>客服电话：400-169-1188（服务时间：9:00-18:00）</span>
    </div>
</div>
</@global.main>
