<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="message-auto-list.js" headLab="content-manage" sideLab="messageManage" title="自动发送站内信管理">

<!-- content area begin -->
<div class="col-md-10">
    <div class="col-md-12">
        <a href="/message-manage/manual-message-list" class="btn btn-default">手动创建消息管理</a>
        <a href="/message-manage/auto-message-list" class="btn btn-default btn-warning">自动发送消息管理</a>
    </div>
    <div class="col-md-12">
    <div class="table-responsive">
        <table class="table table-bordered table-hover ">
            <thead>
            <tr>
                <th>编号</th>
                <th>收件人</th>
                <th>发送条件</th>
                <th>标题</th>
                <th>内容</th>
                <th>跳转页面</th>
                <th>状态</th>
                <th>最后操作人</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
                <#list dto.records as message>
                <tr>
                    <td>${message.id?c}</td>
                    <td>全部用户</td>
                    <td>${(message.messageEventType.getDescription())!}</td>
                    <td>${message.title!}</td>
                    <td>${message.templateTxt!}</td>
                    <td>PC:${message.webUrl!} APP:${(message.appUrl.getDescription())!}</td>
                    <td><#if message.messageStatus.name() == 'TO_APPROVE'>已暂停<#elseif message.messageStatus.name() == 'APPROVED'>已启用</#if></td>
                    <td>${message.updatedBy!}</td>
                    <td>
                        <@security.authorize access="hasAnyAuthority('OPERATOR_ADMIN','ADMIN')">
                            <#if message.messageStatus.name() == 'TO_APPROVE'>
                                <button class="btn btn-success btn-xs enabled-link"
                                        data-link="/message-manage/approve/${message.id?c}">
                                    启用
                                </button>
                            <#elseif message.messageStatus.name() == 'APPROVED'>
                                <button class="btn btn-danger btn-xs disabled-link"
                                        data-link="/message-manage/reject/${message.id?c}">
                                    暂停
                                </button>
                            </#if>
                        </@security.authorize>
                    </td>
                </tr>
                </#list>
            </tbody>
        </table>
    </div>

    <!-- pagination  -->
    <nav>
        <div>
            <span class="bordern">总共${dto.count}条,每页显示${dto.pageSize}条</span>
        </div>
        <#if dto.records?has_content>
            <ul class="pagination">
                <li>
                    <#if dto.hasPreviousPage>
                    <a href="?index=${dto.index - 1}"
                       aria-label="Previous">
                    <#else>
                    <a href="#" aria-label="Previous">
                    </#if>
                    <span aria-hidden="true">&laquo; Prev</span>
                </a>
                </li>
                <li><a>${dto.index}</a></li>
                <li>
                    <#if dto.hasNextPage>
                    <a href="?index=${dto.index + 1}"
                       aria-label="Next">
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
</div>
<!-- content area end -->
</@global.main>