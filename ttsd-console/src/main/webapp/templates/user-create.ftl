<#import "macro/global.ftl" as global>
<@global.main pageCss="" pageJavascript="create-user.js" headLab="user-manage" sideLab="addUser" title="添加用户">

<!-- content area begin -->
<div class="col-md-10">
    <form class="form-horizontal" action="/user-manage/user" method="post">
        <div class="form-group">
            <label class="col-sm-2 control-label">登录名：</label>

            <div class="col-sm-3">
                <input type="text" id="loginName" name="loginName" class="form-control" value="${(user.loginName)!}"/>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">姓名：</label>

            <div class="col-sm-3">
                <input type="text" id="userName" name="userName" class="form-control" value="${(user.userName)!}"/>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">身份证：</label>

            <div class="col-sm-3">
                <input type="text" id="identityNumber" name="identityNumber" class="form-control"
                       value="${(user.identityNumber)!}"/>
            </div>
        </div>
        <div class="form-group">
            <label for="mobile" class="col-sm-2 control-label">手机号码：</label>

            <div class="col-sm-3">
                <input name="mobile" id="mobile" type="text" class="form-control" maxlength="11"
                       value="${(user.mobile)!}"/>
            </div>
        </div>
        <div class="form-group">
            <label for="email" class="col-sm-2 control-label">电子邮件：</label>

            <div class="col-sm-3">
                <input name="email" id="email" type="email" class="form-control" value="${(user.email)!}"/>
            </div>
        </div>
        <div class="form-group">
            <label for="referrer" class="col-sm-2 control-label">推荐人：</label>

            <div class="col-sm-3">
                <input name="referrer" id="referrer" type="text" class="form-control" value="${(user.referrer)!}"/>
            </div>
        </div>
        <div class="form-group">
            <label for="status" class="col-sm-2 control-label">状态：</label>

            <div class="col-sm-3">
                <label class="radio-inline"><input type="radio" name="status" id="status-active" value="ACTIVE"
                                                   <#if !(user?has_content) || (user?? && user.status?? && user.status == "ACTIVE")>checked="checked"</#if>>正常</label>
                <label class="radio-inline"><input type="radio" name="status" id="status-in-active" value="INACTIVE"
                                                   <#if user?? && user.status?? && user.status == "INACTIVE">checked="checked"</#if>>禁用</label>
            </div>
        </div>

        <div class="form-group">
            <label for="referrer" class="col-sm-2 control-label">角色：</label>

            <div class="col-sm-3">
                <input type="hidden" name="roles" value="USER"/>
                <#list roles as roleItem>
                    <#if roleItem.name() != 'USER'>
                        <div class="checkbox">
                            <label><input type="checkbox" name="roles" value="${roleItem.name()}"
                                          <#if user?? && user.roles?? && user.roles?seq_contains(roleItem.name())>checked="checked"</#if>>${roleItem.getDescription()}
                            </label>
                        </div>
                    </#if>
                </#list>
            </div>
        </div>

        <#if errorMessage??>
            <div class="form-group console-error-message">
                <div class="col-sm-offset-2 col-sm-3">
                    <div class="alert alert-danger" role="alert">${errorMessage}</div>
                </div>
            </div>
        </#if>

        <div class="form-group web-error-message">
            <div class="col-sm-offset-2 col-sm-3">
                <div class="alert alert-danger message" role="alert"></div>
            </div>
        </div>

        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-3">
                <input class="btn btn-default btn-submit" type="submit" value="提交">
                <input class="btn btn-default" type="reset" value="重置">
            </div>
        </div>
    </form>
</div>
<!-- content area end -->
</@global.main>