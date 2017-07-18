<#import "../macro/global-dev.ftl" as global>
<#assign jsName = 'experience_detail' >
<#assign js = {"${jsName}":"http://localhost:3008/wapSite/js/investment/${jsName}.js"} >
<#assign css = {"${jsName}":"http://localhost:3008/wapSite/js/investment/${jsName}.css"}>

<@global.main pageCss="${css.experience_detail}" pageJavascript="" title="我的体验金">

<div class="my-account-content experience-amount" id="experienceAmount">
    <div class="account-summary">
        <div class="collection">
            <span class="title">拓天体验金项目</span>
            <span class="summary-box">
                <b>15<i>%</i></b>
                <em>预期年化收益</em>
            </span>
        </div>

        <div class="amount-balance">
            仅限体验金投资  不支持债权转让
        </div>
    </div>

    <div class="experience-total">
        <span>
            <b>50元</b>
            <i>起投金额</i>
        </span>
        <span>
            <b>3天</b>
            <i>项目期限</i>
        </span>
        <span>
            <b>244.11元</b>
            <i>万元收益</i>
        </span>
    </div>

    <ul class="detail-list">
        <li>
            <label>计息方式</label>
            <span >按天计息，即投即生息</span>

        </li>
        <li>
            <label>还款方式</label>
            <span >
               体验金收回，收益归您
            </span>
        </li>
        <li>
            <label>发布日期</label>
            <span>2016-11-21  11:30:00</span>
        </li>
    </ul>

    <button class="to-invest-project" type="button">立即投资</button>
</div>

</@global.main>
