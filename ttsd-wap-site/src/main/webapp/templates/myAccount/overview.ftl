<#import "../macro/global-dev.ftl" as global>
<#assign jsName = 'overview' >
<#assign js = {"${jsName}":"http://localhost:3008/wapSite/js/account/${jsName}.js"} >
<#assign css = {"${jsName}":"http://localhost:3008/wapSite/js/account/${jsName}.css"}>

<@global.main pageCss="${css.overview}" pageJavascript="${js.overview}" title="账户总览">

<div class="my-account-content amount-overview" id="accountOverview">

    <div class="menu-category">
        <span class="current"><a href="#">账户余额</a></span>
        <span><a href="#">累计收益</a></span>
        <span><a href="#">待收回款</a></span>
    </div>

    <div class="overview-content balance-box" >
        <dl class="balance-one">
            <dd>
                <i class="one"></i> <em>59.40</em>
            </dd>
            <dt>可用余额(元)</dt>
        </dl>
        <dl class="balance-two">
            <dd>
                <i class="two"></i><em>59.40</em>
            </dd>
            <dt>投资冻结中(元)</dt>
        </dl>
        <dl class="balance-three">
            <dd>
                <i class="three"></i><em>59.40</em>
            </dd>
            <dt>提现冻结中(元)</dt>
        </dl>
    </div>

    <div class="overview-content accumulate-income" style="display: none">
        <div class="amount-report">
            <div class="income-report" id="dataRecord"></div>
            <dl>
                <dt>累计收益(元)</dt>
                <dd class="total">59.40</dd>
            </dl>
        </div>
        <div class="amount-detail-show">
            <div class="column">
                <span>
                    已收投资收益(元) <i></i>
                    <em class="money">59.40</em>
                </span>

                <span>
                    已收推荐奖励(元)<i></i>
                    <em class="money">59.40</em>
                </span>

                <span>
                    已收体验金收益(元)<i></i>
                    <em class="money">59.40</em>
                </span>
            </div>
            <div class="column">

                 <span>
                     <i></i>已收投资奖励(元)
                    <em class="money">59.40</em>
                </span>

                <span>
                   <i></i> 已收红包奖励(元)
                    <em class="money">59.40</em>
                </span>

            </div>
        </div>

    </div>
    <div class="overview-content payment-received" style="display: none">
        <div class="amount-report">
            <div class="income-report" id="receivedRecord"></div>
            <dl>
                <dt>待收回款(元)</dt>
                <dd class="total">59.40</dd>
            </dl>
        </div>
        <div class="amount-detail-show">
            <div class="column">
                <span>
                    待收投资本金(元) <i></i>
                    <em class="money">59.40</em>
                </span>

                <span>
                    待收投资奖励(元)<i></i>
                    <em class="money">59.40</em>
                </span>
            </div>
            <div class="column">

                 <span>
                     <i></i>待收预期收益(元)
                    <em class="money">59.40</em>
                </span>

                <span>
                   <i></i> 待收体验金收益(元)
                    <em class="money">59.40</em>
                </span>

            </div>
        </div>

    </div>
</div>
</@global.main>
