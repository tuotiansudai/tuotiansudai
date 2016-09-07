<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="activity-autumn-list.js" headLab="activity-manage" sideLab="autumn" title="中秋活动导出">

<div class="col-md-10">
    <div class="panel panel-default">

    </div>

    <div class="table-responsive">
        <form action="/activity-console/activity-manage/autumn-list" method="get" class="form-inline query-build" id ="prizeFrom">


            <button type="submit" class="btn btn-sm btn-primary btnSearch">导出EXCEL</button>
        </form>

        </div>
</div>
<!-- content area end -->
</@global.main>