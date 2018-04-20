<#import "../macro/global-dev.ftl" as global>
<#assign jsName = 'loan_detail' >
<#assign js = {"${jsName}":"http://localhost:3008/wapSite/js/investment/${jsName}.js"} >
<#assign css = {"${jsName}":"http://localhost:3008/wapSite/js/investment/${jsName}.css"}>

<@global.main pageCss="${css.loan_detail}" pageJavascript="${js.loan_detail}" title="项目详情－转让详情">

<div class="my-account-content experience-amount" id="loanDetail">

    <div class="account-summary">
        <div class="collection">
            <span class="title">
                ZR20161121-001
            </span>
            <span class="summary-box">
                <b>15<i>%</i></b>
                <em>约定年化利率</em>
            </span>
        </div>

        <div class="amount-balance transfer-hack">
            <span>剩余期限<br/>56天</span>
            <span>转让价格<br/>100,010.00</span>
        </div>

    </div>

    <div class="leave-time">距项目下架时间：4天  24小时  29分</div>
    <!-- <div class="leave-time">已转让</div> -->

    <ul class="detail-list">
        <li>
            <label>项目本金</label>
            <span >101,050.00元</span>

        </li>
        <li>
            <label>预期收益</label>
            <span >
               1,050.00元
            </span>
        </li>
        <li>
            <label>项目到期时间</label>
            <span>2017-12-12</span>
        </li>
        <li class="repay-plan">
            <label>回款计划</label>
            <span><i class="fa fa-angle-right"></i> </span>
        </li>
        <!-- <li class="repay-plan">
            <label>债权承接纪录</label>
            <span><i class="fa fa-angle-right"></i> </span>
        </li> -->
    </ul>

    <div class="history-record">
        <a href="#" >查看原始项目</a>
    </div>


</div>

</@global.main>
