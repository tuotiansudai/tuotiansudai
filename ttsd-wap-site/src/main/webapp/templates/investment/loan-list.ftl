<#import "../macro/global-dev.ftl" as global>
<#assign jsName = 'loan_list' >
<#assign js = {"${jsName}":"http://localhost:3008/wapSite/js/investment/${jsName}.js"} >
<#assign css = {"${jsName}":"http://localhost:3008/wapSite/js/investment/${jsName}.css"}>

<@global.main pageCss="${css.loan_list}" pageJavascript="${js.loan_list}" title="账户总览">

<div class="my-loan-content" id="loanList">
    <div class="menu-category">
        <span class="current"><a href="#">直投项目</a></span>
        <span><a href="#">转让项目</a></span>
    </div>

    <div id="wrapperOut" class="loan-list-frame">
        <div class="loan-list-content">

            <div class="target-category-box newer-experience" data-url="experience-detail.ftl">
                <b class="newer-title">拓天体验金项目 <i class="icon-sign">体验金投资</i></b>
                <ul class="loan-info clearfix">
                    <li><span class="percent-number"> <i>13</i>%</span><em class="note">约定年化利率</em></li>
                    <li><em class="duration-day">3</em> 天 <em class="note">项目期限</em></li>
                    <li><a href="/loan/1" class="btn-invest btn-normal">立即投资</a></li>
                </ul>
            </div>

            <div class="target-category-box" data-url="loan-transfer-detail.ftl">
                <b class="newer-title">房产抵押借款17070 <i class="icon-sign">新手专享</i> <i class="icon-sign">五一专享</i></b>
                <ul class="loan-info clearfix">
                    <li><span class="percent-number"> <i>10.5+10.8</i>%</span><em class="note">约定年化利率</em></li>
                    <li>最长<em class="duration-day">30</em> 天 <em class="note">项目期限</em></li>
                    <li><a href="/loan/1" class="btn-invest btn-normal">立即投资</a></li>
                </ul>
                <div class="table-row progress-column">
                    <div class="progress-bar">
                        <div class="process-percent">
                            <div class="percent" style="width:80%">
                            </div>
                        </div>
                    </div>
                    <span class="p-title">剩余金额：<i>0.00元</i></span>
                </div>
            </div>

            <div class="target-category-box" data-url="loan-detail.ftl">
                <b class="newer-title">房产抵押借款17070</b>
                <ul class="loan-info clearfix">
                    <li><span class="percent-number"> <i>12.5~13.5</i>%</span><em class="note">约定年化利率</em></li>
                    <li>最长<em class="duration-day">30</em> 天 <em class="note">项目期限</em></li>
                    <li><a href="/loan/1" class="btn-invest btn-normal">设置提醒</a></li>
                </ul>
                <div class="table-row progress-column">
                    <span class="time"> 明日 18:28:00开标</span>
                    <span class="p-title">项目总额：<i>0.00元</i></span>
                </div>
            </div>

            <div class="target-category-box" data-url="loan-detail.ftl">
                <b class="newer-title">房产抵押借款17070</b>
                <ul class="loan-info clearfix">
                    <li><span class="percent-number"> <i>10.5+10.8</i>%</span><em class="note">约定年化利率</em></li>
                    <li>最长<em class="duration-day">30</em> 天 <em class="note">项目期限</em></li>
                    <li><a href="/loan/1" class="btn-invest btn-normal">立即投资</a></li>
                </ul>
                <div class="table-row progress-column">
                    <div class="progress-bar">
                        <div class="process-percent">
                            <div class="percent" style="width:80%">
                            </div>
                        </div>
                    </div>
                    <span class="p-title">剩余金额：<i>0.00元</i></span>
                </div>
            </div>

            <div class="target-category-box sold-out-box" data-url="loan-detail.ftl">
                <b class="newer-title">房产抵押借款17070  <i class="icon-sign">五一专享</i></b>
                <ul class="loan-info  clearfix">
                    <li><span class="percent-number"> <i>10.5+10.8</i>%</span><em class="note">约定年化利率</em></li>
                    <li>最长<em class="duration-day">30</em> 天 <em class="note">项目期限</em></li>
                    <li>
                        <i class="loan-status icon-sellout"></i>
                    </li>
                </ul>
                <div class="transfer-price">转让价格：10,085.00元/12,000.00元(原)</div>
            </div>

        </div>

    </div>

</div>
</@global.main>
