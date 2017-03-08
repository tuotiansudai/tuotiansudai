<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="message-manual-edit.js" headLab="content-manage" sideLab="createManualMessage" title="创建手动站内信">

<!-- content area begin -->
<div class="col-md-10" xmlns="http://www.w3.org/1999/html">
    <form action="/message-manage/manual-message/create" method="post" class="form-horizontal message-form">
        <div class="form-group">
            <input type="hidden" name="id" class="message-id" value="${(dto.id?c)!}">

            <label class="col-sm-1 control-label">接收人: </label>
            <div class="col-sm-3 radio">
                <#list userGroups as userGroup>
                    <label>
                        <input type="radio" name="userGroup" value="${userGroup.name()}"
                               <#if selectedUserGroup.name() == userGroup.name()>checked="checked"</#if>
                               value="${userGroup.name()}">${userGroup.getDescription()}
                    </label>
                </#list>
            </div>

            <div class="file-btn <#if selectedUserGroup.name() == 'ALL_USER'>hidden</#if>">
                <input type="file" id="importBtn">导入用户
                <input type="hidden" id="importUsersFlag" value="${(dto.importUsersFlag?c)!}">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-1 control-label">标题: </label>

            <div class="col-sm-4">
                <input type="text" name="title" class="form-control message-title"
                       value="${(dto.title)!}"/>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-1 control-label">编辑内容: </label>

            <div class="col-sm-6">
                <script id="editor" type="text/plain">${(dto.template)!}</script>
                <input type="hidden" name="template" class="message-template"/>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-1 control-label">消息发送时间</label>

            <div class="col-sm-2">
                <div class='input-group date' id='datepickerBegin'>
                    <input type='text' class="form-control" name="validStartTime" value="${(dto.validStartTime)!}"/>
                    <span class="input-group-addon">
                                <span class="glyphicon glyphicon-calendar"></span>
                            </span>
                </div>
            </div>
            <div class="line-size">
                -
            </div>
            <div class="col-sm-2">
                <div class='input-group date' id='datepickerEnd'>
                    <input type='text' class="form-control" name="validEndTime" value="${(dto.validEndTime)!}"/>
                    <span class="input-group-addon">
                                <span class="glyphicon glyphicon-calendar"></span>
                            </span>
                </div>
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
                    <#list messageCategories as messageCategory>
                        <option value="${messageCategory.name()}"
                                <#if ((dto.messageCategory)?? && dto.messageCategory == messageCategory) || messageCategory_index == 0>selected="selected"</#if>>${messageCategory.getDescription()}</option>
                    </#list>
                </select>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-1 control-label">WEB跳转页面: </label>

            <div class="col-sm-4">
                <input type="text" name="webUrl" class="form-control message-web-url" value="${(dto.webUrl)!}"/>
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-1 control-label">APP跳转页面</label>
            <div class="col-sm-4"><select class="selectpicker message-app-url" name="appUrl">

                <#list appUrls as appUrl>
                    <#if appUrl != "INVEST_DETAILS_REPAY_DETAIL" && appUrl != "MESSAGE_CENTER_DETAIL" && appUrl != "LOAN_NORMAL_DETAIL" && appUrl != "LOAN_TRANSFER_DETAIL">
                        <option value="${appUrl.name()}"
                                <#if (selectedAppUrl?? && selectedAppUrl == appUrl) || ((dto.appUrl)?? && dto.appUrl == appUrl)>selected</#if>>${appUrl.getDescription()}</option>
                    </#if>
                </#list>
            </select></div>
        </div>

        <div class="form-group">
            <label class="col-sm-1 control-label">APP推送: </label>

            <div class="col-sm-4 checkbox">
                <label for="push">
                    <input type="checkbox" class="message-jpush" id="push" <#if (dto.push)??>checked="checked"</#if>>选中后此消息创建推送
                    <input type="hidden" value="${(dto.push.id?c)!}">
                </label>
            </div>
        </div>

        <div class="push-check-item" style="display:none;">
            <div class="form-group">
                <label class="col-sm-1 control-label">推送类型: </label>
                <div class="col-sm-4">
                    <select class="selectpicker message-pushType" name="pushType">
                        <#list pushTypes as pushType>
                            <#if pushType.getType()=='MANUAL'>
                                <option value="${pushType.name()}"
                                        <#if ((dto.push.id)?? && dto.push.pushType.name() == pushType.name()) || pushType_index == 0>selected="selected"</#if>>
                                    ${pushType.getDescription()}
                                </option>
                            </#if>
                        </#list>
                    </select>
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-1 control-label">推送渠道: </label>
                <div class="col-sm-4">
                    <select class="selectpicker message-pushSource" name="pushSource">
                        <#list pushSources as pushSource>
                            <option value="${pushSource.name()}"
                                    <#if ((dto.push.id)?? && dto.push.pushSource.name() == pushSource.name()) || pushSource_index == 0>selected="selected"</#if>>
                                ${pushSource.name()}
                            </option>
                        </#list>
                    </select>
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-1 control-label">推送跳转页面: </label>
                <div class="col-sm-4">
                    <select class="selectpicker message-jumpTo" name="jumpTo">
                        <#list appUrls as jumpToUrl>
                            <#if jumpToUrl != "OTHER" && jumpToUrl != "NONE">
                                <option value="${jumpToUrl.name()}"
                                        <#if ((dto.push.jumpTo)?? && dto.push.jumpTo.name() == jumpToUrl.name()) || jumpToUrl_index == 0>selected="selected"</#if>>${jumpToUrl.getDescription()}</option>
                            </#if>
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

    <!-- Modal -->
    <div class="modal fade" id="confirm-modal" tabindex="-1" role="dialog">
        <div class="modal-dialog modal-sm" role="document">
            <div class="modal-content">
                <div class="modal-body">
                    <h5>确认提交？</h5>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    <button type="button" class="btn btn-default btn-submit">确认</button>
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