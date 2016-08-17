<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="activity-lottery-list.js" headLab="activity-manage" sideLab="lottery" title="抽奖数据统计">

<!-- content area begin -->
<div class="col-md-10">
    <form action="" method="get" class="form-inline query-build">

        <div class="form-group">
            <div class="form-group">
                <label>用户手机号</label>
                <input id="login-name" name="mobile" id="mobile" class="form-control" value="${mobile!}"/>
            </div>

            <div class="form-group">
                <label>奖品</label>
                <select class="selectpicker" name="activityStatus">
                    <option value="" <#if !(activityStatus??)>selected</#if>>全部</option>
                </select>
            </div>

            <label>获奖时间</label>
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
        <button type="reset" class="btn btn-sm btn-default btnSearch">重置</button>

    </form>
    <div class="table-responsive">
        <table class="table table-bordered table-hover">
            <thead>
            <tr>
                <th>用户手机号</th>
                <th>姓名</th>
                <th>可用抽奖机会</th>
                <th>已用抽奖机会</th>
            </tr>
            </thead>
            <tbody>
                <#if lotteryList?? >
                    <#list lotteryList as lottery>
                        <tr>
                            <td>${lottery.mobile!}</td>
                            <td>${lottery.loginName!}</td>
                            <td>${lottery.useCount!}</td>
                            <td>${lottery.unUseCount!}</td>
                        </tr>
                    </#list>
                </#if>
            </tbody>

        </table>
    </div>

    <div class="row">
        <!-- pagination  -->
        <nav class="pagination-control">
            <div><span class="bordern">总共${lotteryCount}条,每页显示${pageSize}条</span></div>
            <ul class="pagination pull-left">
                <li>
                    <#if hasPreviousPage >
                    <a href="/activity-manage/user-lottery-list?mobile=${mobile!}&index=${index-1}&pageSize=${pageSize}">
                    <#else>
                    <a href="#">
                    </#if>
                    <span>« Prev</span>
                </a>
                </li>
                <li><a>${index}</a></li>
                <li>
                    <#if hasNextPage>
                    <a href="/activity-manage/user-lottery-list?mobile=${mobile!}&index=${index+1}&pageSize=${pageSize}">
                    <#else>
                    <a href="#">
                    </#if>
                    <span>Next »</span>
                </a>
                </li>
            </ul>
        </nav>
            <button class="btn btn-default pull-left down-load" id="lotteryTimeBtn" type="button">导出Excel</button>
    </div>

</div>
<!-- content area end -->
</@global.main>