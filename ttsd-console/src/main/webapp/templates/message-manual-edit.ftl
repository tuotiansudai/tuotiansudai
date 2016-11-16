<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="message-manual-edit.js" headLab="message-manage" sideLab="createManualMessage" title="创建手动站内信">

<!-- content area begin -->
<div class="col-md-10">
    <form action="/message-manage/manual-message/create" method="post" class="form-horizontal message-form">
        <div class="form-group">
            <input type="text" name="id" class="message-id" hidden="hidden" value="${(dto.id?c)!"0"}">
            <label class="col-sm-1 control-label">收件人: </label>

            <div class="col-sm-4 receiver-type" id="userGroup">
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
            <label class="col-sm-1 control-label">送达方式: </label>

            <div class="col-sm-4 receiver-type" id="messageChannel">
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
            <label class="col-sm-1 control-label">消息类型</label>
            <div class="col-sm-4">
                <select class="selectpicker messageCategory" name="messageCategory">
                    <#list manualMessageTypes as messageCategory>
                        <option value="${messageCategory.name()}"
                                <#if (selectedManualMessageType?? && selectedManualMessageType == messageCategory) || ((dto.messageCategory)?? && dto.messageCategory == messageCategory)>selected</#if>>${messageCategory.getDescription()}</option>
                    </#list>
                </select>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-1 control-label">WEB跳转页面: </label>

            <div class="col-sm-4">
                <input type="text" name="webUrl" class="form-control message-web-url"
                       value="${(dto.webUrl)!}"/>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-1 control-label">APP跳转页面</label>
            <div class="col-sm-4"><select class="selectpicker message-app-url" name="appUrl">

                <#list appUrls as appUrl>
                    <option value="${appUrl.name()}"
                            <#if (selectedAppUrl?? && selectedAppUrl == appUrl) || ((dto.appUrl)?? && dto.appUrl == appUrl)>selected</#if>>${appUrl.getDescription()}</option>
                </#list>
            </select></div>
        </div>

        <div class="form-group">
            <label class="col-sm-1 control-label">APP推送: </label>

            <div class="col-sm-4 checkbox">
                <label for="extra">
                    <input type="checkbox" class="message-jpush" name="jpush" id="extra"
                           <#if (dto.jpush)?? && dto.jpush>checked</#if>>选中后此消息创建推送
                </label>
            </div>
        </div>
        <div class="check-item" style="display:none;">
            <div class="form-group">
                <label class="col-sm-1 control-label">推送类型: </label>
                <div class="col-sm-4">
                    <select class="selectpicker message-pushType" name="pushType">
                        <#list pushTypes as pushType>
                            <#if pushType.getType()=='MANUAL'>
                                <option value="${pushType.name()}"
                                        <#if jPushAlert?? && jPushAlert.pushType == pushType || ((dto.pushType)?? && dto.pushType == pushType)>selected</#if>>${pushType.getDescription()}</option>
                            </#if>
                        </#list>
                    </select>
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-1 control-label">推送渠道: </label>
                <div class="col-sm-4">
                    <select class="selectpicker message-pushSource" name="message-pushSource">
                        <#list pushSources as pushSource>
                            <option value="${pushSource.name()}"
                                    <#if jPushAlert?? && jPushAlert.pushSource == pushSource || ((dto.pushSource)?? && dto.pushSource == pushSource)>selected</#if>>${pushSource.name()}</option>
                        </#list>
                    </select>
                </div>
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