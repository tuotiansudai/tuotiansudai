<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="hero_ranking.js" headLab="activity-manage" sideLab="heroRanking" title="周年庆管理">

<div class="col-md-10" style="margin-bottom:50px;">
    <div class="col-md-12">
        <div class="btn btn-default invest active">投资榜单查看</div>
        <div class="btn btn-default referrer">推荐榜单查看</div>
    </div>
</div>
<div class="col-md-10" style="margin-bottom:20px;">
    <div class="col-md-2">
        <div class='input-group date' id='datepicker'>
            <input type='text' class="form-control" id = 'tradingTime' value="${tradingTime?string('yyyy-MM-dd')}"/>
            <span class="input-group-addon">
                <span class="glyphicon glyphicon-calendar"></span>
            </span>
        </div>
    </div>
    <div class="col-md-10" style="line-height: 34px;">
        <strong>当日前十平均投资金额：${(avgInvestAmount/100)?string('0.00')}元</strong>
    </div>
</div>
<div class="col-md-10">
    <div class="table-responsive col-md-8 invest-ranking">
        <table class="table table-bordered table-hover ">
            <thead>
                <tr>
                    <th>排名</th>
                    <th>用户名</th>
                    <th>手机号</th>
                    <th>姓名</th>
                    <th>今日投资金额(元)</th>
                </tr>
            </thead>
            <tbody>
            <#list heroRankingViewInvestList as heroRankingViewInvest>
                <#assign varInvest = 0>
            <tr>
                <td>${varInvest+1}</td>
                <td>${heroRankingViewInvest.loginName!}</td>
                <td>${heroRankingViewInvest.mobile?string('0')}</td>
                <td>${heroRankingViewInvest.userName!}</td>
                <td>${(heroRankingViewInvest.sumAmount/100)?string('0.00')}</td>
            </tr>
            </#list>
            </tbody>
        </table>
    </div>

    <div class="table-responsive col-md-8 referrer-ranking" style="display: none">
        <table class="table table-bordered table-hover ">
            <thead>
            <tr>
                <th>排名</th>
                <th>用户名</th>
                <th>手机号</th>
                <th>姓名</th>
                <th>今日推荐投资金额(元)</th>
            </tr>
            </thead>
            <tbody>
            <#list heroRankingViewReferrerList as heroRankingViewReferrer>
                <#assign varReferrer = 0>
            <tr>
                <td>${varReferrer+1}</td>
                <td>${heroRankingViewReferrer.loginName!}</td>
                <td>${heroRankingViewReferrer.mobile?string('0')}</td>
                <td>${heroRankingViewReferrer.userName!}</td>
                <td>${(heroRankingViewReferrer.sumAmount/100)?string('0.00')}</td>
            </tr>
            </#list>
            </tbody>
        </table>
    </div>

</div>

</@global.main>