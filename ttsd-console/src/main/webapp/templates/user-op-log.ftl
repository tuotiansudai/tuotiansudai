<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="user-op-log.js" headLab="security" sideLab="userOpLog" title="用户行为日志">
<!-- content area begin -->
<div class="col-md-10">
    <form action="" class="form-inline query-build">

        <div class="form-group">
            <label for="mobile">用户手机</label>
            <input type="text" id="mobile" name="mobile"
                   class="form-control ui-autocomplete-input" datatype="*" autocomplete="off"
                   value="${mobile!}"/>
        </div>

        <div class="form-group">
            <label for="opType">业务类型</label>
            <select class="selectpicker" id="op-type" name="opType">
                <option value="">全部</option>
                <#list opTypes as opTypeItem>
                    <option value="${opTypeItem.name()}"
                            <#if (opType.name())?has_content && opType.name() == opTypeItem.name()>selected</#if>
                            >${opTypeItem.desc}</option>
                </#list>
            </select>
        </div>

        <div class="form-group">
            <label>日期</label>

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
        <button type="reset" class="btn btn-sm btn-default">重置</button>
    </form>
    <div class="table-responsive">
        <table class="table table-bordered table-hover">
            <thead>
            <tr>
                <th>操作时间</th>
                <th>用户手机</th>
                <th>IP</th>
                <th>操作类型</th>
                <th>终端类型</th>
                <th>设备ID</th>
                <th>操作详情</th>
            </tr>
            </thead>
            <tbody>
                <#list data.records as record>
                <tr>
                    <td>${record.createdTime?string('yyyy-MM-dd HH:mm:ss')}</td>
                    <td>${record.mobile!}</td>
                    <td>${record.ip}</td>
                    <td>${record.opType.getDesc()}</td>
                    <td>${record.source!}</td>
                    <td>${record.deviceId!}</td>
                    <td>${record.description!}</td>
                </tr>
                <#else>
                <tr>
                    <td colspan="7">无记录</td>
                </tr>
                </#list>
            </tbody>
        </table>
    </div>
    <nav>
        <div>
            <span class="bordern">总共${data.count!('0')}条,每页显示${data.pageSize}条</span>
        </div>
        <ul class="pagination">
            <li <#if !data.hasPreviousPage>class="disabled"</#if>>
                <a href="?mobile=${mobile!}&opType=${opType!}&startTime=${(startTime?string('yyyy-MM-dd'))!}&endTime=${(endTime?string('yyyy-MM-dd'))!}&pageSize=${pageSize}&index=${index-1}"
                   class="previous <#if !data.hasPreviousPage>disabled</#if>">
                    <span aria-hidden="true">&laquo;</span>
                </a>
            </li>
            <li class="disabled"><a class="current-page" data-index="${data.index}">${data.index}</a></li>
            <li <#if !data.hasNextPage>class="disabled"</#if>>
                <a href="?mobile=${mobile!}&opType=${opType!}&startTime=${(startTime?string('yyyy-MM-dd'))!}&endTime=${(endTime?string('yyyy-MM-dd'))!}&pageSize=${pageSize}&index=${index+1}"
                   class="next <#if !data.hasNextPage>disabled</#if>">
                    <span aria-hidden="true">&raquo;</span>
                </a>
            </li>
        </ul>
    </nav>
</div>
<!-- content area end -->
</@global.main>