<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="message-manual-list.js" headLab="message-manage" sideLab="manualMessageManage" title="手动发送站内信管理">

<!-- content area begin -->
<div class="col-md-10">
    <div class="tip-container">
        <div class="alert alert-danger alert-dismissible" data-dismiss="alert" aria-label="Close" role="alert">
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
            <span class="txt"></span>
        </div>
    </div>
    <form action="/message-manage/manual-message-list" class="form-inline query-build">
        <div class="form-group">
            <label>标题</label>
            <input type='text' class="form-control" id="title" name="title" value="${title!}"/>
        </div>

        <div class="form-group">
            <label>状态</label>
            <select class="selectpicker" name="messageStatus">
                <option value="" <#if !(selectedMessageStatus??)>selected</#if>>全部</option>
                <#list messageStatuses as messageStatus>
                    <option value="${messageStatus.name()}"
                            <#if selectedMessageStatus?? && messageStatus == selectedMessageStatus>selected</#if>>${messageStatus.getDescription()}</option>
                </#list>
            </select>
        </div>
        <div class="form-group">
            <label>创建人</label>
            <input type='text' class="form-control" id="createdBy" name="createdBy" value="${createdBy!}"/>
        </div>
        <button class="btn btn-sm btn-primary query">查询</button>
        <a href="/message-manage/manual-message-list" class="btn btn-sm btn-default">重置</a>
    </form>

    <div class="table-responsive">
        <table class="table table-bordered table-hover ">
            <thead>
            <tr>
                <th>编号</th>

                <th>收件人</th>
                <th>标题</th>
                <th>内容</th>
                <th>消息类型</th>
                <th>送达方式</th>
                <th>跳转页面</th>
                <th>是否推送</th>
                <th>推送渠道</th>
                <th>状态</th>
                <th>创建时间</th>
                <th>创建人</th>
                <th>最后更新人</th>
                <th>最后更新时间</th>
                <th>审核人</th>
                <th>审核时间</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
                <#list messageList as message>
                <tr>
                    <td class="message-id">${message.id?c}</td>
                    <td>${message.userGroup}</td>
                    <td>${message.title!}</td>
                    <td>${message.templateTxt!}</td>
                    <td>${message.messageCategory.getDescription()!}</td>
                    <td>
                        <#if message.channels?has_content>
                            <#list message.channels as channel>
                            ${channel.getDescription()!}<#sep>, </#sep>
                            </#list>
                        </#if>
                    </td>
                    <td>PC:${(message.webUrl)!} / APP:${(message.appUrl.getDescription())!}
                    </td>
                    <td><#if message.push??>是<#else>否</#if></td>
                    <td>${(message.push.pushSource)!}</td>
                    <td>${message.messageStatus.getDescription()!}</td>
                    <td>${message.createdBy!}</td>
                    <td>${message.createdTime?string('yyyy-MM-dd HH:mm:ss')}</td>
                    <td>${message.updatedBy!}</td>
                    <td><#if message.updatedTime??>${message.updatedTime?string('yyyy-MM-dd HH:mm:ss')!}</#if></td>
                    <td>${message.activatedBy!}</td>
                    <td><#if message.activatedTime??>${(message.activatedTime?string('yyyy-MM-dd HH:mm:ss'))!}</#if></td>
                    <td>
                        <#if message.messageStatus == "TO_APPROVE">
                            <@security.authorize access="hasAnyAuthority('OPERATOR_ADMIN','ADMIN')">
                                <a class="pass approve-btn" href="javascript:void(0)" data-messageId="${message.id?c}">审核</a> |
                                <a class="pass reject-btn" href="javascript:void(0)" data-messageId="${message.id?c}">驳回</a> |
                            </@security.authorize>

                            <@security.authorize access="hasAnyAuthority('OPERATOR','ADMIN')">
                                <a href="/message-manage/manual-message/${message.id?c}/edit">编辑</a> |
                                <a class="pass delete-btn" href="javascript:void(0)" data-messageId="${message.id?c}">删除</a>
                            </@security.authorize>
                        </#if>
                    </td>
                </tr>
                </#list>
            </tbody>
        </table>
    </div>

    <!-- pagination  -->
    <nav>
        <div>
            <span class="bordern">总共${messageCount}条,每页显示${pageSize}条</span>
        </div>
        <#if messageList?has_content>
            <ul class="pagination">
                <#if hasPreviousPage>
                <li>

                    <a href="?index=${index-1}&pageSize=${pageSize}<#if selectedMessageStatus??>&messageStatus=${selectedMessageStatus}</#if><#if title??>&title=${title!}</#if><#if createdBy??>&createdBy=${createdBy!}</#if>"
                       aria-label="Previous">
                        <span aria-hidden="true">&laquo; Prev</span>
                    </a>
                </li>
                </#if>
                <li><a>${index}</a></li>
                <#if hasNextPage>
                <li>
                    <a href="?index=${index+1}&pageSize=${pageSize}<#if selectedMessageStatus??>&messageStatus=${selectedMessageStatus}</#if><#if title??>&title=${title!}</#if><#if createdBy??>&createdBy=${createdBy!}</#if>"
                       aria-label="Next">
                        <span aria-hidden="true">Next &raquo;</span>
                    </a>
                </li>
                </#if>
            </ul>
        </#if>
    </nav>
    <!-- pagination -->

    <!-- Modal -->
    <div class="modal fade" id="confirm-modal" tabindex="-1" role="dialog">
        <div class="modal-dialog modal-sm" role="document">
            <div class="modal-content">
                <div class="modal-body">
                    <h5>确认提交？</h5>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    <button type="button" class="btn btn-default btn-submit" data-post-url="">确认</button>
                </div>
            </div>
        </div>
    </div>

    <!-- Modal -->
    <div class="modal fade" id="error-modal" tabindex="-1" role="dialog">
        <div class="modal-dialog modal-sm" role="document">
            <div class="modal-content">
                <div class="modal-body">
                    <h5></h5>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">确定</button>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- content area end -->
</@global.main>