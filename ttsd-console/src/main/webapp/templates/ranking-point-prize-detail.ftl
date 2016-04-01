<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="" headLab="activity-manage" sideLab="pointPrize" title="中奖用户列表">

<!-- content area begin -->

    <#if pointPrizeWinnerGroupDetails ?? && (pointPrizeWinnerGroupDetails?size>0)>
    <div class="table-responsive">
        <table class="table table-bordered table-hover">
            <thead>
            <tr>
                <th>中奖时间</th>
                <th>用户名</th>
                <th>手机号</th>
                <th>身份证号</th>
            </tr>
            </thead>
            <tbody>
                <#list pointPrizeWinnerGroupDetails as pointPrizeWinnerGroupDetail>
                <tr>
                    <td>${pointPrizeWinnerGroupDetail.createdTime?string('yyyy-MM-dd HH:mm')!}</td>
                    <td>${pointPrizeWinnerGroupDetail.loginName!}</td>
                    <td>${pointPrizeWinnerGroupDetail.mobile!}</td>
                    <td>${pointPrizeWinnerGroupDetail.identityNumber!}</td>
                </tr>
                </#list>
            </tbody>
        </table>
    </div>
    <#else>
    <div class="col-md-10">
        没有中奖记录
    </div>
    </#if>
</@global.main>