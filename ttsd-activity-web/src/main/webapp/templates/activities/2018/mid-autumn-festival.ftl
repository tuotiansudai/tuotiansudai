<#import "../../macro/global.ftl" as global>
<@global.main pageCss="${css.mid_autumn_festival_2018}" pageJavascript="${js.mid_autumn_festival_2018}" activeNav="" activeLeftNav="" title="假日投资_夺魁英雄榜_拓天速贷" keywords="拓天速贷,投资大礼包,返现奖励,实物大奖" description='拓天速贷4月活动,活动期间关注"拓天速贷服务号",380元投资大礼包带回家,每逢国家法定假日,投资"0.6%返现"标签项目可获得返现奖励,累计投资即可夺魁英雄榜赢取实物大奖.'>
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
                <li>.活动期间，每天22：00计算当日新增投资排名，上榜者可获丰厚奖励。</li>
                <li>.投资者在当日22:00之前进行的多次投资，金额可累计计算。</li>
                <li>.截止时间以活动页面倒计时为准。</li>
            </ul>
        </div>

    </div>
</div>
<div class="prize-wrap page-width">
        <div class="big-prize-title">

        </div>
        <p class="big-prize-des">小米全面屏智能手机全网通4G双卡</p>

        <div class="three-prize-bg">
            <div class="phone">
                <img src="" alt="">
            </div>
        </div>
        <div class="other-prize clearfix">
            <div class="seconed prize-box fl"></div>
            <div class="third prize-box fr"></div>
        </div>

</div>
<div class="cut-down-section">
    <div class="top"></div>
    <div class="content">
        <div class="cut-tip"></div>
        <div class="cut-down-wrap">
            02:53:26
        </div>
    </div>
    <div class="bot"></div>
</div>
<div class="heroic-list-section">
    <div class="horo-top page-width">
        <ul class="clearfix">
            <li><span class="icon icon-time"></span>时间：<span class="date-time" id="dateTime" data-starttime="${activityStartTime}" data-endtime="${activityEndTime}>2018-08-09</span></li>
            <li><span class="icon icon-rank"></span>我的排名：<span>登录后查看</span></li>
            <li><span class="icon icon-invest"></span>今日投资额：<span>登录后查看</span></li>
        </ul>
    </div>
    <div class="horo-wrap">
        <div class="title"></div>
        <div class="horo-list-wrap">
            <div class="top-list"><div>排名</div><div>用户名</div><div>投资额（元）</div><div>奖励</div></div>
            <ul class="horo-list" id="contentList">

                <li class="clearfix"><div>1</div><div>157****8989</div><div>333333.3333</div><div>实物大奖</div></li>
                <li class="clearfix"><div>2</div><div>157****8989</div><div>333333.3333</div><div>实物大奖</div></li>
                <li class="clearfix"><div>3</div><div>157****8989</div><div>333333.3333</div><div>实物大奖</div></li>
                <li class="clearfix"><div>4</div><div>157****8989</div><div>333333.3333</div><div>实物大奖</div></li>

            </ul>
        </div>
        <div class="change-btn prev-btn"></div>
        <div class="change-btn next-btn"></div>
    </div>

    <div class="loan-bg">
        <div class="loan-btn"></div>
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


        <#--<% for(var i = 0; i < records.length; i++) {-->
        <#--var item = records[i];-->
        <#--var reward;-->
        <#--if(i==0) {-->
        <#--reward='实物大奖';-->
        <#--}-->
        <#--else if(i>0 && i<4) {-->
        <#--reward='1.1%加息券';-->
        <#--}-->
        <#--else {-->
        <#--reward='100元京东E卡';-->
        <#--}-->
        <#--%>-->
        <#--<tr>-->
            <#--<td><%=i+1%></td>-->
            <#--<td><%=item.loginName%></td>-->
            <#--<td><%=item.centSumAmount%></td>-->
            <#--<td><%=reward%></td>-->
        <#--</tr>-->
        <#--<% } %>-->



    </script>
























</@global.main>

