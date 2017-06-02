<#import "../macro/global.ftl" as global>
<@global.main pageCss="${css.hero_ranking_2017}" pageJavascript="${js.hero_ranking_2017}" activeNav="" activeLeftNav="" title="周年庆活动_拓天活动_拓天速贷" keywords="拓天速贷,拓天活动.生日活动,生日月特权" description="拓天速贷专属生日月特权,生日月投资收益翻倍,拓天速贷专属活动超高收益等你拿.">

<div class="banner-slide" id="bannerSlide"></div>
<div class="activity-page-frame page-width" id="activityPageFrame">
<#--${activityStartTime}-->
    <div class="rule-box clearfix">
        <dl class="title-rule">
            <dt>活动规则</dt>
            <dd>活动期间，每天24点计算当日新增投资排名，上榜者可获丰厚奖励。</dd>
        </dl>
        <div class="reward-list ">
            <dl class="reward-one" >
                <dt>
                 <#if prizeDto??>
                     <img data-reward="${prizeDto.goldPrizeName}" src="${commonStaticServer}${prizeDto.goldImageUrl}"  id="rewardOne">
                </#if>
                </dt>
                <dd>第1名</dd>
            </dl>

            <dl class="coupon-one">
                <dt></dt>
                <dd>第2～4名</dd>
            </dl>
            <dl class="coupon-two">
                <dt></dt>
                <dd>第5～10名</dd>
            </dl>
        </div>
    </div>

    <div class="heroes-list clearfix">
        <div class="title-head"></div>

        <dl class="sort-box" id="sortBox">
            <dd class="fl">日期：<i class="date" > ${currentTime?string('yyyy-MM-dd')}</i></dd>
            <dd class="ranking">

                <@global.isAnonymous>
                    <span class="show-login">登录后查看</span>
                </@global.isAnonymous>
                <@global.isNotAnonymous>
                    <#--<#if investRanking &gt; 20 || investRanking == 0>未上榜<#else>我的排名：${investRanking}</#if>-->
                    我的排名:<i class="ranking-order">3</i>
                </@global.isNotAnonymous>

            </dd>
            <dd class="fr">今日投资总额：<i class="total">${(investAmount/100)?string('0.00')}</i>元</dd>
        </dl>
        <div class="nodata-invest tc" style="display: none;"></div>
        <table class="table-reward">
            <thead>
            <tr>
                <th width="20%">排名</th>
                <th width="25%">用户</th>
                <th width="25%">投资额</th>
                <th >奖励</th>
            </tr>
            </thead>
            <tbody id="investRanking-tbody">
            </tbody>
        </table>

        <div class="date-button" id="investRanking-button">
            <span class="button-small" id="heroPre">查看前一天</span>
            <span class="btn-to-invest" id="toInvest">立即投资抢占排行榜</span>
            <span class="button-small" id="heroNext" style="display: none">查看后一天</span>
        </div>

    </div>

    <div class="note-box">
        <b>温馨提示：</b>
        1、活动期间，每天24点计算当日新增投资排名，投资者在当日24点之前进行的多次投资，金额可累计计算； <br/>
        2、排行榜仅限于直投项目，其余投资不计入今日投资金额中；<br/>
        3、排行榜中奖人数最多10名，如遇金额一致，则当日先达到该投资额的用户优先获奖，其他用户名次顺延；<br/>
        4、每日投资排行榜排名将在活动页面实时更新，加息券奖励在每日24点榜单生成后即时发放，实物奖品将于活动结束后七个工作日内统一安排发放；<br/>
        5、拓天速贷会根据活动的情况，以等值、增值为基础调整奖品类型；<br/>
        6、为了保证获奖结果的公平性，实物大奖获奖用户在活动期间所进行的所有投标不允许进行债权转让，否则奖品将不予发放；<br/>
        7、拓天速贷在法律范围内保留对本活动的最终解释权。
    </div>
</div>

<script type="text/template" id="tplTable">
    <% for(var i = 0; i < records.length; i++) {
    var item = records[i];
    var reward;
    if(i==0) {
    reward=rewardOne;
    }
    else if(i>0 && i<4) {
    reward='1%加息券';
    }
    else {
    reward='0.8%加息券';
    }
    %>
    <tr>
        <td><%=i+1%></td>
        <td><%=item.loginName%></td>
        <td><%=item.centSumAmount%></td>
        <td><%=reward%></td>
    </tr>
    <% } %>
</script>

    <#include "../module/login-tip.ftl" />
</@global.main>