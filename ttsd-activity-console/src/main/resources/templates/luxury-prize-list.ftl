<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="" headLab="activity-manage" sideLab="activityCenter" title="奢侈品更新">

<div class="col-md-10">
    <div class="panel panel-default">
        <div class="panel-body">
            <a class="btn btn-default" href="/activity-console/activity-manage/travel/user-travel-list" role="button">旅游奖品获奖记录</a>
            <a class="btn btn-default btn-primary" href="/activity-console/activity-manage/travel/travel-prize-list" role="button">旅游奖品更新</a>
            <a class="btn btn-default" href="#" role="button">奢侈品获奖记录</a>
            <a class="btn btn-default" href="#" role="button">奢侈品更新</a>
        </div>
    </div>

    <div class="table-responsive">
        <table class="table table-bordered table-hover">
            <thead>
            <tr>
                <th>奖品名称</th>
                <th>奖品价值（元）</th>
                <th>奖品图片</th>
                <th>奖品介绍</th>
                <th>获奖资格</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
                <#list data.data.records as item>
                <tr>
                    <td>${item.name}</td>
                    <td>${item.price}</td>
                    <td><img src="${item.image}"></td>
                    <td><a href="" target="_blank">查看</a></td>
                    <td>投资满${item.investAmount}</td>
                    <td><a href="/activity-console/activity-manage/luxury/${item.id?c}/edit">更新</a></td>
                </tr>
                <#else>
                <tr>
                    <td colspan="7">暂无数据</td>
                </tr>
                </#list>
            </tbody>

        </table>
    </div>
</div>
<!-- content area end -->
</@global.main>