<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="headlines-today-list.js" headLab="activity-manage" sideLab="headlinesToday" title="抽奖数据统计">

<div class="col-md-10">

    <div class="table-responsive">
        <form action="/activity-console/activity-manage/headlines-today-list" method="get" class="form-inline query-build"
              id="prizeFrom">
            <div class="form-group">
                <label>用户手机号</label>
                <input id="login-name" name="mobile" id="mobile" class="form-control" value="${mobile!}"/>
            </div>

            <div class="form-group">
                <label>实名认证</label>
                <select class="selectpicker" name="authenticationType"">
                    <option value="" <#if authenticationType == "">selected</#if>>全部</option>
                    <option value="1" <#if authenticationType == "1">selected</#if>>已实名</option>
                    <option value="0" <#if authenticationType == "0">selected</#if>>未实名</option>
                </select>
            </div>

            <div class="form-group">
                <label>参与时间</label>
                <div class='input-group date' id='datetimepicker1'>
                    <input type='text' class="form-control" name="startTime"
                           value="${(startTime?string('yyyy-MM-dd'))!}"/>
                                            <span class="input-group-addon">
                                                <span class="glyphicon glyphicon-calendar"></span>
                                            </span>
                </div>
                -
                <div class='input-group date' id='datetimepicker2'>
                    <input type='text' class="form-control" name="endTime"
                           value="${(endTime?string('yyyy-MM-dd'))!}"/>
                                            <span class="input-group-addon">
                                                <span class="glyphicon glyphicon-calendar"></span>
                                            </span>
                </div>
            </div>
            <button type="submit" class="btn btn-sm btn-primary btnSearch">查询</button>
        </form>
        <table class="table table-bordered table-hover">
            <thead>
            <tr>
                <th>编号</th>
                <th>手机号</th>
                <th>参与时间</th>
                <th>姓名</th>
                <th>是否投资</th>
            </tr>
            </thead>
            <tbody>
                <#if headlinesTodayList?? >
                    <#list headlinesTodayList as headlinesToday>
                    <tr>
                        <td>${headlinesToday_index+1}</td>
                        <td>${headlinesToday.mobile!}</td>
                        <td>${headlinesToday.lotteryTime?string('yyyy-MM-dd HH:mm')}</td>
                        <td>${headlinesToday.userName!}</td>
                        <td>${headlinesToday.investFlag!}</td>
                    </tr>
                    </#list>
                </#if>
            </tbody>

        </table>
        <!-- pagination  -->
        <nav class="pagination-control">
            <@security.authorize access="hasAnyAuthority('ADMIN','DATA')">
                <button class="btn btn-default pull-left export-activity-prize-prize" id="activityHeadlinesTodayExport" type="button">导出Excel</button>
            </@security.authorize>
        </nav>
    </div>
</div>
<!-- content area end -->
</@global.main>