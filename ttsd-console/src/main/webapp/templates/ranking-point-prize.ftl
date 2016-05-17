<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="" headLab="activity-manage" sideLab="pointPrize" title="财豆奖品管理">

<!-- content area begin -->
<div class="table-responsive">
    <table class="table table-bordered table-hover" style="width: 400px">
        <thead>
        <tr>
            <th>奖品</th>
            <th>中奖人数</th>
            <th>状态</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <#list pointPrizeWinnerGroups as pointPrizeWinnerGroup>
            <tr>
                <td>${pointPrizeWinnerGroup.description!}</td>
                <td>${pointPrizeWinnerGroup.num?string('0')!}</td>
                <td>${pointPrizeWinnerGroup.active?string('已生效','未生效')}</td>
                <td><a href="/activity-manage/point-prize-detail?pointPrizeId=${pointPrizeWinnerGroup.id?string('0')!}">查看详情</a></td>
            </tr>
        </#list>
        </tbody>
    </table>
</div>

</@global.main>