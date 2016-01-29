<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="auto-app-push-list.js" headLab="app-push-manage" sideLab="autoAppPushManage" title="自动推送管理">

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
    <div class="table-responsive">
        <table class="table table-bordered table-hover ">
            <thead>
            <tr>
                <th>
                    编号
                </th>
                <th>
                    通知名称
                </th>
                <th>
                    推送条件
                </th>
                <th>
                    推送渠道
                </th>
                <th>
                    推送模板
                </th>
                <th>
                    状态
                </th>
                <th>
                    操作
                </th>
                <th>
                    创建人
                </th>
                <th>
                    最后操作人
                </th>
            </tr>
            </thead>
            <tbody>
                <#list pushAlerts as pushAlert>
                <tr>
                    <td>${pushAlert.id?string('0')}</td>
                    <td>${(pushAlert.name)!}</td>
                    <td>每月1日推送当月生日用户</td>
                    <td>Android / IOS</td>
                    <td>${(pushAlert.content)!}</td>
                    <td>${pushAlert.status.getDescription()}</td>
                    <td>
                        <button class="btn btn-primary btn-xs edit-content" data-toggle="modal"
                                data-target="#myModal">
                            编辑
                        </button>
                        <#if pushAlert.status=='ENABLED'>
                            <a class="btn btn-danger btn-xs disabled-link"
                               href="/app-push-manage/auto-app-push/${pushAlert.id?string('0')}/disabled">
                                停用
                            </a>
                        <#else>
                            <a class="btn btn-success btn-xs enabled-link"
                               href="/app-push-manage/auto-app-push/${pushAlert.id?string('0')}/enabled">
                                启用
                            </a>
                        </#if>
                    </td>
                    <td>${(pushAlert.createdBy)!}</td>
                    <td>${(pushAlert.updatedBy)!}</td>
                </tr>
                </#list>
            </tbody>
        </table>
    </div>

    <!-- 模态框（Modal） -->
    <div class="modal fade" id="myModal" tabindex="-1" role="dialog"
         aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close"
                            data-dismiss="modal" aria-hidden="true">
                        &times;
                    </button>
                    <h4 class="modal-title" id="myModalLabel">
                        修改推送模板
                    </h4>
                </div>
                <div class="modal-body">
                    <textarea rows="4" cols="50" maxlength="40" class="content" name="content" errormsg="通知模板不能为空"></textarea>（长度请不要超过40个字）
                    <input type="hidden" name="jPushAlertId" class="jPushAlertId"/>
                </div>
                <div class="text-danger web-error-message alertMessage"></div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default"
                            data-dismiss="modal">关闭
                    </button>
                    <button type="button" class="btn btn-primary change-content">
                        提交更改
                    </button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal -->

</div>
<!-- content area end -->
</@global.main>