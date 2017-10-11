<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="" headLab="activity-manage" sideLab="zeroShoppingActivity" title="0元购活动管理">

<div class="col-md-10">
    <div class="panel panel-default">
        <div class="panel-body">
            <a class="btn btn-default btn-primary" href="/activity-console/activity-manage/travel/user-prize-list" role="button">"0"元购获奖记录</a>
            <a class="btn btn-default btn-primary" href="/activity-console/activity-manage/travel/config-prize-list" role="button">奖品管理</a>
        </div>
    </div>

    <div class="table-responsive">
        <table class="table table-bordered table-hover">
            <thead>
            <tr>
                <th>奖品名称</th>
                <th>奖品价值（元）</th>
                <th>获奖资格</th>
                <th>总数</th>
                <th>剩余数量</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
                <#list data as item>
                <tr>
                    <td>${item.prize.description}</td>
                    <td>${item.price}</td>
                    <td><img src="${commonStaticServer}${item.image}"></td>
                    <td>投资满${item.investAmount/100}</td>
                    <td><a href="">更改数量</a></td>
                </tr>
                </#list>
            </tbody>

        </table>
    </div>
</div>
<!-- content area end -->
</@global.main>