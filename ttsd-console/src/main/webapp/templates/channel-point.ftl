<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="" headLab="point-manage" sideLab="channelPoint" title="渠道积分导入记录">

<div class="col-md-10">

    <div class="table-responsive">
        <@security.authorize access="hasAnyAuthority('ADMIN','DATA')">

            <div>
                <button class="btn btn-default" type="button">导入渠道积分</button>
            </div>
        </@security.authorize>
        <label for="control-label">导入总人数: ${sumHeadCount}</label>
        <label for="control-label" style="margin-left: 20px;">导入积分总额: ${sumTotalPoint}</label>
        <table class="table table-bordered table-hover " style="width:80%;">
            <thead>
            <tr>
                <th>导入编号</th>
                <th>导入时间</th>
                <th>操作人</th>
                <th>成功导入人数</th>
                <th>导入渠道积分总额</th>
                <th>详情</th>
            </tr>
            </thead>
            <tbody>
                <#list data.records as item>
                <tr>
                    <td>${item.serialNo}</td>
                    <td>${item.createdTime?string("yyyy-MM-dd HH:mm:ss")}</td>
                    <td>${item.createdBy}</td>
                    <td>${item.headCount?c}</td>
                    <td>${item.totalPoint?c}</td>
                    <td><a href="/point-manage/channel-point-detail/${item.id}">查看详情</a></td>
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
                            <a href="?index=${data.index-1}" aria-label="Previous">
                                <span aria-hidden="true">&laquo; Prev</span>
                            </a>
                        </#if>
                    </li>
                    <li><a>${data.index}</a></li>
                    <li>
                        <#if data.hasNextPage >
                            <a href="?index=${data.index+1}" aria-label="Next">
                                <span aria-hidden="true">Next &raquo;</span>
                            </a>
                        </#if>
                    </li>
                </ul>
            </#if>
        </nav>
    </div>

</div>

</@global.main>