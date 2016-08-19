<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="activity-lottery-list.js" headLab="activity-manage" sideLab="lottery" title="抽奖数据统计">

<div>
    <button class="btn btn-sm btn-primary btnPrizeRecord">抽奖记录</button>
    <button class="btn btn-sm btn-primary btnPrizeTime">抽奖机会统计</button>
</div>

<!-- content area begin -->
<div class="col-md-10" id="prizeTimeDiv">
    <form action="" method="get" class="form-inline query-build" id="lotteryTimeForm">

        <div class="form-group">
            <div class="form-group">
                <label>用户手机号</label>
                <input id="login-name" name="mobile" id="mobile" class="form-control" value="${mobile!}"/>
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
            <@security.authorize access="hasAnyAuthority('DATA')">
                <button class="btn btn-default pull-left down-load" id="lotteryTimeBtn" type="button">导出Excel</button>
            </@security.authorize>
        </nav>

    </div>
</div>


<div class="col-md-10">
    <form action="" method="get" class="form-inline query-build">

        <div class="form-group">
            <div class="form-group">
                <label>用户手机号</label>
                <input id="login-name" name="mobile" id="mobile" class="form-control" value="${mobile!}"/>
            </div>

            <div class="form-group">
                <label>奖品</label>
                <select class="selectpicker" name="selectPrize">
                    <option value="" <#if !(lotteryPrizes??)>selected</#if>>全部</option>
                    <#list lotteryPrizes as prize>
                            <option value="${prize}" <#if lotteryPrizes?? && prize==selectPrize>selected</#if>>
                            ${prize.description}
                            </option>
                    </#list>
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
                <th>中奖时间</th>
                <th>获奖用户手机</th>
                <th>姓名</th>
                <th>奖品</th>
            </tr>
            </thead>
            <tbody>
                <tr>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr>
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
            <@security.authorize access="hasAnyAuthority('DATA')">
                <button class="btn btn-default pull-left down-load" id="lotteryBtn" type="button">导出Excel</button>
            </@security.authorize>
        </nav>
    </div>
</div>
<!-- content area end -->
</@global.main>