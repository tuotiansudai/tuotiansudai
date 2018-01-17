<#import "../macro/global-dev.ftl" as global>
<#assign jsName = 'payment_annual' >
<#assign js = {"${jsName}":"http://localhost:3008/wapSite/js/investment/${jsName}.js"} >
<#assign css = {"${jsName}":"http://localhost:3008/wapSite/js/investment/${jsName}.css"}>

<@global.main pageCss="${css.payment_annual}" pageJavascript="${js.payment_annual}" title="年度回款">

<div class="my-account-content payment-annual"  id="paymentAnnual">
    <script type="text/template" id="paymentAnnualTpl">
        <div class="annual-summary">
        <span class="year">
            2016年
        </span>
        <span>
            <em>年度已回款：<i class="key">6,320.00</i>元</em>
            <em>年度预计待回款：<i class="key"> 60,320.00</i>元</em>
        </span>
        </div>

        <div class="annual-box-list">

            <div class="annual-box">
                <span class="month">1月</span>
                <span>
                <em>预计待回款</em>
                <i>0.00元</i>
            </span>
                <span>
                <em>已回款</em>
                <i>0.00元</i>
            </span>
            </div>

            <div class="annual-box">
                <span class="month">2月</span>
                <span>
                <em>预计待回款</em>
                <i>0.00元</i>
            </span>
                <span>
                <em>已回款</em>
                <i>0.00元</i>
            </span>
            </div>

            <div class="annual-box">
                <span class="month">3月</span>
                <span>
                <em>预计待回款</em>
                <i class="key">0.00元</i>
            </span>
                <span>
                <em>已回款</em>
                <i class="key">0.00元</i>
            </span>
            </div>

            <div class="annual-box">
                <span class="month">3月</span>
                <span>
                <em>预计待回款</em>
                <i class="key">0.00元</i>
            </span>
                <span>
                <em>已回款</em>
                <i class="key">0.00元</i>
            </span>
            </div>

            <div class="annual-box">
                <span class="month">3月</span>
                <span>
                <em>预计待回款</em>
                <i class="key">0.00元</i>
            </span>
                <span>
                <em>已回款</em>
                <i class="key">0.00元</i>
            </span>
            </div>

            <div class="annual-box">
                <span class="month">3月</span>
                <span>
                <em>预计待回款</em>
                <i class="key">0.00元</i>
            </span>
                <span>
                <em>已回款</em>
                <i class="key">0.00元</i>
            </span>
            </div>

            <div class="annual-box">
                <span class="month">3月</span>
                <span>
                <em>预计待回款</em>
                <i class="key">0.00元</i>
            </span>
                <span>
                <em>已回款</em>
                <i class="key">0.00元</i>
            </span>
            </div>

            <div class="annual-box">
                <span class="month">3月</span>
                <span>
                <em>预计待回款</em>
                <i class="key">0.00元</i>
            </span>
                <span>
                <em>已回款</em>
                <i class="key">0.00元</i>
            </span>
            </div>
            <div class="annual-box">
                <span class="month">3月</span>
                <span>
                <em>预计待回款</em>
                <i class="key">0.00元</i>
            </span>
                <span>
                <em>已回款</em>
                <i class="key">0.00元</i>
            </span>
            </div>
            <div class="annual-box">
                <span class="month">3月</span>
                <span>
                <em>预计待回款</em>
                <i>0.00元</i>
            </span>
                <span>
                <em>已回款</em>
                <i>0.00元</i>
            </span>
            </div>
            <div class="annual-box">
                <span class="month">3月</span>
                <span>
                <em>预计待回款</em>
                <i>0.00元</i>
            </span>
                <span>
                <em>已回款</em>
                <i>0.00元</i>
            </span>
            </div>
            <div class="annual-box">
                <span class="month">3月</span>
                <span>
                <em>预计待回款</em>
                <i>0.00元</i>
            </span>
                <span>
                <em>已回款</em>
                <i>0.00元</i>
            </span>
            </div>

        </div>
    </script>



</div>

</@global.main>
