<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="" headLab="activity-manage" sideLab="heroRanking" title="周年庆管理">

<div class="col-md-10">

    <div class="btn">投资榜单查看</div>
    <div class="btn">推荐榜单查看</div>
    <div class="btn">发布神秘大奖</div>

    <div class='input-group date' id='datepicker'>
        <input type='text' class="form-control" name="tradeTime" value="${(tradeTime?string('yyyy-MM-dd'))!}"/>
                        <span class="input-group-addon">
                            <span class="glyphicon glyphicon-calendar"></span>
                        </span>
    </div>
    当日前十平均投资金额：${avgInvestAmount/100?string('0.00')!}元
    <br>
    <div class="table-responsive">
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
                <td>${heroRankingViewInvest.mobile?string('0')!}</td>
                <td>${heroRankingViewInvest.userName!}</td>
                <td>${heroRankingViewInvest.sumAmount/100?string('0.00')!}</td>
            </tr>
            </#list>
            </tbody>
        </table>
    </div>

    <div class="table-responsive">
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
                <td>${heroRankingViewReferrer.mobile?string('0')!}</td>
                <td>${heroRankingViewReferrer.userName!}</td>
                <td>${heroRankingViewReferrer.sumAmount/100?string('0.00')!}</td>
            </tr>
            </#list>
            </tbody>
        </table>
    </div>

    <div>
        
    </div>

</div>

</@global.main>