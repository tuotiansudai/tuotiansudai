<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="datetimePicker.js" headLab="activity-manage" sideLab="startWorkActivity" title="惊喜不重样加息不打烊活动管理">

<div class="col-md-10">
    <form action="/activity-console/activity-manage/start-work-list" class="form-inline query-build"
          method="get">
        <div class="form-group">
            <label for="mobile">手机号</label>
            <input type="text" id="mobile" name="mobile" class="form-control ui-autocomplete-input" datatype="*"
                   autocomplete="off" value="${mobile!}">
        </div>

        <div class="form-group">
            <label for="mobile">姓名</label>
            <input type="text" id="userName" name="userName" class="form-control ui-autocomplete-input" datatype="*"
                   autocomplete="off" value="${userName!}">
        </div>

        <div class="form-group">
            <label for="number">兑换时间</label>

            <div class='input-group date' id='startTime'>
                <input type='text' class="form-control" name="startTime" value="${(startTime?string('yyyy-MM-dd'))!}"/>
                <span class="input-group-addon">
                    <span class="glyphicon glyphicon-calendar"></span>
                </span>
            </div>
            -
            <div class='input-group date' id='endTime'>
                <input type='text' class="form-control" name="endTime" value="${(endTime?string('yyyy-MM-dd'))!}"/>
                <span class="input-group-addon">
                    <span class="glyphicon glyphicon-calendar"></span>
                </span>
            </div>
        </div>
        <button type="submit" class="btn btn-sm btn-primary">查询</button>
    </form>

    <a href="/activity-console/activity-manage/start-work?mobile=${mobile!}&userName=${userName!}&startTime=${(startTime?string('yyyy-MM-dd'))!}&endTime=${(endTime?string('yyyy-MM-dd'))!}" class="form-control" style="width: 170px">请点击此处导出EXCEl</a><br/>

    <div class="table-responsive">
        <table class="table table-bordered table-hover">
            <thead>
            <tr>
                <th>手机号</th>
                <th>姓名</th>
                <th>兑换奖品</th>
                <th>兑换时间</th>
                <th>消耗小金人个数</th>
            </tr>
            </thead>
            <tbody>
                <#list data.records as item>
                <tr>
                    <td>${item.mobile}</td>
                    <td>${item.userName}</td>
                    <td>${item.prize}</td>
                    <td>${item.exchangeTime?string("yyyy-MM-dd HH:mm")}</td>
                    <td>${item.count}</td>
                </tr>
                </#list>
            </tbody>

        </table>
    </div>

    <!-- pagination  -->
    <nav class="pagination-control">
        <#if data.count &gt; 0>
            <div>
                <span class="bordern">总共${data.count}条,每页显示${data.pageSize}条</span>
            </div>
            <ul class="pagination pull-left">
                <li>
                    <#if data.hasPreviousPage >
                        <a href="?mobile=${mobile!}&userName=${userName!}&startTime=${startTime!}&endTime=${endTime!}&index=${data.index-1}"
                           aria-label="Previous">
                            <span aria-hidden="true">&laquo; Prev</span>
                        </a>
                    </#if>
                </li>
                <li><a>${data.index}</a></li>
                <li>
                    <#if data.hasNextPage >
                        <a href="?mobile=${mobile!}&userName=${userName!}&startTime=${startTime!}&endTime=${endTime!}&index=${data.index+1}"
                           aria-label="Previous">
                            <span aria-hidden="true">Next &raquo;</span>
                        </a>
                    </#if>
                </li>
            </ul>
        </#if>
    </nav>
    <!-- pagination -->
</div>
<!-- content area end -->
</@global.main>