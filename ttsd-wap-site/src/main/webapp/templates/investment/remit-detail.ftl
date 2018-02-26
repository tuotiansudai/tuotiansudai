<#import "../macro/global-dev.ftl" as global>
<#assign jsName = 'remit_detail' >
<#assign js = {"${jsName}":"http://localhost:3008/wapSite/js/investment/${jsName}.js"} >
<#assign css = {"${jsName}":"http://localhost:3008/wapSite/js/investment/${jsName}.css"}>

<@global.main pageCss="${css.remit_detail}" pageJavascript="${js.remit_detail}" title="回款详情">

<div class="my-account-content amount-detail" id="wrapperOut" >

    <div class="amount-detail-inner">

        <dl class="payment-plan">
            <dt>
                <span>回款时间</span>
                <span>金额</span>
                <span>还款状态</span>
            </dt>
            <dd>
                <span>2016/10/18</span>
                <span>0.46元</span>
                <em>已完成</em>
            </dd>
            <dd>
                <span>2016/10/18</span>
                <span>0.46元</span>
                <em class="status">待还款</em>
            </dd>
            <dd>
                <span>2016/10/18</span>
                <span>0.46元</span>
                <em class="status">待还款</em>
            </dd>
        </dl>

        
    </div>
</div>


</@global.main>
