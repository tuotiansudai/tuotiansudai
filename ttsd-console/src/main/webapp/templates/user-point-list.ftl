<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="user-point-list.js" headLab="point-manage" sideLab="userPointList" title="用户积分查询">

<!-- content area begin -->
<div class="col-md-10">
    <form action="" class="form-inline query-build">
        <div>
            <div class="form-group">
                <label>积分渠道：</label>
                <select class="form-control" id="channel" name="channel">
                    <option value="">全部</option>
                <#list allChannels as channel_name, channel_desc>
                    <option value="${channel_name}"
                            <#if channel?has_content && channel == channel_name>selected</#if> >${channel_desc}</option>
                </#list>
                </select>
            </div>
            <div class="form-group">
                <label for="control-label">积分范围：</label>
                <input type="text" class="form-control" name="minPoint" value="${(minPoint?string.computer)!}">
                -
                <input type="text" class="form-control" name="maxPoint" value="${(maxPoint?string.computer)!}">
            </div>
        </div>
        <div>
            <div class="form-group">
                <label for="control-label">投资人用户名/手机号：</label>
                <input type="text" class="form-control" name="loginNameOrMobile" value="${loginNameOrMobile!}">
            </div>
            <button class="btn btn-primary" type="submit">查询</button>
            <button class="btn btn-default" type="reset">重置</button>
        </div>
    </form>

    <div class="row">
        <table class="table table-bordered table-hover">
            <thead>
            <tr>
                <th>用户名</th>
                <th>真实姓名</th>
                <th>手机号</th>
                <th>积分渠道名称</th>
                <th>可用渠道积分</th>
                <th>累计渠道积分</th>
                <th>速贷可用积分</th>
                <th>速贷累计积分</th>
                <th>可用积分总额</th>
                <th>综合积分总额</th>
                <th>明细记录</th>
            </tr>
            </thead>
            <tbody>
                <#list userPointList as userPointItem>
                <tr>
                    <td>${userPointItem.loginName!''}</td>
                    <td>${userPointItem.userName!''}</td>
                    <td>${userPointItem.mobile}</td>
                    <td>${userPointItem.channel!''}</td>
                    <td>${userPointItem.channelPoint!''}</td>
                    <td>${userPointItem.totalChannelPoint!''}</td>
                    <td>${userPointItem.sudaiPoint!''}</td>
                    <td>${userPointItem.totalSudaiPoint!''}</td>
                    <td>${userPointItem.point!''}</td>
                    <td>${userPointItem.totalPoint!''}</td>
                    <td>
                        <a href="/point-manage/user-point-detail-list?loginName=${userPointItem.loginName!''}&totalPoint=${userPointItem.totalPoint!''}&point=${userPointItem.point!''}">查看明细</a>
                    </td>
                </tr>
                </#list>
            </tbody>
        </table>
    </div>

    <div class="row">
        <!-- pagination  -->
        <nav class="pagination-control">
            <div><span class="bordern">总共${count}条,每页显示${pageSize}条</span></div>
            <ul class="pagination pull-left">
                <li>
                    <#if hasPreviousPage >
                    <a href="/point-manage/user-point-list?minPoint=${minPoint!}&maxPoint=${maxPoint!}&channel=${channel!}&index=${index-1}&pageSize=${pageSize}">
                    <#else>
                    <a href="#">
                    </#if>
                    <span>« Prev</span></a>
                </li>
                <li><a>${index}</a></li>
                <li>
                    <#if hasNextPage >
                    <a href="/point-manage/user-point-list?minPoint=${minPoint!}&maxPoint=${maxPoint!}&channel=${channel!}&index=${index+1}&pageSize=${pageSize}">
                    <#else>
                    <a href="#">
                    </#if>
                    <span>Next »</span></a>
                </li>
            </ul>
        </nav>
    </div>
</div>
<!-- content area end -->
</@global.main>
