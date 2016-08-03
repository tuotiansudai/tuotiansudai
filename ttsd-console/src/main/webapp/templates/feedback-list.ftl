<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="feedback-list.js" headLab="announce-manage" sideLab="feedbackMan" title="意见反馈">
<!-- content area begin -->

<div class="col-md-10">
    <form action="" class="form-inline query-build">
        <div class="row">
            <div class="form-group">
                <label for="control-label">电话号码</label>
                <input type="text" class="form-control jq-title jq-loginName" name="mobile" value="${mobile!}">
            </div>

            <div class="form-group">
                <label>反馈渠道：</label>
                <select class="selectpicker" name="source">
                    <option value="" <#if !(source??)>selected</#if>>全部</option>
                    <#list sourceList as item>
                        <option value="${item}" <#if source?? && item==source>selected</#if>>
                        ${item}
                        </option>
                    </#list>
                </select>
            </div>

            <div class="form-group">
                <label>反馈类型：</label>
                <select class="selectpicker" name="type">
                    <option value="" <#if !(type??)>selected</#if>>全部</option>
                    <#list typeList as item>
                        <option value="${item}" <#if type?? && item==type>selected</#if>>
                        ${item.desc}
                        </option>
                    </#list>
                </select>
            </div>

            <div class="form-group">
                <label>状态：</label>
                <select class="selectpicker" name="status">
                    <option value="" <#if !(status??)>selected</#if>>全部</option>
                    <#list statusList as item>
                        <option value="${item}" <#if status?? && item==status>selected</#if>>
                        ${item.desc}
                        </option>
                    </#list>
                </select>
            </div>

            <div class="form-group">
                <label>反馈时间：</label>

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

            <button class="btn btn-primary search" type="submit">查询</button>
            <a href="/announce-manage/feedback" class="btn btn-sm btn-default">重置</a>
        </div>
    </form>

    <div class="row">
        <table class="table table-bordered table-hover">
            <thead>
            <tr>
                <th>编号</th>
                <th>用户名</th>
                <th>联系电话</th>
                <th>反馈渠道</th>
                <th>反馈类型</th>
                <th>内容</th>
                <th>时间</th>
                <th>是否处理</th>
                <th>备注</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
                <#list feedbackList as feedback>
                <tr>
                    <td width="50">${(feedback.id?string('0'))!}</td>
                    <td width="150">${feedback.loginName!}</td>
                    <td width="150">${feedback.contact!}</td>
                    <td width="150">${feedback.source!}</td>
                    <td width="150">${(feedback.type.desc)!}</td>
                    <td style="text-align:left;">${feedback.content?replace('\n','<br>')}</td>
                    <td width="150">${(feedback.createdTime?string('yyyy-MM-dd HH:mm:ss'))!}</td>
                    <#if feedback.status == 'NOT_DONE' >
                        <td width="100"><input type="checkbox" class="feedback-status" data-id="${feedback.id?c}"/></td>
                    <#else>
                        <td width="100"><input type="checkbox" class="feedback-status" data-id="${feedback.id?c}" checked/></td>
                    </#if>
                    <td style="text-align:left;" width="160">
                        <#if feedback.remark??>
                                <span class="tooltip-list"
                            <#if feedback.remark?length gt 20 && feedback.remark?contains('|')>
                                      data-original-title="${feedback.remark?replace('|','—————————————————')!}">${(feedback.remark?replace('|',''))?substring(0,20)!}...
                            <#elseif feedback.remark?length gt 20 && !feedback.remark?contains('|')>
                                      data-original-title="${feedback.remark!}">${feedback.remark?substring(0,20)!}...
                            <#elseif feedback.remark?length lt 20 && feedback.remark?contains('|')>
                                      data-original-title="${feedback.remark?replace('|','—————————————————')!}">${(feedback.remark?replace('|',' '))!}
                            <#else>
                                      data-original-title="${feedback.remark!}">${feedback.remark!}
                            </#if>
                                </span>
                        </#if>
                    </td>
                    <td>
                        <@security.authorize access="hasAnyAuthority('ADMIN','CUSTOMER_SERVICE')">
                            <input type="button" class="feedback-remark" value="添加备注" data-feedback-id="${feedback.id?c}">
                        </@security.authorize>
                    </td>
                </tr>
                </#list>
            </tbody>
        </table>
    </div>

    <!-- 模态框（Modal） -->
    <div class="modal fade" id="update" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                    <h4 class="modal-title" id="myModalLabel">添加备注</h4>
                </div>
                <div class="modal-body">
                    <input type="hidden" name="feedbackId" id="feedbackId" />
                    备注信息：<br/>
                    <textarea class="form-control" name="remark" id="remark" rows="3"></textarea>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" class="btn btn-primary" onclick="update()">提交</button>
                </div>
            </div>
            <!-- /.modal-content -->
        </div>
        <!-- /.modal -->
    </div>
    <!-- 模态框（Modal）end -->

    <div class="row">
        <!-- pagination  -->
        <nav class="pagination-control">
            <div><span class="bordern">总共${feedbackCount}条,每页显示${pageSize}条</span></div>

            <ul class="pagination">
                <li <#if !hasPreviousPage>class="disabled"</#if>>
                    <a href="?mobile=${mobile!}&source=${source!}&type=${type!}&status=${status!}&startTime=${(startTime?string('yyyy-MM-dd'))!}&endTime=${(endTime?string('yyyy-MM-dd'))!}&index=${index-1}&pageSize=${pageSize}" class="previous <#if !hasPreviousPage>disabled</#if>"><span
                            aria-hidden="true">&laquo;</span></a>
                </li>
                <li class="disabled"><a class="current-page" data-index="${index}">${index}</a></li>
                <li <#if !hasNextPage>class="disabled"</#if>>
                    <a href="?mobile=${mobile!}&source=${source!}&type=${type!}&status=${status!}&startTime=${(startTime?string('yyyy-MM-dd'))!}&endTime=${(endTime?string('yyyy-MM-dd'))!}&index=${index+1}&pageSize=${pageSize}" class="next <#if !hasNextPage>disabled</#if>"><span
                            aria-hidden="true">&raquo;</span></a>
                </li>
            </ul>
        </nav>
        <button class="btn btn-default pull-left down-load" type="button">导出Excel</button>
    </div>
</div>
<!-- content area end -->
</@global.main>