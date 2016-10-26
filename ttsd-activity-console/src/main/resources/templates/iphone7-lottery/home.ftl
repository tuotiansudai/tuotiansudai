<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "../macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="" headLab="activity-manage" sideLab="iphone7-lottery" title="iphone7活动总览">

<div class="col-md-10">
    <div class="table-responsive">
        <table class="table table-bordered table-hover">
            <thead>
            <tr>
                <th>活动名称</th>
                <th>参与人数/次数</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td>2016.10.9-iphone7活动</td>
                <td><a href="/activity-console/activity-manage/iphone7-lottery/stat">${userCount} (人)
                    / ${investCount}(次)</a></td>
                <td>
                    <a href="/activity-console/activity-manage/iphone7-lottery/winners">查看中奖记录</a>
                    <a href="/activity-console/activity-manage/iphone7-lottery/config">配置/审核投资码</a>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>
<!-- content area end -->
</@global.main>