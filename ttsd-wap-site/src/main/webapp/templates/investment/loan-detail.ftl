<#import "../macro/global-dev.ftl" as global>
<#assign jsName = 'loan_detail' >
<#assign js = {"${jsName}":"http://localhost:3008/wapSite/js/investment/${jsName}.js"} >
<#assign css = {"${jsName}":"http://localhost:3008/wapSite/js/investment/${jsName}.css"}>

<@global.main pageCss="${css.loan_detail}" pageJavascript="${js.loan_detail}" title="直投借款详情">

<div class="my-account-content experience-amount" id="loanDetail">

    <div class="account-summary">
        <a href="javascript:void(0);"><i class="icon-help"></i></a>
        <div class="collection">
            <span class="title">
                车辆抵押借款17014 <br/>
                <i class="icon-sign">五一专享</i>
                <i class="icon-sign">新手专享</i>
            </span>
            <span class="summary-box">
                <b>15<i>%</i></b>
                <em>预期年化收益</em>
            </span>
        </div>

        <div class="amount-balance">
            仅限体验金投资  不支持债权转让
        </div>

        <div class="invest-refer-box" style="display: none">
            <i class="fa fa-caret-up"></i>
            <ul>
                <li><span class="title">投资金额</span><span class="title">投资奖励</span></li>
                <li><span>投资金额>1000元</span><span>0.3%</span></li>
                <li><span>投资金额>1万元</span><span>0.5%</span></li>
                <li><span>投资金额>5万元</span><span>1%</span></li>
            </ul>
        </div>
    </div>

    <div class="experience-total">
    <span>
        <b>50元</b>
        <i>起投金额</i>
    </span>
        <span>
        <b>最长90天</b>
        <i>项目期限</i>
    </span>
        <span>
        <b>244.11元</b>
        <i>最大万元收益</i>
    </span>
    </div>

   <div class="invest-progress-box">

       <div class="progress-bar">
           <div class="process-percent">
               <div class="percent" style="width:80%">
               </div>
           </div>
       </div>

       <div class="project-info">
           <span>项目总额：<i>50万</i></span>
           <span>剩余金额：<i>122.04万元</i></span>
       </div>
   </div>

    <ul class="detail-list">
        <li>
            <label>投资上限</label>
            <span >500,000,000.00元</span>

        </li>
        <li>
            <label>计息方式</label>
            <span >
               按天计息，放款后生息
            </span>
        </li>
        <li>
            <label>还款方式</label>
            <span>先付收益后还投资本金</span>
        </li>
        <li>
            <label>发布日期</label>
            <span>2016-11-21  11:30:00</span>
        </li>
        <li>
            <label>募集期限</label>
            <span>7天</span>
        </li>
        <li>
            <label>借款协议</label>
            <span>借款转让协议（样本）<i class="fa fa-angle-right"></i> </span>
        </li>
        <li>
            <label>项目详情</label>
            <span><i class="fa fa-angle-right"></i></span>
        </li>
    </ul>
    <button class="to-invest-project" type="button" disabled>已售罄</button>
    <#--<button class="to-invest-project" type="button">立即投资</button>-->
</div>

</@global.main>
