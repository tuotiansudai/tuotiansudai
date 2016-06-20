<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="app-push-list.js" headLab="app-push-manage" sideLab="manualMessageManage" title="手动发送站内信管理">

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
                <option value="" <#if !(messageStatusInput??)>selected</#if>>全部</option>
                <#list messageStatuses as messageStatus>
                    <option value="${messageStatus.name()}" <#if messageStatusInput?? && messageStatus == messageStatusInput>selected</#if>>${messageStatus.getDescription()}</option>
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
            <th>
                收件人
            </th>
            <th>
                标题（不超过40个字）
            </th>
            <th>
                内容
            </th>
            <th>
                送达渠道
            </th>
            <th>
                发送时间
            </th>
            <th>
                打开数
            </th>
            <th>
                创建人/审核人
            </th>
            <th>
                状态
            </th>
            <th>
                操作
            </th>
        </tr>
        </thead>
        <tbody>
            <#list messageList as message>
                <tr>
                    <td>
                        <#if message.userGroups?has_content>
                            <#list message.userGroups as userGroup>
                            ${userGroup.getDescription()!}<#sep>, </#sep>
                            </#list>
                        </#if>
                    </td>
                    <td>
                        ${message.title!}
                    </td>
                    <td>
                        ${message.template!}
                    </td>
                    <td>
                        <#if message.channels?has_content>
                            <#list message.channels as channel>
                            ${channel.getDescription()!}<#sep>, </#sep>
                            </#list>
                        </#if>
                    </td>
                    <td>
                        ${message.createdTime?string('yyyy-MM-dd HH:mm:ss')}
                    </td>
                    <td>
                        ${message.readCount!}
                    </td>

                    <td>
                        ${message.createdBy!}/${message.activatedBy!}
                    </td>
                    <td>
                        ${message.status.getDescription()!}
                    </td>
                    <td>
                        <#if message.status == "TO_APPROVE">
                            <@security.authorize access="hasAnyAuthority('OPERATOR_ADMIN','ADMIN')">
                                <a class="pass" href="#" data-messageId="${message.id?c}">审核</a>｜
                                <a href="/message-manage/manual-message/${message.id?c}/reject" onclick="return confirm('确定驳回吗?')">驳回</a>
                            </@security.authorize>
                            <@security.authorize access="hasAuthority('ADMIN')">｜</@security.authorize>
                            <@security.authorize access="hasAnyAuthority('OPERATOR','ADMIN')">
                                <a href="/message-manage/manual-message/${message.id?c}/edit">编辑</a>｜
                                <a href="/message-manage/manual-message/${message.id?c}/delete" onclick="return confirm('确定删除吗?')">删除</a>
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
                <li>
                    <#if hasPreviousPage>
                    <a href="?index=${index-1}&pageSize=${pageSize} <#if messageStatusInput??>&messageStatus=${messageStatusInput}</#if> <#if title??>&title=${title!}</#if> <#if createdBy??>&createBy=${createdBy!}</#if>" aria-label="Previous">
                    <#else>
                    <a href="#" aria-label="Previous">
                    </#if>
                    <span aria-hidden="true">&laquo; Prev</span>
                </a>
                </li>
                <li><a>${index}</a></li>
                <li>
                    <#if hasNextPage>
                    <a href="?index=${index+1}&pageSize=${pageSize} <#if messageStatusInput??>&messageStatus=${messageStatusInput}</#if> <#if title??>&title=${title?string('0')}</#if> <#if createdBy??>&createdBy=${createBy!}</#if>" aria-label="Next">
                    <#else>
                    <a href="#" aria-label="Next">
                    </#if>
                    <span aria-hidden="true">Next &raquo;</span>
                </a>
                </li>
            </ul>
        </#if>
    </nav>
    <!-- pagination -->
</div>
<!-- content area end -->
</@global.main>