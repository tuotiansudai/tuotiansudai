<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="point-consume.js" headLab="point-manage" sideLab="pointBillConsume" title="积分消耗记录">

<div class="col-md-10">
    <form action="" class="form-inline query-build">
        <div class="form-group">
            <label for="control-label">时间</label>

            <div class='input-group date' id="startTime">
                <input type='text' class="form-control jq-startTime" name="startTime"
                       value="${(startTime?string('yyyy-MM-dd'))!}"/>
                <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
            </div>
            -
            <div class='input-group date' id="endTime">
                <input type='text' class="form-control jq-endTime" name="endTime"
                       value="${(endTime?string('yyyy-MM-dd'))!}"/>
                <span class="input-group-addon">
					                    <span class="glyphicon glyphicon-calendar"></span>
					                </span>
            </div>
        </div>
        <div class="form-group">
            <label for="control-label">业务类型：</label>
            <select class="selectpicker" id="pointBusinessType" name="pointBusinessType">
                <option value="">全部</option>
                <#list pointBusinessTypeList as item>
                    <option value="${item}"
                            <#if pointBusinessType?? && item = pointBusinessType>selected</#if>>${item.description}</option>
                </#list>
            </select>
        </div>
        <div class="form-group">
            <label for="control-label">积分渠道：</label>
            <select class="selectpicker" id="channel" name="channel">
                <option value="">全部</option>
                <#list channelMap?keys as key>
                    <option value="${key}" <#if channel?? && key = channel>selected</#if>>${channelMap[key]}</option>
                </#list>
            </select>
        </div>
        <div class="form-group">
            <label for="control-label">积分花费：</label>
            <input type="text" class="form-control jq-minPoint" name="minPoint"
                   value="${(minPoint?c)!}">
            -
            <input type="text" class="form-control jq-maxPoint" name="maxPoint"
                   value="${(maxPoint?c)!}">
        </div>
        <div class="form-group">
            <label for="control-label">用户姓名/手机号：</label>
            <input type="text" class="form-control jq-userNameOrMobile" name="userNameOrMobile"
                   value="${userNameOrMobile!}">
        </div>
        <button class="btn btn-primary" type="submit">查询</button>
        <a href="/point-manage/point-consume" class="btn btn-sm btn-default">重置</a>
    </form>
    <div class="table-responsive">
        <label for="control-label">速贷积分消费总额: ${(sumSudaiPoint * -1)?c}</label>
        <label for="control-label">消费渠道积分总额: ${(sumChannelPoint * -1)?c}</label>
        <table class="table table-bordered table-hover " style="width:80%;">
            <thead>
            <tr>
                <th>时间</th>
                <th>流水号</th>
                <th>用户姓名</th>
                <th>手机号</th>
                <th>消费总积分</th>
                <th>渠道来源</th>
                <th>消费渠道积分</th>
                <th>花费速贷积分</th>
                <th>业务类型</th>

            </tr>
            </thead>
            <tbody>
                <#list data.records as item>
                <tr>
                    <td>${(item.createdTime?string('yyyy-MM-dd HH:mm:ss'))!}</td>
                    <td>${(item.orderId?c)!}</td>
                    <td>${item.userName!}</td>
                    <td>${item.mobile!}</td>
                    <td>${(item.point * -1)?c}</td>

                    <td>${item.channel!}</td>
                    <td>${(item.channelPoint * -1)?c}</td>
                    <td>${(item.sudaiPoint * -1)?c}</td>
                    <td>${item.businessType.description!}</td>
                </tr>
                <#else>
                <tr>
                    <td colspan="9">暂无数据</td>
                </tr>
                </#list>
            </tbody>
        </table>
    </div>

    <div class="row">
        <!-- pagination  -->
        <nav class="pagination-control">
            <#if data.count &gt; 0>
                <div>
                    <span class="bordern">总共${data.count}条,每页显示${data.pageSize}条</span>
                </div>
                <ul class="pagination pull-left">
                    <li>
                        <#if data.hasPreviousPage >
                            <a href="?index=${data.index-1}&startTime=${(startTime?string('yyyy-MM-dd'))!}&endTime=${(endTime?string('yyyy-MM-dd'))!}&pointBusinessType=${pointBusinessType!}&channel=${channel!}&minPoint=${(minPoint?c)!}&maxPoint=${(maxPoint?c)!}&userNameOrMobile=${userNameOrMobile!}"
                               aria-label="Previous">
                                <span aria-hidden="true">&laquo; Prev</span>
                            </a>
                        </#if>
                    </li>
                    <li><a>${data.index?c}</a></li>
                    <li>
                        <#if data.hasNextPage >
                            <a href="?index=${data.index+1}&startTime=${(startTime?string('yyyy-MM-dd'))!}&endTime=${(endTime?string('yyyy-MM-dd'))!}&pointBusinessType=${pointBusinessType!}&channel=${channel!}&minPoint=${(minPoint?c)!}&maxPoint=${(maxPoint?c)!}&userNameOrMobile=${userNameOrMobile!}"
                               aria-label="Next">
                                <span aria-hidden="true">Next &raquo;</span>
                            </a>
                        </#if>
                    </li>
                </ul>
                <@security.authorize access="hasAnyAuthority('ADMIN','DATA')">
                    <button class="btn btn-default pull-left point-consume" type="button">
                        导出Excel
                    </button>
                </@security.authorize>
            </#if>
        </nav>
    </div>

</div>

</@global.main>