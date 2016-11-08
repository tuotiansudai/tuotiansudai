<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="message-auto-list.js" headLab="message-manage" sideLab="autoMessageManage" title="自动发送站内信管理">

<!-- content area begin -->
<div class="col-md-10">
    <div class="table-responsive">
        <table class="table table-bordered table-hover ">
            <thead>
            <tr>
                <th>
                    编号
                </th>
                <th>
                    收件人
                </th>
                <th>
                    发送条件
                </th>
                <th>
                    标题
                </th>
                <th>
                    内容
                </th>
                <th>
                    消息类型
                </th>
                <th>
                    送达渠道
                </th>
                <th>
                    跳转页面
                </th>
                <th>
                    是否推送
                </th>
                <th>
                    推送渠道
                </th>
                <th>
                    状态
                </th>
                <th>
                    操作
                </th>
                <th>
                    最后操作人
                </th>
            </tr>
            </thead>
            <tbody>
                <#list messageList as message>
                <tr>
                    <td>
                    ${message.id?c}
                    </td>
                    <td>
                        全部用户
                    </td>
                    <td>
                    ${(message.pushType.getDescription())!}
                    </td>
                    <td>
                    ${message.title!}
                    </td>
                    <td>
                    ${message.template!}
                    </td>
                    <td>
                        系统消息
                    </td>
                    <td>
                        站内信／APP消息中心
                    </td>
                    <td>
                        PC:${message.webUrl!}
                        APP:${(message.appUrl.getDescription())!}
                    </td>
                    <td>
                        是
                    </td>
                    <td>
                        IOS/Android
                    </td>
                    <td>
                        <#if message.status == 'TO_APPROVE'>
                            已暂停
                        <#elseif message.status == 'APPROVED'>
                            已启用
                        </#if>
                    </td>
                    <td>
                        <@security.authorize access="hasAnyAuthority('OPERATOR_ADMIN','ADMIN')">
                            <#if message.status == 'TO_APPROVE'>
                                <button class="btn btn-success btn-xs enabled-link"
                                        data-link="/message-manage/approve/${message.id?c}">
                                    启用
                                </button>
                            <#elseif message.status == 'APPROVED'>
                                <button class="btn btn-danger btn-xs disabled-link"
                                        data-link="/message-manage/reject/${message.id?c}">
                                    暂停
                                </button>
                            </#if>
                        </@security.authorize>
                    </td>
                    <td>
                    ${message.updatedBy}
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
                    <a href="?index=${index-1}&pageSize=${pageSize}<#if messageStatusInput??>&messageStatus=${messageStatusInput}</#if><#if title??>&title=${title!}</#if><#if createdBy??>&createdBy=${createdBy!}</#if>"
                       aria-label="Previous">
                    <#else>
                    <a href="#" aria-label="Previous">
                    </#if>
                    <span aria-hidden="true">&laquo; Prev</span>
                </a>
                </li>
                <li><a>${index}</a></li>
                <li>
                    <#if hasNextPage>
                    <a href="?index=${index+1}&pageSize=${pageSize}<#if messageStatusInput??>&messageStatus=${messageStatusInput}</#if><#if title??>&title=${title!}</#if><#if createdBy??>&createdBy=${createdBy!}</#if>"
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
<!-- content area end -->
</@global.main>