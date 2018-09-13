<#import "../../macro/global.ftl" as global>
<@global.main pageCss="${css.mid_autumn_festival_2018}" pageJavascript="${js.mid_autumn_festival_2018}" activeNav="" activeLeftNav="" title="中秋国庆活动_投资排行榜_拓天速贷" keywords="拓天速贷,投资排行,加息券,实物大奖" description='拓天速贷中午国庆活动,活动期间每日22点计算当日新增投资排名,上榜者可获得实物大奖、加息券、京东E卡等丰厚奖励,立即投资抢占英雄榜,先到先得.'>
<div class="mid-banner compliance-banner">
    <div class="invest-tip tip-width">市场有风险，投资需谨慎！</div>

</div>
<div class="activity-rules">
    <div class="page-width clearfix inner">
        <div class="rule-title">
            活动规则
        </div>
        <div class="rule-content">
            <ul>
                <li class="paddingTop0"><span class="dian"></span>活动期间，每天22：00计算当日新增投资排名，上榜者可获丰厚奖励。</li>
                <li class="borderTop"><span class="dian"></span>投资者在当日22:00之前进行的多次投资，<span
                        class="mobileStyle">金额可累计计算。</span></li>
                <li class="borderTop borderNone lastLi"><span class="dian"></span>截止时间以活动页面倒计时为准。</li>
            </ul>
        </div>

    </div>
</div>
<div class="cut-down-section mobile-style">
    <div class="top"></div>
    <div class="content">
        <div class="cut-tip"></div>
        <div class="cut-down-wrap clearfix cutDownDOM">
            <span class="num hourDOM">&nbsp;&nbsp;</span><span class="dot">:</span><span class="num minutesDOM">&nbsp;&nbsp;</span><span
                class="dot">:</span><span class="num secondDOM">&nbsp;&nbsp;</span>
        </div>
    </div>
    <div class="bot"></div>
</div>
<div class="prize-wrap page-width">
    <div class="big-prize-title">

    </div>
    <p class="big-prize-des"><#if prizeDto??>${prizeDto.goldPrizeName}<#else>实物大奖</#if></p>

    <div class="three-prize-bg">
        <div class="phone">
            <#if prizeDto??>
                <img src="${commonStaticServer}${prizeDto.goldImageUrl}" alt="">
            <#else>
                <img id="staticImg" src="" alt="">
            </#if>
        </div>
    </div>
    <div class="other-prize clearfix">
        <div class="seconed prize-box fl"></div>
        <div class="third prize-box fr"></div>
    </div>

</div>
<div class="cut-down-section pc-style">
    <div class="top"></div>
    <div class="content">
        <div class="cut-tip"></div>
        <div class="cut-down-wrap clearfix cutDownDOM">
            <span class="num hourDOM">&nbsp;&nbsp;</span><span class="dot">:</span><span class="num minutesDOM">&nbsp;&nbsp;</span><span
                class="dot">:</span><span class="num secondDOM">&nbsp;&nbsp;</span>
        </div>
    </div>
    <div class="bot"></div>
</div>
<div class="heroic-list-section">
    <div class="top"></div>

    <div class="content">
        <div class="horo-top page-width">
            <i id="activity_status" data-starttime="${activityStartTime!}" data-overtime="${activityEndTime!}"
               style="visibility: hidden"></i>

            <div class="money"></div>
            <div class="person"></div>
            <div class="title-font"></div>
            <ul class="clearfix">
                <li><span class="icon icon-time"></span>时间：<span id="dateTime"></span></li>
                <li>
                <@global.isAnonymous><span class="icon icon-rank"></span>我的排名：<a href="javascript:;" id="loginTipBtn">登录后查看</a></@global.isAnonymous>
                <@global.isNotAnonymous><span class="icon icon-rank"></span>
                    <span><#if investRanking &gt; 20 || investRanking == 0>未上榜<#else>
                        我的排名：${investRanking}</#if></span></span></@global.isNotAnonymous>
                </li>
                <li>
                <@global.isAnonymous><span class="icon icon-invest"></span>今日投资额：<a href="javascript:;"
                                                                                    id="loginTipBtnInvest">登录后查看</a></@global.isAnonymous>
                <@global.isNotAnonymous><span class="icon icon-invest"></span>今日投资额：
                    <span>${(investAmount/100)?string('0.00')}元</span></@global.isNotAnonymous>
                </li>
            </ul>

        </div>
    </div>
    <div class="bot"></div>
    <input id="currentDate" type="hidden" value="">
    <div class="horo-wrap">
        <div class="title"></div>
    <div class="horo-list-wrap" id="contentList">
    </div>
        <div class="bot"></div>
        <div class="change-btn prev-btn" id="rankingPre"></div>
        <div class="change-btn next-btn" id="rankingNext"></div>
    </div>

    <div class="loan-bg">
        <div class="loan-btn"><a href="/loan-list"></a></div>
        <div class="big-red-ware"></div>
    </div>
</div>
<div class="kindly-tip">
    <div class="page-width">
        <div class="kindly-title">温馨提示：</div>
        <div class="kindly-content">
            <p class="first">1. 本活动仅限直投项目，债权转让及新手专享项目不参与累计；</p>
            <p>2. 每日投资排行榜排名将在活动页面实时更新，排行榜中奖人数最多10名，如遇金额一致，则当日先达到该投资额的用户优先获奖，其他用户名次顺延；</p>
            <p>3. 排行榜活动加息券将于获奖后三个工作日内统一发放，实物奖品将于活动结束后七个工作日内统一安排发放；</p>
            <p>4. 为了保证获奖结果的公平性，排行榜活动中实物大奖获奖用户在活动期间所进行的所有投标不允许进行债权转让，否则奖品将不予发放；</p>
            <p>5. 拓天速贷会根据活动的情况，以等值、增值为基础调整奖品类型；</p>
            <p>6. 活动中如有使用虚假账号、恶意刷奖等违规行为，一经查出拓天速贷有权撤销您的获奖资格；</p>
            <p>7. 活动遵循拓天速贷法律声明，最终解释权归拓天速贷所有。</p>
        </div>
    </div>
</div>
    <#include "../../module/login-tip.ftl" />
    <script type="text/template" id="tplTable">
        <#--<div class="horo-list-wrap">-->
        <%
        if(records.length>0){
        %>
        <div class="top-list">
            <div>排名</div>
            <div>用户名</div>
            <div>投资额（元）</div>
            <div>奖励</div>
        </div>
        <ul class="horo-list">


            <% for(var i = 0; i < records.length; i++) {
            var item = records[i];
            var reward;
            if(i==0) {
            reward='实物大奖';
            }
            else if(i==1) {
            reward='100元京东E卡';
            }
            else {
            reward='1.1%加息券';
            }
            %>
            <li class="clearfix">
                <div><%=i+1%></div>
                <div><%=item.loginName%></div>
                <div><%=item.centSumAmount%></div>
                <div class="last"><%=reward%></div>

            </li>

            <% }

            if(records.length <10 && records.length>0){
            for(var i = 0;i < 10-records.length;i++){
            %>
            <li class="clearfix">
                <div><%=records.length+i+1%></div>
                <div>-</div>
                <div>-</div>
                <div>-</div>
            </li>
            <% }
            }
            %>
        </ul>
        <%
        }else {
        %>
        <div class="no-invest-tip"></div>
        <%
        }
        %>

        <#--</div>-->

    </script>
























</@global.main>

