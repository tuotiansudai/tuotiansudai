<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="manual-message.js" headLab="app-push-manage" sideLab="createManualMessage" title="编辑手动信息">

<!-- content area begin -->
<div class="col-md-10">
    <form action="/message-manage/manual-message/create" method="post" class="form-horizontal message-form">
        <div class="form-group">
            <input type="text" name="id" hidden="hidden" value="${(dto.id?c)!"0"}">
            <label class="col-sm-1 control-label">收件人: </label>
            <div class="col-sm-4 receiver-type">
                <#list userGroups as userGroup>
                    <label>
                        <input type="checkbox" name="userGroups"
                               <#if userGroup.name()=='ALL_USER'>class="allGroups"
                               <#elseif userGroup.name()=='IMPORT_USER'>class="importGroups"
                               <#else>class="userGroups"</#if>
                               <#if selectedUserGroups?seq_contains(userGroup.name())>checked="checked"</#if>
                               value="${userGroup.name()}">
                    ${userGroup.getDescription()}
                    </label>
                </#list>
            </div>
            <input type="text" name="importUsersId" hidden="hidden" class="importUsersId" value="${importUsersId!0}">

            <div class="file-btn">
                <input type="file" id="importBtn">
                导入用户
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-1 control-label">标题: </label>

            <div class="col-sm-4">
                <input type="text" name="title" class="form-control message-title"
                       value="${(dto.title)!}"/>
            </div>
            <span>不超过40字</span>
        </div>

        <div class="form-group">
            <label class="col-sm-1 control-label">编辑内容: </label>

            <div class="col-sm-10">
                <script id="editor"
                        type="text/plain">${(dto.template)!}</script>
                <input type="hidden" name="template" class="message-template"/>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-1 control-label">送达渠道: </label>

            <div class="col-sm-4 receiver-type">
                <#list channelTypes as channel>
                    <label>
                        <input type="checkbox" name="channels" class="channel"
                               <#if selectedChannelTypes?seq_contains(channel.name())>checked="checked"</#if>
                               value="${channel.name()}">
                    ${channel.getDescription()}
                    </label>
                </#list>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-1 control-label">操作: </label>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

            <div class="col-sm-4">
                <button type="button" class="btn jq-btn-form btn-primary message-save">提交</button>
            </div>
        </div>
    </form>
</div>
<!-- content area end -->
</@global.main>