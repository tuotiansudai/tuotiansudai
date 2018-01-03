<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="channel-point.js" headLab="point-manage" sideLab="channelPoint" title="渠道积分导入记录">

<div class="col-md-10">
    <form action="" class="form-inline query-build">
        <div class="form-group">
            <label for="control-label">积分渠道：</label>
            <select class="selectpicker" id="channel" name="channel">
                <option value="">全部</option>
                <#list channelList as item>
                    <option value="${item}" <#if channel?? && item = channel>selected</#if>>${item}</option>
                </#list>
            </select>
        </div>
        <div class="form-group">
            <label for="control-label">用户名/手机号：</label>
            <input type="text" class="form-control jq-userNameOrMobile" name="userNameOrMobile"
                   value="${userNameOrMobile!}">
        </div>
        <div class="form-group">
            <label for="control-label">状态：</label>
            <select class="selectpicker" id="status" name="success">
                <option value="">全部</option>
                <option value="true" <#if success?? && success>selected</#if>>成功</option>
                <option value="false" <#if success?? && !success>selected</#if>>失败</option>
            </select>
        </div>

        <button class="btn btn-primary" type="submit">查询</button>
        <a href="/point-manage/channel-point-detail/${channelPointId}" class="btn btn-sm btn-default">重置</a>
    </form>
    <div class="table-responsive">
        <label for="control-label">导入总人数: ${sumHeadCount}</label>
        <table class="table table-bordered table-hover " style="width:80%;">
            <thead>
            <tr>
                <th>用户名</th>
                <th>手机号</th>
                <th>积分渠道</th>
                <th>导入状态</th>
                <th>渠道积分</th>
                <th>说明</th>
            </tr>
            </thead>
            <tbody>
                <#list data.records as item>
                <tr>
                    <td>${item.userName!}</td>
                    <td>${item.mobile!}</td>
                    <td>${item.channel!}</td>
                    <td>${item.success?string("成功","失败")}</td>
                    <td>${item.point?c}</td>
                    <td>${item.remark!}</td>
                </tr>
                <#else>
                <tr>
                    <td colspan="8">暂无数据</td>
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
                            <a href="?index=${data.index-1}&channel=${channel!}&userNameOrMobile=${userNameOrMobile!}&success=${success!}"
                               aria-label="Previous">
                                <span aria-hidden="true">&laquo; Prev</span>
                            </a>
                        </#if>
                    </li>
                    <li><a>${data.index}</a></li>
                    <li>
                        <#if data.hasNextPage >
                            <a href="?index=${data.index+1}&channel=${channel!}&userNameOrMobile=${userNameOrMobile!}&success=${success!}"
                               aria-label="Next">
                                <span aria-hidden="true">Next &raquo;</span>
                            </a>
                        </#if>
                    </li>
                </ul>
                <@security.authorize access="hasAnyAuthority('ADMIN','DATA')">
                    <button class="btn btn-default pull-left channel-point-detail"
                            type="button" data-url="/export/channel-point-detail/${channelPointId?string('0')}?">
                        导出Excel
                    </button>
                </@security.authorize>
            </#if>
        </nav>
    </div>

</div>

</@global.main>