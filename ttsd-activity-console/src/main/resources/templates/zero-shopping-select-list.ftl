<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="zero-shopping-list.js" headLab="activity-manage" sideLab="zeroShoppingActivity" title="0元购活动管理">

<div class="col-md-10">
    <div class="panel panel-default">
    <div class="panel-body">
        <a class="btn btn-default btn-primary" href="/activity-console/activity-manage/zero-shopping/user-prize-list" role="button">"0"元购获奖记录</a>
        <a class="btn btn-default btn-primary" href="/activity-console/activity-manage/zero-shopping/config-prize-list" role="button">奖品管理</a>
    </div>
</div>

    <form action="/activity-console/activity-manage/zero-shopping/user-prize-list" class="form-inline query-build" method="get">
        <div class="form-group">
            <label for="mobile">手机号</label>
            <input type="text" id="mobile" name="mobile" class="form-control ui-autocomplete-input" datatype="*" autocomplete="off" value="${mobile!}">
        </div>

        <div class="form-group">
            <label for="number">日期</label>

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
    <div class="table-responsive">
        <table class="table table-bordered table-hover">
            <thead>
            <tr>
                <th>日期</th>
                <th>姓名</th>
                <th>手机号</th>
                <th>投资金额</th>
                <th>选择的奖品</th>
            </tr>
            </thead>
            <tbody>
                <#list data.records as item>
                <tr>
                    <td>${item.investTime?string("yyyy-MM-dd HH:mm")}</td>
                    <td>${item.userName}</td>
                    <td>${item.mobile}</td>
                    <td>${item.investAmount}</td>
                    <td>${item.selectPrize}</td>
                </tr>
                <#else>
                <tr>
                    <td colspan="5">暂无数据</td>
                </tr>
                </#list>
            </tbody>

        </table>
    </div>

    <a href="/activity-console/activity-manage/zero-shopping" class="form-control" style="width: 170px">请点击此处导出EXCEl</a><br/>

    <!-- pagination  -->
    <nav class="pagination-control">
        <#if data.count &gt; 0>
            <div>
                <span class="bordern">总共${data.count}条,每页显示${data.pageSize}条</span>
            </div>
            <ul class="pagination pull-left">
                <li>
                    <#if data.hasPreviousPage >
                        <a href="?mobile=${mobile!}&startTime=${startTime!}&endTime=${endTime!}&index=${data.index-1}" aria-label="Previous">
                            <span aria-hidden="true">&laquo; Prev</span>
                        </a>
                    </#if>
                </li>
                <li><a>${data.index}</a></li>
                <li>
                    <#if data.hasNextPage >
                        <a href="?mobile=${mobile!}&startTime=${startTime!}&endTime=${endTime!}&index=${data.index+1}" aria-label="Previous">
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