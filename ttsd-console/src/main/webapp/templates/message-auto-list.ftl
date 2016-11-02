<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="message-auto-list.js" headLab="message-auto-list" sideLab="messageAutoManage" title="自动发送站内信管理">

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
                <#assign adminRole = false/>
                <@security.authorize access="hasAnyAuthority('OPERATOR_ADMIN','ADMIN')">
                    <#assign adminRole = true/>
                </@security.authorize>

                <#list messages as message>
                <tr>
                    <td>
                    ${message.id}
                    </td>
                    <td>
                        全部用户
                    </td>
                    <td>
                    ${message.pushType.description!}
                    </td>
                    <td>
                    ${message.title!}
                    </td>
                    <td>
                    ${message.templateTxt!}
                    </td>
                    <td>
                        系统消息
                    </td>
                    <td>
                        站内信／APP消息中心
                    </td>
                    <td>
                        PC:${message.webUrl!}
                        APP:${message.appUrl.description!}
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
                        <#if message.status == 'TO_APPROVE'>
                            启用
                        <#elseif message.status == 'APPROVED'>
                            暂停
                        </#if>
                    </td>
                    <td>
                    ${message.updatedBy}
                    </td>
                </tr>
                </#list>
            </tbody>
        </table>
    </div>
</div>
<!-- content area end -->
</@global.main>