<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="activity-lottery-list.js" headLab="activity-manage" sideLab="lottery" title="抽奖数据统计">

<div class="col-md-10" style="height: 80%">
    <div class="panel panel-default">
        <div class="panel-body">
            <a class="btn btn-default " href="/activity-console/activity-manage/user-time-list" role="button">抽奖机会统计</a>
            <a class="btn btn-default btn-primary" href="/activity-console/activity-manage/user-prize-list" role="button">抽奖记录</a>
        </div>
    </div>

    <div class="table-responsive">
        <form action="/activity-console/activity-manage/user-prize-list" method="get" class="form-inline query-build" id ="prizeFrom">
                <div class="form-group">
                    <label>用户手机号</label>
                    <input id="login-name" name="mobile" id="mobile" class="form-control" value="${mobile!}"/>
                </div>

                <div class="form-group">
                    <label>活动类型</label>
                    <select class="selectpicker" name="prizeType" onchange="switchPrize()">
                        <#list prizeTypes as prizeType>
                            <option value="${prizeType}" <#if prizeTypes?? && prizeType==selectPrizeType>selected</#if>>
                            ${prizeType.description}
                            </option>
                        </#list>
                    </select>
                </div>

                <div class="form-group" id = "autumnPrizeDiv">
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
                <div class="form-group" style="display:none;" id="nationalDiv">
                    <select class="selectpicker" name="selectNational">
                        <option value="" <#if !(nationalPrizes??)>selected</#if>>全部</option>
                        <#list nationalPrizes as national>
                            <option value="${national}" <#if nationalPrizes?? && national==selectPrize>selected</#if>>
                            ${national.description}
                            </option>
                        </#list>
                    </select>
                </div>
                <div class="form-group">
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
        </form>
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
                    <#if prizeList?? >
                        <#list prizeList as prize>
                        <tr>
                            <td>${prize.lotteryTime?string('yyyy-MM-dd HH:mm')}</td>
                            <td>${prize.mobile!}</td>
                            <td>${prize.userName!}</td>
                            <td>${prize.prize.description!}</td>
                        </tr>
                        </#list>
                    </#if>
                </tbody>

            </table>
            <!-- pagination  -->
            <nav class="pagination-control">
                <div><span class="bordern">总共${lotteryCount}条,每页显示${pageSize}条</span></div>
                <ul class="pagination pull-left">
                    <li>
                        <#if hasPreviousPage >
                        <a href="/activity-console/activity-manage/user-prize-list?mobile=${mobile!}&index=${index-1}&pageSize=${pageSize}">
                        <#else>
                        <a href="#">
                        </#if>
                        <span>« Prev</span>
                    </a>
                    </li>
                    <li><a>${index}</a></li>
                    <li>
                        <#if hasNextPage>
                        <a href="/activity-console/activity-manage/user-prize-list?mobile=${mobile!}&index=${index+1}&pageSize=${pageSize}">
                        <#else>
                        <a href="#">
                        </#if>
                        <span>Next »</span>
                    </a>
                    </li>
                </ul>
            </nav>
        </div>
</div>
<!-- content area end -->
</@global.main>