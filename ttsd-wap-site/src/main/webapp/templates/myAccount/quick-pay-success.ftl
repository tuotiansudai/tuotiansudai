<#import "../macro/global-dev.ftl" as global>

<#assign jsName = 'quick_pay_manage' >
<#assign js = {"${jsName}":"http://localhost:3008/wapSite/js/account/${jsName}.js"} >
<#assign css = {"${jsName}":"http://localhost:3008/wapSite/js/account/${jsName}.css"}>

<@global.main pageCss="${css.quick_pay_manage}" pageJavascript="${js.quick_pay_manage}" title="绑卡成功">

<div class="my-account-content bank-card-success" >

    <div class="info">
        <i class="icon-success"></i>
        <em>快捷支付开通成功</em>

        <ul class="input-list">
            <li>
                <label for="perMobile">开户银行</label>
                    <span>中国银行</span>

            </li>
            <li>
                <label for="perName">银行卡号</label>
                <span>142733***541</span>
            </li>

        </ul>

    </div>
        <div class="button-note">
            <a href="#" class="btn-wap-normal next-step" >确定</a>
            <span>客服电话：400-169-1188（服务时间：9:00-20:00）</span>
        </div>




</div>
</@global.main>
